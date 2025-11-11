package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.partner.CreatePartnerRequest;
import com.xholacracy.application.dto.partner.PartnerDTO;
import com.xholacracy.application.dto.partner.UpdatePartnerRequest;
import com.xholacracy.domain.model.partner.Partner;
import com.xholacracy.domain.model.partner.PartnerId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Partner entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface PartnerMapper {
    
    @Mapping(target = "id", expression = "java(partner.getId().getValue())")
    @Mapping(target = "roles", ignore = true)
    PartnerDTO toDTO(Partner partner);
    
    // Note: Use Partner.create() factory method instead of mapping from request
    // Entities should be created through domain factory methods to ensure business rules
    
    void updateFromRequest(UpdatePartnerRequest request, @MappingTarget Partner partner);
    
    default PartnerId mapId(String id) {
        return id != null ? PartnerId.of(id) : null;
    }
    
    default String mapId(PartnerId id) {
        return id != null ? id.getValue() : null;
    }
}
