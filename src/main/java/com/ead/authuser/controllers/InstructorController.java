package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InsctructorDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.ZoneId.of;
import static com.ead.authuser.enums.UserType.INSTRUCTOR;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InsctructorDTO insctructorDTO){
        Optional<UserModel> userModelOptional = userService.findById(insctructorDTO.getUserId());
        if(userModelOptional.isEmpty()){
            return status(NOT_FOUND).body("User not found");
        } else {
            var userModel = userModelOptional.get();
            userModel.setUserType(INSTRUCTOR);
            userModel.setLastUpdateDate(now(of("UTC")));
            userService.save(userModel);
            return status(OK).body(userModel);
        }
    }

}