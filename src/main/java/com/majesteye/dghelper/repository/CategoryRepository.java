package com.majesteye.dghelper.repository;

import com.majesteye.dghelper.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Marwen Sbihi
 */

public interface CategoryRepository extends JpaRepository<Category, String> {
}
