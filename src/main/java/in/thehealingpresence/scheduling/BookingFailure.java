package in.thehealingpresence.scheduling;

/**
 * Known-business-reason booking rejections returned by
 * {@link SlotSchedulerService#tryBook}. Each value carries a user-facing
 * message that the controller renders directly back to the receptionist
 * without further interpretation.
 *
 * <p>Failures that are <i>not</i> business reasons (DB write failure, lost
 * connectivity, programmer error) still bubble as exceptions — this enum
 * deliberately does not cover them.
 */
public enum BookingFailure {

    INVALID_DURATION("Duration must be 1 or 2 hours."),
    OUTSIDE_HOURS("Slot start time is outside office hours or during lunch."),
    LUNCH_CROSSOVER("Cannot book — would cross the 1–2 PM lunch break."),
    CROSSES_CLOSE("Cannot book — would extend past office close."),
    OVERLAPS_EXISTING("Slot conflicts with an existing booking."),
    PAST_SLOT("Slot is in the past.");

    private final String userMessage;

    BookingFailure(String userMessage) {
        this.userMessage = userMessage;
    }

    public String userMessage() {
        return userMessage;
    }
}
