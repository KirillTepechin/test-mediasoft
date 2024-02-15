package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.service.ProductService;
import com.mediasoft.warehouse.validation.ValidationMarkers;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    /**
     * Получает все товары.
     *
     * @return список всех товаров
     */
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Получает товар по его UUID.
     *
     * @param uuid UUID товара, который нужно получить
     * @return товар с заданным UUID
     */
    @GetMapping("/{uuid}")
    public ProductDto getProductById(@PathVariable UUID uuid) {
        return productService.getProductById(uuid);
    }

    /**
     * Создает новый товар.
     *
     * @param productDto товар, который нужно создать
     * @return созданный товар
     */
    @PostMapping
    public ProductDto createProduct(@RequestBody @Validated(ValidationMarkers.PostStrategy.class) ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    /**
     * Обновляет товар по его UUID.
     *
     * @param productDto dto товара, который нужно обновить
     * @param uuid UUID товара, который нужно обновить
     * @return обновленный товар
     */
    @PatchMapping("/{uuid}")
    public ProductDto updateProduct(@RequestBody @Validated(ValidationMarkers.PatchStrategy.class) ProductDto productDto,
                                    @PathVariable UUID uuid) {
        return productService.updateProduct(productDto, uuid);
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
