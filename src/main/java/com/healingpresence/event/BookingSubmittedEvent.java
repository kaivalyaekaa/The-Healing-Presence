package com.healingpresence.event;

import com.healingpresence.domain.BookingRequest;

public record BookingSubmittedEvent(BookingRequest request) {
}
