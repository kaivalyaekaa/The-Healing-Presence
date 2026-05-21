package in.thehealingpresence.event;

import in.thehealingpresence.domain.BookingRequest;

public record BookingSubmittedEvent(BookingRequest request) {
}
