package com.npd.core.controller;

import com.npd.core.entity.Base;
import com.npd.core.entity.BaseDTO;
import com.npd.core.entity.DeleteResponse;
import com.npd.core.entity.GenericResponse;
import com.npd.core.service.BaseService;
import com.npd.core.utils.StatusUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Log4j2
@RestController
public abstract class BaseControllerImpl<T extends Base,U extends BaseDTO, V extends BaseService> {

    public abstract V getService();


    @GetMapping
    public ResponseEntity<GenericResponse<List<U>>> getAll() {
        List<U> result = getService().findAll();
        if(result.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(
                GenericResponse.<List<U>>builder()
                        .payload(result)
                        .build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GenericResponse<U>> get(@PathVariable Long id) {
        return ResponseEntity.ok(
                GenericResponse.<U>builder()
                        .payload((U) getService().findById(id))
                        .build());
    }

    @PostMapping
    public ResponseEntity<GenericResponse<U>> create(@RequestBody U object) {
        try {
            return ResponseEntity.ok(
                    GenericResponse.<U>builder()
                            .payload((U) getService().create(object))
                            .build());
        } catch (Exception ex) {
            log.error(object.toString() + " cant be created because of: ", ex);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), ex);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<U>> update(@RequestBody U object, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    GenericResponse.<U>builder()
                            .payload((U) getService().update(object, id))
                            .build());
        } catch (EntityNotFoundException enf) {
            log.error(object.toString() + " cant be updated because of: ", enf);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enf.getMessage(), enf);
            //} catch (InvalidObjectForCreationException ex) {
        } catch (Exception ex) {
            log.error(object.toString() + " cant be updated because of: ", ex);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<DeleteResponse>> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    GenericResponse.<DeleteResponse>builder()
                            .payload(
                                    DeleteResponse.builder()
                                            .deletedId(id)
                                            .message(StatusUtils.SUCCESS)
                                            .build())
                            .build());
        } catch (EntityNotFoundException enf) {
            log.error("Building object cant be removed: ", enf);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enf.getMessage(), enf);
        }
    }
}
