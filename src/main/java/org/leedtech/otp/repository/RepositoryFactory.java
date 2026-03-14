package org.leedtech.otp.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RepositoryFactory {

    private static RepositoryFactory instance;
}