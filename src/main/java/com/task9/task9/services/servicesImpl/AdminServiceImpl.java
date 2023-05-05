package com.task9.task9.services.servicesImpl;

import com.task9.task9.entity.User;
import com.task9.task9.repository.CommentRepository;
import com.task9.task9.repository.PostRepository;
import com.task9.task9.repository.UserRepository;
import com.task9.task9.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private UserRepository userRepository;
    private PostRepository postRepository;

    private CommentRepository commentRepository;
    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            PostRepository postRepository,
                            CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }
    public List<User> viewAllUser() {
        return userRepository.findAll();
    }

    public List<User> getLoggedInUsers(long userID, HttpSession session ) {
        ArrayList<User> loggedInUsers = new ArrayList<>();
        Long sessionID= (Long)session.getAttribute("id");
        if(sessionID==userID){
            loggedInUsers.add((User)session.getAttribute("id"));
            System.out.println(loggedInUsers);
        }
        return loggedInUsers;
    }
}
