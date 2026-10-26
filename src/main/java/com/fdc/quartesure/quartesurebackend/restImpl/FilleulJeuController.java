package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.dto.AttributionJeuDto;
import com.fdc.quartesure.quartesurebackend.dto.DisponibiliteDto;
import com.fdc.quartesure.quartesurebackend.dto.DisponibiliteRequest;
import com.fdc.quartesure.quartesurebackend.dto.MarquerCombinaisonsRequest;
import com.fdc.quartesure.quartesurebackend.serviceImpl.JeuFilleulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Espace du filleul connecté : sa disponibilité (visible par son parrain) et les combinaisons qu'on lui a confiées.
 * Exige un JWT valide (anyRequest().authenticated()) ; le compte vient toujours du JWT, jamais d'un paramètre.
 */
@RestController
@RequestMapping("/user/filleul")
@Slf4j
public class FilleulJeuController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JeuFilleulService jeuFilleulService;

    /** « filleul: false » si le compte n'est le filleul de personne (le front masque alors le bouton). */
    @GetMapping("/disponibilite")
    public ResponseEntity<DisponibiliteDto> getDisponibilite() {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jeuFilleulService.disponibilite(me));
    }

    @PutMapping("/disponibilite")
    public ResponseEntity<?> setDisponibilite(@RequestBody DisponibiliteRequest request) {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request == null || request.getDisponible() == null) {
            return ResponseEntity.badRequest().body("Disponibilité manquante.");
        }
        DisponibiliteDto dto = jeuFilleulService.changerDisponibilite(me, request.getDisponible());
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce compte n'est le filleul de personne.");
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/jeux")
    public ResponseEntity<List<AttributionJeuDto>> listJeux() {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jeuFilleulService.lister(me));
    }

    @PostMapping("/jeux/combinaisons")
    public ResponseEntity<?> marquer(@RequestBody MarquerCombinaisonsRequest request) {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request == null) {
            return ResponseEntity.badRequest().body("Requête vide.");
        }
        JeuFilleulService.MarquerResult result = jeuFilleulService.marquer(me, request.getIds(), request.getStatut());
        switch (result) {
            case OK:
                return ResponseEntity.noContent().build();
            case INVALID:
                return ResponseEntity.badRequest().body("Requête invalide.");
            case LOCKED:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Jeu déjà validé : il n'est plus modifiable.");
            default:
                // NOT_FILLEUL ou combinaison qui n'est pas la vôtre : même réponse, on ne révèle rien
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combinaison introuvable.");
        }
    }

    /** Le filleul valide sa part (« J'ai fini de jouer ») : toutes les combinaisons doivent être cochées. */
    @PostMapping("/jeux/{id}/terminer")
    public ResponseEntity<?> terminer(@PathVariable("id") Long id) {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        switch (jeuFilleulService.terminer(me, id)) {
            case OK:
                return ResponseEntity.noContent().build();
            case INCOMPLETE:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Il reste des combinaisons à cocher.");
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jeu introuvable.");
        }
    }

    /** Compte connecté, d'après le JWT (jamais d'après un paramètre de la requête). */
    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userDao.findByEmail(auth.getName());
    }
}
