package org.gg.repository;

import org.gg.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> { }
//Spring boot automatically scans classes which are extended as above and they inherited all functionalities of MongoRepository.
//User Repository is now registered as a bean and autowired to the userServiceimpl class to perform various CRUD functionalities
