<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Bookings">

    <section class="reception-dashboard thp-section">
        <div class="container">
            <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
                <div>
                    <span class="eyebrow d-inline-block mb-2">Receptionist panel</span>
                    <h1 class="font-serif mb-0">Bookings &mdash; <span class="text-gradient-gold fst-italic"><fmt:parseDate value="${date}" pattern="yyyy-MM-dd" var="parsedDate"/><fmt:formatDate value="${parsedDate}" pattern="EEEE, d MMM yyyy"/></span></h1>
                </div>
                <div class="reception-date-nav d-flex align-items-center gap-2">
                    <a class="btn-thp-outline btn-sm" href="<c:url value='/reception?date=${prevDate}'/>">&larr; Prev</a>
                    <a class="btn-thp-outline btn-sm" href="<c:url value='/reception?date=${today}'/>">Today</a>
                    <a class="btn-thp-outline btn-sm" href="<c:url value='/reception?date=${nextDate}'/>">Next &rarr;</a>
                </div>
            </div>

            <p class="text-muted">Office hours: ${openHour}:00 &ndash; ${closeHour}:00. Lunch 1&ndash;2 PM is a hard block.
                A 2-hour booking auto-blocks the following hour (cascade).</p>

            <c:if test="${not empty toast}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${toast}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="slot-grid row g-3 mt-2">
                <c:forEach var="slot" items="${slots}">
                    <div class="col-6 col-md-4 col-lg-3">
                        <c:choose>
                            <c:when test="${slot.status == 'AVAILABLE'}">
                                <a class="slot-card available" href="<c:url value='/reception/new?date=${date}&hour=${slot.start.hour}'/>">
                                    <div class="slot-time">${slot.label()}</div>
                                    <div class="slot-state">Available</div>
                                </a>
                            </c:when>
                            <c:when test="${slot.status == 'BOOKED'}">
                                <a class="slot-card booked" href="<c:url value='/reception/booking/${slot.booking.id}'/>">
                                    <div class="slot-time">${slot.label()}</div>
                                    <div class="slot-state">${slot.booking.name}</div>
                                    <div class="slot-meta small">
                                        <c:choose>
                                            <c:when test="${slot.durationHours == 2}">2 h &middot; </c:when>
                                            <c:otherwise>1 h &middot; </c:otherwise>
                                        </c:choose>
                                        ${slot.booking.therapyType}
                                    </div>
                                </a>
                            </c:when>
                            <c:when test="${slot.status == 'BLOCKED_BY_CASCADE'}">
                                <div class="slot-card blocked">
                                    <div class="slot-time">${slot.label()}</div>
                                    <div class="slot-state">Blocked by previous 2-h booking</div>
                                </div>
                            </c:when>
                            <c:when test="${slot.status == 'LUNCH'}">
                                <div class="slot-card lunch">
                                    <div class="slot-time">${slot.label()}</div>
                                    <div class="slot-state">Lunch break</div>
                                </div>
                            </c:when>
                            <c:when test="${slot.status == 'PAST'}">
                                <div class="slot-card past">
                                    <div class="slot-time">${slot.label()}</div>
                                    <div class="slot-state">
                                        <c:choose>
                                            <c:when test="${not empty slot.booking}">${slot.booking.name}</c:when>
                                            <c:otherwise>Past</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>

            <div class="mt-4 small text-muted">
                <span class="legend-dot" style="background:#1f8a4c;"></span> Available
                <span class="legend-dot ms-3" style="background:#b03a3a;"></span> Booked
                <span class="legend-dot ms-3" style="background:#8a8a8a;"></span> Blocked (cascade)
                <span class="legend-dot ms-3" style="background:#c9a646;"></span> Lunch
                <span class="legend-dot ms-3" style="background:#ddd;"></span> Past
            </div>
        </div>
    </section>

</t:layout>
