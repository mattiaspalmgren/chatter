package com.chatter.repository;

import java.util.HashMap;

import com.chatter.type.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends HashMap<String, User> { }
