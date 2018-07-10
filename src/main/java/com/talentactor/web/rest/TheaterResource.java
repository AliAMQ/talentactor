package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.TheaterService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.TheaterDTO;
import com.talentactor.service.dto.TheaterCriteria;
import com.talentactor.service.TheaterQueryService;
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
 * REST controller for managing Theater.
 */
@RestController
@RequestMapping("/api")
public class TheaterResource {

    private final Logger log = LoggerFactory.getLogger(TheaterResource.class);

    private static final String ENTITY_NAME = "theater";

    private final TheaterService theaterService;

    private final TheaterQueryService theaterQueryService;

    public TheaterResource(TheaterService theaterService, TheaterQueryService theaterQueryService) {
        this.theaterService = theaterService;
        this.theaterQueryService = theaterQueryService;
    }

    /**
     * POST  /theaters : Create a new theater.
     *
     * @param theaterDTO the theaterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new theaterDTO, or with status 400 (Bad Request) if the theater has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/theaters")
    @Timed
    public ResponseEntity<TheaterDTO> createTheater(@Valid @RequestBody TheaterDTO theaterDTO) throws URISyntaxException {
        log.debug("REST request to save Theater : {}", theaterDTO);
        if (theaterDTO.getId() != null) {
            throw new BadRequestAlertException("A new theater cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TheaterDTO result = theaterService.save(theaterDTO);
        return ResponseEntity.created(new URI("/api/theaters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /theaters : Updates an existing theater.
     *
     * @param theaterDTO the theaterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated theaterDTO,
     * or with status 400 (Bad Request) if the theaterDTO is not valid,
     * or with status 500 (Internal Server Error) if the theaterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/theaters")
    @Timed
    public ResponseEntity<TheaterDTO> updateTheater(@Valid @RequestBody TheaterDTO theaterDTO) throws URISyntaxException {
        log.debug("REST request to update Theater : {}", theaterDTO);
        if (theaterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TheaterDTO result = theaterService.save(theaterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, theaterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /theaters : get all the theaters.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of theaters in body
     */
    @GetMapping("/theaters")
    @Timed
    public ResponseEntity<List<TheaterDTO>> getAllTheaters(TheaterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Theaters by criteria: {}", criteria);
        Page<TheaterDTO> page = theaterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/theaters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /theaters/:id : get the "id" theater.
     *
     * @param id the id of the theaterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the theaterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/theaters/{id}")
    @Timed
    public ResponseEntity<TheaterDTO> getTheater(@PathVariable Long id) {
        log.debug("REST request to get Theater : {}", id);
        Optional<TheaterDTO> theaterDTO = theaterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(theaterDTO);
    }

    /**
     * DELETE  /theaters/:id : delete the "id" theater.
     *
     * @param id the id of the theaterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/theaters/{id}")
    @Timed
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        log.debug("REST request to delete Theater : {}", id);
        theaterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
