package com.mediasoft.warehouse.mapper;

import com.mediasoft.warehouse.model.Customer;
import com.mediasoft.warehouse.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toOrder(String deliveryAddress, Customer customer);
}
