// The Healing Presence — site-wide JS
// Vanilla AJAX (fetch) + small interaction helpers. No HTMX.

(function () {
    'use strict';

    // ---------- Toast helper ----------
    function ensureToastStack() {
        let stack = document.querySelector('.toast-stack');
        if (!stack) {
            stack = document.createElement('div');
            stack.className = 'toast-stack';
            document.body.appendChild(stack);
        }
        return stack;
    }

    function showToast(message, kind) {
        const stack = ensureToastStack();
        const toast = document.createElement('div');
        toast.className = 'thp-toast' + (kind === 'error' ? ' error' : '');
        toast.textContent = message;
        stack.appendChild(toast);
        setTimeout(function () {
            toast.style.transition = 'opacity 0.3s ease';
            toast.style.opacity = '0';
            setTimeout(function () { toast.remove(); }, 300);
        }, 4500);
    }
    window.thpToast = showToast;

    // ---------- AJAX form submission ----------
    function clearErrors(form) {
        form.querySelectorAll('.is-invalid').forEach(function (el) {
            el.classList.remove('is-invalid');
        });
        form.querySelectorAll('.form-error').forEach(function (el) {
            el.textContent = '';
        });
    }

    function applyErrors(form, errors) {
        if (!errors) return;
        Object.keys(errors).forEach(function (field) {
            const input = form.querySelector('[name="' + field + '"]');
            if (input) {
                input.classList.add('is-invalid');
                let msgNode = input.parentElement.querySelector('.form-error[data-for="' + field + '"]');
                if (!msgNode) {
                    msgNode = document.createElement('div');
                    msgNode.className = 'form-error';
                    msgNode.setAttribute('data-for', field);
                    input.parentElement.appendChild(msgNode);
                }
                msgNode.textContent = errors[field];
            }
        });
    }

    function formToJson(form) {
        const data = {};
        const formData = new FormData(form);
        formData.forEach(function (value, key) {
            data[key] = value;
        });
        form.querySelectorAll('input[type="checkbox"]').forEach(function (cb) {
            data[cb.name] = cb.checked;
        });
        form.querySelectorAll('input[type="number"]').forEach(function (n) {
            if (data[n.name] !== undefined && data[n.name] !== '') {
                const parsed = parseInt(data[n.name], 10);
                data[n.name] = isNaN(parsed) ? null : parsed;
            } else {
                data[n.name] = null;
            }
        });
        return data;
    }

    // ---------- CSRF helper ----------
    function readCookie(name) {
        const prefix = name + '=';
        const parts = document.cookie ? document.cookie.split(';') : [];
        for (let i = 0; i < parts.length; i++) {
            const c = parts[i].replace(/^\s+/, '');
            if (c.indexOf(prefix) === 0) {
                return decodeURIComponent(c.substring(prefix.length));
            }
        }
        return null;
    }
    function csrfToken() {
        const meta = document.querySelector('meta[name="_csrf"]');
        if (meta && meta.getAttribute('content')) return meta.getAttribute('content');
        return readCookie('XSRF-TOKEN');
    }

    function renderSuccess(target, message) {
        // Build via DOM APIs (textContent) to neutralise any HTML in the message.
        target.textContent = '';
        const wrapper = document.createElement('div');
        wrapper.className = 'thp-form-success';
        const heading = document.createElement('h4');
        heading.className = 'font-serif mb-2';
        heading.textContent = 'Thank you!';
        const para = document.createElement('p');
        para.className = 'mb-0';
        para.textContent = message || 'Submission received.';
        wrapper.appendChild(heading);
        wrapper.appendChild(para);
        target.appendChild(wrapper);
    }

    function bindAjaxForm(form) {
        if (form.dataset.thpBound) return;
        form.dataset.thpBound = '1';

        form.addEventListener('submit', function (event) {
            event.preventDefault();
            clearErrors(form);

            const submitBtn = form.querySelector('button[type="submit"], input[type="submit"]');
            const originalLabel = submitBtn ? submitBtn.innerHTML : null;
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = 'Sending…';
            }

            const url = form.getAttribute('action') || window.location.pathname;
            const payload = formToJson(form);

            const headers = {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            };
            const token = csrfToken();
            if (token) {
                headers['X-CSRF-TOKEN'] = token;
                headers['X-XSRF-TOKEN'] = token;
            }

            fetch(url, {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(payload),
                credentials: 'same-origin'
            }).then(function (response) {
                return response.json().then(function (body) {
                    return { ok: response.ok, body: body };
                }).catch(function () {
                    return { ok: response.ok, body: {} };
                });
            }).then(function (result) {
                if (result.ok && result.body && result.body.ok) {
                    const successTarget = form.dataset.successTarget;
                    if (successTarget) {
                        const target = document.querySelector(successTarget);
                        if (target) {
                            renderSuccess(target, result.body.message);
                            target.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                    } else {
                        showToast(result.body.message || 'Submission received.');
                        form.reset();
                    }
                } else {
                    applyErrors(form, result.body && result.body.errors);
                    showToast(
                        (result.body && result.body.errors)
                            ? 'Please correct the highlighted fields.'
                            : 'Sorry, something went wrong. Please try again.',
                        'error');
                }
            }).catch(function () {
                showToast('Network error — please check your connection and try again.', 'error');
            }).finally(function () {
                if (submitBtn) {
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = originalLabel;
                }
            });
        });
    }

    // ---------- FAQ accordion ----------
    function bindFaq() {
        document.querySelectorAll('.faq-item button').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const item = btn.closest('.faq-item');
                const expanded = item.classList.toggle('open');
                btn.setAttribute('aria-expanded', expanded ? 'true' : 'false');
            });
        });
    }

    // ---------- Image fallback ----------
    function replaceWithFallback(img) {
        const fb = document.createElement('div');
        fb.className = 'image-fallback';
        fb.textContent = img.dataset.fallback || 'Image';
        img.replaceWith(fb);
    }
    function bindImageFallback() {
        document.querySelectorAll('img[data-fallback]').forEach(function (img) {
            // If the image already finished loading and is broken (naturalHeight 0), replace now.
            if (img.complete && img.naturalHeight === 0) {
                replaceWithFallback(img);
                return;
            }
            img.addEventListener('error', function () {
                replaceWithFallback(img);
            });
        });
    }

    // ---------- Active nav link ----------
    function highlightActiveNav() {
        const path = window.location.pathname;
        document.querySelectorAll('.thp-navbar .nav-link').forEach(function (link) {
            const href = link.getAttribute('href');
            if (!href) return;
            // Exact match for root, or path === href, or path starts with href + '/'.
            // Prevents '/therapy' from matching '/therapist-anything'.
            if (href === '/') {
                if (path === '/') link.classList.add('active');
            } else if (path === href || path.startsWith(href + '/')) {
                link.classList.add('active');
            }
        });
    }

    // ---------- Premium scroll-fade-in (Intersection Observer) ----------
    function bindScrollReveal() {
        // Respect user motion preferences
        if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;
        if (!('IntersectionObserver' in window)) return;

        const observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('in-view');
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.12, rootMargin: '0px 0px -10% 0px' });

        const selectors = [
            '.service-card', '.feature-card', '.therapist-card', '.testimonial-card',
            '.big-service-card', '.therapy-hero-photo', '.therapy-workshop-photo',
            '.training-card', '.vasudha-photo', '.founders-photo'
        ];
        document.querySelectorAll(selectors.join(', ')).forEach(function (el) {
            el.classList.add('thp-reveal');
            observer.observe(el);
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('form[data-thp-ajax]').forEach(bindAjaxForm);
        bindFaq();
        bindImageFallback();
        highlightActiveNav();
        bindScrollReveal();

        const flashToast = document.querySelector('meta[name="thp-flash"]');
        if (flashToast && flashToast.content) {
            showToast(flashToast.content);
        }
    });
})();
