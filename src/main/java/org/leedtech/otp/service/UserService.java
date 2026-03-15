package org.leedtech.otp.service;

import org.leedtech.otp.domain.User;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
public interface UserService {

    ResponseMessage<User> changeUserRole(String email, String role);

    User getUserProfile(UUID userId);

    List<User> getAllUsers();

    ResponseMessage<User> enableUser(UUID id);

}
