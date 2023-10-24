package com.majesteye.dghelper.repository;


import com.majesteye.dghelper.models.enums.ERole;
import com.majesteye.dghelper.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Marwen Sbihi
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(ERole name);
  boolean existsByName(ERole name);
}
