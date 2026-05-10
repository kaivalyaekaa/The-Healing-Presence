<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Page not found">
    <section class="hero-section text-center">
        <div class="container">
            <span class="eyebrow d-inline-block">404</span>
            <h1 class="mt-3">This page wandered off the path</h1>
            <p class="lead text-muted mt-3">The page you're looking for doesn't exist — but your journey continues.</p>
            <a class="btn-thp-primary mt-3" href="<c:url value='/'/>">Return home <i class="bi bi-arrow-right"></i></a>
        </div>
    </section>
</t:layout>
