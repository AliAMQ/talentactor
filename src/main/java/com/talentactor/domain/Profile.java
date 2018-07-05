package com.talentactor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.talentactor.domain.enumeration.State;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "since")
    private LocalDate since;

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

    @Lob
    @Column(name = "audio")
    private byte[] audio;

    @Column(name = "audio_content_type")
    private String audioContentType;

    @Column(name = "imagepath")
    private String imagepath;

    @Column(name = "videopath")
    private String videopath;

    @Column(name = "audiopath")
    private String audiopath;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Film> films = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Television> televisions = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Internet> internets = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commercial> commercials = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Print> prints = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Theater> theaters = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Voice> voices = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_skill",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "skills_id", referencedColumnName = "id"))
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_sport",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "sports_id", referencedColumnName = "id"))
    private Set<Sport> sports = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_swimming",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "swimmings_id", referencedColumnName = "id"))
    private Set<Swimming> swimmings = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_combat",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "combats_id", referencedColumnName = "id"))
    private Set<Combat> combats = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_language",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "languages_id", referencedColumnName = "id"))
    private Set<Language> languages = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_instrument",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "instruments_id", referencedColumnName = "id"))
    private Set<Instrument> instruments = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_weapon",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "weapons_id", referencedColumnName = "id"))
    private Set<Weapon> weapons = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_cycling",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "cyclings_id", referencedColumnName = "id"))
    private Set<Cycling> cyclings = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_circus",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "circuses_id", referencedColumnName = "id"))
    private Set<Circus> circuses = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "profile_horse",
               joinColumns = @JoinColumn(name = "profiles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "horses_id", referencedColumnName = "id"))
    private Set<Horse> horses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public Profile state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public Profile city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public Profile address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Profile phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getSince() {
        return since;
    }

    public Profile since(LocalDate since) {
        this.since = since;
        return this;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public byte[] getImage() {
        return image;
    }

    public Profile image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Profile imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getVideo() {
        return video;
    }

    public Profile video(byte[] video) {
        this.video = video;
        return this;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return videoContentType;
    }

    public Profile videoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
        return this;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public byte[] getAudio() {
        return audio;
    }

    public Profile audio(byte[] audio) {
        this.audio = audio;
        return this;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public String getAudioContentType() {
        return audioContentType;
    }

    public Profile audioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
        return this;
    }

    public void setAudioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
    }

    public String getImagepath() {
        return imagepath;
    }

    public Profile imagepath(String imagepath) {
        this.imagepath = imagepath;
        return this;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getVideopath() {
        return videopath;
    }

    public Profile videopath(String videopath) {
        this.videopath = videopath;
        return this;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getAudiopath() {
        return audiopath;
    }

    public Profile audiopath(String audiopath) {
        this.audiopath = audiopath;
        return this;
    }

    public void setAudiopath(String audiopath) {
        this.audiopath = audiopath;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Profile roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Profile addRole(Role role) {
        this.roles.add(role);
        role.setProfile(this);
        return this;
    }

    public Profile removeRole(Role role) {
        this.roles.remove(role);
        role.setProfile(null);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Film> getFilms() {
        return films;
    }

    public Profile films(Set<Film> films) {
        this.films = films;
        return this;
    }

    public Profile addFilm(Film film) {
        this.films.add(film);
        film.setProfile(this);
        return this;
    }

    public Profile removeFilm(Film film) {
        this.films.remove(film);
        film.setProfile(null);
        return this;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    public Set<Television> getTelevisions() {
        return televisions;
    }

    public Profile televisions(Set<Television> televisions) {
        this.televisions = televisions;
        return this;
    }

    public Profile addTelevision(Television television) {
        this.televisions.add(television);
        television.setProfile(this);
        return this;
    }

    public Profile removeTelevision(Television television) {
        this.televisions.remove(television);
        television.setProfile(null);
        return this;
    }

    public void setTelevisions(Set<Television> televisions) {
        this.televisions = televisions;
    }

    public Set<Internet> getInternets() {
        return internets;
    }

    public Profile internets(Set<Internet> internets) {
        this.internets = internets;
        return this;
    }

    public Profile addInternet(Internet internet) {
        this.internets.add(internet);
        internet.setProfile(this);
        return this;
    }

    public Profile removeInternet(Internet internet) {
        this.internets.remove(internet);
        internet.setProfile(null);
        return this;
    }

    public void setInternets(Set<Internet> internets) {
        this.internets = internets;
    }

    public Set<Commercial> getCommercials() {
        return commercials;
    }

    public Profile commercials(Set<Commercial> commercials) {
        this.commercials = commercials;
        return this;
    }

    public Profile addCommercial(Commercial commercial) {
        this.commercials.add(commercial);
        commercial.setProfile(this);
        return this;
    }

    public Profile removeCommercial(Commercial commercial) {
        this.commercials.remove(commercial);
        commercial.setProfile(null);
        return this;
    }

    public void setCommercials(Set<Commercial> commercials) {
        this.commercials = commercials;
    }

    public Set<Print> getPrints() {
        return prints;
    }

    public Profile prints(Set<Print> prints) {
        this.prints = prints;
        return this;
    }

    public Profile addPrint(Print print) {
        this.prints.add(print);
        print.setProfile(this);
        return this;
    }

    public Profile removePrint(Print print) {
        this.prints.remove(print);
        print.setProfile(null);
        return this;
    }

    public void setPrints(Set<Print> prints) {
        this.prints = prints;
    }

    public Set<Theater> getTheaters() {
        return theaters;
    }

    public Profile theaters(Set<Theater> theaters) {
        this.theaters = theaters;
        return this;
    }

    public Profile addTheater(Theater theater) {
        this.theaters.add(theater);
        theater.setProfile(this);
        return this;
    }

    public Profile removeTheater(Theater theater) {
        this.theaters.remove(theater);
        theater.setProfile(null);
        return this;
    }

    public void setTheaters(Set<Theater> theaters) {
        this.theaters = theaters;
    }

    public Set<Voice> getVoices() {
        return voices;
    }

    public Profile voices(Set<Voice> voices) {
        this.voices = voices;
        return this;
    }

    public Profile addVoice(Voice voice) {
        this.voices.add(voice);
        voice.setProfile(this);
        return this;
    }

    public Profile removeVoice(Voice voice) {
        this.voices.remove(voice);
        voice.setProfile(null);
        return this;
    }

    public void setVoices(Set<Voice> voices) {
        this.voices = voices;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Profile skills(Set<Skill> skills) {
        this.skills = skills;
        return this;
    }

    public Profile addSkill(Skill skill) {
        this.skills.add(skill);
        skill.getProfiles().add(this);
        return this;
    }

    public Profile removeSkill(Skill skill) {
        this.skills.remove(skill);
        skill.getProfiles().remove(this);
        return this;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<Sport> getSports() {
        return sports;
    }

    public Profile sports(Set<Sport> sports) {
        this.sports = sports;
        return this;
    }

    public Profile addSport(Sport sport) {
        this.sports.add(sport);
        sport.getProfiles().add(this);
        return this;
    }

    public Profile removeSport(Sport sport) {
        this.sports.remove(sport);
        sport.getProfiles().remove(this);
        return this;
    }

    public void setSports(Set<Sport> sports) {
        this.sports = sports;
    }

    public Set<Swimming> getSwimmings() {
        return swimmings;
    }

    public Profile swimmings(Set<Swimming> swimmings) {
        this.swimmings = swimmings;
        return this;
    }

    public Profile addSwimming(Swimming swimming) {
        this.swimmings.add(swimming);
        swimming.getProfiles().add(this);
        return this;
    }

    public Profile removeSwimming(Swimming swimming) {
        this.swimmings.remove(swimming);
        swimming.getProfiles().remove(this);
        return this;
    }

    public void setSwimmings(Set<Swimming> swimmings) {
        this.swimmings = swimmings;
    }

    public Set<Combat> getCombats() {
        return combats;
    }

    public Profile combats(Set<Combat> combats) {
        this.combats = combats;
        return this;
    }

    public Profile addCombat(Combat combat) {
        this.combats.add(combat);
        combat.getProfiles().add(this);
        return this;
    }

    public Profile removeCombat(Combat combat) {
        this.combats.remove(combat);
        combat.getProfiles().remove(this);
        return this;
    }

    public void setCombats(Set<Combat> combats) {
        this.combats = combats;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public Profile languages(Set<Language> languages) {
        this.languages = languages;
        return this;
    }

    public Profile addLanguage(Language language) {
        this.languages.add(language);
        language.getProfiles().add(this);
        return this;
    }

    public Profile removeLanguage(Language language) {
        this.languages.remove(language);
        language.getProfiles().remove(this);
        return this;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public Profile instruments(Set<Instrument> instruments) {
        this.instruments = instruments;
        return this;
    }

    public Profile addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
        instrument.getProfiles().add(this);
        return this;
    }

    public Profile removeInstrument(Instrument instrument) {
        this.instruments.remove(instrument);
        instrument.getProfiles().remove(this);
        return this;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public Set<Weapon> getWeapons() {
        return weapons;
    }

    public Profile weapons(Set<Weapon> weapons) {
        this.weapons = weapons;
        return this;
    }

    public Profile addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
        weapon.getProfiles().add(this);
        return this;
    }

    public Profile removeWeapon(Weapon weapon) {
        this.weapons.remove(weapon);
        weapon.getProfiles().remove(this);
        return this;
    }

    public void setWeapons(Set<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Set<Cycling> getCyclings() {
        return cyclings;
    }

    public Profile cyclings(Set<Cycling> cyclings) {
        this.cyclings = cyclings;
        return this;
    }

    public Profile addCycling(Cycling cycling) {
        this.cyclings.add(cycling);
        cycling.getProfiles().add(this);
        return this;
    }

    public Profile removeCycling(Cycling cycling) {
        this.cyclings.remove(cycling);
        cycling.getProfiles().remove(this);
        return this;
    }

    public void setCyclings(Set<Cycling> cyclings) {
        this.cyclings = cyclings;
    }

    public Set<Circus> getCircuses() {
        return circuses;
    }

    public Profile circuses(Set<Circus> circuses) {
        this.circuses = circuses;
        return this;
    }

    public Profile addCircus(Circus circus) {
        this.circuses.add(circus);
        circus.getProfiles().add(this);
        return this;
    }

    public Profile removeCircus(Circus circus) {
        this.circuses.remove(circus);
        circus.getProfiles().remove(this);
        return this;
    }

    public void setCircuses(Set<Circus> circuses) {
        this.circuses = circuses;
    }

    public Set<Horse> getHorses() {
        return horses;
    }

    public Profile horses(Set<Horse> horses) {
        this.horses = horses;
        return this;
    }

    public Profile addHorse(Horse horse) {
        this.horses.add(horse);
        horse.getProfiles().add(this);
        return this;
    }

    public Profile removeHorse(Horse horse) {
        this.horses.remove(horse);
        horse.getProfiles().remove(this);
        return this;
    }

    public void setHorses(Set<Horse> horses) {
        this.horses = horses;
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
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", city='" + getCity() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", since='" + getSince() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", video='" + getVideo() + "'" +
            ", videoContentType='" + getVideoContentType() + "'" +
            ", audio='" + getAudio() + "'" +
            ", audioContentType='" + getAudioContentType() + "'" +
            ", imagepath='" + getImagepath() + "'" +
            ", videopath='" + getVideopath() + "'" +
            ", audiopath='" + getAudiopath() + "'" +
            "}";
    }
}
