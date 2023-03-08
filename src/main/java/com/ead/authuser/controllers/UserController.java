package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;

import static com.ead.authuser.specfications.SpecificationTemplate.UserSpec;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(UserSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId", direction = ASC) Pageable pageable){
        Page<UserModel> userModelPage = userService.findAll(pageable, spec);

        if(!userModelPage.isEmpty()){
            userModelPage.stream().forEach(x -> x.add(linkTo(methodOn(UserController.class).getOneUser(x.getUserId())).withSelfRel()));
        }

        return status(OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<Object> getOneUser(@PathVariable(value = "userId")UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        return status(OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public  ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        log.debug("DELETE delete User userDTO received: {} ", userId);
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        userService.deleteUser( userModelOptional.get());
        log.debug("DELETE delete User userDTO received: {} ", userId);
        log.info("User deleted successfully: {} ", userId);
        return status(OK).body("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public  ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody
                                              @Validated(UserPut.class)
                                              @JsonView(UserPut.class) UserDto userDto){

        log.debug("PUT updateUser userDTO received: {} ", userDto.toString());

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        var userModel = userModelOptional.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.info("PUT registerUser userModel updated: {} ", userModel.toString());
        log.info("PUT updated successfully userId: {} ", userModel.getUserId());
        return status(OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public  ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                  @RequestBody
                                                  @Validated(PasswordPut.class)
                                                  @JsonView(PasswordPut.class) UserDto userDto){
        log.debug("PUT updatePassword userDto received {} ", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(userModelOptional.isEmpty()) {
            return status(NOT_FOUND).body("User not found");
        }
        else if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
                log.warn("Mismatched old password {} ", userDto.getUserId());
                return status(CONFLICT).body("Error: Mismatched old password!");
            }
        var userModel = userModelOptional.get();
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("PUT updatePassword userModel saved {} ", userModel.toString());
        log.info("Password updated successfully userId {} ", userModel.getUserId());
        return status(OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public  ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                               @RequestBody
                                               @Validated(ImagePut.class)
                                               @JsonView(ImagePut.class) UserDto userDto){
        log.debug("PUT updateImage userDto received {} ", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        var userModel = userModelOptional.get();
        userModel.setImageUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("PUT updatePassword userModel saved {} ", userModel.toString());
        log.info("Password updated successfully userId {} ", userModel.getUserId());
        return status(OK).body(userModel);
    }
}
