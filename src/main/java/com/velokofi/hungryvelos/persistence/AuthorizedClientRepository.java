package com.velokofi.hungryvelos.persistence;

import com.velokofi.hungryvelos.model.AuthorizedClient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorizedClientRepository extends MongoRepository<AuthorizedClient, String> {

}
