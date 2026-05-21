<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Home">

    <!-- ============================== HERO (centered text + video) ============================== -->
    <section class="hero-centered">
        <div class="container text-center">
            <h1 class="hero-headline">Holistic Healing for <span class="text-gradient-gold fst-italic">Mind&nbsp;Body&nbsp;&amp;&nbsp;Soul</span></h1>
            <p class="hero-lede mx-auto">
                At The Healing Presence, we are dedicated to providing a sanctuary where individuals can embark
                on a transformative journey of healing, personal growth and self discovery. We believe in the
                innate power of every individual to create positive change in their lives. Our offerings are
                homed in empathy, mindfulness, and the invisible energy within us.
            </p>
            <div class="home-hero-video mx-auto mt-5">
                <video autoplay muted loop playsinline preload="metadata"
                       poster="<c:url value='/images/founders.jpg'/>">
                    <source src="<c:url value='/videos/home-hero.mp4'/>" type="video/mp4">
                </video>
            </div>
        </div>
    </section>

    <!-- ============================== FEATURES (4 icon cards) ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-4">
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card">
                        <div class="feature-icon"><i class="bi bi-patch-check-fill"></i></div>
                        <h3>Certified Professionals</h3>
                        <p class="small mb-0">Passionate experts to guide you through transformative experiences with their extensive training and expertise.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card">
                        <div class="feature-icon"><i class="bi bi-flask"></i></div>
                        <h3>Scientific Methodology</h3>
                        <p class="small mb-0">We leverage evidence-based practices and techniques to ensure tangible and effective results in your healing journey.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card">
                        <div class="feature-icon"><i class="bi bi-flower1"></i></div>
                        <h3>Tranquil Environment</h3>
                        <p class="small mb-0">Our nurturing space is specifically designed to foster a sense of calm and relaxation vital for holistic well-being.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card">
                        <div class="feature-icon"><i class="bi bi-graph-up-arrow"></i></div>
                        <h3>Measurable Healing</h3>
                        <p class="small mb-0">Witness your progress with quantifiable outcomes and celebrate your healing milestones confidently.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== PARTNER + FOUNDERS PHOTO ============================== -->
    <section class="thp-section">
        <div class="container text-center">
            <h2 class="section-title">Your Partner In <span class="text-gradient-gold fst-italic">Holistic Healing</span></h2>
            <p class="lead-narrow mx-auto mt-3">
                We are aligned to walk towards to the highest potential of your life to help you live a better life.
                Imagine, getting over all the things that are holding you back. We help you fight depression,
                anxiety, self-doubt, self-worth, and so many more battles that we struggle with every day.
            </p>
            <a class="btn-thp-text mt-3 d-inline-flex align-items-center" href="<c:url value='/about'/>">
                Learn More <i class="bi bi-arrow-right ms-2"></i>
            </a>

            <div class="founders-photo mx-auto mt-5">
                <img src="<c:url value='/images/founders.jpg'/>" alt="Founders of The Healing Presence" data-fallback="Founders Photo" class="img-fluid">
            </div>
        </div>
    </section>

    <!-- ============================== BIG SERVICE CARDS (Therapy / Training) ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-4">
                <div class="col-md-6">
                    <div class="big-service-card">
                        <div class="big-service-photo">
                            <img src="<c:url value='/images/team-photo.jpg'/>" alt="Holistic healing therapy session" data-fallback="Therapy Session">
                        </div>
                        <div class="big-service-body">
                            <h3 class="font-serif">Holistic Healing Therapy</h3>
                            <p class="small text-muted">Discover the power of healing within with compassionate care, guiding you towards inner peace and well-being.</p>
                            <a class="btn-thp-text" href="<c:url value='/therapy'/>">Learn More <i class="bi bi-arrow-right ms-1"></i></a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="big-service-card">
                        <div class="big-service-photo">
                            <img src="<c:url value='/images/therapist-portrait.jpg'/>" alt="Certified expert-led training session" data-fallback="Training Session">
                        </div>
                        <div class="big-service-body">
                            <h3 class="font-serif">Certified Expert-Led Training</h3>
                            <p class="small text-muted">Become a certified practitioner with expert-led training and tools to empower you on your healing journey.</p>
                            <a class="btn-thp-text" href="<c:url value='/training'/>">Learn More <i class="bi bi-arrow-right ms-1"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== SECONDARY CARDS (Tranquil / Merchandise) ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-4">
                <div class="col-md-6">
                    <a href="<c:url value='/rent-our-space'/>" class="text-decoration-none">
                        <div class="big-service-card">
                            <div class="big-service-photo">
                                <img src="<c:url value='/images/rent-space.jpg'/>" alt="Peaceful consultation lounge" data-fallback="Tranquil Space">
                            </div>
                            <div class="big-service-body">
                                <h3 class="font-serif">Peaceful Tranquil Space</h3>
                                <p class="small text-muted">The tranquil ambience, well-equipped facilities, and home-like feeling make it a perfect setting to create a nurturing experience.</p>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-md-6">
                    <div class="big-service-card">
                        <div class="big-service-photo">
                            <img src="<c:url value='/images/merchandise.jpg'/>" alt="Curated shelves of crystals and aromas" data-fallback="Merchandise">
                        </div>
                        <div class="big-service-body">
                            <h3 class="font-serif">Hand-Picked Merchandise</h3>
                            <p class="small text-muted">Handpicked collection of crystals, gemstones, aromatic oils and more to promote positive energy in your daily life.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== WHY CHOOSE US (arched reception photo + checklist) ============================== -->
    <section class="thp-section alt">
        <div class="container">
            <div class="row align-items-center g-5">
                <div class="col-lg-6 d-flex justify-content-center" style="overflow:hidden;">
                    <div class="arched-photo">
                        <img src="<c:url value='/images/home-hero.jpg'/>" alt="The Healing Presence reception" data-fallback="Reception">
                    </div>
                </div>
                <div class="col-lg-6">
                    <h2 class="section-title">Why Choose <br><span class="text-gradient-gold fst-italic">The Healing Presence?</span></h2>
                    <p class="mt-3">We offer you tailored solutions to help you live a better life.</p>
                    <ul class="thp-checklist list-unstyled mt-4">
                        <li>Driven by Empathy &amp; Compassion</li>
                        <li>Experienced &amp; Certified Practitioner</li>
                        <li>Interactive Training Sessions</li>
                        <li>Wide Range of Therapies</li>
                        <li>Tranquil Space For Workshops</li>
                        <li>Handpicked High-quality Merchandise</li>
                    </ul>
                    <a class="btn-thp-text mt-3 d-inline-flex align-items-center" href="<c:url value='/about'/>">
                        Learn More <span class="ms-2">&rarr;</span>
                    </a>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== TRAININGS (data-driven) ============================== -->
    <c:if test="${not empty trainings}">
        <section class="thp-section">
            <div class="container">
                <div class="text-center mb-5">
                    <div class="section-eyebrow">Our trainings</div>
                    <h2 class="section-title">Programmes for transformation</h2>
                </div>
                <div class="row g-4">
                    <c:forEach var="t" items="${trainings}">
                        <div class="col-md-6 col-lg-3">
                            <div class="training-card h-100">
                                <div class="body">
                                    <span class="badge-code">${t.code}</span>
                                    <h4 class="font-serif mt-3">${t.title}</h4>
                                    <p class="small text-muted">${t.description}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </c:if>

    <!-- ============================== TESTIMONIALS ============================== -->
    <c:if test="${not empty testimonials}">
        <section class="thp-section alt">
            <div class="container">
                <div class="text-center mb-5">
                    <div class="section-eyebrow">Voices of healing</div>
                    <h2 class="section-title">Stories from our community</h2>
                </div>
                <div class="row g-4">
                    <c:forEach var="ts" items="${testimonials}" end="2">
                        <div class="col-md-4">
                            <div class="testimonial-card">
                                <div class="stars">
                                    <c:forEach begin="1" end="${ts.rating}"><i class="bi bi-star-fill"></i></c:forEach>
                                </div>
                                <blockquote>&ldquo;${ts.body}&rdquo;</blockquote>
                                <div class="testimonial-author">
                                    <c:choose>
                                        <c:when test="${not empty ts.avatarPath}">
                                            <img src="<c:url value='${ts.avatarPath}'/>" alt="${ts.clientName}" class="testimonial-avatar">
                                        </c:when>
                                        <c:otherwise>
                                            <span class="testimonial-avatar testimonial-avatar-initials">
                                                ${fn:toUpperCase(fn:substring(ts.clientName,0,1))}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                    <cite>&mdash; ${ts.clientName}</cite>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </c:if>

    <!-- ============================== GLIMPSES (3-clip video gallery) ============================== -->
    <section class="thp-section alt">
        <div class="container">
            <div class="text-center mb-5">
                <div class="section-eyebrow">A glimpse inside</div>
                <h2 class="section-title">The <span class="text-gradient-gold fst-italic">Healing Presence</span>, in motion</h2>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="home-clip">
                        <video autoplay muted loop playsinline preload="metadata"
                               poster="<c:url value='/images/team-photo.jpg'/>">
                            <source src="<c:url value='/videos/home-clip-1.mp4'/>" type="video/mp4">
                        </video>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="home-clip">
                        <video autoplay muted loop playsinline preload="metadata"
                               poster="<c:url value='/images/therapist-portrait.jpg'/>">
                            <source src="<c:url value='/videos/home-clip-2.mp4'/>" type="video/mp4">
                        </video>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="home-clip">
                        <video autoplay muted loop playsinline preload="metadata"
                               poster="<c:url value='/images/rent-space.jpg'/>">
                            <source src="<c:url value='/videos/home-clip-3.mp4'/>" type="video/mp4">
                        </video>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== FAQ (home-specific) ============================== -->
    <section class="thp-section">
        <div class="container">
            <div class="text-center mb-5">
                <div class="section-eyebrow">FAQ</div>
                <h2 class="section-title">Frequently Asked <span class="text-gradient-gold fst-italic">Questions</span></h2>
            </div>
            <div class="row justify-content-center">
                <div class="col-lg-9">
                    <div class="faq-item open">
                        <button type="button" aria-expanded="true">
                            <span>How can therapy sessions at The Healing Presence benefit me</span>
                            <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                        </button>
                        <div class="answer">Our therapy sessions provide a safe and supportive environment for deep healing and personal growth. Through expert guidance, you can address emotional challenges, overcome limiting beliefs, and cultivate inner peace and well-being.</div>
                    </div>
                    <div class="faq-item">
                        <button type="button" aria-expanded="false">
                            <span>What can I expect from a crystal healing session</span>
                            <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                        </button>
                        <div class="answer">During a crystal healing session, you'll experience the gentle energy and vibrations of crystals, promoting balance and harmony within your mind, body, and spirit. It can help release energetic blockages, reduce stress, and enhance overall well-being.</div>
                    </div>
                    <div class="faq-item">
                        <button type="button" aria-expanded="false">
                            <span>Can the training programs be beneficial for personal growth even if I don't plan to become a practitioner?</span>
                            <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                        </button>
                        <div class="answer">Absolutely! Our training programs are designed not only for aspiring practitioners but also for individuals seeking personal growth and self-development. You'll gain valuable tools, insights, and techniques to enhance your own well-being and empower your journey.</div>
                    </div>
                    <div class="faq-item">
                        <button type="button" aria-expanded="false">
                            <span>How can hypnotherapy sessions at The Healing Presence benefit me?</span>
                            <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                        </button>
                        <div class="answer">Hypnotherapy is a powerful therapeutic modality that can assist in overcoming various challenges. At The Healing Presence, our skilled hypnotherapists guide you into a deep state of relaxation and tap into your subconscious mind to address issues such as anxiety, phobias, self-limiting beliefs, and more. By harnessing the power of your mind, hypnotherapy can help you create positive changes, boost confidence, and achieve personal transformation.</div>
                    </div>
                    <div class="faq-item">
                        <button type="button" aria-expanded="false">
                            <span>How can I rent space at The Healing Presence for my event or session</span>
                            <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                        </button>
                        <div class="answer">Renting our space is simple. Get in touch with our team to discuss your requirements, including the date, duration, and nature of your event or session. We'll provide you with the necessary information and assist you in creating a nurturing and inspiring environment for your participants.</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== CTA ============================== -->
    <section class="thp-section">
        <div class="container">
            <div class="cta-banner">
                <h2 class="font-serif">Ready to embark on a journey of healing and self-discovery?</h2>
                <a class="btn-thp-gold mt-4" href="<c:url value='/contact'/>">LET'S TALK <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </section>

</t:layout>
