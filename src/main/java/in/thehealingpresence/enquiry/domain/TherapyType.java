package in.thehealingpresence.enquiry.domain;

import java.util.Arrays;
import java.util.Optional;

/**
 * Canonical list of therapy modalities offered by The Healing Presence.
 *
 * <p>Single source of truth — the public booking form, the receptionist booking
 * form, the DB column, and the email templates all reference this enum. Adding
 * a modality means editing this file + the JSP option list and nothing else.
 *
 * <p>{@link #display()} returns the user-facing label as it appears in the JSP
 * dropdowns. {@link #fromString(String)} parses either an enum constant name
 * (case-insensitive) or a display label (case-insensitive) so legacy DB rows
 * persisted as free-text continue to resolve cleanly.
 */
public enum TherapyType {
    HYPNOTHERAPY("Hypnotherapy"),
    TRANSPERSONAL_REGRESSION("Transpersonal Regression"),
    FAMILY_CONSTELLATIONS("Family Constellations"),
    SMOKING_CESSATION("Smoking Cessation"),
    ALLERGIES("Allergies"),
    COUNSELLING("Counselling"),
    SOUND_HEALING("Sound Healing"),
    ACCESS_BARS("Access Bars"),
    CRYSTAL_HEALING("Crystal Healing"),
    PENDULUM_DOWSING("Pendulum Dowsing"),
    AURA_PHOTOGRAPHY("Aura Photography"),
    CHAKRA_HEALING("Chakra Healing"),
    WEIGHT_LOSS("Weight Loss"),
    OTHER("Other");

    private final String display;

    TherapyType(String display) {
        this.display = display;
    }

    public String display() {
        return display;
    }

    /**
     * Resolve a string to a {@link TherapyType}.
     * Accepts either:
     * <ul>
     *   <li>The enum constant name, case-insensitive ({@code "HYPNOTHERAPY"}, {@code "hypnotherapy"})</li>
     *   <li>The display label, case-insensitive ({@code "Hypnotherapy"}, {@code "crystal healing"})</li>
     * </ul>
     */
    public static Optional<TherapyType> fromString(String raw) {
        if (raw == null || raw.isBlank()) return Optional.empty();
        String trimmed = raw.trim();
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(trimmed) || t.display.equalsIgnoreCase(trimmed))
                .findFirst();
    }
}
