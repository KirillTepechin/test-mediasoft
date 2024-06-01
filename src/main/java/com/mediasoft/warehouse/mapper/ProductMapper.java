package com.mediasoft.warehouse.mapper;

import com.mediasoft.warehouse.integration.currency.CurrencyProvider;
import com.mediasoft.warehouse.integration.currency.CurrencyRateProvider;
import com.mediasoft.warehouse.dto.CreateProductDto;
import com.mediasoft.warehouse.dto.GetProductDto;
import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.dto.UpdateProductDto;
import com.mediasoft.warehouse.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Маппер для {@link Product} и {@link ProductDto}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProductMapper {
    @Autowired
    protected CurrencyRateProvider currencyRateProvider;
    @Autowired
    protected CurrencyProvider currencyProvider;
    /**
     * Преобразует {@link ProductDto} в {@link Product}.
     *
     * @param productDto {@link ProductDto} для преобразования
     * @return преобразованный {@link Product}
     */
    public abstract Product toProduct(ProductDto productDto);

    /**
     * Преобразует {@link Product} в {@link ProductDto}.
     *
     * @param product {@link Product} для преобразования
     * @return преобразованный {@link ProductDto}
     */
    public abstract ProductDto toProductDto(Product product);

    @Mapping(target = "currency", expression = "java(currencyProvider.getCurrentCurrency())")
    @Mapping(target = "price", expression = "java(calculatePrice(product))")
    public abstract GetProductDto toGetProductDto(Product product);
    public abstract ProductDto toProductDto(CreateProductDto product);
    public abstract ProductDto toProductDto(UpdateProductDto product);

    protected BigDecimal calculatePrice(Product product) {
        BigDecimal price = product.getPrice();
        BigDecimal currencyRate = currencyRateProvider.getCurrentCurrencyRate();
        return price.divide(currencyRate, RoundingMode.HALF_UP);
    }
}