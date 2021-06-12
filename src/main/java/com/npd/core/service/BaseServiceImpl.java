package com.npd.core.service;

import com.npd.core.entity.AuditEntry;
import com.npd.core.entity.Base;
import com.npd.core.entity.BaseDTO;
import com.npd.core.errors.BusinessError;
import com.npd.core.errors.InternalErrorCode;
import com.npd.core.errors.exceptions.InvalidObjectForCreationException;
import com.npd.core.repository.AuditEntryRepository;
import com.npd.core.repository.BaseRepository;
import com.npd.core.utils.StatusUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.PropertyUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Auditable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public abstract class BaseServiceImpl<T extends Base, U extends BaseDTO> implements BaseService<T, U> {

    //@Autowired
    //public AuditEntryRepository auditEntryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public T findById(Long id) {
        Optional<T> optionalObject = getRepository().findById(id);
        T entity = optionalObject.orElse(null);
        if (null == entity) {
            log.warn("there's no an object with id: " + id);
            throw new EntityNotFoundException(id.toString());
        }
        return entity;
    }

    @Override
    public List<U> findAll() {
        List<T> queryResult = getRepository().findAll();
        return queryResult.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public U create(U dtoObject) throws InvalidObjectForCreationException {
        List<BusinessError> errors = new ArrayList<>();
        validateEntityForCreation(dtoObject, errors);

        if (errors.isEmpty()) {
            T entity = convertToEntity(dtoObject);

            entity.setStatus(StatusUtils.ACTIVE);

            linkEntityWithHistoricalEntityAndPersist(entity, null);
            addAuditDetails(entity);

            return convertToDTO(entity);
        } else {
            log.error("entity wont be created because of validation failures");
            throw new InvalidObjectForCreationException(errors);
        }
    }

    @Override
    public U update(U dtoObject, Long id) throws InvalidObjectForCreationException {
        List<BusinessError> errors = new ArrayList<>();
        validateEntityForUpdate(dtoObject, errors);

        if (errors.isEmpty()) {
            T entity = findById(id);
            if (null == entity) {
                log.error("entity wont be updated because of validation failures");
                throw new EntityNotFoundException(id.toString());
            } else if (entity.isHistorical()) {
                log.error("historical entities cant be update");
                throw new EntityNotFoundException(id.toString());
            } else {
                Long copyId = buildAndSaveCopy(entity);

                copyFromDTOToEntity(entity, dtoObject);

                linkEntityWithHistoricalEntityAndPersist(entity, copyId);
                addAuditDetails(entity);

                return convertToDTO(entity);
            }
        } else {
            log.error("entity wont be updated because of validation failures");
            throw new InvalidObjectForCreationException(errors);
        }
    }

    protected Long buildAndSaveCopy(T entity) {
        try {
            T copyObject = getManagedEntityClass().newInstance();
            PropertyUtils.copyProperties(copyObject, entity);
            copyObject.setId(null);
            copyObject.setHistorical(true);
            return ((T) getRepository().save(copyObject)).getId();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T linkEntityWithHistoricalEntityAndPersist(T entity, Long historicalEntityId) {
        entity.setHistoryId(historicalEntityId);
        //entity.setTimestamp(new Date());

        return (T) getRepository().save(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = findById(id);
        if (null == entity) {
            log.error("entity wont be updated because of validation failures");
            throw new EntityNotFoundException(id.toString());
        } else if (entity.isHistorical()) {
            log.error("cant remove historical object");
            throw new EntityNotFoundException(id.toString());
        } else {
            Long copyId = buildAndSaveCopy(entity);

            entity.setStatus(StatusUtils.REMOVED);

            linkEntityWithHistoricalEntityAndPersist(entity, copyId);
            addAuditDetails(entity);
        }
    }


    protected abstract BaseRepository getRepository();

    protected abstract Class<T> getManagedEntityClass();

    protected abstract Class<U> getManagedDTOClass();

    protected abstract void copySpecificFromDTOToEntity(T entity, U dtoObject);

    protected U convertToDTO(T entity) {
        U dtoObject = modelMapper.map(entity, getManagedDTOClass());
        return dtoObject;
    }

    protected T convertToEntity(U dtoObject) {
        T entity = modelMapper.map(dtoObject, getManagedEntityClass());
        return entity;
    }

    protected void validateEntityForCreation(U dtoObject, List<BusinessError> errors) {
        if (null != dtoObject.getId()) {
            errors.add(new BusinessError(InternalErrorCode.NOT_ACCEPTED_ID, "id is not accepted for creation"));
        }
        if (null != dtoObject.getStatus()) {
            errors.add(new BusinessError(InternalErrorCode.NOT_ACCEPTED_STATUS, "status is not accepted for creation"));
        }
        if (null != dtoObject.getTimestamp()) {
            errors.add(new BusinessError(InternalErrorCode.NOT_ACCEPTED_TIMESTAMP, "timestamp is not accepted for creation"));
        }
    }

    protected void validateEntityForUpdate(U entity, List<BusinessError> errors) {
        if (null == entity.getStatus()) {
            errors.add(new BusinessError(InternalErrorCode.NOT_ACCEPTED_STATUS, "status must be present"));
        }
        if (null != entity.getTimestamp()) {
            errors.add(new BusinessError(InternalErrorCode.NOT_ACCEPTED_TIMESTAMP, "timestamp is not accepted for modification"));
        }
    }

    private void copyFromDTOToEntity(T entity, U dtoObject) {
        entity.setStatus(dtoObject.getStatus());
        copySpecificFromDTOToEntity(entity, dtoObject);
    }

    private void addAuditDetails(T entity) {
        /*
        if (entity instanceof Auditable) {
            log.info("adding audit details");
            AuditEntry auditEntry = AuditEntry
                    .builder()
                    .entityId(entity.getId())
                    .objectType(entity.getClass().getName())
                    .timestamp(entity.getTimestamp())
                    .build();
            auditEntryRepository.save(auditEntry);
        }*/
    }

}