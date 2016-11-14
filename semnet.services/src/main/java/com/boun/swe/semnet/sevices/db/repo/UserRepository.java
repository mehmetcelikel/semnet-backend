package com.boun.swe.semnet.sevices.db.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String userName);

    User findByUsernameAndPassword(String username, String password);
    
    User findByOneTimeToken(String oneTimeToken);
    
    User findById(String id);
    
    List<User> searchUser(String queryString);
}
