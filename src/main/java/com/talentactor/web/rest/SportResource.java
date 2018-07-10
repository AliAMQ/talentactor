package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.SportService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.SportDTO;
import com.talentactor.service.dto.SportCriteria;
import com.talentactor.service.SportQueryService;
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
 * REST controller for managing Sport.
 */
@RestController
@RequestMapping("/api")
public class SportResource {

    private final Logger log = LoggerFactory.getLogger(SportResource.class);

    private static final String ENTITY_NAME = "sport";

    private final SportService sportService;

    private final SportQueryService sportQueryService;

    public SportResource(SportService sportService, SportQueryService sportQueryService) {
        this.sportService = sportService;
        this.sportQueryService = sportQueryService;
    }

    /**
     * POST  /sports : Create a new sport.
     *
     * @param sportDTO the sportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sportDTO, or with status 400 (Bad Request) if the sport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sports")
    @Timed
    public ResponseEntity<SportDTO> createSport(@Valid @RequestBody SportDTO sportDTO) throws URISyntaxException {
        log.debug("REST request to save Sport : {}", sportDTO);
        if (sportDTO.getId() != null) {
            throw new BadRequestAlertException("A new sport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SportDTO result = sportService.save(sportDTO);
        return ResponseEntity.created(new URI("/api/sports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sports : Updates an existing sport.
     *
     * @param sportDTO the sportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sportDTO,
     * or with status 400 (Bad Request) if the sportDTO is not valid,
     * or with status 500 (Internal Server Error) if the sportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sports")
    @Timed
    public ResponseEntity<SportDTO> updateSport(@Valid @RequestBody SportDTO sportDTO) throws URISyntaxException {
        log.debug("REST request to update Sport : {}", sportDTO);
        if (sportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SportDTO result = sportService.save(sportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sports : get all the sports.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sports in body
     */
    @GetMapping("/sports")
    @Timed
    public ResponseEntity<List<SportDTO>> getAllSports(SportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sports by criteria: {}", criteria);
        Page<SportDTO> page = sportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sports/:id : get the "id" sport.
     *
     * @param id the id of the sportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sports/{id}")
    @Timed
    public ResponseEntity<SportDTO> getSport(@PathVariable Long id) {
        log.debug("REST request to get Sport : {}", id);
        Optional<SportDTO> sportDTO = sportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sportDTO);
    }

    /**
     * DELETE  /sports/:id : delete the "id" sport.
     *
     * @param id the id of the sportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sports/{id}")
    @Timed
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        log.debug("REST request to delete Sport : {}", id);
        sportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
