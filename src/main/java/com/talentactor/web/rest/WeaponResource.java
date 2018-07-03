package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.WeaponService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.WeaponDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Weapon.
 */
@RestController
@RequestMapping("/api")
public class WeaponResource {

    private final Logger log = LoggerFactory.getLogger(WeaponResource.class);

    private static final String ENTITY_NAME = "weapon";

    private final WeaponService weaponService;

    public WeaponResource(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    /**
     * POST  /weapons : Create a new weapon.
     *
     * @param weaponDTO the weaponDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weaponDTO, or with status 400 (Bad Request) if the weapon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/weapons")
    @Timed
    public ResponseEntity<WeaponDTO> createWeapon(@Valid @RequestBody WeaponDTO weaponDTO) throws URISyntaxException {
        log.debug("REST request to save Weapon : {}", weaponDTO);
        if (weaponDTO.getId() != null) {
            throw new BadRequestAlertException("A new weapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeaponDTO result = weaponService.save(weaponDTO);
        return ResponseEntity.created(new URI("/api/weapons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weapons : Updates an existing weapon.
     *
     * @param weaponDTO the weaponDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weaponDTO,
     * or with status 400 (Bad Request) if the weaponDTO is not valid,
     * or with status 500 (Internal Server Error) if the weaponDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/weapons")
    @Timed
    public ResponseEntity<WeaponDTO> updateWeapon(@Valid @RequestBody WeaponDTO weaponDTO) throws URISyntaxException {
        log.debug("REST request to update Weapon : {}", weaponDTO);
        if (weaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeaponDTO result = weaponService.save(weaponDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, weaponDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weapons : get all the weapons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of weapons in body
     */
    @GetMapping("/weapons")
    @Timed
    public ResponseEntity<List<WeaponDTO>> getAllWeapons(Pageable pageable) {
        log.debug("REST request to get a page of Weapons");
        Page<WeaponDTO> page = weaponService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weapons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /weapons/:id : get the "id" weapon.
     *
     * @param id the id of the weaponDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weaponDTO, or with status 404 (Not Found)
     */
    @GetMapping("/weapons/{id}")
    @Timed
    public ResponseEntity<WeaponDTO> getWeapon(@PathVariable Long id) {
        log.debug("REST request to get Weapon : {}", id);
        Optional<WeaponDTO> weaponDTO = weaponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weaponDTO);
    }

    /**
     * DELETE  /weapons/:id : delete the "id" weapon.
     *
     * @param id the id of the weaponDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/weapons/{id}")
    @Timed
    public ResponseEntity<Void> deleteWeapon(@PathVariable Long id) {
        log.debug("REST request to delete Weapon : {}", id);
        weaponService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
