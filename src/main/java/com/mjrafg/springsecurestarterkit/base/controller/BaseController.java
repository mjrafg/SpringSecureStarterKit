package com.mjrafg.springsecurestarterkit.base.controller;


import com.mjrafg.springsecurestarterkit.base.service.BaseService;
import com.mjrafg.springsecurestarterkit.payload.InsertBulkRequestBody;
import com.mjrafg.springsecurestarterkit.payload.request.PagingRequestBody;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Optional;

public abstract class BaseController<T, ID extends Serializable> {

    private final BaseService<T, ID> service;

    protected BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return MainResponse.ok(service.findAll());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> findAllFilter(@RequestBody PagingRequestBody requestBody, @RequestHeader("softDelete") Boolean softDelete) {
        return MainResponse.ok(service.findAllFilter(requestBody,softDelete));
    }

    @GetMapping("/activated")
    public ResponseEntity<?> findAllActivated() {
        return MainResponse.ok(service.findByActive(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        if(entity.isPresent()){
            return MainResponse.ok(entity.get());
        }else{
            return MainResponse.error(entity.getClass() + " not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody T entity) {
        T createdEntity = service.save(entity);
        return MainResponse.ok(createdEntity);
    }
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody InsertBulkRequestBody<T> body) {
        service.insertBulk(body.getRows());
        return MainResponse.ok();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody T entity) {
        Optional<T> updatedEntity = service.update(id, entity);
        if(updatedEntity.isPresent()){
            return MainResponse.ok(updatedEntity.get());
        }else{
            return MainResponse.error(entity.getClass() + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable ID id) {
        service.deleteById(id);
        return MainResponse.ok("deleted successfully");
    }
}

