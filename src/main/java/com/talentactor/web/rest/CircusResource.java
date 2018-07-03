package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.CircusService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.CircusDTO;
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
 * REST controller for managing Circus.
 */
@RestController
@RequestMapping("/api")
public class CircusResource {

    private final Logger log = LoggerFactory.getLogger(CircusResource.class);

    private static final String ENTITY_NAME = "circus";

    private final CircusService circusService;

    public CircusResource(CircusService circusService) {
        this.circusService = circusService;
    }

    /**
     * POST  /circuses : Create a new circus.
     *
     * @param circusDTO the circusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new circusDTO, or with status 400 (Bad Request) if the circus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/circuses")
    @Timed
    public ResponseEntity<CircusDTO> createCircus(@Valid @RequestBody CircusDTO circusDTO) throws URISyntaxException {
        log.debug("REST request to save Circus : {}", circusDTO);
        if (circusDTO.getId() != null) {
            throw new BadRequestAlertException("A new circus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CircusDTO result = circusService.save(circusDTO);
        return ResponseEntity.created(new URI("/api/circuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /circuses : Updates an existing circus.
     *
     * @param circusDTO the circusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated circusDTO,
     * or with status 400 (Bad Request) if the circusDTO is not valid,
     * or with status 500 (Internal Server Error) if the circusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/circuses")
    @Timed
    public ResponseEntity<CircusDTO> updateCircus(@Valid @RequestBody CircusDTO circusDTO) throws URISyntaxException {
        log.debug("REST request to update Circus : {}", circusDTO);
        if (circusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CircusDTO result = circusService.save(circusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, circusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /circuses : get all the circuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of circuses in body
     */
    @GetMapping("/circuses")
    @Timed
    public ResponseEntity<List<CircusDTO>> getAllCircuses(Pageable pageable) {
        log.debug("REST request to get a page of Circuses");
        Page<CircusDTO> page = circusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/circuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /circuses/:id : get the "id" circus.
     *
     * @param id the id of the circusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the circusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/circuses/{id}")
    @Timed
    public ResponseEntity<CircusDTO> getCircus(@PathVariable Long id) {
        log.debug("REST request to get Circus : {}", id);
        Optional<CircusDTO> circusDTO = circusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(circusDTO);
    }

    /**
     * DELETE  /circuses/:id : delete the "id" circus.
     *
     * @param id the id of the circusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/circuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCircus(@PathVariable Long id) {
        log.debug("REST request to delete Circus : {}", id);
        circusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
