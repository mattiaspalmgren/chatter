package com.chatter.repository;

import java.util.List;

import com.chatter.model.Channel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface ChannelRepository extends CrudRepository<Channel, String> {
    List<Channel> findAll();
}
