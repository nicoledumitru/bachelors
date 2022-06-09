package com.fils.backend.controllers;

import com.fils.backend.domain.RegisterForm;
import com.fils.backend.domain.User;
import com.fils.backend.security.AuthRequest;
import com.fils.backend.security.JwtResponse;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.security.MyUserDetailsService;
import com.fils.backend.services.EmailTokenService;
import com.fils.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class LoginRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private EmailTokenService emailVerificationTokenService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
        System.out.println("authrequestbody este : " + authenticationRequest.toString());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INCORRECT CREDENTIALS AUTHENTICATE ENDPOINT");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println("userdetails trimise din login endpoint: " + userDetails);
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        System.out.println("lista de authorities userdetails:" + userDetails.getAuthorities());
        System.out.println(roles);

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity registerEndpoint(@RequestBody RegisterForm registerForm) {
        Optional<User> userByRepo = userService.getUserByUsername(registerForm.getUsername());
        if (userByRepo.isEmpty()) {
            User user = new User(
                    registerForm.getUsername(),
                    registerForm.getEmail(),
                    passwordEncoder.encode(registerForm.getPassword())
                    );
            user.setRoles("ROLE_GUEST");
            user.setActive(false);
            user.setSubsToNews(false);
            userService.saveUser(user);

            try {
                String emailTokenString = emailVerificationTokenService.createEmailTokenForUserInDB(user);
                emailVerificationTokenService.sendVerificationEmail(user, emailTokenString);
                return ResponseEntity.ok("");
            } catch (Exception e) {
                e.printStackTrace();
                userService.deleteUser(user);
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERROR: EmailService failed. User couldn't be created !!");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERROR : USERNAME ALREADY EXISTS !!");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity verifyUser(@RequestParam String code) {
        if (emailVerificationTokenService.verify(code)) {
            return ResponseEntity.ok("Email Verification Successful");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR : Email Verification failed ");
        }
    }

}
