package com.outfit7.fun7.admin.service;

import com.outfit7.fun7.admin.exception.UserNotFoundException;
import com.outfit7.fun7.admin.repository.UserRepository;
import com.outfit7.fun7.admin.user.model.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Provides user management functionality, such as getting all users, getting a user by ID, and deleting a user.
 */
@Service
@RequiredArgsConstructor
public class UserManagementService {

  private final UserRepository userRepository;

  /**
   * Retrieves a page of users based on the specified pagination information.
   *
   * @param pageable the pagination information
   * @return a {@link Page} object containing the requested users
   */
  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  /**
   * Retrieves the user with the specified ID.
   *
   * @param id the ID of the user to retrieve
   * @return the {@link User} object corresponding to the specified ID
   * @throws UserNotFoundException if a user with the specified ID does not exist
   */
  public User getUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> {
      throw new UserNotFoundException("User with ID '" + id + "' not found.");
    });
  }

  /**
   * Deletes the user with the specified ID. This action is idempotent.
   *
   * @param id the ID of the user to delete
   */
  public void deleteUserById(UUID id) {
    userRepository.deleteById(id);
  }
}
