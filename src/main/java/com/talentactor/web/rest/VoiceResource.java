package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.VoiceService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.VoiceDTO;
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
 * REST controller for managing Voice.
 */
@RestController
@RequestMapping("/api")
public class VoiceResource {

    private final Logger log = LoggerFactory.getLogger(VoiceResource.class);

    private static final String ENTITY_NAME = "voice";

    private final VoiceService voiceService;

    public VoiceResource(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    /**
     * POST  /voices : Create a new voice.
     *
     * @param voiceDTO the voiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voiceDTO, or with status 400 (Bad Request) if the voice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voices")
    @Timed
    public ResponseEntity<VoiceDTO> createVoice(@Valid @RequestBody VoiceDTO voiceDTO) throws URISyntaxException {
        log.debug("REST request to save Voice : {}", voiceDTO);
        if (voiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new voice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoiceDTO result = voiceService.save(voiceDTO);
        return ResponseEntity.created(new URI("/api/voices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voices : Updates an existing voice.
     *
     * @param voiceDTO the voiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voiceDTO,
     * or with status 400 (Bad Request) if the voiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the voiceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/voices")
    @Timed
    public ResponseEntity<VoiceDTO> updateVoice(@Valid @RequestBody VoiceDTO voiceDTO) throws URISyntaxException {
        log.debug("REST request to update Voice : {}", voiceDTO);
        if (voiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VoiceDTO result = voiceService.save(voiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voices : get all the voices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of voices in body
     */
    @GetMapping("/voices")
    @Timed
    public ResponseEntity<List<VoiceDTO>> getAllVoices(Pageable pageable) {
        log.debug("REST request to get a page of Voices");
        Page<VoiceDTO> page = voiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/voices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /voices/:id : get the "id" voice.
     *
     * @param id the id of the voiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/voices/{id}")
    @Timed
    public ResponseEntity<VoiceDTO> getVoice(@PathVariable Long id) {
        log.debug("REST request to get Voice : {}", id);
        Optional<VoiceDTO> voiceDTO = voiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voiceDTO);
    }

    /**
     * DELETE  /voices/:id : delete the "id" voice.
     *
     * @param id the id of the voiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voices/{id}")
    @Timed
    public ResponseEntity<Void> deleteVoice(@PathVariable Long id) {
        log.debug("REST request to delete Voice : {}", id);
        voiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
