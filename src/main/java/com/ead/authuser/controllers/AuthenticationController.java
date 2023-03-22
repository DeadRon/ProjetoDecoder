package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ead.authuser.dtos.UserView.RegistrationPost;
import static com.ead.authuser.services.enums.UserStatus.ACTIVE;
import static com.ead.authuser.services.enums.UserType.STUDENT;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.of;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(RegistrationPost.class) @JsonView(RegistrationPost.class) UserDto userDto){

        log.debug("POST registerUser userDTO received: {} ", userDto.toString());
        if(userService.existsByUserName(userDto.getUserName())) {
            log.warn("UserName {} is Already Taken!", userDto.getUserName());
            return status(CONFLICT).body("Error: UserName is Already Taken!");
        }
        if(userService.existsByEmail(userDto.getEmail())) {
            log.warn("Email {} is Already Taken!", userDto.getEmail());
            return status(CONFLICT).body("Error: Email is Already Taken!");
        }
        var userModel = new UserModel();
        copyProperties(userDto, userModel);
        userModel.setUserStatus(ACTIVE);
        userModel.setUserType(STUDENT);
        userModel.setCreationDate(now(of("UTC")));
        userModel.setLastUpdateDate(now(of("UTC")));
        userService.save(userModel);
        log.info("POST registerUser userId saved: {} ", userModel.getUserId());
        log.info("POST saved successfully userId: {} ", userModel.getUserId());
        return status(CREATED).body(userModel);
    }

    //exemplo de nívies de logs
    @GetMapping("/")
    public String index(){
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");

        return "Logging spring boot....";
    }
}
