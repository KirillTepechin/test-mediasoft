package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.configuration.S3ConfigurationProperties;
import com.mediasoft.warehouse.dto.GetProductDto;
import com.mediasoft.warehouse.dto.ProductDto;
import com.mediasoft.warehouse.exception.ArticleAlreadyExistsException;
import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.mapper.ProductMapper;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.ProductImage;
import com.mediasoft.warehouse.repository.ProductImageRepository;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.search.SpecificationBuilder;
import com.mediasoft.warehouse.service.search.criteria.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
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
    private final SpecificationBuilder specificationBuilder;
    private final S3ConfigurationProperties s3ConfigurationProperties;
    private final ProductImageRepository productImageRepository;
    private final S3StorageService s3StorageService;

    /**
     * Получить все товары.
     * @return список всех товаров
     */
    @Transactional(readOnly = true)
    public List<GetProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).getContent().stream()
                .map(productMapper::toGetProductDto).collect(Collectors.toList());
    }

    /**
     * Получить товар по его UUID.
     *
     * @param uuid UUID товар, который нужно получить
     * @return товар с заданным UUID
     */
    @Transactional(readOnly = true)
    public GetProductDto getProductById(UUID uuid) {
        return productMapper.toGetProductDto(productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid)));
    }

    /**
     * Создает новый товар.
     *
     * @param productDto товар, который нужно создать
     * @return созданный товар
     */
    @Transactional
    public UUID createProduct(ProductDto productDto) {
        checkArticle(productDto.getArticle());
        return productRepository.save(productMapper.toProduct(productDto)).getUuid();
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
    public UUID updateProduct(ProductDto productDto, UUID uuid) {
        Product productFromDb = productRepository.findByIdLocked(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid));
        checkArticle(productDto.getArticle());
        if(productDto.getName() != null && !productDto.getName().isBlank()){
            productFromDb.setName(productDto.getName());
        }
        if(productDto.getArticle() != null && !productDto.getArticle().isBlank()){
            productFromDb.setArticle(productDto.getArticle());
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

        return productRepository.save(productFromDb).getUuid();
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

    @Transactional
    public List<GetProductDto> searchProducts(List<SearchCriteria<?>> searchCriteriaList, Pageable pageable) {
        var specification = specificationBuilder.getSpecification(searchCriteriaList);

        List<Product> result = productRepository.findAll(specification, pageable).getContent();
        return result.stream().map(productMapper::toGetProductDto).toList();
    }

    @Transactional
    public UUID addImageToProduct(UUID productUuid, MultipartFile file) {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(productRepository.findById(productUuid)
                .orElseThrow(()-> new ProductNotFoundException(productUuid)));
        UUID key = productImageRepository.save(productImage).getUuid();

        String bucketName = s3ConfigurationProperties.getBucketName();

        s3StorageService.uploadFileToBucket(bucketName, key.toString(), file);

        return key;
    }

    @Transactional
    public void downloadProductImages(UUID productUuid, OutputStream outputStream) {
        String bucketName = s3ConfigurationProperties.getBucketName();

        List<ProductImage> productImages = productImageRepository.findAllByProductUuid(productUuid);
        List<String> keys = productImages.stream()
                .map(productImage -> productImage.getUuid().toString()).toList();

        s3StorageService.downloadFilesFromBucket(bucketName, keys, outputStream);
    }

    private void checkArticle(String article){
        var productFromDb = productRepository.findByArticle(article);
        if(productFromDb!=null){
            throw new ArticleAlreadyExistsException(article);
        }
    }
}
