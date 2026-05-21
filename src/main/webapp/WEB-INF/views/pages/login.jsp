<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Sign in">

    <section class="hero-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-7 col-lg-5">
                    <div class="text-center mb-4">
                        <span class="eyebrow d-inline-block">Staff sign in</span>
                        <h1 class="mt-3">Welcome back</h1>
                        <p class="text-muted">Sign in with your LDAP directory credentials.</p>
                    </div>

                    <c:if test="${not empty loginError}">
                        <div class="alert alert-danger" role="alert">${loginError}</div>
                    </c:if>
                    <c:if test="${not empty loginInfo}">
                        <div class="alert alert-info" role="alert">${loginInfo}</div>
                    </c:if>

                    <form action="<c:url value='/login'/>" method="post" class="thp-form bg-white p-4 rounded-4 border">
                        <div class="mb-3">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="d-grid">
                            <button type="submit" class="btn-thp-primary">Sign in</button>
                        </div>
                    </form>

                    <c:if test="${isDevProfile}">
                        <div class="alert alert-warning small mt-4">
                            <strong>Demo credentials (dev profile only):</strong>
                            <br><code>admin</code> / <code>admin123</code> &mdash; ROLE_ADMIN, ROLE_STAFF, ROLE_RECEPTIONIST
                            <br><code>therapist</code> / <code>therapist123</code> &mdash; ROLE_STAFF
                            <br><code>reception</code> / <code>reception123</code> &mdash; ROLE_RECEPTIONIST
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </section>

</t:layout>
