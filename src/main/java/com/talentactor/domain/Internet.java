package com.talentactor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Internet.
 */
@Entity
@Table(name = "internet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Internet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "cameraman")
    private String cameraman;

    @Column(name = "jhi_link")
    private String link;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Column(name = "video_content_type")
    private String videoContentType;

    @Column(name = "imagepath")
    private String imagepath;

    @Column(name = "videopath")
    private String videopath;

    @ManyToOne
    @JsonIgnoreProperties("internets")
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

    public Internet title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public Internet director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCameraman() {
        return cameraman;
    }

    public Internet cameraman(String cameraman) {
        this.cameraman = cameraman;
        return this;
    }

    public void setCameraman(String cameraman) {
        this.cameraman = cameraman;
    }

    public String getLink() {
        return link;
    }

    public Internet link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getImage() {
        return image;
    }

    public Internet image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Internet imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getVideo() {
        return video;
    }

    public Internet video(byte[] video) {
        this.video = video;
        return this;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return videoContentType;
    }

    public Internet videoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
        return this;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public String getImagepath() {
        return imagepath;
    }

    public Internet imagepath(String imagepath) {
        this.imagepath = imagepath;
        return this;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getVideopath() {
        return videopath;
    }

    public Internet videopath(String videopath) {
        this.videopath = videopath;
        return this;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public Profile getProfile() {
        return profile;
    }

    public Internet profile(Profile profile) {
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
        Internet internet = (Internet) o;
        if (internet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), internet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Internet{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", director='" + getDirector() + "'" +
            ", cameraman='" + getCameraman() + "'" +
            ", link='" + getLink() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", video='" + getVideo() + "'" +
            ", videoContentType='" + getVideoContentType() + "'" +
            ", imagepath='" + getImagepath() + "'" +
            ", videopath='" + getVideopath() + "'" +
            "}";
    }
}
