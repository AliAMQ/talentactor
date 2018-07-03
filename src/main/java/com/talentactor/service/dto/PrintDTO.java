package com.talentactor.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Print entity.
 */
public class PrintDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String photographer;

    private String hair;

    private String link;

    @Lob
    private byte[] image;
    private String imageContentType;

    private String imagepath;

    private Long profileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrintDTO printDTO = (PrintDTO) o;
        if (printDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), printDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrintDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", photographer='" + getPhotographer() + "'" +
            ", hair='" + getHair() + "'" +
            ", link='" + getLink() + "'" +
            ", image='" + getImage() + "'" +
            ", imagepath='" + getImagepath() + "'" +
            ", profile=" + getProfileId() +
            "}";
    }
}
