package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.domain.user.dto.JwtToken;
import com.anitalk.app.domain.user.dto.LoginUser;
import com.anitalk.app.domain.user.dto.UserRecord;
import com.anitalk.app.exception.UserTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username + "으로 가입된 계정이 없습니다"));
        AuthenticateUserRecord loginUserRecord = AuthenticateUserRecord.of(entity);

        return new LoginUser(loginUserRecord);
    }

    public JwtToken setRefreshToken(Long id, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(id).orElse(new RefreshTokenEntity(id));
        refreshTokenEntity.setRefreshToken(refreshToken);

        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        return new JwtToken(null, refreshTokenEntity.getRefreshToken());
    }

    public UserRecord validateRefreshToken(Long userId, String refreshToken) throws Exception {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(userId).orElse(null);

        if(refreshTokenEntity != null && refreshTokenEntity.getRefreshToken().equals(refreshToken)){
            return UserRecord.of(userRepository.findById(refreshTokenEntity.getId()).orElseThrow());
        }

        throw new UserTokenException("refreshToken_invalidate");
    }

    public void logoutUser(Long id) {
        RefreshTokenEntity refreshEntity = refreshTokenRepository.findById(id).orElseThrow();
        refreshTokenRepository.delete(refreshEntity);
    }
}
