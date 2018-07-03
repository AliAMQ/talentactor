package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.TelevisionService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.TelevisionDTO;
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
 * REST controller for managing Television.
 */
@RestController
@RequestMapping("/api")
public class TelevisionResource {

    private final Logger log = LoggerFactory.getLogger(TelevisionResource.class);

    private static final String ENTITY_NAME = "television";

    private final TelevisionService televisionService;

    public TelevisionResource(TelevisionService televisionService) {
        this.televisionService = televisionService;
    }

    /**
     * POST  /televisions : Create a new television.
     *
     * @param televisionDTO the televisionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new televisionDTO, or with status 400 (Bad Request) if the television has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/televisions")
    @Timed
    public ResponseEntity<TelevisionDTO> createTelevision(@Valid @RequestBody TelevisionDTO televisionDTO) throws URISyntaxException {
        log.debug("REST request to save Television : {}", televisionDTO);
        if (televisionDTO.getId() != null) {
            throw new BadRequestAlertException("A new television cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelevisionDTO result = televisionService.save(televisionDTO);
        return ResponseEntity.created(new URI("/api/televisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /televisions : Updates an existing television.
     *
     * @param televisionDTO the televisionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated televisionDTO,
     * or with status 400 (Bad Request) if the televisionDTO is not valid,
     * or with status 500 (Internal Server Error) if the televisionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/televisions")
    @Timed
    public ResponseEntity<TelevisionDTO> updateTelevision(@Valid @RequestBody TelevisionDTO televisionDTO) throws URISyntaxException {
        log.debug("REST request to update Television : {}", televisionDTO);
        if (televisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TelevisionDTO result = televisionService.save(televisionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, televisionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /televisions : get all the televisions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of televisions in body
     */
    @GetMapping("/televisions")
    @Timed
    public ResponseEntity<List<TelevisionDTO>> getAllTelevisions(Pageable pageable) {
        log.debug("REST request to get a page of Televisions");
        Page<TelevisionDTO> page = televisionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/televisions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /televisions/:id : get the "id" television.
     *
     * @param id the id of the televisionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the televisionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/televisions/{id}")
    @Timed
    public ResponseEntity<TelevisionDTO> getTelevision(@PathVariable Long id) {
        log.debug("REST request to get Television : {}", id);
        Optional<TelevisionDTO> televisionDTO = televisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(televisionDTO);
    }

    /**
     * DELETE  /televisions/:id : delete the "id" television.
     *
     * @param id the id of the televisionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/televisions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelevision(@PathVariable Long id) {
        log.debug("REST request to delete Television : {}", id);
        televisionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
