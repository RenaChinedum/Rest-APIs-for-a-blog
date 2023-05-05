package com.task9.task9.services.servicesImpl;

import com.task9.task9.dto.CommentDTO;
import com.task9.task9.dto.PostPageResponse;
import com.task9.task9.entity.Comment;
import com.task9.task9.entity.Post;
import com.task9.task9.entity.User;
import com.task9.task9.enums.Role;
import com.task9.task9.repository.CommentRepository;
import com.task9.task9.repository.PostRepository;
import com.task9.task9.repository.UserRepository;
import com.task9.task9.services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final HttpServletRequest request;
    private final ModelMapper modelMapper;


//    public Comment createComment(long postID, long userID, CommentDTO commentDTO) {
//       Post post = postRepository.findById(postID).orElseThrow(()-> new EntityNotFoundException("Post not found"));
//        User user = userRepository.findById(userID).orElseThrow(() -> new EntityNotFoundException("User not found"));
//        Comment comment = new Comment();
//        comment.setPost(post);
//        comment.setUser(user);
//        return commentRepository.save(comment);
//    }

    public CommentDTO createComment(long postID, CommentDTO commentDTO) throws IllegalAccessException {
        HttpSession session = request.getSession();
        Long commentingUser = (Long) session.getAttribute("id");
        Post post = postRepository.findById(postID).get();
        if (commentingUser == null) {
            throw new IllegalAccessException("Log in to comment");
        } else {

            User user = userRepository.findById(commentingUser).orElseThrow(() -> new EntityNotFoundException("User Not Found!"));
            if (user.isSuspended() || user.isBlocked()) {
                throw new IllegalAccessException("You're suspended from commenting");
            } else {
                Comment comment = commentRepository.save(Comment.builder()
                        .content(commentDTO.getContent())
                        .createdAt(LocalDate.now())
                        .user(user)
                        .post(post)
                        .build());
                return mapToDTO(comment);
            }

        }
    }

    public Comment editComment(long commentID, CommentDTO commentDTO, long userID) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentID).orElseThrow(()
                -> new EntityNotFoundException("Post with " + commentID + " Not Found!"));
        if (!comment.getUser().getId().equals(userID)) {
            throw new IllegalAccessException("You don't have the permission to delete this comment");
        }
        comment.setContent(commentDTO.getContent());
        return commentRepository.save(comment);
    }

    public Page<Comment> getPostComments(long postID, Integer pageNO, Integer pageSize) {
        Post post = postRepository.findById(postID).orElseThrow(()
                -> new EntityNotFoundException("Post with " + postID + " Not Found!"));
        Pageable pageable = PageRequest.of(pageNO, pageSize, Sort.by("createdAt").ascending());
        return commentRepository.findByPostOrderByCreatedAtAsc(post, pageable);
    }


    public int commentCount(long postID, int pageNo, int pageSizes) {
        return getPostComments(postID, pageNo, pageSizes).getSize();
    }

    public void deleteComment(long commentID, long userID, Role role) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentID).orElseThrow(()
                -> new EntityNotFoundException("Comment not found"));
        User user = userRepository.findById(userID).orElseThrow(() ->
                new EntityNotFoundException("User not Found"));
        if (comment.getUser().getId().equals(userID) || user.getRole().equals(Role.Admin)) {
            commentRepository.delete(comment);
        }
        throw new IllegalAccessException("Access Denied!");
    }


    private CommentDTO mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
