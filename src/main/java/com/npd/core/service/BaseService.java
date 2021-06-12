package com.npd.core.service;

import com.npd.core.entity.Base;
import com.npd.core.entity.BaseDTO;
import com.npd.core.errors.exceptions.InvalidObjectForCreationException;

import java.util.List;

public interface BaseService<T extends Base, U extends BaseDTO> {

    List<U> findAll();

    T findById(Long id);

    U create(U dtoObject) throws InvalidObjectForCreationException;

    U update(U dtoObject, Long id) throws InvalidObjectForCreationException;

    void delete(Long id);

}