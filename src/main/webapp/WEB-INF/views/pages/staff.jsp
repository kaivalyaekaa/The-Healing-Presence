<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Staff dashboard">

    <section class="hero-section">
        <div class="container">
            <span class="eyebrow d-inline-block">Admin area</span>
            <h1 class="mt-3">Hello, <span class="text-gradient-gold fst-italic">${displayName}</span></h1>
            <p class="text-muted">You are signed in as administrator.</p>
        </div>
    </section>

    <section class="thp-section">
        <div class="container">
            <div class="row g-4">
                <div class="col-md-6">
                    <div class="service-card text-start">
                        <h3>Your roles</h3>
                        <ul class="list-unstyled mt-3">
                            <c:forEach var="auth" items="${authorities}">
                                <li><i class="bi bi-shield-check me-2" style="color:var(--thp-gold-deep);"></i>${auth.authority}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="service-card text-start">
                        <h3>Quick links</h3>
                        <ul class="list-unstyled mt-3">
                            <sec:authorize access="hasRole('ADMIN')">
                                <li class="mb-2"><a href="<c:url value='/reception'/>"><i class="bi bi-calendar-check me-2" style="color:var(--thp-gold-deep);"></i>Reception bookings panel</a></li>
                            </sec:authorize>
                            <sec:authorize access="hasRole('ADMIN')">
                                <li class="mb-2"><a href="<c:url value='/admin/google-calendar'/>"><i class="bi bi-google me-2" style="color:var(--thp-gold-deep);"></i>Google Calendar integration</a></li>
                            </sec:authorize>
                            <li class="mb-2"><a href="<c:url value='/'/>"><i class="bi bi-arrow-right-short"></i>Back to public site</a></li>
                        </ul>
                        <form action="<c:url value='/logout'/>" method="post" class="mt-3">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn-thp-outline">Sign out</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
