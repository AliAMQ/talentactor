package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.CyclingService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.CyclingDTO;
import com.talentactor.service.dto.CyclingCriteria;
import com.talentactor.service.CyclingQueryService;
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
 * REST controller for managing Cycling.
 */
@RestController
@RequestMapping("/api")
public class CyclingResource {

    private final Logger log = LoggerFactory.getLogger(CyclingResource.class);

    private static final String ENTITY_NAME = "cycling";

    private final CyclingService cyclingService;

    private final CyclingQueryService cyclingQueryService;

    public CyclingResource(CyclingService cyclingService, CyclingQueryService cyclingQueryService) {
        this.cyclingService = cyclingService;
        this.cyclingQueryService = cyclingQueryService;
    }

    /**
     * POST  /cyclings : Create a new cycling.
     *
     * @param cyclingDTO the cyclingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cyclingDTO, or with status 400 (Bad Request) if the cycling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cyclings")
    @Timed
    public ResponseEntity<CyclingDTO> createCycling(@Valid @RequestBody CyclingDTO cyclingDTO) throws URISyntaxException {
        log.debug("REST request to save Cycling : {}", cyclingDTO);
        if (cyclingDTO.getId() != null) {
            throw new BadRequestAlertException("A new cycling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CyclingDTO result = cyclingService.save(cyclingDTO);
        return ResponseEntity.created(new URI("/api/cyclings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cyclings : Updates an existing cycling.
     *
     * @param cyclingDTO the cyclingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cyclingDTO,
     * or with status 400 (Bad Request) if the cyclingDTO is not valid,
     * or with status 500 (Internal Server Error) if the cyclingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cyclings")
    @Timed
    public ResponseEntity<CyclingDTO> updateCycling(@Valid @RequestBody CyclingDTO cyclingDTO) throws URISyntaxException {
        log.debug("REST request to update Cycling : {}", cyclingDTO);
        if (cyclingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CyclingDTO result = cyclingService.save(cyclingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cyclingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cyclings : get all the cyclings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cyclings in body
     */
    @GetMapping("/cyclings")
    @Timed
    public ResponseEntity<List<CyclingDTO>> getAllCyclings(CyclingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cyclings by criteria: {}", criteria);
        Page<CyclingDTO> page = cyclingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cyclings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cyclings/:id : get the "id" cycling.
     *
     * @param id the id of the cyclingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cyclingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cyclings/{id}")
    @Timed
    public ResponseEntity<CyclingDTO> getCycling(@PathVariable Long id) {
        log.debug("REST request to get Cycling : {}", id);
        Optional<CyclingDTO> cyclingDTO = cyclingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cyclingDTO);
    }

    /**
     * DELETE  /cyclings/:id : delete the "id" cycling.
     *
     * @param id the id of the cyclingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cyclings/{id}")
    @Timed
    public ResponseEntity<Void> deleteCycling(@PathVariable Long id) {
        log.debug("REST request to delete Cycling : {}", id);
        cyclingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
