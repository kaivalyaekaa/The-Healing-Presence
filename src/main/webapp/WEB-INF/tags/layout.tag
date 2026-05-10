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

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,500;0,600;0,700;1,400&family=Nunito+Sans:ital,wght@0,300;0,400;0,600;0,700&display=swap" rel="stylesheet">

    <!-- Site CSS (cache-busted in dev so edits show on every reload) -->
    <link rel="stylesheet" href="<c:url value='/css/thp.css'/>?v=<%= System.currentTimeMillis() %>">

    <c:if test="${not empty toast}">
        <meta name="thp-flash" content="${toast}">
    </c:if>
</head>
<body class="${bodyClass}">

    <!-- Top utility bar -->
    <div class="thp-topbar">
        <div class="container d-flex flex-wrap justify-content-between align-items-center">
            <div class="thp-topbar-left">
                <i class="bi bi-telephone-fill me-1"></i>
                <a href="tel:+918095008095">+91 8095-00-8095</a>
                <sec:authorize access="isAuthenticated()">
                    <span class="mx-2 d-none d-sm-inline">·</span>
                    <span class="d-none d-sm-inline">Hi, <sec:authentication property="name"/></span>
                    <form action="<c:url value='/logout'/>" method="post" class="d-inline ms-2">
                        <sec:csrfInput/>
                        <button type="submit" class="btn btn-link btn-sm p-0 text-decoration-none" style="color:inherit">Sign out</button>
                    </form>
                </sec:authorize>
            </div>
            <div class="thp-topbar-right d-flex align-items-center gap-2">
                <sec:authorize access="!isAuthenticated()">
                    <a class="thp-topbar-link d-none d-sm-inline" href="<c:url value='/login'/>">Staff sign in</a>
                </sec:authorize>
                <a class="thp-book-pill" href="<c:url value='/contact'/>">Book A Session <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </div>

    <!-- Sticky navigation -->
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
                    <img src="<c:url value='/images/logo.png'/>" alt="The Healing Presence" class="thp-logo-footer mb-3" height="64" width="auto">
                    <p class="small fst-italic">Solution lies within.</p>
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
