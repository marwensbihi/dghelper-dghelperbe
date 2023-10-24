package com.majesteye.dghelper.repository;

import com.majesteye.dghelper.models.Information;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Marwen Sbihi
 */
public interface InformationRepository extends JpaRepository<Information, Long> {
}
