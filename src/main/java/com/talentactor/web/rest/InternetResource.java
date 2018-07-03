package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.InternetService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.InternetDTO;
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
 * REST controller for managing Internet.
 */
@RestController
@RequestMapping("/api")
public class InternetResource {

    private final Logger log = LoggerFactory.getLogger(InternetResource.class);

    private static final String ENTITY_NAME = "internet";

    private final InternetService internetService;

    public InternetResource(InternetService internetService) {
        this.internetService = internetService;
    }

    /**
     * POST  /internets : Create a new internet.
     *
     * @param internetDTO the internetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internetDTO, or with status 400 (Bad Request) if the internet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/internets")
    @Timed
    public ResponseEntity<InternetDTO> createInternet(@Valid @RequestBody InternetDTO internetDTO) throws URISyntaxException {
        log.debug("REST request to save Internet : {}", internetDTO);
        if (internetDTO.getId() != null) {
            throw new BadRequestAlertException("A new internet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternetDTO result = internetService.save(internetDTO);
        return ResponseEntity.created(new URI("/api/internets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /internets : Updates an existing internet.
     *
     * @param internetDTO the internetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internetDTO,
     * or with status 400 (Bad Request) if the internetDTO is not valid,
     * or with status 500 (Internal Server Error) if the internetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/internets")
    @Timed
    public ResponseEntity<InternetDTO> updateInternet(@Valid @RequestBody InternetDTO internetDTO) throws URISyntaxException {
        log.debug("REST request to update Internet : {}", internetDTO);
        if (internetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InternetDTO result = internetService.save(internetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, internetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /internets : get all the internets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internets in body
     */
    @GetMapping("/internets")
    @Timed
    public ResponseEntity<List<InternetDTO>> getAllInternets(Pageable pageable) {
        log.debug("REST request to get a page of Internets");
        Page<InternetDTO> page = internetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /internets/:id : get the "id" internet.
     *
     * @param id the id of the internetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/internets/{id}")
    @Timed
    public ResponseEntity<InternetDTO> getInternet(@PathVariable Long id) {
        log.debug("REST request to get Internet : {}", id);
        Optional<InternetDTO> internetDTO = internetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(internetDTO);
    }

    /**
     * DELETE  /internets/:id : delete the "id" internet.
     *
     * @param id the id of the internetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/internets/{id}")
    @Timed
    public ResponseEntity<Void> deleteInternet(@PathVariable Long id) {
        log.debug("REST request to delete Internet : {}", id);
        internetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
