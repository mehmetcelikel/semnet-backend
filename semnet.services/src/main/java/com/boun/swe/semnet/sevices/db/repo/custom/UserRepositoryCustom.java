package com.boun.swe.semnet.sevices.db.repo.custom;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.User;

public interface UserRepositoryCustom {

    User findById(Long id);

    User findByUsernameAndPassword(String username, String password);
    
    User findByUsername(String username);
    
    User findByOneTimeToken(String oneTimeToken);
    
    List<User> searchUser(String queryString);

}
