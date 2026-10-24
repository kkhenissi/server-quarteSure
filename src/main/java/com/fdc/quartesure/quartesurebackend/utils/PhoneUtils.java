package com.fdc.quartesure.quartesurebackend.utils;

/**
 * Normalisation des numéros de téléphone au format international (E.164), avec +33 (France) par défaut
 * pour les numéros nationaux qui commencent par 0. Prépare l'envoi de SMS ; sans effet sur les e-mails.
 */
public final class PhoneUtils {

    private static final String DEFAULT_COUNTRY_CODE = "+33";

    private PhoneUtils() {
    }

    /**
     * @return le numéro au format international (ex. +33612345678), ou null s'il est vide ou invalide
     */
    public static String toInternational(String raw) {
        if (raw == null) {
            return null;
        }
        // on retire espaces, points, tirets et parenthèses
        String cleaned = raw.trim().replaceAll("[\\s.\\-()]", "");
        if (cleaned.isEmpty()) {
            return null;
        }

        String international;
        if (cleaned.startsWith("+")) {
            international = cleaned;
        } else if (cleaned.startsWith("00")) {
            international = "+" + cleaned.substring(2);
        } else if (cleaned.startsWith("0") && cleaned.length() == 10) {
            // numéro national français : 06 12 34 56 78 -> +33 6 12 34 56 78
            international = DEFAULT_COUNTRY_CODE + cleaned.substring(1);
        } else {
            return null;
        }

        // Erreur de saisie courante : « +33 0612345678 » (zéro national gardé après l'indicatif)
        if (international.startsWith("+330")) {
            international = DEFAULT_COUNTRY_CODE + international.substring(4);
        }

        // + suivi de 8 à 15 chiffres
        return international.matches("^\\+[1-9]\\d{7,14}$") ? international : null;
    }
}
