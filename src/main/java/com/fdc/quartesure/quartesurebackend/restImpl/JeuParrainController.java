package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.JeuParrain;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.JeuParrainDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.dto.JeuParrainDto;
import com.fdc.quartesure.quartesurebackend.dto.JeuParrainRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Jeux du parrain connecté : un instantané complet par course (pari, chevaux retirés, parts des filleuls, arrivée, gain).
 * Exige un JWT valide ; le parrain vient toujours du JWT, jamais d'un paramètre.
 */
@RestController
@RequestMapping("/user/jeux-parrain")
@Slf4j
public class JeuParrainController {

    /** Garde-fou : un instantané reste bien en dessous de cette taille (caractères) */
    private static final int MAX_SNAPSHOT = 2_000_000;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JeuParrainDao jeuParrainDao;

    /** Liste (sans le snapshot) des jeux du parrain, les plus récents d'abord. */
    @GetMapping
    public ResponseEntity<List<JeuParrainDto>> list() {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<JeuParrainDto> jeux = jeuParrainDao.findTop100ByUserIdOrderByDepartMsDescIdDesc(me.getUser_id())
                .stream().map(j -> toDto(j, false)).collect(Collectors.toList());
        return ResponseEntity.ok(jeux);
    }

    /** Détail d'un jeu, avec son snapshot complet. */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return jeuParrainDao.findByIdAndUserId(id, me.getUser_id())
                .<ResponseEntity<?>>map(j -> ResponseEntity.ok(toDto(j, true)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jeu introuvable."));
    }

    /** Crée le jeu de cette course, ou le met à jour s'il existe déjà (clé : parrain + courseKey). */
    @PutMapping
    @Transactional
    public ResponseEntity<?> save(@RequestBody JeuParrainRequest request) {
        User me = currentUser();
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request == null || isBlank(request.getCourseKey()) || isBlank(request.getSnapshot())) {
            return ResponseEntity.badRequest().body("Course ou contenu du jeu manquant.");
        }
        if (request.getSnapshot().length() > MAX_SNAPSHOT) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Jeu trop volumineux.");
        }
        LocalDateTime now = LocalDateTime.now();
        JeuParrain jeu = jeuParrainDao.findByUserIdAndCourseKey(me.getUser_id(), request.getCourseKey())
                .orElseGet(() -> {
                    JeuParrain nouveau = new JeuParrain();
                    nouveau.setUserId(me.getUser_id());
                    nouveau.setCourseKey(request.getCourseKey());
                    nouveau.setCreatedAt(now);
                    return nouveau;
                });
        jeu.setCourse(request.getCourse());
        jeu.setDepartMs(request.getDepartMs());
        jeu.setPari(request.getPari());
        jeu.setMise(request.getMise());
        jeu.setGain(request.getGain());
        jeu.setSnapshot(request.getSnapshot());
        jeu.setUpdatedAt(now);
        return ResponseEntity.ok(toDto(jeuParrainDao.save(jeu), false));
    }

    private JeuParrainDto toDto(JeuParrain j, boolean withSnapshot) {
        return new JeuParrainDto(j.getId(), j.getCourseKey(), j.getCourse(), j.getDepartMs(), j.getPari(), j.getMise(),
                j.getGain(), String.valueOf(j.getCreatedAt()), String.valueOf(j.getUpdatedAt()),
                withSnapshot ? j.getSnapshot() : null);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
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
