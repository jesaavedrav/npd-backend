package com.npd.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Auditable;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@SuperBuilder
public class AuditEntry extends Base implements Auditable {

    public Long entityId;
    public String objectType;

    @Override
    public Optional getCreatedBy() {
        return Optional.empty();
    }

    @Override
    public void setCreatedBy(Object createdBy) {

    }

    @Override
    public Optional getCreatedDate() {
        return Optional.empty();
    }

    @Override
    public void setCreatedDate(TemporalAccessor creationDate) {

    }

    @Override
    public Optional getLastModifiedBy() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedBy(Object lastModifiedBy) {

    }

    @Override
    public Optional getLastModifiedDate() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedDate(TemporalAccessor lastModifiedDate) {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
