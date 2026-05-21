package com.healingpresence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "testimonials")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant createdAt;

    private String clientName;

    @Column(columnDefinition = "TEXT")
    private String body;

    private int rating;

    private boolean published;

    /** Optional reviewer avatar path; null falls back to a coloured initial circle in the JSP. */
    @Column(length = 255)
    private String avatarPath;

    public Testimonial() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
}
