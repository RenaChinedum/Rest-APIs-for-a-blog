package com.task9.task9.services.servicesImpl;

import com.task9.task9.entity.Image;
import com.task9.task9.entity.Post;
import com.task9.task9.entity.User;
import com.task9.task9.exception.ResourceNotFoundException;
import com.task9.task9.exception.UnauthorisedException;
import com.task9.task9.repository.ImageRepository;
import com.task9.task9.repository.PostRepository;
import com.task9.task9.repository.UserRepository;
import com.task9.task9.services.ImageService;
import com.task9.task9.utility.SessionChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public String saveImage(MultipartFile[] files, Long postID, HttpServletRequest request) throws ResourceNotFoundException {
        Long loggedInUser = SessionChecker.checkUserSession(request);

        Optional<Post> optionalPost = postRepository.findById(postID);

        if(optionalPost.isEmpty()){
            throw new ResourceNotFoundException("Post Not Found!");
        }

        User user = userRepository.findById(loggedInUser).get();

        String contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        Post post = optionalPost.get();

        if(post.getUser().getId() != user.getId()){
            throw new UnauthorisedException("You're Not Authorised");
        }

        for(MultipartFile file: files){
            String fileName;
            byte[] imageArray;
            String fileType;

            if(!file.getContentType().startsWith("image/")){

                throw new ResourceNotFoundException("Invalid file type");
            }
            fileName = file.getOriginalFilename();
            fileType = file.getContentType();
            try {
                imageArray = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image image = new Image();
            image.setImage(imageArray);
            image.setName(fileName);
            image.setPost(post);
            Image savedImage = imageRepository.save(image);
            String url = contextPath + "/post" +  "/image/" + savedImage.getId() +   "/" + savedImage.getName();
            savedImage.setUrl(url);
            imageRepository.save(savedImage);
        }

        return "success";


    }

    public String uploadImage(MultipartFile[] files){
        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image/")) {
                throw new ResourceNotFoundException("Invalid file type");
            }

            try {
                String fileName = file.getOriginalFilename();
                byte[] imageArray = file.getBytes();

                // save the image to the file system or database
                // ...
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return "success";
    }

}


