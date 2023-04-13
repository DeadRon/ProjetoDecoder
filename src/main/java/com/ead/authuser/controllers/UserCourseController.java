package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("")
public class UserCourseController {
    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseClient userClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = ASC) Pageable pageable,
                                                              @PathVariable(value = "userId") UUID userId){
        return status(OK).body(userClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                               @RequestBody @Valid UserCourseDTO userCourseDTO){
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty())
            return status(NOT_FOUND).body("User not found");

        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDTO.getCourseId()))
            return status(CONFLICT).body("Error: subscription already exists!");

        UserCourseModel userCourseModel = userCourseService.save(userModelOptional.get().convertToUserCourseModel(userCourseDTO.getCourseId()));
        log.info("User {} subscripted in course {}", userCourseModel.getUser().getUserId(), userCourseModel.getCourseId());
        return status(CREATED).body(userCourseModel);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId){
        if(!userCourseService.existsByCourseId(courseId)){
            return status(NOT_FOUND).body("UserCourse not found");
        }
        userCourseService.deleteUserCourseByCourse(courseId);
            return status(OK).body("UserCourse deleted succefully.");
    }

}