package com.anitalk.app.user;

import com.anitalk.app.user.dto.AuthenticateUserRecord;
import com.anitalk.app.user.dto.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserRecord joinUser(AuthenticateUserRecord authenticateUserRecord) {
        UserEntity userEntity = authenticateUserRecord.toEntity(passwordEncoder);
        userEntity = repository.save(userEntity);
        return UserRecord.of(userEntity);
    }
}
