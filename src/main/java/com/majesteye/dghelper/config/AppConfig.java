package com.majesteye.dghelper.config;

import com.majesteye.dghelper.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Majd Selmi
 */
@Configuration
public class AppConfig {
    @Bean
    public Utils utils() {
        return new Utils();
    }
}
