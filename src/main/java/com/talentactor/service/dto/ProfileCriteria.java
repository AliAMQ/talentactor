package com.talentactor.service.dto;

import java.io.Serializable;
import com.talentactor.domain.enumeration.State;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Profile entity. This class is used in ProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable {
    /**
     * Class for filtering State
     */
    public static class StateFilter extends Filter<State> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StateFilter state;

    private StringFilter city;

    private StringFilter address;

    private StringFilter phone;

    private LocalDateFilter since;

    private StringFilter imagepath;

    private StringFilter videopath;

    private StringFilter audiopath;

    private LongFilter userId;

    private LongFilter roleId;

    private LongFilter filmId;

    private LongFilter televisionId;

    private LongFilter internetId;

    private LongFilter commercialId;

    private LongFilter printId;

    private LongFilter theaterId;

    private LongFilter voiceId;

    private LongFilter skillId;

    private LongFilter sportId;

    private LongFilter swimmingId;

    private LongFilter combatId;

    private LongFilter languageId;

    private LongFilter instrumentId;

    private LongFilter weaponId;

    private LongFilter cyclingId;

    private LongFilter circusId;

    private LongFilter horseId;

    public ProfileCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StateFilter getState() {
        return state;
    }

    public void setState(StateFilter state) {
        this.state = state;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LocalDateFilter getSince() {
        return since;
    }

    public void setSince(LocalDateFilter since) {
        this.since = since;
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

    public StringFilter getAudiopath() {
        return audiopath;
    }

    public void setAudiopath(StringFilter audiopath) {
        this.audiopath = audiopath;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
    }

    public LongFilter getFilmId() {
        return filmId;
    }

    public void setFilmId(LongFilter filmId) {
        this.filmId = filmId;
    }

    public LongFilter getTelevisionId() {
        return televisionId;
    }

    public void setTelevisionId(LongFilter televisionId) {
        this.televisionId = televisionId;
    }

    public LongFilter getInternetId() {
        return internetId;
    }

    public void setInternetId(LongFilter internetId) {
        this.internetId = internetId;
    }

    public LongFilter getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(LongFilter commercialId) {
        this.commercialId = commercialId;
    }

    public LongFilter getPrintId() {
        return printId;
    }

    public void setPrintId(LongFilter printId) {
        this.printId = printId;
    }

    public LongFilter getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(LongFilter theaterId) {
        this.theaterId = theaterId;
    }

    public LongFilter getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(LongFilter voiceId) {
        this.voiceId = voiceId;
    }

    public LongFilter getSkillId() {
        return skillId;
    }

    public void setSkillId(LongFilter skillId) {
        this.skillId = skillId;
    }

    public LongFilter getSportId() {
        return sportId;
    }

    public void setSportId(LongFilter sportId) {
        this.sportId = sportId;
    }

    public LongFilter getSwimmingId() {
        return swimmingId;
    }

    public void setSwimmingId(LongFilter swimmingId) {
        this.swimmingId = swimmingId;
    }

    public LongFilter getCombatId() {
        return combatId;
    }

    public void setCombatId(LongFilter combatId) {
        this.combatId = combatId;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    public LongFilter getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(LongFilter instrumentId) {
        this.instrumentId = instrumentId;
    }

    public LongFilter getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(LongFilter weaponId) {
        this.weaponId = weaponId;
    }

    public LongFilter getCyclingId() {
        return cyclingId;
    }

    public void setCyclingId(LongFilter cyclingId) {
        this.cyclingId = cyclingId;
    }

    public LongFilter getCircusId() {
        return circusId;
    }

    public void setCircusId(LongFilter circusId) {
        this.circusId = circusId;
    }

    public LongFilter getHorseId() {
        return horseId;
    }

    public void setHorseId(LongFilter horseId) {
        this.horseId = horseId;
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (since != null ? "since=" + since + ", " : "") +
                (imagepath != null ? "imagepath=" + imagepath + ", " : "") +
                (videopath != null ? "videopath=" + videopath + ", " : "") +
                (audiopath != null ? "audiopath=" + audiopath + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (roleId != null ? "roleId=" + roleId + ", " : "") +
                (filmId != null ? "filmId=" + filmId + ", " : "") +
                (televisionId != null ? "televisionId=" + televisionId + ", " : "") +
                (internetId != null ? "internetId=" + internetId + ", " : "") +
                (commercialId != null ? "commercialId=" + commercialId + ", " : "") +
                (printId != null ? "printId=" + printId + ", " : "") +
                (theaterId != null ? "theaterId=" + theaterId + ", " : "") +
                (voiceId != null ? "voiceId=" + voiceId + ", " : "") +
                (skillId != null ? "skillId=" + skillId + ", " : "") +
                (sportId != null ? "sportId=" + sportId + ", " : "") +
                (swimmingId != null ? "swimmingId=" + swimmingId + ", " : "") +
                (combatId != null ? "combatId=" + combatId + ", " : "") +
                (languageId != null ? "languageId=" + languageId + ", " : "") +
                (instrumentId != null ? "instrumentId=" + instrumentId + ", " : "") +
                (weaponId != null ? "weaponId=" + weaponId + ", " : "") +
                (cyclingId != null ? "cyclingId=" + cyclingId + ", " : "") +
                (circusId != null ? "circusId=" + circusId + ", " : "") +
                (horseId != null ? "horseId=" + horseId + ", " : "") +
            "}";
    }

}
