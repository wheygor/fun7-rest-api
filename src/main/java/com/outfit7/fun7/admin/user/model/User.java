package com.outfit7.fun7.admin.user.model;

import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
public class User {

  @Id
  private UUID id;

  private final String username;

  private int playCount = 0;

  private User(UUID id, String username, int playCount) {
    this.id = id;
    this.username = username;
    this.playCount = playCount;
  }

  public static User create(UUID id, String username, int playCount) {
    return new User(id, username, playCount);
  }
}
