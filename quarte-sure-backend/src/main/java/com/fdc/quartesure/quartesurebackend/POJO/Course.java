package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "course")
@Embeddable
@ToString
public class Course implements Serializable  {
    public static final Long serialVersionUid = 12456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId; // "R1C4-25042024"
    @Column(name = "listOfParticipant", length = 10000)
    private ArrayList<InfosParticipant> participants;
    private ArrayList<Ecurie> ecuries;
    @Column(name = "SpriteCasaque", length = 10000)
    private ArrayList<SpriteCasaque> spriteCasaques;
    private Boolean cached;


    public class InfosParticipant implements Serializable{


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
        private Robe          robe;
        private String        tauxReclamation;
        private boolean       indicateurInedit;  // false,
        private String        musique;  // "0p7p3p1p",
        private String        nombreCourses;  // 22,
        private String        nombreVictoires; // 6,
        private String        nombrePlaces;  // 13,
        private String        nombrePlacesSecond;  // 3,
        private String        nombrePlacesTroisieme; //
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
        private DernierRapportDirect    dernierRapportDirect;
        private DernierRapportReference dernierRapportReference;
        private String                  urlCasaque;
        private CommentaireApresCourse  commentaireApresCourse;
        private String                  eleveur;
        private String                  paysEntrainement;
        private String                  allure;

        private class Robe implements Serializable {
            private String code; // :"020",
            private String libelleCourt; //:"BAI",
            private String libelleLong; //":"BAI"
        }

        public class GainPatricipant implements Serializable {

            private String gainsCarriere; // 3820700,
            private String gainsVictoires;  // 2704400,
            private String gainsPlace; // 1116300,
            private String gainsAnneeEnCours;  // 26139800,
            private String gainsAnneePrecedente; // 7865945
        }
        public class DistanceChevalPrecedent implements Serializable {
            private String      libelleCourt;  //1 L 1/2",
            private String      libelleLong;  // "1 longueur 1/2",
            private String      code;  // 2,
            private String      identifiant;  // "UNE_LONGUEUR_ET_DEMIE"
        }

        public class DernierRapportDirect implements Serializable {

            private  String typePari;  // "E_SIMPLE_GAGNANT",
            private  String rapport;  // 6.6,
            private  String typeRapport;  // "DIRECT",
            private  String indicateurTendance;  // "-",
            private  String nombreIndicateurTendance;  // -1.49,
            private  String dateRapport;  // 1707509929000,
            private  String permutation;  // 1,
            private  String favoris;  // false,
            private  String numPmu1;  // 1,
            private  String grossePrise;  // false
        }

        public class DernierRapportReference implements Serializable {

            private String  typePari;  // "E_SIMPLE_GAGNANT",
            private String  rapport;  // 5.8,
            private String  typeRapport;  // "REFERENCE",
            private String  indicateurTendance;  // " ",
            private String  nombreIndicateurTendance;  // 0.0,
            private String  dateRapport;  // 1707507905000,
            private String  permutation;  // 1,
            private String  favoris;  // false,
            private String  numPmu1;  // 1,
            private String  grossePrise;  // false
        }

        private class CommentaireApresCourse implements Serializable{
           private String texte;
           private String source;
        }
    }


}
