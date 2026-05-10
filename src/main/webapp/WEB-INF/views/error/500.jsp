<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Something went wrong">
    <section class="hero-section text-center">
        <div class="container">
            <span class="eyebrow d-inline-block">500</span>
            <h1 class="mt-3">Something went wrong on our end</h1>
            <p class="lead text-muted mt-3">Please try again in a moment, or reach out and we'll help directly.</p>
            <a class="btn-thp-primary mt-3" href="<c:url value='/'/>">Return home <i class="bi bi-arrow-right"></i></a>
        </div>
    </section>
</t:layout>
