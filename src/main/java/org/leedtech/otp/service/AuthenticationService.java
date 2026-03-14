package org.leedtech.otp.service;

import org.leedtech.otp.utils.helperclasses.HelperDomain.*;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
