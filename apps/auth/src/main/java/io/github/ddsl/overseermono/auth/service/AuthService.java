package io.github.ddsl.overseermono.auth.service;

import io.github.ddsl.overseermono.auth.dto.CreateUserDto;
import io.github.ddsl.overseermono.auth.dto.LoginUserDto;
import io.github.ddsl.overseermono.auth.dto.mapper.UserMapper;
import io.github.ddsl.overseermono.commonlib.entities.User;
import io.github.ddsl.overseermono.commonlib.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepo;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Optional<User> createUser(CreateUserDto userDto){
        if (userRepo.existsByLogin(userDto.login())) {
            throw new IllegalArgumentException("User with login '" + userDto.login() + "' already exists");
        }
        if (userRepo.existsByEmail(userDto.email())) {
            throw new IllegalArgumentException("User with email '" + userDto.email() + "' already exists");
        }
        var user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        userRepo.save(user);
        return Optional.of(user);
    }

    public Authentication Authenticate(LoginUserDto loginUserDto) {
        var authToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.login(),loginUserDto.password());
        var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    // not in use but as option could be used in jwtCookieFilter to create authentication object
    public UsernamePasswordAuthenticationToken generateAuthenticationByUsername(String username) {
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        return authentication;
    }
}
