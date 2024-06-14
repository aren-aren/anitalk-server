package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.domain.user.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username + "으로 가입된 계정이 없습니다"));
        AuthenticateUserRecord loginUserRecord = AuthenticateUserRecord.of(entity);

        return new LoginUser(loginUserRecord);
    }
}
