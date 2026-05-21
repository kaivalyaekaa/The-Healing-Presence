<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<t:layout title="Vasudha — Stay with us">

    <!-- ============================== HERO ============================== -->
    <section class="hero-centered">
        <div class="container text-center">
            <span class="eyebrow mb-3 d-inline-block">Vasudha &middot; Yelahanka, Bengaluru</span>
            <h1 class="hero-headline">Indulge in the epitome of comfort at <span class="text-gradient-gold fst-italic">Vasudha</span></h1>
            <p class="hero-lede mx-auto">
                Our exquisite 3BHK serviced apartment &mdash; a sanctuary of luxury, comfort and rejuvenation.
                Experience more than a stay; indulge in an irresistible proposition at Vasudha.
            </p>
        </div>
    </section>

    <!-- ============================== HIGHLIGHT DETAILS (icon grid) ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="text-center mb-4">
                <div class="section-eyebrow">Highlight details of the property</div>
            </div>
            <div class="row g-4 justify-content-center">
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-house-heart"></i></span><span>Fully Furnished Rooms</span></div>
                </div>
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-snow"></i></span><span>AC Rooms</span></div>
                </div>
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-droplet"></i></span><span>Attached Toilets</span></div>
                </div>
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-cup-hot"></i></span><span>Functional Kitchen</span></div>
                </div>
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-wifi"></i></span><span>Free Wi-Fi</span></div>
                </div>
                <div class="col-6 col-md-4 col-lg-2">
                    <div class="vasudha-highlight"><span class="vh-icon"><i class="bi bi-puzzle"></i></span><span>Games &amp; Books</span></div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== INTRO COPY ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-9">
                    <p>Step into the epitome of comfort at Vasudha. Discover the charm of <strong>Gulmohar</strong>, <strong>Palash</strong>, and <strong>Amaltash</strong> &mdash; each room thoughtfully furnished with two single beds, almirahs for storage, and a cozy chair-table arrangement. While Gulmohar and Palash offer attached washrooms and bedroom TVs, Amaltash boasts Alexa, with a TV and washroom in the common area.</p>
                    <p>Our functional kitchen is equipped with a stove, microwave, water purifier, and fridge, featuring a delightful array of green teas, instant noodles, cookies, chocolates, ice cream, soft drinks, and beverages (chargeable on MRP). Enjoy the convenience of a fully automatic washing machine and iron for laundry needs.</p>
                    <p>Immerse yourself in our common area, where books, indoor board games, free WiFi, and a television await to captivate your leisure moments. Elevate your stay with a touch of holistic well-being &mdash; we serve <em>rose quartz crystal elixir</em>, believed to resonate with unconditional love and promote emotional healing.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== VIDEO TOUR ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <div class="text-center mb-4">
                <div class="section-eyebrow">Take a tour</div>
                <h2 class="section-title">Step inside <span class="text-gradient-gold fst-italic">Vasudha</span></h2>
            </div>
            <div class="vasudha-video mx-auto">
                <video controls preload="metadata" playsinline poster="<c:url value='/images/vasudha-living.jpg'/>">
                    <source src="<c:url value='/videos/vasudha-tour.mp4'/>" type="video/mp4">
                    Your browser does not support the video tag.
                </video>
            </div>
        </div>
    </section>

    <!-- ============================== THREE THEMED BLOCKS ============================== -->
    <section class="thp-section pt-0">
        <div class="container">
            <!-- Block 1: The Gulmohar -->
            <div class="row align-items-center g-5 mb-5">
                <div class="col-lg-7">
                    <div class="vasudha-photo">
                        <img src="<c:url value='/images/vasudha-gulmohar.jpg'/>" alt="The Gulmohar room at Vasudha" data-fallback="The Gulmohar">
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="section-eyebrow">Bedroom</div>
                    <h3 class="font-serif">The Gulmohar at Vasudha</h3>
                    <p>Two single beds with two independent almirahs for storage, plus a table-and-chair set so you can work in the privacy of your own room. The room is provided with an air conditioner, TV, free Wi-Fi and toilet supplies.</p>
                </div>
            </div>

            <!-- Block 2: The Living Room -->
            <div class="row align-items-center g-5 mb-5 flex-lg-row-reverse">
                <div class="col-lg-7">
                    <div class="vasudha-photo">
                        <img src="<c:url value='/images/vasudha-living.jpg'/>" alt="The Living Room at Vasudha" data-fallback="The Living Room">
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="section-eyebrow">Common space</div>
                    <h3 class="font-serif">The Living Room</h3>
                    <p class="lead-narrow">Luxurious. Serene. Tranquil. Calm.</p>
                    <p>A meditative living area with books, plants and a Buddha alcove &mdash; the perfect place to unwind with a cup of tea after a long day, at Vasudha, Yelahanka, Bengaluru.</p>
                </div>
            </div>

            <!-- Block 3: Engagements -->
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <div class="vasudha-photo">
                        <img src="<c:url value='/images/vasudha-engagements.jpg'/>" alt="Engagements at Vasudha" data-fallback="Engagements">
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="section-eyebrow">Together time</div>
                    <h3 class="font-serif">Engagements at Vasudha</h3>
                    <p><strong>Feeling a burnout?</strong></p>
                    <p>Unwind with your loved ones. Enjoy a staycation at Vasudha &mdash; a home away from home. Yelahanka, Bengaluru.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== ROOM CARDS ============================== -->
    <section class="thp-section alt">
        <div class="container">
            <div class="text-center mb-5">
                <div class="section-eyebrow">Three rooms</div>
                <h2 class="section-title">Choose your <span class="text-gradient-gold fst-italic">room</span></h2>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <div class="feature-icon mx-auto"><i class="bi bi-tree"></i></div>
                        <h3>Gulmohar</h3>
                        <p class="small mb-0">Two single beds, attached washroom, bedroom TV, AC, free Wi-Fi.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <div class="feature-icon mx-auto"><i class="bi bi-flower3"></i></div>
                        <h3>Palash</h3>
                        <p class="small mb-0">Two single beds, attached washroom, bedroom TV, AC, free Wi-Fi.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <div class="feature-icon mx-auto"><i class="bi bi-flower2"></i></div>
                        <h3>Amaltash</h3>
                        <p class="small mb-0">Two single beds with Alexa. TV and washroom in the common area.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== WHAT THIS PLACE OFFERS ============================== -->
    <section class="thp-section">
        <div class="container">
            <div class="text-center mb-5">
                <div class="section-eyebrow">What this place offers</div>
                <h2 class="section-title">Comforts of <span class="text-gradient-gold fst-italic">home, with care</span></h2>
            </div>
            <div class="row g-4">
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-stars"></i></span><span>Opulent Living</span></div></div>
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-egg-fried"></i></span><span>Equipped Kitchen</span></div></div>
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-house-heart"></i></span><span>Twin Beds</span></div></div>
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-tv"></i></span><span>Television</span></div></div>
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-book"></i></span><span>Library</span></div></div>
                <div class="col-6 col-md-4 col-lg-2"><div class="vasudha-offer"><span class="vh-icon"><i class="bi bi-puzzle"></i></span><span>Board Games</span></div></div>
            </div>
            <div class="row g-5 mt-3">
                <div class="col-lg-7">
                    <h4 class="font-serif">Plus, all the everyday essentials</h4>
                    <ul class="thp-checklist list-unstyled mt-3">
                        <li>Fully furnished AC rooms</li>
                        <li>Functional kitchen with stove, microwave, water purifier &amp; fridge</li>
                        <li>Stocked pantry &mdash; green teas, instant noodles, cookies, chocolates, soft drinks (chargeable on MRP)</li>
                        <li>Fully automatic washing machine and iron</li>
                        <li>Books, indoor board games, television in the common area</li>
                        <li>Free Wi-Fi throughout</li>
                        <li>Rose quartz crystal elixir on arrival</li>
                    </ul>
                </div>
                <div class="col-lg-5">
                    <div class="vasudha-pricing">
                        <div class="section-eyebrow">Per bed, per night</div>
                        <h3 class="font-serif mt-2">Affordable luxury</h3>
                        <ul class="list-unstyled mt-3">
                            <li class="d-flex justify-content-between border-bottom py-2">
                                <span>Room sharing</span>
                                <strong>&#8377; 1,500</strong>
                            </li>
                            <li class="d-flex justify-content-between border-bottom py-2">
                                <span>Single occupancy</span>
                                <strong>&#8377; 3,000</strong>
                            </li>
                        </ul>
                        <p class="small text-muted mt-3 mb-0">Pantry items chargeable at MRP. Long-stay enquiries welcome.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== WHERE YOU'LL BE (map) ============================== -->
    <section class="thp-section alt">
        <div class="container">
            <div class="row g-5 align-items-stretch">
                <div class="col-lg-5">
                    <div class="section-eyebrow">Where you'll be</div>
                    <h2 class="section-title">Yelahanka, Bengaluru</h2>
                    <address class="mt-3">
                        Corner House, 3rd Cross, 12th Main, 1st Floor,<br>
                        Swamiji Layout, Opp Sunshine Public School,<br>
                        Ananthapur Nagar, Yelahanka,<br>
                        Bangalore &mdash; 650064
                    </address>
                    <p class="mt-3">
                        <i class="bi bi-telephone-fill me-2 text-gradient-gold"></i>
                        <a href="tel:+918095008095" class="fw-semibold">+91 8095-00-8095</a>
                    </p>
                    <a class="btn-thp-outline mt-2" href="https://maps.google.com/?q=Yelahanka%20Bangalore%20Healing%20Presence" target="_blank" rel="noopener">
                        Open in Google Maps <i class="bi bi-box-arrow-up-right ms-1"></i>
                    </a>
                </div>
                <div class="col-lg-7">
                    <div class="vasudha-map">
                        <iframe
                            src="https://www.google.com/maps?q=Yelahanka+Bengaluru+560064&output=embed"
                            width="100%" height="100%" style="border:0;"
                            allowfullscreen="" loading="lazy"
                            referrerpolicy="no-referrer-when-downgrade"
                            title="Vasudha location map"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ============================== BOOKING REQUEST ============================== -->
    <section class="thp-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="text-center mb-4">
                        <div class="section-eyebrow">Booking Request</div>
                        <h2 class="section-title">Reserve your stay</h2>
                        <p class="text-muted">Tell us your preferred dates and we'll confirm availability within one business day.</p>
                    </div>
                    <div id="vasudhaFormHolder">
                        <form:form action="${pageContext.request.contextPath}/enquire-space" method="post"
                                   modelAttribute="enquiryForm"
                                   cssClass="thp-form bg-white p-4 rounded-4 border"
                                   data-thp-ajax="true" data-success-target="#vasudhaFormHolder">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="vName">Name</label>
                                    <form:input path="name" id="vName" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="name" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="vEmail">Email</label>
                                    <form:input path="email" type="email" id="vEmail" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="email" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="vPhone">Phone</label>
                                    <form:input path="phone" type="tel" id="vPhone" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required"/>
                                    <form:errors path="phone" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="vRoom">Preferred room</label>
                                    <form:select path="eventType" id="vRoom" cssClass="form-select" cssErrorClass="form-select is-invalid" required="required">
                                        <form:option value="Stay at Vasudha">No preference</form:option>
                                        <form:option value="Stay at Vasudha — Gulmohar">Gulmohar</form:option>
                                        <form:option value="Stay at Vasudha — Palash">Palash</form:option>
                                        <form:option value="Stay at Vasudha — Amaltash">Amaltash</form:option>
                                    </form:select>
                                    <form:errors path="eventType" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="vDate">Check-in date</label>
                                    <form:input path="preferredDate" type="date" id="vDate" cssClass="form-control"/>
                                </div>
                                <div class="col-md-6">
                                    <label for="vGuests">Number of guests</label>
                                    <form:input path="attendees" type="number" min="1" id="vGuests" cssClass="form-control"/>
                                </div>
                                <div class="col-12">
                                    <label for="vMessage">Anything we should know?</label>
                                    <form:textarea path="message" id="vMessage" cssClass="form-control" rows="3"/>
                                    <form:errors path="message" cssClass="form-error" element="div"/>
                                </div>
                                <div class="col-12 text-end">
                                    <button type="submit" class="btn-thp-primary">Send booking request <i class="bi bi-arrow-right"></i></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </section>

</t:layout>
