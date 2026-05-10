<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="Rent our space">

    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">Rent our space</span>
            <h1 class="hero-headline">Rent our <span class="text-gradient-gold fst-italic">Space</span></h1>
            <p class="hero-lede mx-auto">
                We've built a serene and welcoming sanctuary at Yelahanka, Bangalore. The tranquil ambience,
                well-equipped facilities, and home-like feeling make it a perfect setting for practitioners
                to create a nurturing experience for their clients.
            </p>
            <a class="btn-thp-primary mt-4" href="#enquiryFormHolder">Enquire About Availability <i class="bi bi-arrow-right"></i></a>
        </div>
    </section>

    <!-- ===================== 4 SPACE TYPES ===================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-4">
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-grid-3x3-gap"></i></div>
                        <h3>Welcoming Modular Space</h3>
                        <p class="small mb-0">Our welcoming space can be customized to suit your specific needs, to create the perfect setting for your workshops, events, or healing sessions.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-mortarboard"></i></div>
                        <h3>Host Workshops &amp; Training</h3>
                        <p class="small mb-0">Utilize our space to host impactful workshops and training sessions, providing a conducive environment for learning, growth, and inspiration.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-door-closed"></i></div>
                        <h3>Private Counseling Rooms</h3>
                        <p class="small mb-0">Our counselling rooms offer a confidential and comfortable setting for one-on-one counselling sessions, ensuring privacy and a safe space for therapeutic conversations.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-stars"></i></div>
                        <h3>State-of-the-Art Facilities</h3>
                        <p class="small mb-0">Experience our space equipped with state-of-the-art amenities and facilities, including advanced audiovisual equipment, comfortable seating, and a professional environment.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="thp-section alt">
        <div class="container">
            <div class="row g-5">
                <div class="col-lg-6">
                    <h2 class="section-title">What's included</h2>
                    <ul class="list-unstyled mt-4">
                        <li class="d-flex gap-3 mb-3"><span class="value-bullet"><i class="bi bi-check2"></i></span><div>Multiple rooms &mdash; workshop hall, meditation room, group circle space</div></li>
                        <li class="d-flex gap-3 mb-3"><span class="value-bullet"><i class="bi bi-check2"></i></span><div>Yoga props, cushions, mats, blankets</div></li>
                        <li class="d-flex gap-3 mb-3"><span class="value-bullet"><i class="bi bi-check2"></i></span><div>Audio system, projector, whiteboards</div></li>
                        <li class="d-flex gap-3 mb-3"><span class="value-bullet"><i class="bi bi-check2"></i></span><div>Chai, water, simple meals on request</div></li>
                        <li class="d-flex gap-3 mb-3"><span class="value-bullet"><i class="bi bi-check2"></i></span><div>Outdoor garden space</div></li>
                    </ul>
                </div>
                <div class="col-lg-6">
                    <h2 class="section-title">Tell us about your event</h2>
                    <div id="enquiryFormHolder">
                        <form:form action="${pageContext.request.contextPath}/enquire-space" method="post"
                                   modelAttribute="enquiryForm"
                                   cssClass="thp-form bg-white p-4 rounded-4 border"
                                   data-thp-ajax="true" data-success-target="#enquiryFormHolder">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="enquiryName">Name</label>
                                    <form:input path="name" id="enquiryName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="name" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="enquiryEmail">Email</label>
                                    <form:input path="email" type="email" id="enquiryEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="email" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="enquiryPhone">Phone</label>
                                    <form:input path="phone" type="tel" id="enquiryPhone" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="phone" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="enquiryEventType">Event type</label>
                                    <form:select path="eventType" id="enquiryEventType" cssClass="form-select" cssErrorClass="form-select is-invalid" required="required">
                                        <form:option value="">Select&hellip;</form:option>
                                        <form:option value="Workshop">Workshop</form:option>
                                        <form:option value="Retreat">Retreat</form:option>
                                        <form:option value="Private session">Private session</form:option>
                                        <form:option value="Training programme">Training programme</form:option>
                                        <form:option value="Other">Other</form:option>
                                    </form:select>
                                    <form:errors path="eventType" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="enquiryDate">Preferred date</label>
                                    <form:input path="preferredDate" type="date" id="enquiryDate" cssClass="form-control"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="enquiryAttendees">Expected attendees</label>
                                    <form:input path="attendees" type="number" min="1" id="enquiryAttendees" cssClass="form-control"/>
                                </div>
                                <div class="col-12">
                                    <label for="enquiryMessage">Anything else?</label>
                                    <form:textarea path="message" id="enquiryMessage" cssClass="form-control" rows="3"/>
                                    <form:errors path="message" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12 text-end">
                                    <button type="submit" class="btn-thp-primary">Send enquiry <i class="bi bi-arrow-right"></i></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
