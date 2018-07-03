package com.talentactor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Print.
 */
@Entity
@Table(name = "print")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Print implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "photographer")
    private String photographer;

    @Column(name = "hair")
    private String hair;

    @Column(name = "jhi_link")
    private String link;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "imagepath")
    private String imagepath;

    @ManyToOne
    @JsonIgnoreProperties("prints")
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Print title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotographer() {
        return photographer;
    }

    public Print photographer(String photographer) {
        this.photographer = photographer;
        return this;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getHair() {
        return hair;
    }

    public Print hair(String hair) {
        this.hair = hair;
        return this;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getLink() {
        return link;
    }

    public Print link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getImage() {
        return image;
    }

    public Print image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Print imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImagepath() {
        return imagepath;
    }

    public Print imagepath(String imagepath) {
        this.imagepath = imagepath;
        return this;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Profile getProfile() {
        return profile;
    }

    public Print profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Print print = (Print) o;
        if (print.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), print.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Print{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", photographer='" + getPhotographer() + "'" +
            ", hair='" + getHair() + "'" +
            ", link='" + getLink() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", imagepath='" + getImagepath() + "'" +
            "}";
    }
}
