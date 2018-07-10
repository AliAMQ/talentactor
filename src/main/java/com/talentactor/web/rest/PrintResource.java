package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.PrintService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.PrintDTO;
import com.talentactor.service.dto.PrintCriteria;
import com.talentactor.service.PrintQueryService;
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
 * REST controller for managing Print.
 */
@RestController
@RequestMapping("/api")
public class PrintResource {

    private final Logger log = LoggerFactory.getLogger(PrintResource.class);

    private static final String ENTITY_NAME = "print";

    private final PrintService printService;

    private final PrintQueryService printQueryService;

    public PrintResource(PrintService printService, PrintQueryService printQueryService) {
        this.printService = printService;
        this.printQueryService = printQueryService;
    }

    /**
     * POST  /prints : Create a new print.
     *
     * @param printDTO the printDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new printDTO, or with status 400 (Bad Request) if the print has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prints")
    @Timed
    public ResponseEntity<PrintDTO> createPrint(@Valid @RequestBody PrintDTO printDTO) throws URISyntaxException {
        log.debug("REST request to save Print : {}", printDTO);
        if (printDTO.getId() != null) {
            throw new BadRequestAlertException("A new print cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrintDTO result = printService.save(printDTO);
        return ResponseEntity.created(new URI("/api/prints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prints : Updates an existing print.
     *
     * @param printDTO the printDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated printDTO,
     * or with status 400 (Bad Request) if the printDTO is not valid,
     * or with status 500 (Internal Server Error) if the printDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prints")
    @Timed
    public ResponseEntity<PrintDTO> updatePrint(@Valid @RequestBody PrintDTO printDTO) throws URISyntaxException {
        log.debug("REST request to update Print : {}", printDTO);
        if (printDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrintDTO result = printService.save(printDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, printDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prints : get all the prints.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of prints in body
     */
    @GetMapping("/prints")
    @Timed
    public ResponseEntity<List<PrintDTO>> getAllPrints(PrintCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Prints by criteria: {}", criteria);
        Page<PrintDTO> page = printQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prints/:id : get the "id" print.
     *
     * @param id the id of the printDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the printDTO, or with status 404 (Not Found)
     */
    @GetMapping("/prints/{id}")
    @Timed
    public ResponseEntity<PrintDTO> getPrint(@PathVariable Long id) {
        log.debug("REST request to get Print : {}", id);
        Optional<PrintDTO> printDTO = printService.findOne(id);
        return ResponseUtil.wrapOrNotFound(printDTO);
    }

    /**
     * DELETE  /prints/:id : delete the "id" print.
     *
     * @param id the id of the printDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prints/{id}")
    @Timed
    public ResponseEntity<Void> deletePrint(@PathVariable Long id) {
        log.debug("REST request to delete Print : {}", id);
        printService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
