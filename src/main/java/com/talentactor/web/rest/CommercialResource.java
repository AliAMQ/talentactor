package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.CommercialService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.CommercialDTO;
import com.talentactor.service.dto.CommercialCriteria;
import com.talentactor.service.CommercialQueryService;
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
 * REST controller for managing Commercial.
 */
@RestController
@RequestMapping("/api")
public class CommercialResource {

    private final Logger log = LoggerFactory.getLogger(CommercialResource.class);

    private static final String ENTITY_NAME = "commercial";

    private final CommercialService commercialService;

    private final CommercialQueryService commercialQueryService;

    public CommercialResource(CommercialService commercialService, CommercialQueryService commercialQueryService) {
        this.commercialService = commercialService;
        this.commercialQueryService = commercialQueryService;
    }

    /**
     * POST  /commercials : Create a new commercial.
     *
     * @param commercialDTO the commercialDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commercialDTO, or with status 400 (Bad Request) if the commercial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commercials")
    @Timed
    public ResponseEntity<CommercialDTO> createCommercial(@Valid @RequestBody CommercialDTO commercialDTO) throws URISyntaxException {
        log.debug("REST request to save Commercial : {}", commercialDTO);
        if (commercialDTO.getId() != null) {
            throw new BadRequestAlertException("A new commercial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommercialDTO result = commercialService.save(commercialDTO);
        return ResponseEntity.created(new URI("/api/commercials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commercials : Updates an existing commercial.
     *
     * @param commercialDTO the commercialDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commercialDTO,
     * or with status 400 (Bad Request) if the commercialDTO is not valid,
     * or with status 500 (Internal Server Error) if the commercialDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commercials")
    @Timed
    public ResponseEntity<CommercialDTO> updateCommercial(@Valid @RequestBody CommercialDTO commercialDTO) throws URISyntaxException {
        log.debug("REST request to update Commercial : {}", commercialDTO);
        if (commercialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommercialDTO result = commercialService.save(commercialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commercialDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commercials : get all the commercials.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of commercials in body
     */
    @GetMapping("/commercials")
    @Timed
    public ResponseEntity<List<CommercialDTO>> getAllCommercials(CommercialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Commercials by criteria: {}", criteria);
        Page<CommercialDTO> page = commercialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commercials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commercials/:id : get the "id" commercial.
     *
     * @param id the id of the commercialDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commercialDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commercials/{id}")
    @Timed
    public ResponseEntity<CommercialDTO> getCommercial(@PathVariable Long id) {
        log.debug("REST request to get Commercial : {}", id);
        Optional<CommercialDTO> commercialDTO = commercialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commercialDTO);
    }

    /**
     * DELETE  /commercials/:id : delete the "id" commercial.
     *
     * @param id the id of the commercialDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commercials/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommercial(@PathVariable Long id) {
        log.debug("REST request to delete Commercial : {}", id);
        commercialService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
