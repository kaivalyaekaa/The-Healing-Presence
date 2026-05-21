<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="New booking">

    <section class="thp-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="mb-4">
                        <a class="btn-thp-text" href="<c:url value='/reception?date=${date}'/>">&larr; Back to ${date}</a>
                    </div>
                    <div class="text-center mb-4">
                        <span class="eyebrow d-inline-block mb-2">New booking</span>
                        <h1 class="font-serif">Book a session</h1>
                        <p class="text-muted">
                            <fmt:parseDate value="${date}" pattern="yyyy-MM-dd" var="parsedDate"/>
                            <fmt:formatDate value="${parsedDate}" pattern="EEEE, d MMM yyyy"/>
                            &middot; starting at ${hour}:00
                        </p>
                    </div>

                    <div id="bookingFormHolder">
                        <form:form action="${pageContext.request.contextPath}/reception/new" method="post"
                                   modelAttribute="receptionistBooking"
                                   cssClass="thp-form bg-white p-4 rounded-4 border">

                            <form:errors path="*" cssClass="alert alert-danger d-block mb-3" element="div"/>

                            <form:hidden path="slotDate"/>
                            <form:hidden path="slotHour"/>

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="clientName">Client name</label>
                                    <form:input path="clientName" id="clientName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="clientName" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="clientEmail">Email</label>
                                    <form:input path="clientEmail" type="email" id="clientEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="clientEmail" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="clientPhone">Phone</label>
                                    <form:input path="clientPhone" type="tel" id="clientPhone" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="clientPhone" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label>Duration</label>
                                    <div class="d-flex gap-3 mt-2">
                                        <div class="form-check">
                                            <form:radiobutton path="durationHours" value="1" id="dur1" cssClass="form-check-input"/>
                                            <label class="form-check-label" for="dur1">1 hour</label>
                                        </div>
                                        <c:if test="${twoHourPossible}">
                                            <div class="form-check">
                                                <form:radiobutton path="durationHours" value="2" id="dur2" cssClass="form-check-input"/>
                                                <label class="form-check-label" for="dur2">2 hours</label>
                                            </div>
                                        </c:if>
                                    </div>
                                    <c:if test="${not twoHourPossible}">
                                        <div class="form-text">2-hour booking not available for this slot (would cross lunch or office close).</div>
                                    </c:if>
                                </div>

                                <div class="col-12">
                                    <label for="therapyType">Therapy type</label>
                                    <form:select path="therapyType" id="therapyType" cssClass="form-select" cssErrorClass="form-select is-invalid" required="required">
                                        <form:option value="">Select a modality&hellip;</form:option>
                                        <form:option value="Clinical Hypnotherapy">Clinical Hypnotherapy</form:option>
                                        <form:option value="Transpersonal Regression Therapy">Transpersonal Regression Therapy</form:option>
                                        <form:option value="Family Constellations">Family Constellations</form:option>
                                        <form:option value="Smoking Cessation / De-Addiction">Smoking Cessation / De-Addiction</form:option>
                                        <form:option value="Addressing Allergies">Addressing Allergies</form:option>
                                        <form:option value="Counselling Sessions">Counselling Sessions</form:option>
                                        <form:option value="Sound Healing">Sound Healing</form:option>
                                        <form:option value="Access Bars">Access Bars</form:option>
                                        <form:option value="Crystal Healing">Crystal Healing</form:option>
                                        <form:option value="Pendulum Dowsing">Pendulum Dowsing</form:option>
                                        <form:option value="Aura Photography">Aura Photography</form:option>
                                        <form:option value="Chakra Healing">Chakra Healing</form:option>
                                        <form:option value="Weight Loss Program">Weight Loss Program</form:option>
                                        <form:option value="Other">Other</form:option>
                                    </form:select>
                                    <form:errors path="therapyType" cssClass="form-error" element="div"/>
                                </div>

                                <div class="col-12">
                                    <label for="notes">Notes (optional)</label>
                                    <form:textarea path="notes" id="notes" cssClass="form-control" rows="3"/>
                                    <form:errors path="notes" cssClass="form-error" element="div"/>
                                </div>

                                <div class="col-12 d-flex justify-content-between align-items-center">
                                    <a class="btn-thp-text" href="<c:url value='/reception?date=${date}'/>">Cancel</a>
                                    <button type="submit" class="btn-thp-primary">Confirm booking <i class="bi bi-check2"></i></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
