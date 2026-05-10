<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="Book a Free Consultation">

    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">Contact</span>
            <h1 class="hero-headline">Book a Free <span class="text-gradient-gold fst-italic">Consultation</span></h1>
            <p class="hero-lede mx-auto">
                Solution lies within. Tell us a little about what you're looking for &mdash;
                a team member will get back to you within one business day.
            </p>
        </div>
    </section>

    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-5">
                <div class="col-lg-5">
                    <h3 class="font-serif">Reach us</h3>
                    <p class="text-muted">Mon&ndash;Sat, 9am&ndash;7pm IST</p>
                    <ul class="list-unstyled">
                        <li class="mb-2"><i class="bi bi-telephone-fill me-2 text-gradient-gold"></i><a href="tel:+918095008095">+91 8095-00-8095</a></li>
                        <li class="mb-2"><i class="bi bi-telephone me-2"></i><a href="tel:+919545098905">+91 9545-098-905</a></li>
                        <li class="mb-2"><i class="bi bi-telephone me-2"></i><a href="tel:+919792250000">+91 9792-250-000</a></li>
                        <li class="mb-2"><i class="bi bi-envelope me-2"></i><a href="mailto:info@thehealingpresence.in">info@thehealingpresence.in</a></li>
                        <li class="mb-2"><i class="bi bi-geo-alt me-2"></i>Yelahanka, Bangalore, Karnataka</li>
                    </ul>
                    <div class="d-flex flex-wrap gap-2 mt-3">
                        <a href="https://wa.me/918095008095" class="btn-thp-outline" target="_blank" rel="noopener">
                            <i class="bi bi-whatsapp me-2"></i>WhatsApp us
                        </a>
                        <a href="https://www.instagram.com/the_healing_presence" class="btn-thp-outline" target="_blank" rel="noopener" aria-label="Instagram">
                            <i class="bi bi-instagram"></i>
                        </a>
                        <a href="https://www.facebook.com/TheHealingPresence.Life" class="btn-thp-outline" target="_blank" rel="noopener" aria-label="Facebook">
                            <i class="bi bi-facebook"></i>
                        </a>
                        <a href="https://www.youtube.com/@TheHealingPresence" class="btn-thp-outline" target="_blank" rel="noopener" aria-label="YouTube">
                            <i class="bi bi-youtube"></i>
                        </a>
                    </div>
                </div>
                <div class="col-lg-7">
                    <div id="contactFormHolder">
                        <form:form action="${pageContext.request.contextPath}/contact" method="post"
                                   modelAttribute="form"
                                   cssClass="thp-form bg-white p-4 rounded-4 border"
                                   data-thp-ajax="true" data-success-target="#contactFormHolder">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="firstName">First name</label>
                                    <form:input path="firstName" id="firstName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="firstName" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="lastName">Last name</label>
                                    <form:input path="lastName" id="lastName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="lastName" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="email">Email</label>
                                    <form:input path="email" type="email" id="email" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="email" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="phone">Phone</label>
                                    <form:input path="phone" type="tel" id="phone" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" pattern="^\+?\d[\d\s-]{7,15}$"/>
                                    <form:errors path="phone" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12">
                                    <label for="message">Message</label>
                                    <form:textarea path="message" id="message" cssClass="form-control" cssErrorClass="form-control is-invalid" rows="4"/>
                                    <form:errors path="message" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12">
                                    <div class="form-check">
                                        <form:checkbox path="consent" id="consent" cssClass="form-check-input" required="required"/>
                                        <label class="form-check-label small" for="consent">
                                            I agree to be contacted regarding my enquiry. I have read the privacy policy.
                                        </label>
                                    </div>
                                    <form:errors path="consent" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12 text-end">
                                    <button type="submit" class="btn-thp-primary">Send message <i class="bi bi-arrow-right"></i></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
