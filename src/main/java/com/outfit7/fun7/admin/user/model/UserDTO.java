package com.outfit7.fun7.admin.user.model;

import java.util.UUID;

public record UserDTO(UUID id, String username, int playCount) {

}
