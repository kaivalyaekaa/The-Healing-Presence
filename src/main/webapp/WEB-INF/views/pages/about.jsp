<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="About">

    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">About</span>
            <h1 class="hero-headline">Transforming Lives One <span class="text-gradient-gold fst-italic">Session At A Time</span></h1>
        </div>
    </section>

    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-5 align-items-center">
                <div class="col-lg-6">
                    <span class="eyebrow mb-3 d-inline-block">Who we are</span>
                    <h2 class="section-title">A sanctuary in the city</h2>
                    <p>In a bustling city filled with noise and chaos, there is a hidden sanctuary known as The Healing Presence.</p>
                    <p>At its core, The Healing Presence is more than just a space; it is a beacon of hope, a sanctuary for the weary souls seeking healing and empowerment.</p>
                    <p>The Healing Presence is a place where mind, body, and spirit converge, where the power of ancient wisdom and modern techniques intertwine to create a holistic approach to well-being.</p>
                </div>
                <div class="col-lg-6">
                    <div class="hero-image-card" style="aspect-ratio: 4/3; border-radius: 1.5rem;">
                        <img src="<c:url value='/images/about-space.jpg'/>" alt="Our space" data-fallback="Our Space">
                    </div>
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-lg-10 mx-auto">
                    <p>From the gentle guidance of expert practitioners to the unconditional support of a compassionate community, each person who walks through its doors finds a nurturing environment that encourages growth and self-discovery.</p>
                    <p>The journey begins with the realization that true healing lies within oneself. Through therapies like hypnotherapy, transpersonal regression, and family constellations, people find the courage to heal generational wounds, break free from self-imposed limitations, and step into a life of authenticity.</p>
                    <p>The Healing Presence celebrates diversity and inclusivity, offering specialized services like queer counselling to create a safe space for everyone to be heard and supported.</p>
                    <p>The Healing Presence becomes a beacon of hope for those seeking transformation. It is a place where stories of resilience are written, where individuals find the strength to rise above their challenges, and where each person realizes the immense power they hold within.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Four core attributes (matching live site) -->
    <section class="thp-section alt">
        <div class="container">
            <div class="row g-4">
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-patch-check-fill"></i></div>
                        <h3>Certified Professionals</h3>
                        <p class="small mb-0">Passionate experts to guide you through transformative experiences with their extensive training and expertise.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-flask"></i></div>
                        <h3>Scientific Methodology</h3>
                        <p class="small mb-0">We leverage evidence-based practices and techniques to ensure tangible and effective results in your healing journey.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-flower1"></i></div>
                        <h3>Tranquil Environment</h3>
                        <p class="small mb-0">Our nurturing space is specifically designed to foster a sense of calm and relaxation vital for holistic well-being.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-graph-up-arrow"></i></div>
                        <h3>Measurable Healing</h3>
                        <p class="small mb-0">Witness your progress with quantifiable outcomes and celebrate your healing milestones confidently.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="thp-section">
        <div class="container">
            <div class="cta-banner">
                <h2 class="font-serif">Ready to embark on a journey of healing and self-discovery?</h2>
                <a class="btn-thp-gold mt-4" href="<c:url value='/contact'/>">LET'S TALK <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </section>

</t:layout>
