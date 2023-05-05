package com.task9.task9.controller;

import com.task9.task9.entity.User;
import com.task9.task9.services.servicesImpl.AdminServiceImpl;
import com.task9.task9.services.servicesImpl.CommentServiceImpl;
import com.task9.task9.services.servicesImpl.PostServiceImpl;
import com.task9.task9.services.servicesImpl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
private UserServiceImpl userService;
private PostServiceImpl postService;
private CommentServiceImpl commentService;
private AdminServiceImpl adminService;

    public AdminController(UserServiceImpl userService, PostServiceImpl postService, CommentServiceImpl commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> viewUsers(){
        return new ResponseEntity<>(adminService.viewAllUser(), HttpStatus.OK);
    }
    @GetMapping("/logged-in-users")
    public ResponseEntity<List<User>> listOfLoggedInUsers(@PathVariable long userID, HttpSession session){
        return new ResponseEntity<>(adminService.getLoggedInUsers(userID,session),HttpStatus.OK);
    }



}
