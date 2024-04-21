package com.mediasoft.warehouse.scheduler;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import com.mediasoft.warehouse.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Profile("!dev")
@ConditionalOnExpression("${app.scheduling.enabled} and ${app.scheduling.optimization}")
@RequiredArgsConstructor
public class OptimizedProductPriceScheduler {

    private final DataSource dataSource;

    @Value("#{new java.math.BigDecimal('${app.scheduling.percentage}')}")
    private BigDecimal percentage;

    @Transactional
    @MeasureExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    public void increasePrices() {
        System.out.println("Start scheduling (optimized)");

        PreparedStatementCreator preparedStatementCreator = con -> {
            String query = "WITH updated AS (UPDATE Product SET price = price + (price * ? / 100) RETURNING *)\n" +
                    "SELECT * FROM Product for update";

            PreparedStatement statement = con.prepareStatement(query);
            statement.setBigDecimal(1, percentage);
            ResultSet resultSet = statement.executeQuery();
            StringBuilder resultString = new StringBuilder();
            FileUtil fileUtil = new FileUtil();
            while (resultSet.next()){
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

                fileUtil.writeDataToFile(resultString.toString());
                resultString.setLength(0);
            }

            con.close();
            statement.close();
            resultSet.close();
            fileUtil.close();

            return statement;
        };
        try {
            preparedStatementCreator.createPreparedStatement(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End scheduling (optimized)");
    }

}
