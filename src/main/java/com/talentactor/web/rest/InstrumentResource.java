package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.InstrumentService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.InstrumentDTO;
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
 * REST controller for managing Instrument.
 */
@RestController
@RequestMapping("/api")
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);

    private static final String ENTITY_NAME = "instrument";

    private final InstrumentService instrumentService;

    public InstrumentResource(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    /**
     * POST  /instruments : Create a new instrument.
     *
     * @param instrumentDTO the instrumentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instrumentDTO, or with status 400 (Bad Request) if the instrument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/instruments")
    @Timed
    public ResponseEntity<InstrumentDTO> createInstrument(@Valid @RequestBody InstrumentDTO instrumentDTO) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrumentDTO);
        if (instrumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new instrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstrumentDTO result = instrumentService.save(instrumentDTO);
        return ResponseEntity.created(new URI("/api/instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instruments : Updates an existing instrument.
     *
     * @param instrumentDTO the instrumentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instrumentDTO,
     * or with status 400 (Bad Request) if the instrumentDTO is not valid,
     * or with status 500 (Internal Server Error) if the instrumentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/instruments")
    @Timed
    public ResponseEntity<InstrumentDTO> updateInstrument(@Valid @RequestBody InstrumentDTO instrumentDTO) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}", instrumentDTO);
        if (instrumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InstrumentDTO result = instrumentService.save(instrumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, instrumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instruments : get all the instruments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instruments in body
     */
    @GetMapping("/instruments")
    @Timed
    public ResponseEntity<List<InstrumentDTO>> getAllInstruments(Pageable pageable) {
        log.debug("REST request to get a page of Instruments");
        Page<InstrumentDTO> page = instrumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instruments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instruments/:id : get the "id" instrument.
     *
     * @param id the id of the instrumentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instrumentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/instruments/{id}")
    @Timed
    public ResponseEntity<InstrumentDTO> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Optional<InstrumentDTO> instrumentDTO = instrumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instrumentDTO);
    }

    /**
     * DELETE  /instruments/:id : delete the "id" instrument.
     *
     * @param id the id of the instrumentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/instruments/{id}")
    @Timed
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
