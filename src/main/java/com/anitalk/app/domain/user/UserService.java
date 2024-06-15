package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.domain.user.dto.UserRecord;
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

    public UserRecord changeNickname(Long id, UserRecord userRecord) {
        UserEntity userEntity = repository.findById(id).orElseThrow();
        userEntity.setNickname(userRecord.nickname());
        userEntity = repository.save(userEntity);
        return UserRecord.of(userEntity);
    }

    public UserRecord changePassword(Long id, AuthenticateUserRecord userRecord) {
        UserEntity userEntity = repository.findById(id).orElseThrow();
        userEntity.setPassword(passwordEncoder.encode(userRecord.password()));
        userEntity = repository.save(userEntity);

        return UserRecord.of(userEntity);
    }
}
