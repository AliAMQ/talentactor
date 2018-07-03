package com.talentactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.talentactor.service.FilmService;
import com.talentactor.web.rest.errors.BadRequestAlertException;
import com.talentactor.web.rest.util.HeaderUtil;
import com.talentactor.web.rest.util.PaginationUtil;
import com.talentactor.service.dto.FilmDTO;
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
 * REST controller for managing Film.
 */
@RestController
@RequestMapping("/api")
public class FilmResource {

    private final Logger log = LoggerFactory.getLogger(FilmResource.class);

    private static final String ENTITY_NAME = "film";

    private final FilmService filmService;

    public FilmResource(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * POST  /films : Create a new film.
     *
     * @param filmDTO the filmDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new filmDTO, or with status 400 (Bad Request) if the film has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/films")
    @Timed
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO filmDTO) throws URISyntaxException {
        log.debug("REST request to save Film : {}", filmDTO);
        if (filmDTO.getId() != null) {
            throw new BadRequestAlertException("A new film cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilmDTO result = filmService.save(filmDTO);
        return ResponseEntity.created(new URI("/api/films/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /films : Updates an existing film.
     *
     * @param filmDTO the filmDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated filmDTO,
     * or with status 400 (Bad Request) if the filmDTO is not valid,
     * or with status 500 (Internal Server Error) if the filmDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/films")
    @Timed
    public ResponseEntity<FilmDTO> updateFilm(@Valid @RequestBody FilmDTO filmDTO) throws URISyntaxException {
        log.debug("REST request to update Film : {}", filmDTO);
        if (filmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FilmDTO result = filmService.save(filmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, filmDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /films : get all the films.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of films in body
     */
    @GetMapping("/films")
    @Timed
    public ResponseEntity<List<FilmDTO>> getAllFilms(Pageable pageable) {
        log.debug("REST request to get a page of Films");
        Page<FilmDTO> page = filmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/films");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /films/:id : get the "id" film.
     *
     * @param id the id of the filmDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the filmDTO, or with status 404 (Not Found)
     */
    @GetMapping("/films/{id}")
    @Timed
    public ResponseEntity<FilmDTO> getFilm(@PathVariable Long id) {
        log.debug("REST request to get Film : {}", id);
        Optional<FilmDTO> filmDTO = filmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filmDTO);
    }

    /**
     * DELETE  /films/:id : delete the "id" film.
     *
     * @param id the id of the filmDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/films/{id}")
    @Timed
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        log.debug("REST request to delete Film : {}", id);
        filmService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
