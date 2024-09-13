package com.example.repository;

import com.example.model.dao.RateLimit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<RateLimit, String> {
}
