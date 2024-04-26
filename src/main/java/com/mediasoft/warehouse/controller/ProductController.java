package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.CreateProductDto;
import com.mediasoft.warehouse.dto.GetProductDto;
import com.mediasoft.warehouse.dto.UpdateProductDto;
import com.mediasoft.warehouse.mapper.ProductMapper;
import com.mediasoft.warehouse.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST API контроллер для товаров.
 */
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * Получает все товары.
     *
     * @return список всех товаров
     */
    @GetMapping
    public List<GetProductDto> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    /**
     * Получает товар по его UUID.
     *
     * @param uuid UUID товара, который нужно получить
     * @return товар с заданным UUID
     */
    @GetMapping("/{uuid}")
    public GetProductDto getProductById(@PathVariable UUID uuid) {
        return productService.getProductById(uuid);
    }

    /**
     * Создает новый товар.
     *
     * @param createProductDto товар, который нужно создать
     * @return uuid созданного товара
     */
    @PostMapping
    public UUID createProduct(@RequestBody @Valid CreateProductDto createProductDto) {
        return productService.createProduct(productMapper.toProductDto(createProductDto));
    }

    /**
     * Обновляет товар по его UUID.
     *
     * @param updateProductDto dto товара, который нужно обновить
     * @param uuid UUID товара, который нужно обновить
     * @return обновленный товар
     */
    @PatchMapping("/{uuid}")
    public UUID updateProduct(@RequestBody @Valid UpdateProductDto updateProductDto, @PathVariable UUID uuid) {
        return productService.updateProduct(productMapper.toProductDto(updateProductDto), uuid);
    }

    /**
     * Удаляет товар по его UUID.
     *
     * @param uuid UUID товара, который нужно удалить
     */
    @DeleteMapping("/{uuid}")
    public void deleteProduct(@PathVariable UUID uuid) {
        productService.deleteProduct(uuid);
    }

}
