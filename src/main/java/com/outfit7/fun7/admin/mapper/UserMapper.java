package com.outfit7.fun7.admin.mapper;

import com.outfit7.fun7.admin.user.model.User;
import com.outfit7.fun7.admin.user.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserDTO toDTO(User source);
}
