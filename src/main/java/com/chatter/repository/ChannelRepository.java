package com.chatter.repository;

import java.util.HashMap;

import com.chatter.type.Channel;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelRepository extends HashMap<String, Channel> { }
