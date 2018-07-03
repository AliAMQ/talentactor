package com.talentactor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Swimming.
 */
@Entity
@Table(name = "swimming")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Swimming implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "swimmings")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> profiles = new HashSet<>();

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

    public Swimming title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public Swimming profiles(Set<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }

    public Swimming addProfile(Profile profile) {
        this.profiles.add(profile);
        profile.getSwimmings().add(this);
        return this;
    }

    public Swimming removeProfile(Profile profile) {
        this.profiles.remove(profile);
        profile.getSwimmings().remove(this);
        return this;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
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
        Swimming swimming = (Swimming) o;
        if (swimming.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), swimming.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Swimming{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
