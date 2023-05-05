package com.task9.task9.services.servicesImpl;

import com.task9.task9.dto.LikesDTO;
import com.task9.task9.entity.Likes;
import com.task9.task9.entity.Post;
import com.task9.task9.entity.User;
import com.task9.task9.repository.CommentRepository;
import com.task9.task9.repository.LikeRepository;
import com.task9.task9.repository.PostRepository;
import com.task9.task9.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LikeServiceImpl {
    private final PostRepository postRepository;
    private CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LikeServiceImpl(PostRepository postRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository,
                           LikeRepository likeRepository,
                           ModelMapper modelMapper
    ) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.modelMapper = modelMapper;
    }

    public LikesDTO AddLike(long postID, long userID) throws Exception {
        Post post = postRepository.findById(postID).orElseThrow(()
                -> new EntityNotFoundException("Post Not Found!"));
        log.info("post-----{}",post);
        User user = userRepository.findById(userID).orElseThrow(()
                -> new EntityNotFoundException("User Not Found!"));
        log.info("user-----{}",user);
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new EntityExistsException("You've already liked the post");
        }
        Likes likes = new Likes();
        likes.setPost(post);
        likes.setUser(user);
        LikesDTO sendlikedTo = modelMapper.map(likeRepository.save(likes),LikesDTO.class);
        return sendlikedTo;
    }
// this maps dto to likes... the class in the argument is being mapped to the class or datatype.
    public Likes mapToLike(LikesDTO likesDTO) {

        return modelMapper.map(likesDTO,Likes.class);
    }
    public LikesDTO mapToLikeDTO(Likes likes){
        return modelMapper.map(likes, LikesDTO.class);
    }

    public void dislike(long likeID, HttpServletRequest request) throws IllegalAccessException {
        HttpSession session = request.getSession();
        Long loggedInUser = (Long)session.getAttribute("id");
        Likes likes = likeRepository.findById(likeID).orElseThrow(()
                -> new EntityNotFoundException("No Like Found!"));
        User user =userRepository.findById(loggedInUser).orElseThrow(()
                -> new EntityNotFoundException("User Not Found!"));
        if(!likes.getUser().getId().equals(loggedInUser)){
            throw new IllegalAccessException("You don't have permission to delete this like");
        }
        likeRepository.delete(likes);
    }

    public List<Likes> getAllLikes(){
        return likeRepository.findAll();
    }
    public int likesCount(){
        return getAllLikes().size();
    }
}
