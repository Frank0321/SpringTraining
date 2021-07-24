package tw.com.softleader.SpringTraining.Config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {"tw.com.softleader.SpringTraining"})
public class PrimaryDataSourceConfig {


    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.configuration")
    public HikariDataSource primaryDataSource(DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }



    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder primaryEntityManagerFactoryBuilder, @Qualifier("primaryDataSource") DataSource primaryDataSource) {

        Map<String, String> primaryJpaProperties = new HashMap<>();
        primaryJpaProperties.put("hibernate.hbm2ddl.auto", "create");
        primaryJpaProperties.put("hibernate.show_sql", "true");
        primaryJpaProperties.put("hibernate.format_sql", "true");

        return primaryEntityManagerFactoryBuilder
                .dataSource(primaryDataSource)
                .packages("tw.com.softleader.SpringTraining.Entity")  //指到 ENtity 上一層
                .persistenceUnit("primaryDataSource")
                .properties(primaryJpaProperties)
                .build();
    }


    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {

        return new JpaTransactionManager(primaryEntityManagerFactory);
    }


}

