package com.outfit7.fun7.admin.controller;

import com.outfit7.fun7.admin.mapper.UserMapper;
import com.outfit7.fun7.admin.service.UserManagementService;
import com.outfit7.fun7.admin.user.model.User;
import com.outfit7.fun7.admin.user.model.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user-management")
public class UserManagementController {

  private final UserManagementService service;
  private final UserMapper mapper;

  @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all users", description = "Returns a pageable list of all users",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))

      })
  public ResponseEntity<PageImpl<UserDTO>> getAllUsers(@ParameterObject @PageableDefault Pageable pageable) {
    Page<User> users = service.getAllUsers(pageable);
    List<UserDTO> userDTOS = users.stream()
        .map(mapper::toDTO)
        .toList();

    PageImpl<UserDTO> page = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get user by ID", description = "Returns the user with the specified ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = UserDTO.class))),
          @ApiResponse(responseCode = "404", description = "User not found",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))}
  )
  public ResponseEntity<UserDTO> getUserById(
      @Parameter(description = "User ID in UUID format", example = "5e6d919a-9de9-42d3-a167-7dd0ca9b2805")
      @PathVariable("id") UUID id) {
    User user = service.getUserById(id);
    return new ResponseEntity<>(mapper.toDTO(user), HttpStatus.OK);
  }

  @DeleteMapping(value = "/users/{id}")
  @Operation(summary = "Delete user by ID", description = "Deletes the user with the specified ID",
      responses = {
          @ApiResponse(responseCode = "204", description = "User deleted"),
          @ApiResponse(responseCode = "404", description = "User not found")}
  )
  public ResponseEntity<Void> deleteUserById(
      @Parameter(description = "User ID in UUID format", example = "5e6d919a-9de9-42d3-a167-7dd0ca9b2805")
      @PathVariable("id") UUID id) {
    service.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
