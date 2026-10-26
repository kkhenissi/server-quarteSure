package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Part de combinaisons qu'un parrain a confiée à l'un de ses filleuls pour une course.
 * Créée à l'envoi collectif (avant l'e-mail) : le filleul la retrouve dans son compte, « Mes jeux ».
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attribution_jeu")
public class AttributionJeu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le filleul destinataire (le parrain s'en déduit : fillol.user) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fillol_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Fillol fillol;

    /** Libellé de la course, ex. « R4C1 · LONGCHAMP · 14:35 » */
    @Column(name = "course")
    private String course;

    /** Heure de départ de la course (epoch ms), null si non transmise */
    @Column(name = "depart_ms")
    private Long departMs;

    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;

    /** Tickets « champ réduit », une ligne chacun (séparées par \n) */
    @Column(name = "tickets", columnDefinition = "text")
    private String tickets;

    /** Le filleul a cliqué « J'ai fini de jouer » : sa part est validée et n'est plus modifiable. */
    @Column(name = "terminee", columnDefinition = "boolean default false")
    private Boolean terminee;

    @Column(name = "terminee_le")
    private LocalDateTime termineeLe;

    @OneToMany(mappedBy = "attribution", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    private List<CombinaisonJeu> combinaisons = new ArrayList<>();

    public boolean estTerminee() {
        return Boolean.TRUE.equals(terminee);
    }

    public void addCombinaison(CombinaisonJeu combinaison) {
        combinaison.setAttribution(this);
        combinaisons.add(combinaison);
    }
}
