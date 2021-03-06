package com.fils.backend.controllers;

import com.fils.backend.domain.User;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("admin/all")
    public ResponseEntity getAll(@RequestHeader("Authorization") String auth) {
        String jwtToken = auth.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        Optional<User> userByUsername = userService.getUserByUsername(username);
        if(userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ADMIN account is required");
        }
    }


    @GetMapping("admin/id")
    public ResponseEntity getById(@RequestParam String id) {
        try {
            long userId = Long.parseLong(id);
            Optional<User> userById = userService.getUserById(userId);
            if (userById.isPresent()) {
                return ResponseEntity.ok(userById.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found in database");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR : INVALID REQUEST");
        }
    }

    @GetMapping("admin/email")
    public ResponseEntity getByEmail(@RequestParam String email) {
        try {
            Optional<User> userByEmail = userService.getUserByEmail(email);
            if (userByEmail.isPresent()) {
                return ResponseEntity.ok(userByEmail.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found in database");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR : INVALID REQUEST");
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity addUser(@RequestBody User user) {
        user.setId(null);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/admin/edit")
    public ResponseEntity editUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok("DELETED: " + user);
    }
}
