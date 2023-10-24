package com.majesteye.dghelper;

import com.majesteye.dghelper.models.enums.ERole;
import com.majesteye.dghelper.models.Role;
import com.majesteye.dghelper.repository.RoleRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Marwen Sbihi
 */
@SpringBootApplication
public class DGHelperApplication {

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(DGHelperApplication.class, args);
    }


    // --------------------- INITIALIZE THE APP ----------------------
    @Bean
    InitializingBean init() {
        return () -> {
            // --------------------- ROLES ----------------------
            if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }
            if (!roleRepository.existsByName(ERole.ROLE_USER)) {
                roleRepository.save(new Role(ERole.ROLE_USER));
            }
        };
    }
}
