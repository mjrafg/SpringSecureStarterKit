package com.mjrafg.springsecurestarterkit.base.service;


import com.mjrafg.springsecurestarterkit.payload.FilterParams;
import com.mjrafg.springsecurestarterkit.payload.request.PagingRequestBody;
import com.mjrafg.springsecurestarterkit.base.repository.BaseRepository;
import com.mjrafg.springsecurestarterkit.utils.FilterSpecificationUtils;
import com.mjrafg.springsecurestarterkit.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract  class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
    private static final Logger LOGGER = Logger.getLogger(BaseServiceImpl.class.getName());
    @Autowired
    private ModelMapperUtil modelMapperUtil;
    private final BaseRepository<T, ID> repository;

    public BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }
    // Utility method to get ID using reflection
    @SuppressWarnings("unchecked")
    private ID getId(T entity) {
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            return (ID) getIdMethod.invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ID", e);
        }
    }

    // Utility method to set ID using reflection
    private void setId(T entity, ID id) {
        try {
            Method setIdMethod = findSetIdMethod(entity.getClass(), id);
            setIdMethod.invoke(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID", e);
        }
    }
    // Helper method to find the setId method with the correct parameter type
    private Method findSetIdMethod(Class<?> clazz, ID id) throws NoSuchMethodException {
        // Assuming the ID setter method is named 'setId'. Adjust if your naming convention is different.
        // This loops through all methods named 'setId' and selects the first with a compatible type.
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals("setId") && method.getParameterTypes().length == 1) {
                // Check if the method parameter is assignable from the ID class
                if (method.getParameterTypes()[0].isAssignableFrom(id.getClass())) {
                    return method;
                }
            }
        }
        throw new NoSuchMethodException("Method setId not found for class " + clazz.getName() + " with parameter type " + id.getClass().getName());
    }
    @Override
    public Optional<T> update(ID id, T details) {
        return repository.findById(id).map(existingEntity -> {
            ID originalId = getId(existingEntity);
            modelMapperUtil.map(details, existingEntity);
            setId(existingEntity, originalId);
            return repository.save(existingEntity);
        });
    }
    @Override
    @Transactional
    public void insertBulk(List<T> rows) {
        repository.saveAll(rows);
    }

    @Override
    public Page<T> findAllFilter(PagingRequestBody requestBody, boolean softDelete) {
        Pageable pageable = PageRequest.of(requestBody.getPageNum(), requestBody.getPageSize()
                , Sort.by(Sort.Direction.fromString(requestBody.getOrderSort()), requestBody.getOrderColumn()));
        if(softDelete){
            FilterParams filterParams = new FilterParams();
            filterParams.setColumnName("softDelete");
            filterParams.setOperator("is");
            filterParams.setLogic("and");
            filterParams.setColumnValue("false");
            requestBody.getFilters().add(filterParams);
        }
        return repository.findAll(FilterSpecificationUtils.createSpecification(requestBody.getFilters()), pageable);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findByActive(boolean isActive) {
        return repository.findByActive(isActive);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }
    @Override
    public boolean deleteById(ID id) {
        return deleteById(id, false); // Call the overloaded method with softDelete set to false
    }
    @Override
    public boolean deleteById(ID id,boolean softDelete) {
        return repository.findById(id).map(entity -> {
            if(softDelete){
                try {
                    Method setSoftDeleteMethod = entity.getClass().getMethod("setSoftDelete", boolean.class);
                    setSoftDeleteMethod.invoke(entity, true);
                } catch (NoSuchMethodException e) {
                    LOGGER.warning("Soft delete not supported for entity type: " + entity.getClass().getName());
                    return false;
                } catch (Exception e) {
                    LOGGER.severe("Failed to set soft delete for entity: " + e.getMessage());
                    return false;
                }
                repository.save(entity);
            } else {
                repository.deleteById(id);
            }
            return true;
        }).orElse(false);

    }

}

