<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<t:layout title="Booking detail">

    <section class="thp-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-7">
                    <div class="mb-4">
                        <c:if test="${not empty booking.slotStart}">
                            <a class="btn-thp-text" href="<c:url value='/reception?date=${booking.slotStart.toLocalDate()}'/>">&larr; Back to day grid</a>
                        </c:if>
                    </div>

                    <div class="bg-white p-4 rounded-4 border">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div>
                                <span class="eyebrow d-inline-block mb-2">Booking #${booking.id}</span>
                                <h2 class="font-serif mb-1">${booking.name}</h2>
                                <div class="text-muted">
                                    <c:choose>
                                        <c:when test="${booking.status == 'CANCELLED'}">
                                            <span class="badge bg-secondary">Cancelled</span>
                                        </c:when>
                                        <c:when test="${booking.status == 'CONFIRMED'}">
                                            <span class="badge bg-success">Confirmed</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-info">${booking.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="ms-2">${booking.bookingSource}</span>
                                </div>
                            </div>
                        </div>

                        <dl class="row mb-0">
                            <dt class="col-sm-4">When</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${not empty booking.slotStart}">
                                        ${booking.slotStart.toLocalDate()} &middot;
                                        ${booking.slotStart.toLocalTime()} &ndash; ${booking.slotEnd.toLocalTime()}
                                        <span class="text-muted">(${booking.durationHours} h)</span>
                                    </c:when>
                                    <c:otherwise>${booking.preferredDate}</c:otherwise>
                                </c:choose>
                            </dd>

                            <dt class="col-sm-4">Therapy</dt>
                            <dd class="col-sm-8">${booking.therapyType}</dd>

                            <dt class="col-sm-4">Email</dt>
                            <dd class="col-sm-8"><a href="mailto:${booking.email}">${booking.email}</a></dd>

                            <dt class="col-sm-4">Phone</dt>
                            <dd class="col-sm-8"><a href="tel:${booking.phone}">${booking.phone}</a></dd>

                            <c:if test="${not empty booking.notes}">
                                <dt class="col-sm-4">Notes</dt>
                                <dd class="col-sm-8" style="white-space:pre-wrap;">${booking.notes}</dd>
                            </c:if>

                            <c:if test="${not empty booking.googleEventId}">
                                <dt class="col-sm-4">Google Calendar</dt>
                                <dd class="col-sm-8 small text-muted">Event ID: <code>${booking.googleEventId}</code></dd>
                            </c:if>
                        </dl>

                        <c:if test="${booking.status != 'CANCELLED'}">
                            <hr>
                            <form action="<c:url value='/reception/booking/${booking.id}/cancel'/>" method="post" class="d-inline">
                                <sec:csrfInput/>
                                <button type="submit" class="btn btn-outline-danger btn-sm"
                                        onclick="return confirm('Cancel this booking? The slot will be freed up.');">
                                    Cancel booking
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
