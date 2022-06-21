package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;

import com.ead.authuser.specfications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static com.ead.authuser.dtos.UserView.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId", direction = ASC) Pageable pageable){
        Page<UserModel> userModelPage = userService.findAll(pageable, spec);
        return status(OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<Object> getUser(@PathVariable(value = "userId")UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        return status(OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public  ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        userService.deleteUser( userModelOptional.get());
        return status(OK).body("User deleted sucessfully");
    }

    @PutMapping("/{userId}")
    public  ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody
                                              @Validated(UserPut.class)
                                              @JsonView(UserPut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        var userModel = userModelOptional.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        return status(OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public  ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                  @RequestBody
                                                  @Validated(PasswordPut.class)
                                                  @JsonView(PasswordPut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");
        else if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword()))
            return status(CONFLICT).body("Error: Mismatched old password!");


        var userModel = userModelOptional.get();
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        return status(OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public  ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                               @RequestBody
                                               @Validated(ImagePut.class)
                                               @JsonView(ImagePut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        var userModel = userModelOptional.get();
        userModel.setImageUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        return status(OK).body(userModel);
    }
}
