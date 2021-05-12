package com.kdimitrov.edentist.server.common.services.abstractions;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.List;

public interface NewsfeedService {

    List<Article> getTop() throws Throwable;

    List<Article> getAll() throws Throwable;
}
