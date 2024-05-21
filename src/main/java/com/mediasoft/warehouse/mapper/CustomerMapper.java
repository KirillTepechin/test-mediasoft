package com.mediasoft.warehouse.mapper;

import com.mediasoft.warehouse.dto.GetCustomerDto;
import com.mediasoft.warehouse.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    GetCustomerDto toGetCustomerDto(Customer customer);
}
