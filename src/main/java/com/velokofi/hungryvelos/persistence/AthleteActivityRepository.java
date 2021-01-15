package com.velokofi.hungryvelos.persistence;

import com.velokofi.hungryvelos.model.AthleteActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AthleteActivityRepository extends MongoRepository<AthleteActivity, String> {

}
