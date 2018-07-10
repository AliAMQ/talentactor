package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.CombatService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.CombatDTO;
import com.talentactor.service.dto.CombatCriteria;
import com.talentactor.service.CombatQueryService;
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
 * REST controller for managing Combat.
 */
@RestController
@RequestMapping("/api")
public class CombatResource {

    private final Logger log = LoggerFactory.getLogger(CombatResource.class);

    private static final String ENTITY_NAME = "combat";

    private final CombatService combatService;

    private final CombatQueryService combatQueryService;

    public CombatResource(CombatService combatService, CombatQueryService combatQueryService) {
        this.combatService = combatService;
        this.combatQueryService = combatQueryService;
    }

    /**
     * POST  /combats : Create a new combat.
     *
     * @param combatDTO the combatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new combatDTO, or with status 400 (Bad Request) if the combat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/combats")
    @Timed
    public ResponseEntity<CombatDTO> createCombat(@Valid @RequestBody CombatDTO combatDTO) throws URISyntaxException {
        log.debug("REST request to save Combat : {}", combatDTO);
        if (combatDTO.getId() != null) {
            throw new BadRequestAlertException("A new combat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CombatDTO result = combatService.save(combatDTO);
        return ResponseEntity.created(new URI("/api/combats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /combats : Updates an existing combat.
     *
     * @param combatDTO the combatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated combatDTO,
     * or with status 400 (Bad Request) if the combatDTO is not valid,
     * or with status 500 (Internal Server Error) if the combatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/combats")
    @Timed
    public ResponseEntity<CombatDTO> updateCombat(@Valid @RequestBody CombatDTO combatDTO) throws URISyntaxException {
        log.debug("REST request to update Combat : {}", combatDTO);
        if (combatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CombatDTO result = combatService.save(combatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, combatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /combats : get all the combats.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of combats in body
     */
    @GetMapping("/combats")
    @Timed
    public ResponseEntity<List<CombatDTO>> getAllCombats(CombatCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Combats by criteria: {}", criteria);
        Page<CombatDTO> page = combatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/combats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /combats/:id : get the "id" combat.
     *
     * @param id the id of the combatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the combatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/combats/{id}")
    @Timed
    public ResponseEntity<CombatDTO> getCombat(@PathVariable Long id) {
        log.debug("REST request to get Combat : {}", id);
        Optional<CombatDTO> combatDTO = combatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(combatDTO);
    }

    /**
     * DELETE  /combats/:id : delete the "id" combat.
     *
     * @param id the id of the combatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/combats/{id}")
    @Timed
    public ResponseEntity<Void> deleteCombat(@PathVariable Long id) {
        log.debug("REST request to delete Combat : {}", id);
        combatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
