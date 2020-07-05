package ru.nshipyakov.ig.problem.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CamundaConfiguration {

    @Configuration
    @ConfigurationProperties(prefix = "spring.datasource.url")
    class JdbcCamundaProperties extends HikariConfig {}

    @Bean
    @Qualifier("camundaDataSource")
    public DataSource camundaDataSource(JdbcCamundaProperties camundaDBProperties) {
        HikariConfig conf = new HikariConfig();
        conf.setJdbcUrl("jdbc:h2:file:./camunda-h2-database");
        conf.setUsername("sa");
        conf.setPassword("");
        return new HikariDataSource(conf);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("camundaDataSource")DataSource camundaDataSource){
        return new DataSourceTransactionManager(camundaDataSource);
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(
        @Qualifier("camundaDataSource")DataSource camundaDataSource,
        PlatformTransactionManager transactionManager,
        JobExecutor jobExecutor
    ){
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(camundaDataSource);
        config.setTransactionManager(transactionManager);
        config.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setJobExecutorActivate(true);
        config.setJobExecutor(jobExecutor);
        config.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
        config.setMetricsEnabled(false);
        return config;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration processEngineConfiguration){
        ProcessEngineFactoryBean factory = new ProcessEngineFactoryBean();
        factory.setProcessEngineConfiguration(processEngineConfiguration);
        return factory;
    }
}
