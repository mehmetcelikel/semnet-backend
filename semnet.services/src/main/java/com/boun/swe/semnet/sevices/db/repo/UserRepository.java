package com.boun.swe.semnet.sevices.db.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.db.repo.custom.UserRepositoryCustom;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

}
