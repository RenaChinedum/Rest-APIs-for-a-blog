package com.task9.task9.services.servicesImpl;

import com.task9.task9.dto.PostDTO;
import com.task9.task9.dto.PostPageResponse;
import com.task9.task9.entity.Post;
import com.task9.task9.entity.User;
import com.task9.task9.enums.Role;
import com.task9.task9.repository.PostRepository;
import com.task9.task9.repository.UserRepository;
import com.task9.task9.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
   private final ModelMapper modelMapper;
    private  final HttpServletRequest request;


    @Override
    public PostDTO createPost(PostDTO postDTO) throws IllegalAccessException {
        HttpSession session = request.getSession();
       Long loggedInId = (Long) session.getAttribute("id");
        if(loggedInId == null){
            throw new IllegalAccessException("Log in to post");
        } else {
            User user = userRepository.findById(loggedInId).orElseThrow(()-> new EntityNotFoundException("User Not Found!"));
            if(user.isSuspended()|| user.isBlocked()){
                throw new IllegalAccessException("You're Suspended from making post");
            }else {
                Post post = postRepository.save(Post.builder()
                        .title(postDTO.getTitle())
                        .description(postDTO.getDescription())
                        .content(postDTO.getContent())
                        .createdAt(LocalDate.now())
                        .user(user)
                        .build());
                return mapToPostDTO(post);
            }
        }

    }
    @Override
    public Post savePost(Post post, Long id) {
        Optional<User> user = userRepository.findById(id);
        post.setUser(user.get());
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAllPost() {
        return postRepository.findAll();
    }

    @Override
    public PostPageResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir){
       Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
       Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
       Page<Post> postpage = postRepository.findAll(pageable);
       List<PostDTO> postDTOList = postpage.getContent().stream().map(this::mapToPostDTO).toList();
       return PostPageResponse.builder()
               .content(postDTOList)
               .last(postpage.isLast())
               .pageNo(postpage.getNumber())
               .pageSize(postpage.getSize())
               .totalElement(postpage.getTotalElements())
               .totalPages(postpage.getTotalPages())
               .build();
    }

    public Post mapToPost(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO mapToPostDTO(Post post){
        return modelMapper.map(post, PostDTO.class);
    }
    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
    }

    @Override
    public Post editPost(Long postId, PostDTO postDTO, Long userId) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new Exception("You are not authorized to edit this post");
        }
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        return postRepository.save(post);
    }

    @Override
    public PostPageResponse searchPostByTitle(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> postpage = postRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrContentContainingIgnoreCase(keyword,keyword,keyword, pageable);
        List<PostDTO> postDTOList = postpage.getContent().stream().map(this::mapToPostDTO).toList();
        return PostPageResponse.builder()
                .content(postDTOList)
                .last(postpage.isLast())
                .pageNo(postpage.getNumber())
                .pageSize(postpage.getSize())
                .totalElement(postpage.getTotalElements())
                .totalPages(postpage.getTotalPages())
                .build();

    }

    public String deletePost(long id, long userID, Role role) throws AccessDeniedException {

        Post post = postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("post not found"));
        User user = userRepository.getUserByRole(role);
        if(post.getUser().getId().equals(userID) || user.getRole().equals(Role.Admin)){
            postRepository.deleteById(id);
        }else {
            throw new AccessDeniedException("You don't have access to delete this post");
        }
        return "Deleted Successfully";
    }

    public List<Post> getUserPost() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("id");
        return postRepository.findPostByUser(user);
    }
}
