package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.AttributionJeu;
import com.fdc.quartesure.quartesurebackend.POJO.CombinaisonJeu;
import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.AttributionJeuDao;
import com.fdc.quartesure.quartesurebackend.dao.CombinaisonJeuDao;
import com.fdc.quartesure.quartesurebackend.dao.FillolDao;
import com.fdc.quartesure.quartesurebackend.dto.AttributionJeuDto;
import com.fdc.quartesure.quartesurebackend.dto.CombinaisonJeuDto;
import com.fdc.quartesure.quartesurebackend.dto.DisponibiliteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Côté filleul : disponibilité et combinaisons attribuées par le parrain.
 * Un compte est « filleul » quand son e-mail figure dans la table fillol (invitation d'un parrain).
 * Tout est toujours cherché à partir du compte connecté : un filleul n'accède jamais aux données d'un autre.
 */
@Service
@Slf4j
public class JeuFilleulService {

    public static final int MAX_IDS = 1000;

    public enum MarquerResult { OK, INVALID, NOT_FILLEUL, NOT_FOUND }

    @Autowired
    private FillolDao fillolDao;

    @Autowired
    private AttributionJeuDao attributionJeuDao;

    @Autowired
    private CombinaisonJeuDao combinaisonJeuDao;

    /** Ligne fillol du compte connecté, null s'il n'est le filleul de personne. */
    public Fillol findFillol(User me) {
        if (me == null || me.getEmail() == null || me.getEmail().trim().isEmpty()) {
            return null;
        }
        return fillolDao.findFirstByEmailIgnoreCase(me.getEmail().trim());
    }

    // ── Disponibilité ───────────────────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public DisponibiliteDto disponibilite(User me) {
        Fillol fillol = findFillol(me);
        return fillol == null ? new DisponibiliteDto(false, false, null) : toDto(fillol);
    }

    /** Active ou désactive la disponibilité. Renvoie null si le compte n'est le filleul de personne. */
    @Transactional
    public DisponibiliteDto changerDisponibilite(User me, boolean disponible) {
        Fillol fillol = findFillol(me);
        if (fillol == null) {
            return null;
        }
        if (fillol.isDisponibleOuNonRenseigne() != disponible || fillol.getDisponibleDepuis() == null) {
            fillol.setDisponible(disponible);
            fillol.setDisponibleDepuis(LocalDateTime.now());
            fillolDao.save(fillol);
            log.info("Filleul {} : disponible = {}", fillol.getEmail(), disponible);
        }
        return toDto(fillol);
    }

    private DisponibiliteDto toDto(Fillol fillol) {
        return new DisponibiliteDto(true, fillol.isDisponibleOuNonRenseigne(), iso(fillol.getDisponibleDepuis()));
    }

    // ── Attribution (appelée à l'envoi collectif, AVANT l'e-mail) ───────────────────────────────
    @Transactional
    public Long enregistrer(Fillol fillol, String course, Long departMs, List<String> tickets, List<String> combinations) {
        AttributionJeu attribution = new AttributionJeu();
        attribution.setFillol(fillol);
        attribution.setCourse(course);
        attribution.setDepartMs(departMs != null && departMs > 0 ? departMs : null);
        attribution.setDateEnvoi(LocalDateTime.now());
        attribution.setTickets(tickets == null || tickets.isEmpty() ? null : String.join("\n", tickets));

        int ordre = 0;
        for (String numeros : combinations) {
            CombinaisonJeu combinaison = new CombinaisonJeu();
            combinaison.setNumeros(numeros);
            combinaison.setStatut(CombinaisonJeu.A_JOUER);
            combinaison.setOrdre(ordre++);
            attribution.addCombinaison(combinaison);
        }
        return attributionJeuDao.save(attribution).getId();
    }

    // ── Lecture ─────────────────────────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<AttributionJeuDto> lister(User me) {
        Fillol fillol = findFillol(me);
        if (fillol == null) {
            return Collections.emptyList();
        }
        String parrainNom = fillol.getUser() == null ? null : fillol.getUser().fullName();

        List<AttributionJeuDto> result = new ArrayList<>();
        for (AttributionJeu a : attributionJeuDao.findTop50ByFillolOrderByDateEnvoiDescIdDesc(fillol)) {
            List<CombinaisonJeuDto> combinaisons = new ArrayList<>();
            for (CombinaisonJeu c : a.getCombinaisons()) {
                combinaisons.add(new CombinaisonJeuDto(c.getId(), c.getNumeros(), c.getStatut()));
            }
            List<String> tickets = a.getTickets() == null || a.getTickets().isEmpty()
                    ? Collections.<String>emptyList()
                    : Arrays.asList(a.getTickets().split("\n"));
            result.add(new AttributionJeuDto(a.getId(), a.getCourse(), a.getDepartMs(), parrainNom,
                    iso(a.getDateEnvoi()), tickets, combinaisons));
        }
        return result;
    }

    // ── Cocher / décocher ───────────────────────────────────────────────────────────────────────
    /**
     * Passe des combinaisons à JOUEE ou A_JOUER. Seules celles du filleul connecté sont acceptées : si un
     * identifiant ne lui appartient pas (ou n'existe pas), rien n'est modifié. Les combinaisons déjà jugées
     * (GAGNANTE / PERDUE) sont laissées telles quelles.
     */
    @Transactional
    public MarquerResult marquer(User me, List<Long> ids, String statut) {
        if (ids == null || ids.isEmpty() || ids.size() > MAX_IDS
                || !(CombinaisonJeu.JOUEE.equals(statut) || CombinaisonJeu.A_JOUER.equals(statut))) {
            return MarquerResult.INVALID;
        }
        Fillol fillol = findFillol(me);
        if (fillol == null) {
            return MarquerResult.NOT_FILLEUL;
        }
        Set<Long> wanted = new LinkedHashSet<>();
        for (Long id : ids) {
            if (id == null) {
                return MarquerResult.INVALID;
            }
            wanted.add(id);
        }
        List<CombinaisonJeu> owned = combinaisonJeuDao.findOwned(wanted, fillol);
        if (owned.size() != wanted.size()) {
            return MarquerResult.NOT_FOUND;
        }
        for (CombinaisonJeu c : owned) {
            if (!c.isVerrouillee()) {
                c.setStatut(statut);
            }
        }
        combinaisonJeuDao.saveAll(owned);
        return MarquerResult.OK;
    }

    private static String iso(LocalDateTime date) {
        return date == null ? null : date.atZone(ZoneId.systemDefault()).toInstant().toString();
    }
}
