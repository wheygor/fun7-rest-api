package com.outfit7.fun7.admin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.outfit7.fun7.admin.exception.UserNotFoundException;
import com.outfit7.fun7.admin.repository.UserRepository;
import com.outfit7.fun7.admin.user.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserManagementService userManagementService;

  @Test
  public void getAllUsers_returnsAllUsers() {
    // given
    PageRequest pageable = PageRequest.of(0, 10);
    Page<User> expectedResponse = new PageImpl<>(List.of(User.create(UUID.randomUUID(), "test123", 3)));

    doReturn(expectedResponse).when(userRepository).findAll(pageable);

    // when
    Page<User> result = userManagementService.getAllUsers(pageable);

    // then
    assertFalse(result.isEmpty());
    assertEquals(1, result.getSize());
    assertEquals(expectedResponse, result);
  }

  @Test
  public void getUserById_returnsUserWithGivenUserId() {
    // given
    UUID userId = UUID.randomUUID();
    User expectedResponse = User.create(userId, "test", 0);

    doReturn(Optional.of(expectedResponse)).when(userRepository).findById(userId);

    // when
    User result = userManagementService.getUserById(userId);

    // then
    assertEquals(expectedResponse, result);
  }

  @Test
  public void getUserById_throwsAnErrorWhenUserWithUserIdDoesNotExist() {
    // given
    doReturn(Optional.empty()).when(userRepository).findById(any(UUID.class));

    // when
    // then
    assertThrows(UserNotFoundException.class, () -> userManagementService.getUserById(UUID.randomUUID()));
  }

  @Test
  public void deleteUserById_deletesUser() {
    // given
    UUID userId = UUID.randomUUID();

    // when
    userManagementService.deleteUserById(userId);

    // then
    verify(userRepository).deleteById(userId);
  }
}