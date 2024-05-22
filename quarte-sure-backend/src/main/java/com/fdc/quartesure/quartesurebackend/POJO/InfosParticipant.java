package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "infosParticipant")
public class InfosParticipant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infosParticipantId")
    private Integer infosParticipantId; // "R1C4-25042024"
    private String        nom;
    private String        numPmu; //: 1,
    private String        age;  // 6,
    private String        sexe; // "FEMELLES",
    private String        race; // "PUR-SANG",
    private String        statut;  // "PARTANT",
    private String        placeCorde; // 1,
    private String        oeilleres; // "SANS_OEILLERES",
    private String        proprietaire;  // "DONA SERAFINA",
    private String        entraineur;  // "CARLOS ALBERTO BANI",
    private String        driver;  //TOMAS DANIEL BAEZ",
    private boolean       driverChange; // false,
    @OneToOne
    @JoinColumn(name = "robeId", referencedColumnName = "robeId")
    private Robe robe;
    private String        tauxReclamation;
    private boolean       indicateurInedit;  // false,
    private String        musique;  // "0p7p3p1p",
    private String        nombreCourses;  // 22,
    private String        nombreVictoires; // 6,
    private String        nombrePlaces;  // 13,
    private String        nombrePlacesSecond;  // 3,
    private String        nombrePlacesTroisieme; //
    @OneToOne
    @JoinColumn(name = "gainsParticipantId", referencedColumnName = "gainsParticipantId")
    private GainPatricipant gainsParticipant;
    private double        handicapValeur;
    private String        nomPere; // "CURLINELLO",
    private String        nomMere;  // "GIRLISH INTUITION",
    private String        nomPereMere;  // "ORPEN",
    private String        ordreArrivee;  // 5,
    private String        jumentPleine; /// false,
    private String        engagement;  // false,
    private String        supplement; // 0,
    private String        handicapPoids;  // 575,
    private String        poidsConditionMonteChange;  // false,
    private DistanceChevalPrecedent distanceChevalPrecedent;
    @Column(name = "dernierRapportDirect", length = 10000)
    private DernierRapportDirect dernierRapportDirect;
    @Column(name = "dernierRapportReference", length = 10000)
    private DernierRapportReference dernierRapportReference;
    private String                  urlCasaque;
    @OneToOne
    @JoinColumn(name = "commentaireCourseId", referencedColumnName = "commentaireId")

    private CommentaireApresCourse commentaireApresCourse;
    private String                  eleveur;
    private String                  paysEntrainement;
    private String                  allure;
    @ManyToOne
    @JoinColumn(name="courseId", nullable=false)
    private Course course;

}
