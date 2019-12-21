package com.momo.webservice.web;

import com.momo.webservice.domain.posts.PostsRepository;
import com.momo.webservice.domain.posts.PostsSaveRequestDto;
import com.momo.webservice.service.PostsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WebRestController {

    private PostsService postsService;

    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }

    @PostMapping("/posts")
    public long savePosts(@RequestBody PostsSaveRequestDto dto){
        return postsService.save(dto);
    }
}
