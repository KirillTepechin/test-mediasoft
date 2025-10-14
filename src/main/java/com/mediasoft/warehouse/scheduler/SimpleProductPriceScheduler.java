package com.mediasoft.warehouse.scheduler;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Profile("!local")
@ConditionalOnProperty("app.scheduling.enabled")
@ConditionalOnMissingBean(OptimizedProductPriceScheduler.class)
@RequiredArgsConstructor
@Slf4j
public class SimpleProductPriceScheduler {

    private final ProductRepository productRepository;
    @Value("#{new java.math.BigDecimal('${app.scheduling.percentage}')}")
    private BigDecimal percentage;

    @Transactional
    @MeasureExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    public void increasePrices() {
        log.info("Start scheduling (simple)");

        List<Product> products = productRepository.findAll();
        products.forEach(product ->
                product.setPrice(product.getPrice()
                        .add(product.getPrice()
                        .multiply(percentage)
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
        );
        productRepository.saveAll(products);

        log.info("End scheduling (simple)");
    }
}
