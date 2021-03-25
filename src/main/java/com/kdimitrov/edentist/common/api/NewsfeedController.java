package com.kdimitrov.edentist.common.api;

import com.kdimitrov.edentist.common.services.NewsfeedService;
import com.kwabenaberko.newsapilib.models.Article;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class NewsfeedController {

    final NewsfeedService newsfeedService;

    public NewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    @GetMapping("/api/newsfeed/top")
    @ResponseBody
    public List<Article> getTop() {
        try {
            return newsfeedService.getTop();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/newsfeed/all")
    @ResponseBody
    public List<Article> getAll() {
        try {
            return newsfeedService.getAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}