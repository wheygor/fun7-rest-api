package com.outfit7.fun7.admin.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.outfit7.fun7.admin.mapper.UserMapper;
import com.outfit7.fun7.admin.service.UserManagementService;
import com.outfit7.fun7.admin.user.model.User;
import com.outfit7.fun7.admin.user.model.UserDTO;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserManagementController.class)
class UserManagementControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserManagementService userManagementService;

  @MockBean
  private UserMapper userMapper;

  @Test
  public void getAllUsers_returnsAllUsers() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    String username = "user1";
    int playCount = 33;

    User user = User.create(userId, username, playCount);
    PageRequest pageable = PageRequest.of(0, 10);
    List<User> expectedResponse = List.of(user);
    Page<User> pageResponse = new PageImpl<>(expectedResponse, pageable, expectedResponse.size());

    doReturn(pageResponse).when(userManagementService).getAllUsers(pageable);

    doReturn(new UserDTO(userId, username, playCount)).when(userMapper).toDTO(user);

    // when
    mockMvc.perform(get("/admin/user-management/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content[0].id").value(userId.toString()))
        .andExpect(jsonPath("$.content[0].username").value(username))
        .andExpect(jsonPath("$.content[0].playCount").value(playCount));

    // then
    verify(userManagementService).getAllUsers(pageable);
    verify(userMapper).toDTO(user);
  }

  @Test
  public void getUserById_returnsUserWithGivenId() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    String username = "user2";
    int playCount = 2;

    User user = User.create(userId, username, playCount);

    doReturn(user).when(userManagementService).getUserById(userId);

    doReturn(new UserDTO(userId, username, playCount)).when(userMapper).toDTO(user);

    // when
    mockMvc.perform(get("/admin/user-management/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.playCount").value(playCount));

    // then
    verify(userManagementService).getUserById(userId);
    verify(userMapper).toDTO(user);
  }

  @Test
  public void deleteUserById_deletesUserWithGivenId() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    // when
    mockMvc.perform(delete("/admin/user-management/users/" + userId))
        .andExpect(status().isNoContent());

    // then
    verify(userManagementService).deleteUserById(userId);
  }

  @Test
  public void deleteUserById_returns404WhenHttpMethodDeleteWithNoId() throws Exception {
    // given
    // when
    mockMvc.perform(delete("/admin/user-management/users/"))
        .andExpect(status().isNotFound());

    // then
    verify(userManagementService, never()).deleteUserById(any());
  }

}