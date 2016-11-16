package com.boun.swe.semnet.sevices.db.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.repo.custom.FriendshipRepositoryCustom;

public interface FriendshipRepository extends MongoRepository<Friendship, String>, FriendshipRepositoryCustom {

    

}
