package com.cipriano.utils;


import com.cipriano.Main;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class POSEntryModes {

    public static final int PAN_ENTRY_MODE_UNKNOWN = 0;
    public static final int PAN_ENTRY_MODE_MANUAL = 1;
    public static final int PAN_ENTRY_MODE_MAGSTRIPE = 2;
    public static final int PAN_ENTRY_MODE_ICC = 5;
    public static final int PAN_ENTRY_MODE_CONTACTLESS = 7;
    public static final int PAN_ENTRY_MODE_ECOMMERCE = 8;
    public static final int PAN_ENTRY_MODE_MAGSTRIPE_UNALTERED = 9;

    public static final int PIN_ENTRY_CAPABILITY_UNKNOWN = 0;
    public static final int PIN_ENTRY_CAPABILITY_CAPTURED = 1;
    public static final int PIN_ENTRY_CAPABILITY_NOT_CAPTURED = 2;

    public static final int SECURITY_METHOD_UNKNOWN = 0;
    public static final int SECURITY_METHOD_PIN_VERIFIED_BY_ICC = 1;
    public static final int SECURITY_METHOD_PIN_VERIFIED_BY_ISSUER_HOST = 2;
    public static final int SECURITY_METHOD_PIN_VERIFIED_BY_OFFLINE_PIN_PAD = 3;
    public static final int SECURITY_METHOD_NO_PIN_VERIFICATION = 4;

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void getEntryModeDescription(int entryMode) {
        int panEntryMode = getPanEntryMode(entryMode);
        int pinEntryCapability = getPinEntryCapability(entryMode);
        int securityMethod = getSecurityMethod(entryMode);

        LOGGER.info("PAN Entry Mode: {}", getPanEntryModeDescription(panEntryMode));
        LOGGER.info("PIN Entry Capability: {}", getPinEntryCapabilityDescription(pinEntryCapability));
        LOGGER.info("Security Method: {}", getSecurityMethodDescription(securityMethod));
    }

    // Helper methods to get descriptions (optional)
    public static String getPanEntryModeDescription(int panEntryMode) {
        return switch (panEntryMode) {
            case PAN_ENTRY_MODE_UNKNOWN -> "Unknown";
            case PAN_ENTRY_MODE_MANUAL -> "Manual key entry";
            case PAN_ENTRY_MODE_MAGSTRIPE -> "Magnetic stripe read";
            case PAN_ENTRY_MODE_ICC -> "ICC (chip) read";
            case PAN_ENTRY_MODE_CONTACTLESS -> "Contactless (NFC) read";
            case PAN_ENTRY_MODE_ECOMMERCE -> "E-commerce";
            case PAN_ENTRY_MODE_MAGSTRIPE_UNALTERED -> "Magnetic stripe read (unaltered)";
            default -> "Invalid PAN Entry Mode";
        };
    }

    public static String getPinEntryCapabilityDescription(int pinEntryCapability) {
        return switch (pinEntryCapability) {
            case PIN_ENTRY_CAPABILITY_UNKNOWN -> "Unknown";
            case PIN_ENTRY_CAPABILITY_CAPTURED -> "PIN captured";
            case PIN_ENTRY_CAPABILITY_NOT_CAPTURED -> "No PIN captured";
            default -> "Invalid PIN Entry Capability";
        };
    }

    public static String getSecurityMethodDescription(int securityMethod) {
        return switch (securityMethod) {
            case SECURITY_METHOD_UNKNOWN -> "Unknown";
            case SECURITY_METHOD_PIN_VERIFIED_BY_ICC -> "PIN verified by ICC";
            case SECURITY_METHOD_PIN_VERIFIED_BY_ISSUER_HOST -> "PIN verified by issuer host";
            case SECURITY_METHOD_PIN_VERIFIED_BY_OFFLINE_PIN_PAD -> "PIN verified by offline PIN pad";
            case SECURITY_METHOD_NO_PIN_VERIFICATION -> "No PIN verification";
            default -> "Invalid Security Method";
        };
    }

    public static boolean isValidEntryMode(int posEntryMode) {
        if (posEntryMode < 0 || posEntryMode > 999) {
            return false;
        }

        int panEntryMode = getPanEntryMode(posEntryMode);
        int pinEntryCapability = getPinEntryCapability(posEntryMode);
        int securityMethod = getSecurityMethod(posEntryMode);

        // Check if each digit is within the valid range
        return isValidPanEntryMode(panEntryMode) &&
                isValidPinEntryCapability(pinEntryCapability) &&
                isValidSecurityMethod(securityMethod);
    }

    private static boolean isValidPanEntryMode(int panEntryMode) {
        return panEntryMode == PAN_ENTRY_MODE_UNKNOWN ||
                panEntryMode == PAN_ENTRY_MODE_MANUAL ||
                panEntryMode == PAN_ENTRY_MODE_MAGSTRIPE ||
                panEntryMode == PAN_ENTRY_MODE_ICC ||
                panEntryMode == PAN_ENTRY_MODE_CONTACTLESS ||
                panEntryMode == PAN_ENTRY_MODE_ECOMMERCE ||
                panEntryMode == PAN_ENTRY_MODE_MAGSTRIPE_UNALTERED;
    }

    private static boolean isValidPinEntryCapability(int pinEntryCapability) {
        return pinEntryCapability == PIN_ENTRY_CAPABILITY_UNKNOWN ||
                pinEntryCapability == PIN_ENTRY_CAPABILITY_CAPTURED ||
                pinEntryCapability == PIN_ENTRY_CAPABILITY_NOT_CAPTURED;
    }

    private static boolean isValidSecurityMethod(int securityMethod) {
        return securityMethod == SECURITY_METHOD_UNKNOWN ||
                securityMethod == SECURITY_METHOD_PIN_VERIFIED_BY_ICC ||
                securityMethod == SECURITY_METHOD_PIN_VERIFIED_BY_ISSUER_HOST ||
                securityMethod == SECURITY_METHOD_PIN_VERIFIED_BY_OFFLINE_PIN_PAD ||
                securityMethod == SECURITY_METHOD_NO_PIN_VERIFICATION;
    }

    private static int getSecurityMethod(int entryMode) {
        return entryMode % 10;
    }

    private static int getPinEntryCapability(int entryMode) {
        return (entryMode % 100) / 10;
    }

    private static int getPanEntryMode(int entryMode) {
        return entryMode / 100;
    }

}