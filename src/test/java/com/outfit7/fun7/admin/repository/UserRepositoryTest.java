package com.outfit7.fun7.admin.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.outfit7.fun7.admin.user.model.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest
class UserRepositoryTest {

  private static final String MONGODB_DOCKER_IMAGE = "mongo:5.0.15";

  @Container
  static MongoDBContainer container = new MongoDBContainer(MONGODB_DOCKER_IMAGE);

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
  }

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void clearDatabase() {
    userRepository.deleteAll();
  }

  @Test
  void save_savesNewUser() {
    // given
    UUID userId = UUID.randomUUID();
    String username = "woosh";
    int playCount = 5;
    this.userRepository.save(User.create(userId, username, playCount));

    List<User> userList = userRepository.findAll();

    assertEquals(1, userList.size());

    User user = userList.get(0);
    assertEquals(userId, user.getId());
    assertEquals(playCount, user.getPlayCount());
    assertEquals(playCount, user.getPlayCount());
  }


  @Test
  void findById_returnsUserWithGivenId() {
    // given
    UUID userId = UUID.randomUUID();
    String username = "woosh";
    int playCount = 5;

    this.userRepository.save(User.create(userId, username, playCount));

    // when
    User user = userRepository.findById(userId).get();

    // then
    assertNotNull(user);
    assertEquals(userId, user.getId());
    assertEquals(playCount, user.getPlayCount());
    assertEquals(playCount, user.getPlayCount());
  }

  @Test
  void deleteById_deletesUserWithGivenId() {
    // given
    UUID userId = UUID.randomUUID();
    String username = "woosh";
    int playCount = 5;

    this.userRepository.save(User.create(userId, username, playCount));

    List<User> userList = userRepository.findAll();

    assertEquals(1, userList.size());

    // when
    userRepository.deleteById(userId);

    userList = userRepository.findAll();

    // then
    assertTrue(userList.isEmpty());
  }

}