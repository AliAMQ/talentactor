package com.talentactor.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Theater entity. This class is used in TheaterResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /theaters?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TheaterCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter director;

    private StringFilter link;

    private StringFilter imagepath;

    private StringFilter videopath;

    private LongFilter profileId;

    public TheaterCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDirector() {
        return director;
    }

    public void setDirector(StringFilter director) {
        this.director = director;
    }

    public StringFilter getLink() {
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public StringFilter getImagepath() {
        return imagepath;
    }

    public void setImagepath(StringFilter imagepath) {
        this.imagepath = imagepath;
    }

    public StringFilter getVideopath() {
        return videopath;
    }

    public void setVideopath(StringFilter videopath) {
        this.videopath = videopath;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return "TheaterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (director != null ? "director=" + director + ", " : "") +
                (link != null ? "link=" + link + ", " : "") +
                (imagepath != null ? "imagepath=" + imagepath + ", " : "") +
                (videopath != null ? "videopath=" + videopath + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
            "}";
    }

}
