package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.mapper.ProductMapper;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Бизнес-логика работы с товарами.
 */
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Получить все товары.
     * @return список всех товаров
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductDto).collect(Collectors.toList());
    }

    /**
     * Получить товар по его UUID.
     *
     * @param uuid UUID товар, который нужно получить
     * @return товар с заданным UUID
     */
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID uuid) {
        return productMapper.toProductDto(productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid)));
    }

    /**
     * Создает новый товар.
     *
     * @param productDto товар, который нужно создать
     * @return созданный товар
     */
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        return productMapper.toProductDto(productRepository.save(productMapper.toProduct(productDto)));
    }

    /**
     * Обновляет товар по его UUID.
     *
     * @param productDto dto товара, который нужно обновить
     * @param uuid UUID товара, который нужно обновить
     * @return обновленный товар
     * @throws ProductNotFoundException если товар с указанным идентификатором не найден
     */
    @Transactional
    public ProductDto updateProduct(ProductDto productDto, UUID uuid) {
        Product productFromDb = productRepository.findByIdLocked(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid));

        if(productDto.getName() != null && !productDto.getName().isBlank()){
            productFromDb.setName(productDto.getName());
        }
        if(productDto.getDescription() != null && !productDto.getDescription().isBlank()){
            productFromDb.setDescription(productDto.getDescription());
        }
        if(productDto.getCategory()!=null){
            productFromDb.setCategory(productDto.getCategory());
        }
        if(productDto.getPrice()!=null){
            productFromDb.setPrice(productDto.getPrice());
        }

        // Обновить поле lastQuantityChangeDate только если количество было изменено
        if (!productFromDb.getQuantity().equals(productDto.getQuantity())) {
            productFromDb.setLastQuantityChangeDate(LocalDateTime.now());
        }

        if(productDto.getQuantity()!=null){
            productFromDb.setQuantity(productDto.getQuantity());
        }

        return productMapper.toProductDto(productRepository.save(productFromDb));
    }

    /**
     * Удаляет товар по его UUID.
     *
     * @param uuid UUID товара, который нужно удалить
     * @throws ProductNotFoundException если товар с указанным идентификатором не найден
     */
    @Transactional
    public void deleteProduct(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(() -> new ProductNotFoundException(uuid));

        productRepository.delete(product);
    }
}
