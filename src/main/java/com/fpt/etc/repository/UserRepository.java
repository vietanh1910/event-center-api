package com.fpt.etc.repository;

import com.fpt.etc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<User> findByIdAndDeletedFalse(Long id);

}
