package com.kindle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // to make @Transactional annotations work
@Profile("kindleStoreWithTransactionalAnnotation")
public class TransactionalAppConfig {
}
