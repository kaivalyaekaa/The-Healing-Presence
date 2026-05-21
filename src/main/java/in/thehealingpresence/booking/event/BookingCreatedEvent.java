package in.thehealingpresence.booking.event;

import in.thehealingpresence.booking.domain.Booking;

/**
 * Published after a receptionist booking is persisted. Listened-to by
 * {@link in.thehealingpresence.notification.BookingNotificationListener} on
 * {@link org.springframework.transaction.event.TransactionPhase#AFTER_COMMIT}
 * to fire email + Google Calendar side-effects.
 *
 * <p>Carries the domain {@link Booking} (not the persistence entity) — events
 * stay in the domain language; the listener is persistence-ignorant.
 */
public record BookingCreatedEvent(Booking booking) {
}
