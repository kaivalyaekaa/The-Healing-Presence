<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="Therapy">

    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">Therapy</span>
            <h1 class="hero-headline">Discover the path to <span class="text-gradient-gold fst-italic">Healing and Joy</span></h1>
            <p class="hero-lede mx-auto">
                Reach out and book a session &mdash; you're never alone in this journey.
            </p>
            <p class="hero-lede mx-auto mt-3">
                At The Healing Presence, we believe healing is personal, and so is our care.
                Our therapists walk with you, through every step of your journey. This is a space
                where your story is heard, your pain is honored, and your growth is celebrated.
                Choose The Healing Presence &mdash; because you deserve more than just therapy,
                you deserve transformation.
            </p>
            <a class="btn-thp-primary mt-4" href="#bookingFormHolder">Book A Session <i class="bi bi-arrow-right"></i></a>

            <div class="therapy-hero-photo mx-auto mt-5">
                <img src="<c:url value='/images/therapy-room.jpg'/>" alt="Therapy room at The Healing Presence" data-fallback="Therapy Room">
            </div>
        </div>
    </section>

    <!-- ===================== 13 MODALITIES ===================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row g-4">
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-cloud-moon"></i></div>
                        <h3>Clinical Hypnotherapy</h3>
                        <p class="small mb-0">Experience deep relaxation and tap into your inner resources for personal transformation, create positive change and overcome challenges.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-arrow-counterclockwise"></i></div>
                        <h3>Transpersonal Regression Therapy</h3>
                        <p class="small mb-0">Explore past experiences and uncover insights for healing and growth. Release unresolved emotions and patterns for profound healing.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-diagram-3"></i></div>
                        <h3>Family Constellations</h3>
                        <p class="small mb-0">Heal family dynamics and unresolved issues through this transformative therapy. Restore balance and harmony within relationships.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-x-circle"></i></div>
                        <h3>Smoking Cessation / De-Addiction</h3>
                        <p class="small mb-0">Get personalized support and effective techniques to overcome cravings and live a healthier, addiction-free life.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-shield-shaded"></i></div>
                        <h3>Addressing Allergies</h3>
                        <p class="small mb-0">Identify and address the root causes of allergies, helping you regain balance and restore well-being through holistic approaches.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-chat-heart"></i></div>
                        <h3>Counselling Sessions</h3>
                        <p class="small mb-0">Explore challenges, gain clarity, and get compassionate guidance and support through personalized counselling sessions.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-music-note-beamed"></i></div>
                        <h3>Sound Healing</h3>
                        <p class="small mb-0">Experience deep relaxation, release stress, and restore energetic balance for holistic well-being through soothing vibrations and frequencies of sound.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-stars"></i></div>
                        <h3>Access Bars</h3>
                        <p class="small mb-0">Open up new possibilities, rebalance and rejuvenate your mind, body, and spirit through gentle touch therapy.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-gem"></i></div>
                        <h3>Crystal Healing</h3>
                        <p class="small mb-0">Harness the healing energy and the gentle power of crystals to support physical, emotional, and spiritual healing.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-arrow-down-circle"></i></div>
                        <h3>Pendulum Dowsing</h3>
                        <p class="small mb-0">Unlock experienced wisdom and access subconscious information and receive answers to your questions through this divination technique.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-camera"></i></div>
                        <h3>Aura Photography</h3>
                        <p class="small mb-0">Gain insights into your energy field and explore the colours and patterns that reflect your emotional and spiritual state.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-flower2"></i></div>
                        <h3>Chakra Healing</h3>
                        <p class="small mb-0">Restore harmony to your chakra system and unblock and balance your energy centres to promote vitality, balance, and holistic well-being.</p>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="feature-card"><div class="feature-icon"><i class="bi bi-arrow-down-up"></i></div>
                        <h3>Weight Loss Program</h3>
                        <p class="small mb-0">Embark on a personalized weight loss journey that combines holistic approaches, nutrition guidance, and emotional support.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <c:if test="${not empty therapists}">
        <section class="thp-section alt">
            <div class="container">
                <div class="text-center mb-5">
                    <div class="section-eyebrow">Meet your therapists</div>
                    <h2 class="section-title">Walking with you</h2>
                </div>
                <div class="row g-4">
                    <c:forEach var="th" items="${therapists}">
                        <div class="col-md-6 col-lg-4">
                            <div class="therapist-card">
                                <div class="photo">
                                    <c:choose>
                                        <c:when test="${not empty th.photoPath}">
                                            <img src="<c:url value='${th.photoPath}'/>" alt="${th.name}" data-fallback="${th.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="image-fallback">${th.name}</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="body">
                                    <span class="title">${th.title}</span>
                                    <h4 class="font-serif mt-2">${th.name}</h4>
                                    <p class="small text-muted mb-0">${th.bio}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </c:if>

    <!-- ============================== WORKSHOP / CERTIFICATION SHOWCASE ============================== -->
    <section class="thp-section">
        <div class="container">
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <div class="therapy-workshop-photo">
                        <img src="<c:url value='/images/therapy-workshop.jpg'/>" alt="Recent workshop graduates with their certificates" data-fallback="Workshop Graduates">
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="section-eyebrow">From the practice</div>
                    <h2 class="section-title">Real practice, <span class="text-gradient-gold fst-italic">real graduates</span></h2>
                    <p class="mt-3">Our practitioner workshops &mdash; Access Bars, Crystal Healing, Hypnotherapy, and more &mdash; produce certified facilitators who go on to hold space for their own communities. Healing, when shared, multiplies.</p>
                    <a class="btn-thp-outline mt-2" href="<c:url value='/training'/>">See our training programmes <i class="bi bi-arrow-right"></i></a>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== HAND-PICKED MERCHANDISE ============================== -->
    <section class="thp-section alt">
        <div class="container">
            <div class="row align-items-center g-5 flex-lg-row-reverse">
                <div class="col-lg-7">
                    <div class="therapy-workshop-photo">
                        <img src="<c:url value='/images/therapy-merch.jpg'/>" alt="Curated shelves of crystals, aromas and spiritual merchandise" data-fallback="Merchandise">
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="section-eyebrow">Take home a piece</div>
                    <h2 class="section-title">Hand-picked <span class="text-gradient-gold fst-italic">merchandise</span></h2>
                    <p class="mt-3">A curated collection of crystals, gemstones, aromatic oils, incense and singing bowls &mdash; carefully selected to support your daily practice and bring positive energy into your home.</p>
                </div>
            </div>
        </div>
    </section>

    <c:if test="${not empty faqs}">
        <section class="thp-section">
            <div class="container">
                <div class="row g-5">
                    <div class="col-lg-5">
                        <div class="section-eyebrow">FAQ</div>
                        <h2 class="section-title">Common questions</h2>
                        <p class="text-muted">If you don't see what you're looking for, please reach out &mdash; we're happy to chat.</p>
                        <a class="btn-thp-outline" href="<c:url value='/contact'/>">Ask a question <i class="bi bi-arrow-right"></i></a>
                    </div>
                    <div class="col-lg-7">
                        <c:forEach var="f" items="${faqs}" varStatus="loop">
                            <div class="faq-item ${loop.first ? 'open' : ''}">
                                <button type="button" aria-expanded="${loop.first ? 'true' : 'false'}">
                                    <span>${f.question}</span>
                                    <span class="chevron"><i class="bi bi-chevron-down"></i></span>
                                </button>
                                <div class="answer">${f.answer}</div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </section>
    </c:if>

    <c:if test="${not empty testimonials}">
        <section class="thp-section alt">
            <div class="container">
                <div class="text-center mb-5">
                    <div class="section-eyebrow">Voices of healing</div>
                    <h2 class="section-title">What our clients share</h2>
                </div>
                <div class="row g-4">
                    <c:forEach var="ts" items="${testimonials}" end="2">
                        <div class="col-md-4">
                            <div class="testimonial-card">
                                <div class="stars">
                                    <c:forEach begin="1" end="${ts.rating}"><i class="bi bi-star-fill"></i></c:forEach>
                                </div>
                                <blockquote>&ldquo;${ts.body}&rdquo;</blockquote>
                                <cite>&mdash; ${ts.clientName}</cite>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </c:if>

    <!-- ===================== BOOKING (AJAX) ===================== -->
    <section class="thp-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="text-center mb-4">
                        <div class="section-eyebrow">Book a session</div>
                        <h2 class="section-title">Begin your journey</h2>
                        <p class="text-muted">Submit a request and our team will reach out within one business day.</p>
                    </div>

                    <div id="bookingFormHolder">
                        <form:form action="${pageContext.request.contextPath}/book-session" method="post"
                                   modelAttribute="bookingForm"
                                   cssClass="thp-form bg-white p-4 rounded-4 border"
                                   data-thp-ajax="true" data-success-target="#bookingFormHolder">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="bookingName">Name</label>
                                    <form:input path="name" id="bookingName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="name" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="bookingEmail">Email</label>
                                    <form:input path="email" type="email" id="bookingEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="email" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="bookingPhone">Phone</label>
                                    <form:input path="phone" type="tel" id="bookingPhone" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="phone" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="bookingDate">Preferred date</label>
                                    <form:input path="preferredDate" type="date" id="bookingDate" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="preferredDate" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12">
                                    <label for="bookingTherapy">Therapy type</label>
                                    <form:select path="therapyType" id="bookingTherapy" cssClass="form-select" cssErrorClass="form-select is-invalid" required="required">
                                        <form:option value="">Select a modality&hellip;</form:option>
                                        <form:option value="Hypnotherapy">Hypnotherapy</form:option>
                                        <form:option value="Transpersonal Regression">Transpersonal Regression</form:option>
                                        <form:option value="Family Constellations">Family Constellations</form:option>
                                        <form:option value="Smoking Cessation">Smoking Cessation</form:option>
                                        <form:option value="Allergies">Allergies</form:option>
                                        <form:option value="Counselling">Counselling</form:option>
                                        <form:option value="Sound Healing">Sound Healing</form:option>
                                        <form:option value="Access Bars">Access Bars</form:option>
                                        <form:option value="Crystal Healing">Crystal Healing</form:option>
                                        <form:option value="Pendulum Dowsing">Pendulum Dowsing</form:option>
                                        <form:option value="Aura Photography">Aura Photography</form:option>
                                        <form:option value="Chakra Healing">Chakra Healing</form:option>
                                        <form:option value="Weight Loss">Weight Loss</form:option>
                                        <form:option value="Other">Other</form:option>
                                    </form:select>
                                    <form:errors path="therapyType" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12">
                                    <label for="bookingNotes">Anything else you'd like to share?</label>
                                    <form:textarea path="notes" id="bookingNotes" cssClass="form-control" rows="3"/>
                                    <form:errors path="notes" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12 text-end">
                                    <button type="submit" class="btn-thp-primary">Request booking <i class="bi bi-arrow-right"></i></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
