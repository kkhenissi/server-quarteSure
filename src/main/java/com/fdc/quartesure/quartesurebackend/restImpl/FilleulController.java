package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.FillolDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.dto.FilleulDto;
import com.fdc.quartesure.quartesurebackend.dto.FilleulJeuStatutDto;
import com.fdc.quartesure.quartesurebackend.dto.SendCombinationsRequest;
import com.fdc.quartesure.quartesurebackend.dto.SendCombinationsResult;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import com.fdc.quartesure.quartesurebackend.service.JeuFilleulService;
import com.fdc.quartesure.quartesurebackend.utils.PhoneUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Filleuls du parrain connecté, et envoi collectif de leur part de combinaisons.
 * Les deux routes exigent un JWT valide (anyRequest().authenticated()) et ne travaillent QUE sur les filleuls
 * du parrain connecté : cette API ne peut pas servir de relais d'envoi d'e-mails vers des adresses arbitraires.
 */
@RestController
@RequestMapping("/user/filleuls")
@Slf4j
public class FilleulController {

    // Garde-fous contre les abus
    private static final int MAX_SHARES = 50;
    private static final int MAX_COMBINATIONS = 1000;
    private static final int MAX_TICKETS = 200;
    private static final int MAX_LINE_LENGTH = 200;

    @Autowired
    private FillolDao fillolDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JeuFilleulService jeuFilleulService;

    /** Liste des filleuls du parrain connecté : nom, téléphone et e-mail. */
    @GetMapping
    public ResponseEntity<List<FilleulDto>> list() {
        User parrain = currentUser();
        if (parrain == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<FilleulDto> result = new ArrayList<>();
        for (Fillol fillol : fillolDao.findByUser(parrain)) {
            // Le filleul a un compte s'il existe un utilisateur avec la même adresse e-mail
            User account = fillol.getEmail() == null ? null : userDao.findByEmail(fillol.getEmail());
            result.add(FilleulDto.builder()
                    .id(fillol.getId())
                    .nom(account == null ? null : account.fullName())
                    .telephone(account == null ? null : account.getPhoneNumber())
                    .telephoneInternational(account == null ? null : PhoneUtils.toInternational(account.getPhoneNumber()))
                    .email(fillol.getEmail())
                    .status(fillol.getStatus())
                    .inscrit(account != null)
                    .disponible(fillol.isDisponibleOuNonRenseigne())
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Avancement des parts envoyées par le parrain connecté pour une course (identifiée par son heure de départ, à défaut
     * par son libellé) : le front coche automatiquement la part d'un filleul quand celui-ci a validé son jeu.
     */
    @GetMapping("/jeux/statuts")
    public ResponseEntity<List<FilleulJeuStatutDto>> statuts(@RequestParam(value = "departMs", required = false) Long departMs,
                                                             @RequestParam(value = "course", required = false) String course) {
        User parrain = currentUser();
        if (parrain == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jeuFilleulService.statutsPourParrain(parrain, departMs, course));
    }

    /** Envoi collectif par e-mail : à chaque filleul sa part. */
    @PostMapping("/send-combinations")
    public ResponseEntity<?> sendCombinations(@RequestBody SendCombinationsRequest request) {
        User parrain = currentUser();
        if (parrain == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request == null || request.getShares() == null || request.getShares().isEmpty()) {
            return ResponseEntity.badRequest().body("Aucune part à envoyer.");
        }
        if (request.getShares().size() > MAX_SHARES) {
            return ResponseEntity.badRequest().body("Trop de filleuls (maximum " + MAX_SHARES + ").");
        }
        String course = clean(request.getCourse());
        if (course.isEmpty()) {
            return ResponseEntity.badRequest().body("Course manquante.");
        }

        List<SendCombinationsResult> results = new ArrayList<>();
        for (SendCombinationsRequest.Share share : request.getShares()) {
            results.add(sendOne(parrain, course, request.getDepartMs(), share));
        }

        long sent = results.stream().filter(r -> "SENT".equals(r.getStatus())).count();
        log.info("Envoi des combinaisons par {} : {}/{} e-mails envoyés", parrain.getEmail(), sent, results.size());
        return ResponseEntity.ok(results);
    }

    private SendCombinationsResult sendOne(User parrain, String course, Long departMs, SendCombinationsRequest.Share share) {
        Integer id = share == null ? null : share.getFilleulId();
        if (id == null) {
            return new SendCombinationsResult(null, null, "REFUSED", "Filleul manquant.");
        }

        // Le filleul doit appartenir au parrain connecté
        Fillol fillol = fillolDao.findById(id).orElse(null);
        if (fillol == null || fillol.getUser() == null
                || !parrain.getUser_id().equals(fillol.getUser().getUser_id())) {
            return new SendCombinationsResult(id, null, "REFUSED", "Ce filleul n'est pas le vôtre.");
        }

        User account = fillol.getEmail() == null ? null : userDao.findByEmail(fillol.getEmail());
        String nom = account == null ? fillol.getEmail() : account.fullName();

        if (fillol.getEmail() == null || fillol.getEmail().isBlank()) {
            return new SendCombinationsResult(id, nom, "SKIPPED", "Pas d'adresse e-mail.");
        }

        List<String> combinations = cleanList(share.getCombinations(), MAX_COMBINATIONS);
        List<String> tickets = cleanList(share.getTickets(), MAX_TICKETS);
        if (combinations.isEmpty()) {
            return new SendCombinationsResult(id, nom, "SKIPPED", "Aucune combinaison à envoyer.");
        }

        // Disponibilité revérifiée ici (lue en base à l'instant) : le filleul a pu la retirer depuis la répartition
        if (!fillol.isDisponibleOuNonRenseigne()) {
            return new SendCombinationsResult(id, nom, "UNAVAILABLE", "Ce filleul s'est déclaré indisponible : rien envoyé.");
        }

        // 1) On enregistre d'abord la part : le filleul la retrouve dans son compte quoi qu'il arrive à l'e-mail
        try {
            jeuFilleulService.enregistrer(fillol, course, departMs, tickets, combinations);
        } catch (Exception e) {
            log.error("Enregistrement de la part du filleul {} impossible", id, e);
            return new SendCombinationsResult(id, nom, "FAILED", "Enregistrement impossible : rien n'a été envoyé.");
        }

        // 2) Puis l'e-mail (simple notification)
        boolean ok = emailService.sendCombinations(fillol.getEmail(), nom, parrain.fullName(), course, tickets, combinations);
        return new SendCombinationsResult(id, nom, ok ? "SENT" : "FAILED",
                ok ? "E-mail envoyé." : "E-mail non envoyé, mais les combinaisons sont visibles dans le compte du filleul.");
    }

    /** Parrain connecté, d'après le JWT (jamais d'après un paramètre de la requête). */
    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userDao.findByEmail(auth.getName());
    }

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        return v.length() > MAX_LINE_LENGTH ? v.substring(0, MAX_LINE_LENGTH) : v;
    }

    private static List<String> cleanList(List<String> values, int max) {
        List<String> out = new ArrayList<>();
        if (values == null) {
            return out;
        }
        for (String v : values) {
            String c = clean(v);
            if (!c.isEmpty()) {
                out.add(c);
            }
            if (out.size() >= max) {
                break;
            }
        }
        return out;
    }
}
