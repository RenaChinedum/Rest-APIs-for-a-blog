package com.task9.task9.controller;

import com.task9.task9.services.servicesImpl.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageServiceImpl imageService;

}
