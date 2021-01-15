package com.velokofi.hungryvelos.persistence;

import com.velokofi.hungryvelos.model.OAuthorizedClient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthorizedClientRepository extends MongoRepository<OAuthorizedClient, String> {

}
