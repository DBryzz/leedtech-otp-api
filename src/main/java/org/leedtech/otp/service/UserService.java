package org.leedtech.otp.service;

import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface UserService {

    ResponseMessage<User> changeUserRole(String email, String role);

    User getUserProfile(UUID userId);

    List<User> getAllUsers();

    ResponseMessage<User> enableUser(UUID id);

}
