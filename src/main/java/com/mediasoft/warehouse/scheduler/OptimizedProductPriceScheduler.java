package com.mediasoft.warehouse.scheduler;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Profile("!dev")
@ConditionalOnExpression("${app.scheduling.enabled} and ${app.scheduling.optimization}")
@RequiredArgsConstructor
@Slf4j
public class OptimizedProductPriceScheduler {

    private final DataSource dataSource;
    private final String FILENAME = "data.txt";

    @Value("#{new java.math.BigDecimal('${app.scheduling.percentage}')}")
    private BigDecimal percentage;

    @Transactional
    @MeasureExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    public void increasePrices() {
        log.info("Start scheduling (optimized)");

        try (Connection connection = dataSource.getConnection();
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            String query = "UPDATE Product SET price = price + (price * ? / 100) RETURNING *";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBigDecimal(1, percentage);
            ResultSet resultSet = statement.executeQuery();
            StringBuilder resultString = new StringBuilder();
            while (resultSet.next()) {
                resultString
                        .append(resultSet.getString("uuid")).append("\t")
                        .append(resultSet.getString("name")).append("\t")
                        .append(resultSet.getString("description")).append("\t")
                        .append(resultSet.getInt("category")).append("\t")
                        .append(resultSet.getBigDecimal("price")).append("\t")
                        .append(resultSet.getInt("quantity")).append("\t")
                        .append(resultSet.getDate("last_quantity_change_date")).append("\t")
                        .append(resultSet.getDate("created_date")).append("\t")
                        .append("\n");

                writer.append(resultString.toString());
                resultString.setLength(0);
            }


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        log.info("End scheduling (optimized)");
    }

}
