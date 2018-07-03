package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.SwimmingService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.SwimmingDTO;
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
 * REST controller for managing Swimming.
 */
@RestController
@RequestMapping("/api")
public class SwimmingResource {

    private final Logger log = LoggerFactory.getLogger(SwimmingResource.class);

    private static final String ENTITY_NAME = "swimming";

    private final SwimmingService swimmingService;

    public SwimmingResource(SwimmingService swimmingService) {
        this.swimmingService = swimmingService;
    }

    /**
     * POST  /swimmings : Create a new swimming.
     *
     * @param swimmingDTO the swimmingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new swimmingDTO, or with status 400 (Bad Request) if the swimming has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/swimmings")
    @Timed
    public ResponseEntity<SwimmingDTO> createSwimming(@Valid @RequestBody SwimmingDTO swimmingDTO) throws URISyntaxException {
        log.debug("REST request to save Swimming : {}", swimmingDTO);
        if (swimmingDTO.getId() != null) {
            throw new BadRequestAlertException("A new swimming cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SwimmingDTO result = swimmingService.save(swimmingDTO);
        return ResponseEntity.created(new URI("/api/swimmings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /swimmings : Updates an existing swimming.
     *
     * @param swimmingDTO the swimmingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated swimmingDTO,
     * or with status 400 (Bad Request) if the swimmingDTO is not valid,
     * or with status 500 (Internal Server Error) if the swimmingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/swimmings")
    @Timed
    public ResponseEntity<SwimmingDTO> updateSwimming(@Valid @RequestBody SwimmingDTO swimmingDTO) throws URISyntaxException {
        log.debug("REST request to update Swimming : {}", swimmingDTO);
        if (swimmingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SwimmingDTO result = swimmingService.save(swimmingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, swimmingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /swimmings : get all the swimmings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of swimmings in body
     */
    @GetMapping("/swimmings")
    @Timed
    public ResponseEntity<List<SwimmingDTO>> getAllSwimmings(Pageable pageable) {
        log.debug("REST request to get a page of Swimmings");
        Page<SwimmingDTO> page = swimmingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/swimmings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /swimmings/:id : get the "id" swimming.
     *
     * @param id the id of the swimmingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the swimmingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/swimmings/{id}")
    @Timed
    public ResponseEntity<SwimmingDTO> getSwimming(@PathVariable Long id) {
        log.debug("REST request to get Swimming : {}", id);
        Optional<SwimmingDTO> swimmingDTO = swimmingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(swimmingDTO);
    }

    /**
     * DELETE  /swimmings/:id : delete the "id" swimming.
     *
     * @param id the id of the swimmingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/swimmings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSwimming(@PathVariable Long id) {
        log.debug("REST request to delete Swimming : {}", id);
        swimmingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
