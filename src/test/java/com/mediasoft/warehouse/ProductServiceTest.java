package com.mediasoft.warehouse;

import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.mapper.ProductMapper;
import com.mediasoft.warehouse.model.enums.Category;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unit тесты для слоя бизнес-логики {@link ProductService}.
 */
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    /**
     * Список товаров, используемых в тестах.
     */
    private final List<Product> productList = new ArrayList<>();

    /**
     * Устанавливает тестовые данные.
     */
    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setUuid(UUID.randomUUID());
        product.setName("Товар 1");
        product.setDescription("Описание товара 1");
        product.setCategory(Category.ELECTRONICS);
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(BigDecimal.valueOf(10));

        productList.add(productRepository.save(product));

        product = new Product();
        product.setUuid(UUID.randomUUID());
        product.setName("Товар 2");
        product.setDescription("Описание товара 2");
        product.setCategory(Category.FOOD);
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(BigDecimal.valueOf(5));

        productList.add(productRepository.save(product));

    }

    /**
     * Сбрасывает тестовые данные.
     */
    @AfterEach
    void setDown(){
        productRepository.deleteAll();
    }

    /**
     * Тест для метода {@link ProductService#getAllProducts(Pageable)}.
     */
    @Test
    void getAllProductTest() {
        PageRequest pageRequest = PageRequest.of(0,10);
        var productDtoListFromDb = productService.getAllProducts(pageRequest);
        var productDtoList = productList.stream().map(productMapper::toProductDto).toList();
        Assertions.assertIterableEquals(productDtoList, productDtoListFromDb);
    }

    /**
     * Тест для метода {@link ProductService#getProductById(UUID)}.
     */
    @Test
    void getProductByIdTest() {
        var product = productMapper.toProductDto(productList.get(0));
        Assertions.assertEquals(product, productService.getProductById(product.getUuid()));
    }

    /**
     * Тест для метода {@link ProductService#getProductById(UUID)}, который должен выбросить {@link ProductNotFoundException}.
     */
    @Test
    void throwProductNotFoundExceptionTest() {
        Assertions.assertThrows(ProductNotFoundException.class,
                ()->productService.getProductById(UUID.randomUUID()));
    }

    /**
     * Тест для метода {@link ProductService#createProduct(ProductDto)}.
     */
    @Test
    void createProductTest() {
        ProductDto productDto = new ProductDto();
        productDto.setUuid(UUID.randomUUID());
        productDto.setName("Товар");
        productDto.setDescription("Описание товара");
        productDto.setCategory(Category.ELECTRONICS);
        productDto.setPrice(BigDecimal.valueOf(100.00));
        productDto.setQuantity(BigDecimal.valueOf(10));

        var uuid = productService.createProduct(productDto);

        Assertions.assertEquals(productDto.getUuid(), productService.getProductById(uuid).getUuid());
    }

    /**
     * Тест для метода {@link ProductService#updateProduct(ProductDto, UUID)}.
     */
    @Test
    void updateProductTest() {
        ProductDto productDto = new ProductDto();
        String newName = "Новое название товара";
        productDto.setName(newName);

        UUID uuid = productList.get(0).getUuid();

        productService.updateProduct(productDto, uuid);

        Assertions.assertEquals(newName, productService.getProductById(uuid).getName());
    }


    /**
     * Tест для метода {@link ProductService#deleteProduct(UUID)}.
     */
    @Test
    void deleteProductTest() {
        PageRequest pageRequest = PageRequest.of(0,10);

        productService.deleteProduct(productList.get(0).getUuid());
        //Проверяем что что-то удалилось
        Assertions.assertEquals(1, productService.getAllProducts(pageRequest).size());
        //Проверяем что удалился правильный обьект
        Assertions.assertNotEquals(productList.get(0), productService.getAllProducts(pageRequest).get(0));
    }


    /**
     * Тест для метода {@link ProductService#updateProduct(ProductDto, UUID)}, который должен поменять {@link Product#lastQuantityChangeDate}.
     */
    @Test
    void updateLastQuantityChangeDateWhenQuantityChangedTest() throws InterruptedException {
        Thread.sleep(2000);

        var oldProduct = productList.get(0);
        LocalDateTime oldLocalDateTime = oldProduct.getLastQuantityChangeDate();
        UUID uuid = oldProduct.getUuid();

        ProductDto productDto = new ProductDto();
        productDto.setQuantity(BigDecimal.valueOf(999));

        productService.updateProduct(productDto, uuid);
        var newLocalDateTime = productService.getProductById(uuid).getLastQuantityChangeDate();

        LocalTime oldLocalTime = LocalTime.of(oldLocalDateTime.getHour(), oldLocalDateTime.getMinute(), oldLocalDateTime.getSecond());
        LocalTime newLocalTime = LocalTime.of(newLocalDateTime.getHour(), newLocalDateTime.getMinute(), newLocalDateTime.getSecond());

        Assertions.assertNotEquals(oldLocalTime, newLocalTime);
    }

    /**
     * Тест для метода {@link ProductService#updateProduct(ProductDto, UUID)}, который не должен поменять {@link Product#lastQuantityChangeDate}.
     */
    @Test
    void updateLastQuantityChangeDateWhenQuantityNotChangedTest() throws InterruptedException {
        Thread.sleep(2000);

        var oldProduct = productList.get(0);
        LocalDateTime oldLocalDateTime = oldProduct.getLastQuantityChangeDate();
        UUID uuid = oldProduct.getUuid();

        ProductDto productDto = new ProductDto();
        productDto.setQuantity(oldProduct.getQuantity());

        productService.updateProduct(productDto, uuid);
        var newLocalDateTime = productService.getProductById(uuid).getLastQuantityChangeDate();

        LocalTime oldLocalTime = LocalTime.of(oldLocalDateTime.getHour(), oldLocalDateTime.getMinute(), oldLocalDateTime.getSecond());
        LocalTime newLocalTime = LocalTime.of(newLocalDateTime.getHour(), newLocalDateTime.getMinute(), newLocalDateTime.getSecond());

        Assertions.assertEquals(oldLocalTime, newLocalTime);
    }
}
