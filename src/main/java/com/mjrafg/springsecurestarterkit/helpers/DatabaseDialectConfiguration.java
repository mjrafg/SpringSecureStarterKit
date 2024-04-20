package com.mjrafg.springsecurestarterkit.helpers;

import com.mjrafg.springsecurestarterkit.utils.FilterSpecificationUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Configuration
public class DatabaseDialectConfiguration {

    private final DataSource dataSource;

    public DatabaseDialectConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void detectDatabaseDialect() {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();

            switch (databaseProductName) {
                case "PostgreSQL":
                    FilterSpecificationUtils.setDatabaseDialectHelper(new PostgreSQLDialectHelper());
                    break;
                case "MySQL":
                    FilterSpecificationUtils.setDatabaseDialectHelper(new MySQLDialectHelper());
                    break;
                case "MariaDB":
                    FilterSpecificationUtils.setDatabaseDialectHelper(new MariaDBDialectHelper());
                    break;
                default:
                    FilterSpecificationUtils.setDatabaseDialectHelper(new GenericSQLDialectHelper());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

