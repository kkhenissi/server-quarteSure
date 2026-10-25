package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/** Une combinaison attribuée à un filleul, que celui-ci coche une fois jouée. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "combinaison_jeu")
public class CombinaisonJeu {

    public static final String A_JOUER = "A_JOUER";
    public static final String JOUEE = "JOUEE";
    /** Posés plus tard par le serveur à partir de l'arrivée : le filleul ne peut plus les modifier */
    public static final String GAGNANTE = "GAGNANTE";
    public static final String PERDUE = "PERDUE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribution_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttributionJeu attribution;

    /** Ex. « 3 - 7 - 12 - 15 » ou « 3 - 7 - 12 - 15 (⇄ ordre) » */
    @Column(name = "numeros", nullable = false)
    private String numeros;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut = A_JOUER;

    /** Position dans la part du filleul (ordre d'affichage) */
    @Column(name = "ordre", nullable = false)
    private int ordre;

    public boolean isVerrouillee() {
        return GAGNANTE.equals(statut) || PERDUE.equals(statut);
    }
}
