package com.payMyBuddy.app.util;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.model.User;

public interface IUserService {
    User registerNewUserAccount(UserDto userDto);
}
