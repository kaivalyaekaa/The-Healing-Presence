<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Google Calendar integration">

    <section class="thp-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="mb-4">
                        <a class="btn-thp-text" href="<c:url value='/staff'/>">&larr; Back to staff dashboard</a>
                    </div>
                    <div class="text-center mb-4">
                        <span class="eyebrow d-inline-block mb-2">Admin &middot; Integration</span>
                        <h1 class="font-serif">Google Calendar</h1>
                        <p class="text-muted">
                            One-way push: receptionist-created bookings appear automatically on Upma's
                            Google Calendar. Public-form bookings are not pushed.
                        </p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            <strong>Connection error:</strong> ${error}
                        </div>
                    </c:if>

                    <c:if test="${justConnected}">
                        <div class="alert alert-success" role="alert">
                            Google Calendar connected. From now on, every receptionist booking will appear on the configured calendar.
                        </div>
                    </c:if>

                    <div class="bg-white p-4 rounded-4 border">
                        <c:choose>
                            <c:when test="${not configured}">
                                <h3 class="font-serif">Not configured</h3>
                                <p class="text-muted">
                                    The environment variables <code>GOOGLE_CLIENT_ID</code> and
                                    <code>GOOGLE_CLIENT_SECRET</code> are not set. Configure a Google Cloud project,
                                    enable the Calendar API, create an OAuth Client ID (Web application),
                                    and export the credentials before restarting the app.
                                </p>
                            </c:when>
                            <c:when test="${connected}">
                                <div class="d-flex align-items-start gap-3">
                                    <span class="badge bg-success p-2"><i class="bi bi-check2-circle"></i></span>
                                    <div>
                                        <h3 class="font-serif mb-1">Connected</h3>
                                        <p class="mb-0 text-muted">
                                            <c:if test="${not empty connectedAt}">
                                                Last updated <code>${connectedAt}</code>.
                                            </c:if>
                                            <c:if test="${not empty scope}">
                                                <br>Scope: <code>${scope}</code>
                                            </c:if>
                                        </p>
                                    </div>
                                </div>
                                <hr>
                                <p class="small text-muted mb-3">If Upma's account changes, you can reconnect to refresh the stored token.</p>
                                <a class="btn-thp-outline" href="<c:url value='/admin/google-calendar/connect'/>">Reconnect</a>
                            </c:when>
                            <c:otherwise>
                                <h3 class="font-serif">Not connected yet</h3>
                                <p class="text-muted">
                                    Click below to sign in with Upma's Google account and authorize
                                    The Healing Presence to create calendar events on her behalf.
                                </p>
                                <a class="btn-thp-primary mt-2" href="<c:url value='/admin/google-calendar/connect'/>">
                                    Connect Google Calendar <i class="bi bi-arrow-right"></i>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
