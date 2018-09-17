package com.chatter.repository;

import java.util.List;

import com.chatter.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
}
