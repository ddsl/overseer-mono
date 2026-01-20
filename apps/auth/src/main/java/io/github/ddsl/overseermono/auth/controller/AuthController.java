package io.github.ddsl.overseermono.auth.controller;

import io.github.ddsl.overseermono.auth.dto.CreateUserDto;
import io.github.ddsl.overseermono.auth.dto.LoginUserDto;
import io.github.ddsl.overseermono.auth.service.AuthService;
import io.github.ddsl.overseermono.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController()
@RequestMapping("auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> signup(@RequestBody CreateUserDto newUserDto) {
        try {
            var user = authService.createUser(newUserDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(e.getMessage());
        }
        return ResponseEntity.ok("User created!");
    }

    @PostMapping("signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> signin(@RequestBody LoginUserDto loginUserDto) {
        try {
            var authentication = authService.Authenticate(loginUserDto);
            var refreshCookie = jwtService.generateRefreshCookie(authentication);
            var accessCookie = jwtService.generateAccessCookie(authentication);

            var headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
