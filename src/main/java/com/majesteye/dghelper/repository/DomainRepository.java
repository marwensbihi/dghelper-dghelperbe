package com.majesteye.dghelper.repository;

import com.majesteye.dghelper.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Marwen Sbihi
 */
public interface DomainRepository extends JpaRepository<Domain, String> {
}
