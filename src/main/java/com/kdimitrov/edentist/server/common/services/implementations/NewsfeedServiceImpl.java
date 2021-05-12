package com.kdimitrov.edentist.server.common.services.implementations;

import com.kdimitrov.edentist.app.config.ApplicationConfig;
import com.kdimitrov.edentist.server.common.services.abstractions.NewsfeedService;
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

    private final int limit;
    private final int timeout;
    private final NewsApiClient newsApiClient;
    private Throwable error;
    private final List<Article> articlesList = new ArrayList<>();

    public NewsfeedServiceImpl(ApplicationConfig.NewsApiClientConfig config) {
        this.newsApiClient = new NewsApiClient(config.getToken());
        this.timeout = config.getTimeout();
        this.limit = config.getLimit();
    }

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

        wait(timeout);
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

        wait(timeout);
        if (error != null) {
            throw error;
        }

        return articlesList;
    }

    private NewsApiClient.ArticlesResponseCallback callback() {
        return new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse response) {
                for (int i = 0; i < limit; i++) {
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
