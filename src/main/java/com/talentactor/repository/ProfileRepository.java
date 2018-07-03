package com.talentactor.repository;

import com.talentactor.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(value = "select distinct profile from Profile profile left join fetch profile.skills left join fetch profile.sports left join fetch profile.swimmings left join fetch profile.combats left join fetch profile.languages left join fetch profile.instruments left join fetch profile.weapons left join fetch profile.cyclings left join fetch profile.circuses left join fetch profile.horses",
        countQuery = "select count(distinct profile) from Profile profile")
    Page<Profile> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct profile from Profile profile left join fetch profile.skills left join fetch profile.sports left join fetch profile.swimmings left join fetch profile.combats left join fetch profile.languages left join fetch profile.instruments left join fetch profile.weapons left join fetch profile.cyclings left join fetch profile.circuses left join fetch profile.horses")
    List<Profile> findAllWithEagerRelationships();

    @Query("select profile from Profile profile left join fetch profile.skills left join fetch profile.sports left join fetch profile.swimmings left join fetch profile.combats left join fetch profile.languages left join fetch profile.instruments left join fetch profile.weapons left join fetch profile.cyclings left join fetch profile.circuses left join fetch profile.horses where profile.id =:id")
    Optional<Profile> findOneWithEagerRelationships(@Param("id") Long id);

}
