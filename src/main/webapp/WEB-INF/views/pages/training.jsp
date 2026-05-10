<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Training">

    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">Training</span>
            <h1 class="hero-headline"><span class="text-gradient-gold fst-italic">Expert-Led</span> Training</h1>
            <p class="hero-lede mx-auto">
                Unlock your true potential and learn transformative healing techniques to help people
                lead better lives. Become a certified practitioner with expert-led training and tools
                to empower you on your personal and professional journey.
            </p>
            <a class="btn-thp-primary mt-4" href="<c:url value='/contact'/>">Register For A Training <i class="bi bi-arrow-right"></i></a>
        </div>
    </section>

    <c:if test="${not empty trainings}">
        <section class="thp-section">
            <div class="container">
                <div class="row g-4">
                    <c:forEach var="tr" items="${trainings}">
                        <div class="col-md-6">
                            <div class="training-card h-100">
                                <div class="body">
                                    <span class="badge-code">${tr.code}</span>
                                    <h3 class="font-serif mt-3">${tr.title}</h3>
                                    <p class="text-muted">${tr.description}</p>
                                    <a class="btn-thp-outline mt-2" href="<c:url value='/contact'/>">Learn More <i class="bi bi-arrow-right"></i></a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </c:if>

    <section class="thp-section alt">
        <div class="container">
            <div class="cta-banner">
                <h2 class="font-serif">Ready to learn with us?</h2>
                <p class="lead mt-3 mb-4">Cohorts are intentionally small. Talk to us about the next intake.</p>
                <a class="btn-thp-gold" href="<c:url value='/contact'/>">Speak to our team <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </section>

</t:layout>
