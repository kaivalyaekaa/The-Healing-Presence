package com.healingpresence.event;

import com.healingpresence.domain.ContactSubmission;

public record ContactSubmittedEvent(ContactSubmission submission) {
}
