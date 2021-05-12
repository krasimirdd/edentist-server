package com.kdimitrov.edentist.server.common.api;

import com.kdimitrov.edentist.server.common.services.NewsfeedService;
import com.kwabenaberko.newsapilib.models.Article;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kdimitrov.edentist.server.common.utils.Routes.ALL;
import static com.kdimitrov.edentist.server.common.utils.Routes.TOP;
import static com.kdimitrov.edentist.server.common.utils.Routes.NEWSFEED;

@RestController
@RequestMapping(value = NEWSFEED)
@CrossOrigin("*")
public class NewsfeedController {

    final NewsfeedService newsfeedService;

    public NewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    @GetMapping(TOP)
    @ResponseBody
    public List<Article> getTop() {
        try {
            return newsfeedService.getTop();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @GetMapping(ALL)
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
