package com.mediasoft.warehouse.mapper;

import com.mediasoft.warehouse.dto.CreateProductDto;
import com.mediasoft.warehouse.dto.GetProductDto;
import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.dto.UpdateProductDto;
import com.mediasoft.warehouse.model.Product;
import org.mapstruct.*;

/**
 * Маппер для {@link Product} и {@link ProductDto}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    /**
     * Преобразует {@link ProductDto} в {@link Product}.
     *
     * @param productDto {@link ProductDto} для преобразования
     * @return преобразованный {@link Product}
     */
    Product toProduct(ProductDto productDto);

    /**
     * Преобразует {@link Product} в {@link ProductDto}.
     *
     * @param product {@link Product} для преобразования
     * @return преобразованный {@link ProductDto}
     */
    ProductDto toProductDto(Product product);
    GetProductDto toGetProductDto(Product product);
    ProductDto toProductDto(CreateProductDto product);
    ProductDto toProductDto(UpdateProductDto product);

}