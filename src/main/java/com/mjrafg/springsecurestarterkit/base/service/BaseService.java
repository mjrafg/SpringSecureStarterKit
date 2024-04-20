package com.mjrafg.springsecurestarterkit.base.service;

import com.mjrafg.springsecurestarterkit.payload.request.PagingRequestBody;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void insertBulk(List<T> rows);
    boolean deleteById(ID id,boolean softDelete);
    boolean deleteById(ID id);
    Page<T> findAllFilter(PagingRequestBody requestBody, boolean softDelete);
    Optional<T> update(ID id, T details);
    List<T> findByActive(boolean isActive);
}
