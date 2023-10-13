package com.document.management.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.document.management.configuration.FeignClientConfiguration;
import com.document.management.model.Comment;
import com.document.management.model.Post;

@FeignClient(name ="jsonPlaceholderFeignService",
                url = "https://jsonplaceholder.typicode.com",
                configuration = FeignClientConfiguration.class)
public interface IJsonPlaceholderFeignService {

    /**
     * Add the post for a given document
     * @param post
     * @return
     */
    @PostMapping(path ="/posts")
    Post createPostForDocument(@RequestBody Post post);

    /**
     * Add the comment for a given document
     * @param comment
     * @return
     */
    @PostMapping(path = "/comments")
    Comment createCommentForDocument(@RequestBody Comment comment);

}