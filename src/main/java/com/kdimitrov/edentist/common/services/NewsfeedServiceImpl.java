package com.kdimitrov.edentist.common.services;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsfeedServiceImpl implements NewsfeedService {

    private static final int LIMIT = 15;
    private static final int TIMEOUT = 1500;
    NewsApiClient newsApiClient = new NewsApiClient("7c18617c8c3d4d32bae73e8970677dd8");
    Throwable error;
    private List<Article> articlesList = new ArrayList<>();

    @Override
    public synchronized List<Article> getTop() throws Throwable {
        TopHeadlinesRequest request = new TopHeadlinesRequest.Builder()
                .q("health")
                .language("en")
                .build();

        newsApiClient.getTopHeadlines(
                request,
                callback()
        );

        wait(TIMEOUT);
        if (error != null) {
            throw error;
        }

        return articlesList;
    }

    @Override
    public List<Article> getAll() throws Throwable {

        EverythingRequest request = new EverythingRequest.Builder()
                .q("health")
                .language("en")
                .build();

        newsApiClient.getEverything(
                request,
                callback()
        );

        wait(TIMEOUT);
        if (error != null) {
            throw error;
        }

        return articlesList;
    }

    private NewsApiClient.ArticlesResponseCallback callback() {
        return new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse response) {
                for (int i = 0; i < LIMIT; i++) {
                    articlesList.add(response.getArticles().get(i));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                error = throwable;
            }
        };
    }

}
