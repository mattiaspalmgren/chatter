package com.chatter.repository;

import java.util.List;
import java.util.Optional;

import com.chatter.model.Message;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface MessageRepository extends CrudRepository<Message, String> {
    Optional<Message> findById(String id);

    List<Message> findByFromIdAndToId(String fromId, String toId);

    List<Message> findByToId(String toId);

    List<Message> findAll();
}
