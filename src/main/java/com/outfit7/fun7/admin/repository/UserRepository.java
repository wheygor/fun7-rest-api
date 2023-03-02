package com.outfit7.fun7.admin.repository;

import com.outfit7.fun7.admin.user.model.User;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, UUID> { }
