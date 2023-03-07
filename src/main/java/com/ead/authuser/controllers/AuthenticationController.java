package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ead.authuser.dtos.UserView.RegistrationPost;
import static com.ead.authuser.models.enums.UserStatus.ACTIVE;
import static com.ead.authuser.models.enums.UserType.STUDENT;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.of;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(RegistrationPost.class) @JsonView(RegistrationPost.class) UserDto userDto){

        if(userService.existsByUserName(userDto.getUserName()))
            return status(CONFLICT).body("Error: UserName is Already Taken!");

        if(userService.existsByEmail(userDto.getEmail()))
            return status(CONFLICT).body("Error: Email is Already Taken!");

        var userModel = new UserModel();
        copyProperties(userDto, userModel);
        userModel.setUserStatus(ACTIVE);
        userModel.setUserType(STUDENT);
        userModel.setCreationDate(now(of("UTC")));
        userModel.setLastUpdateDate(now(of("UTC")));
        userService.save(userModel);

        return status(CREATED).body(userModel);
    }

    //exemplo de nívies de logs
    @GetMapping("/")
    public String index(){
        logger.trace("TRACE");
        logger.debug("DEBUG");
        logger.info("INFO");
        logger.warn("WARN");
        logger.error("ERROR");
        return "Logging spring boot....";
    }
}
