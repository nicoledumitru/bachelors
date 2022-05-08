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
//
//    @Autowired
//    UserProfileRepository userProfileRepository;

    @GetMapping("admin/all")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAll(@RequestHeader("Authorization") String auth) {
        String jwtToken = auth.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        Optional<User> userByUsername = userService.getUserByUsername(username);
        if(userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN")){
//                userByUsername.get().setPassword(null);
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

    @DeleteMapping("admin/deletebyid")
    public ResponseEntity deleteUserById(@RequestParam Long id) {
        Optional<User> tempUser = userService.getUserById(id);
        if (tempUser.isPresent()) {
            userService.deleteUser(tempUser.get());
            return ResponseEntity.ok("DELETED: " + tempUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User with id: " + id + " was NOT FOUND !");
        }
    }

    @DeleteMapping("admin/deletebyusername")
    public ResponseEntity deleteUser(@RequestParam String username) {
        Optional<User> tempUser = userService.getUserByUsername(username);
        if (tempUser.isPresent()) {
            userService.deleteUser(tempUser.get());
            return ResponseEntity.ok("DELETED: " + tempUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User with email: " + username + " was NOT FOUND !");
        }
    }

//    @GetMapping("/current")
//    public ResponseEntity getCurrentUser(@RequestHeader("Authorization") String auth) {
//        try {
//            String jwtToken = auth.substring(7);
//            String username = jwtUtil.extractUsername(jwtToken);
//            Optional<User> userByEmail = userService.getUserByUsername(username);
//            if(userByEmail.isPresent()){
//                userByEmail.get().setPassword(null);
//                return ResponseEntity.ok(userByEmail);
//            } else{
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Invalid USER from JWT");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Couldn't parse current user jwt to get user");
//        }
//    }

}
