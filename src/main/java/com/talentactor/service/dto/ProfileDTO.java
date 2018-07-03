package com.talentactor.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Profile entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    private String state;

    private String city;

    private String address;

    private String phone;

    private LocalDate since;

    @Lob
    private byte[] image;
    private String imageContentType;

    @Lob
    private byte[] video;
    private String videoContentType;

    @Lob
    private byte[] audio;
    private String audioContentType;

    private String imagepath;

    private String videopath;

    private String audiopath;

    private Long userId;

    private Set<SkillDTO> skills = new HashSet<>();

    private Set<SportDTO> sports = new HashSet<>();

    private Set<SwimmingDTO> swimmings = new HashSet<>();

    private Set<CombatDTO> combats = new HashSet<>();

    private Set<LanguageDTO> languages = new HashSet<>();

    private Set<InstrumentDTO> instruments = new HashSet<>();

    private Set<WeaponDTO> weapons = new HashSet<>();

    private Set<CyclingDTO> cyclings = new HashSet<>();

    private Set<CircusDTO> circuses = new HashSet<>();

    private Set<HorseDTO> horses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
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

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return videoContentType;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public String getAudioContentType() {
        return audioContentType;
    }

    public void setAudioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getAudiopath() {
        return audiopath;
    }

    public void setAudiopath(String audiopath) {
        this.audiopath = audiopath;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<SkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillDTO> skills) {
        this.skills = skills;
    }

    public Set<SportDTO> getSports() {
        return sports;
    }

    public void setSports(Set<SportDTO> sports) {
        this.sports = sports;
    }

    public Set<SwimmingDTO> getSwimmings() {
        return swimmings;
    }

    public void setSwimmings(Set<SwimmingDTO> swimmings) {
        this.swimmings = swimmings;
    }

    public Set<CombatDTO> getCombats() {
        return combats;
    }

    public void setCombats(Set<CombatDTO> combats) {
        this.combats = combats;
    }

    public Set<LanguageDTO> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<LanguageDTO> languages) {
        this.languages = languages;
    }

    public Set<InstrumentDTO> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<InstrumentDTO> instruments) {
        this.instruments = instruments;
    }

    public Set<WeaponDTO> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<WeaponDTO> weapons) {
        this.weapons = weapons;
    }

    public Set<CyclingDTO> getCyclings() {
        return cyclings;
    }

    public void setCyclings(Set<CyclingDTO> cyclings) {
        this.cyclings = cyclings;
    }

    public Set<CircusDTO> getCircuses() {
        return circuses;
    }

    public void setCircuses(Set<CircusDTO> circuses) {
        this.circuses = circuses;
    }

    public Set<HorseDTO> getHorses() {
        return horses;
    }

    public void setHorses(Set<HorseDTO> horses) {
        this.horses = horses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (profileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", city='" + getCity() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", since='" + getSince() + "'" +
            ", image='" + getImage() + "'" +
            ", video='" + getVideo() + "'" +
            ", audio='" + getAudio() + "'" +
            ", imagepath='" + getImagepath() + "'" +
            ", videopath='" + getVideopath() + "'" +
            ", audiopath='" + getAudiopath() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
