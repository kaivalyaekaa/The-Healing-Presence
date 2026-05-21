<%@ tag description="Site-wide layout for The Healing Presence" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="bodyClass" required="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="The Healing Presence — a holistic therapy and training centre in Yelahanka, Bangalore.">
    <title><c:choose><c:when test="${not empty title}">${title} — The Healing Presence</c:when><c:otherwise>The Healing Presence</c:otherwise></c:choose></title>

    <link rel="icon" type="image/x-icon" href="<c:url value='/favicon.ico'/>">

    <!-- Bootstrap 5 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- Google Fonts:
         Source Sans 3 — the open-source twin of Adobe's Myriad Pro
         (Adobe designed it to be a free Myriad alternative). Modern, clean body.
         Cormorant Garamond — elegant serif kept for headlines + display copy. -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,500;0,600;0,700;1,400&family=Source+Sans+3:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&display=swap" rel="stylesheet">

    <!-- CSRF token for AJAX POSTs — main.js reads these meta tags -->
    <sec:csrfMetaTags/>

    <!-- Site CSS (cache-busted in dev so edits show on every reload) -->
    <link rel="stylesheet" href="<c:url value='/css/thp.css'/>?v=<%= System.currentTimeMillis() %>">

    <c:if test="${not empty toast}">
        <meta name="thp-flash" content="${toast}">
    </c:if>
</head>
<body class="${bodyClass}">

    <!-- Sticky navigation — utility links are absorbed into the right side of the nav for a single, cleaner band. -->
    <nav class="navbar navbar-expand-lg sticky-top thp-navbar">
        <div class="container">
            <a class="navbar-brand thp-brand" href="<c:url value='/'/>">
                <img src="<c:url value='/images/logo.png'/>" alt="The Healing Presence" class="thp-logo" height="56" width="auto">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#thpNav"
                    aria-controls="thpNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="thpNav">
                <ul class="navbar-nav ms-auto align-items-lg-center">
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/'/>">Home</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/therapy'/>">Therapy</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/training'/>">Training</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/rent-our-space'/>">Rent our space</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/accommodations'/>">Accommodations</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/contact'/>">Contact</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/about'/>">About</a></li>
                    <sec:authorize access="hasRole('ADMIN')">
                        <li class="nav-item"><a class="nav-link nav-link-admin" href="<c:url value='/reception'/>"><i class="bi bi-calendar-check me-1"></i>Bookings</a></li>
                        <li class="nav-item"><a class="nav-link nav-link-admin" href="<c:url value='/admin/google-calendar'/>"><i class="bi bi-google me-1"></i>Calendar</a></li>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <li class="nav-item d-flex align-items-center ms-lg-2">
                            <form action="<c:url value='/logout'/>" method="post" class="d-inline m-0">
                                <sec:csrfInput/>
                                <button type="submit" class="btn-thp-ghost btn-sm">Sign out</button>
                            </form>
                        </li>
                    </sec:authorize>
                    <li class="nav-item ms-lg-2"><a class="btn-thp-primary btn-sm" href="<c:url value='/contact'/>">Book A Session <i class="bi bi-arrow-right ms-1"></i></a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page content -->
    <main>
        <jsp:doBody/>
    </main>

    <!-- Footer -->
    <footer class="thp-footer">
        <div class="container">
            <div class="row gy-4">
                <div class="col-md-4">
                    <a href="<c:url value='/'/>" class="d-inline-block mb-3">
                        <img src="<c:url value='/images/logo.png'/>" alt="The Healing Presence" class="thp-logo-footer" height="84" width="auto">
                    </a>
                    <p class="thp-tagline-footer fst-italic">Solution lies within.</p>
                    <div class="thp-social mt-3">
                        <a href="https://www.instagram.com/the_healing_presence" target="_blank" rel="noopener" aria-label="Instagram"><i class="bi bi-instagram"></i></a>
                        <a href="https://www.facebook.com/TheHealingPresence.Life" target="_blank" rel="noopener" aria-label="Facebook"><i class="bi bi-facebook"></i></a>
                        <a href="https://www.youtube.com/@TheHealingPresence" target="_blank" rel="noopener" aria-label="YouTube"><i class="bi bi-youtube"></i></a>
                        <a href="https://wa.me/918095008095" target="_blank" rel="noopener" aria-label="WhatsApp"><i class="bi bi-whatsapp"></i></a>
                    </div>
                </div>
                <div class="col-md-2 col-6">
                    <h6 class="text-uppercase small fw-bold">Visit</h6>
                    <ul class="list-unstyled small">
                        <li><a href="<c:url value='/about'/>">About</a></li>
                        <li><a href="<c:url value='/therapy'/>">Therapy</a></li>
                        <li><a href="<c:url value='/training'/>">Training</a></li>
                        <li><a href="<c:url value='/accommodations'/>">Accommodations</a></li>
                    </ul>
                </div>
                <div class="col-md-3 col-6">
                    <h6 class="text-uppercase small fw-bold">Connect</h6>
                    <ul class="list-unstyled small">
                        <li><a href="<c:url value='/rent-our-space'/>">Rent our space</a></li>
                        <li><a href="<c:url value='/contact'/>">Contact us</a></li>
                        <li><a href="tel:+918095008095">+91 8095-00-8095</a></li>
                        <li><a href="tel:+919545098905">+91 9545-098-905</a></li>
                        <li><a href="mailto:info@thehealingpresence.in">info@thehealingpresence.in</a></li>
                    </ul>
                </div>
                <div class="col-md-3">
                    <h6 class="text-uppercase small fw-bold">Address</h6>
                    <p class="small mb-0">Yelahanka, Bangalore<br>Karnataka, India</p>
                </div>
            </div>
            <hr class="border-secondary mt-4">
            <div class="d-flex flex-wrap justify-content-between align-items-center small">
                <p class="mb-0">Copyright &copy; <%= java.time.Year.now().getValue() %> The Healing Presence</p>
                <p class="mb-0">Solution lies within.</p>
            </div>
        </div>
    </footer>

    <!-- WhatsApp FAB -->
    <a class="whatsapp-fab" href="https://wa.me/918095008095" target="_blank" rel="noopener" aria-label="WhatsApp us">
        <i class="bi bi-whatsapp" style="font-size: 1.5rem;"></i>
    </a>

    <!-- jQuery (for AJAX-friendly use cases) -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- Bootstrap bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Site JS (cache-busted in dev) -->
    <script src="<c:url value='/js/main.js'/>?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
