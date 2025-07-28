package org.example.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("org.example")
@PropertySource("classpath:application.properties")
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;


    @Bean
    public DataSource dataSource() {
        log.info("Creating datasource");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("db.url"));
        config.setUsername(env.getProperty("db.username"));
        config.setPassword(env.getProperty("db.password"));
        config.setDriverClassName(env.getProperty("db.driver"));
        config.setMaximumPoolSize(Integer.parseInt(env.getProperty("db.pool.size")));
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }


    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        log.info("Creating sessionFactory");
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.example.model");

        Properties hibernateProps = new Properties();
        hibernateProps.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProps.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        hibernateProps.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        hibernateProps.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        sessionFactory.setHibernateProperties(hibernateProps);

        try {
            sessionFactory.afterPropertiesSet();
            return sessionFactory.getObject();
        } catch (Exception e) {
            throw new BeanInitializationException("Failed to initialize Hibernate SessionFactory", e);
        }
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        log.info("Creating flyway");
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(env.getProperty("flyway.locations"))
                .baselineOnMigrate(Boolean.parseBoolean(env.getProperty("flyway.baselineOnMigrate")))
                .load();
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        log.info("Creating transactionManager");
        return new HibernateTransactionManager(sessionFactory);
    }

}
