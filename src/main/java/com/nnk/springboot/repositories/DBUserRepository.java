package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface DBUserRepository extends JpaRepository<DBUser, Integer>, JpaSpecificationExecutor<DBUser> {
    public DBUser findByUsername(String username);
}
