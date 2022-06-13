package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.UserB2B;
import com.reliance.jmdb2b.repository.UserB2BRepository;
import com.reliance.jmdb2b.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reliance.jmdb2b.domain.UserB2B}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserB2BResource {

    private final Logger log = LoggerFactory.getLogger(UserB2BResource.class);

    private static final String ENTITY_NAME = "userB2B";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserB2BRepository userB2BRepository;

    public UserB2BResource(UserB2BRepository userB2BRepository) {
        this.userB2BRepository = userB2BRepository;
    }

    /**
     * {@code POST  /user-b-2-bs} : Create a new userB2B.
     *
     * @param userB2B the userB2B to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userB2B, or with status {@code 400 (Bad Request)} if the userB2B has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-b-2-bs")
    public ResponseEntity<UserB2B> createUserB2B(@Valid @RequestBody UserB2B userB2B) throws URISyntaxException {
        log.debug("REST request to save UserB2B : {}", userB2B);
        if (userB2B.getId() != null) {
            throw new BadRequestAlertException("A new userB2B cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserB2B result = userB2BRepository.save(userB2B);
        return ResponseEntity
            .created(new URI("/api/user-b-2-bs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-b-2-bs/:id} : Updates an existing userB2B.
     *
     * @param id the id of the userB2B to save.
     * @param userB2B the userB2B to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userB2B,
     * or with status {@code 400 (Bad Request)} if the userB2B is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userB2B couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-b-2-bs/{id}")
    public ResponseEntity<UserB2B> updateUserB2B(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserB2B userB2B
    ) throws URISyntaxException {
        log.debug("REST request to update UserB2B : {}, {}", id, userB2B);
        if (userB2B.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userB2B.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userB2BRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserB2B result = userB2BRepository.save(userB2B);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userB2B.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-b-2-bs/:id} : Partial updates given fields of an existing userB2B, field will ignore if it is null
     *
     * @param id the id of the userB2B to save.
     * @param userB2B the userB2B to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userB2B,
     * or with status {@code 400 (Bad Request)} if the userB2B is not valid,
     * or with status {@code 404 (Not Found)} if the userB2B is not found,
     * or with status {@code 500 (Internal Server Error)} if the userB2B couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-b-2-bs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserB2B> partialUpdateUserB2B(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserB2B userB2B
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserB2B partially : {}, {}", id, userB2B);
        if (userB2B.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userB2B.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userB2BRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserB2B> result = userB2BRepository
            .findById(userB2B.getId())
            .map(existingUserB2B -> {
                if (userB2B.getAgentId() != null) {
                    existingUserB2B.setAgentId(userB2B.getAgentId());
                }
                if (userB2B.getEmail() != null) {
                    existingUserB2B.setEmail(userB2B.getEmail());
                }
                if (userB2B.getPassword() != null) {
                    existingUserB2B.setPassword(userB2B.getPassword());
                }
                if (userB2B.getCreatedTime() != null) {
                    existingUserB2B.setCreatedTime(userB2B.getCreatedTime());
                }
                if (userB2B.getUpdatedTime() != null) {
                    existingUserB2B.setUpdatedTime(userB2B.getUpdatedTime());
                }
                if (userB2B.getCreatedBy() != null) {
                    existingUserB2B.setCreatedBy(userB2B.getCreatedBy());
                }
                if (userB2B.getUpdatedBy() != null) {
                    existingUserB2B.setUpdatedBy(userB2B.getUpdatedBy());
                }

                return existingUserB2B;
            })
            .map(userB2BRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userB2B.getId().toString())
        );
    }

    /**
     * {@code GET  /user-b-2-bs} : get all the userB2BS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userB2BS in body.
     */
    @GetMapping("/user-b-2-bs")
    public List<UserB2B> getAllUserB2BS() {
        log.debug("REST request to get all UserB2BS");
        return userB2BRepository.findAll();
    }

    /**
     * {@code GET  /user-b-2-bs/:id} : get the "id" userB2B.
     *
     * @param id the id of the userB2B to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userB2B, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-b-2-bs/{id}")
    public ResponseEntity<UserB2B> getUserB2B(@PathVariable Long id) {
        log.debug("REST request to get UserB2B : {}", id);
        Optional<UserB2B> userB2B = userB2BRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userB2B);
    }

    /**
     * {@code DELETE  /user-b-2-bs/:id} : delete the "id" userB2B.
     *
     * @param id the id of the userB2B to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-b-2-bs/{id}")
    public ResponseEntity<Void> deleteUserB2B(@PathVariable Long id) {
        log.debug("REST request to delete UserB2B : {}", id);
        userB2BRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
