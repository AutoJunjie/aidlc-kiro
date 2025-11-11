package com.xholacracy.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA and Hibernate configuration
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.xholacracy.infrastructure.persistence")
@EnableTransactionManagement
public class JpaConfig {
    // Additional JPA configuration can be added here if needed
}
