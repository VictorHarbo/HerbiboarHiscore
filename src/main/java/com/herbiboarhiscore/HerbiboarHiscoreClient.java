package com.herbiboarhiscore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class HerbiboarHiscoreClient
{
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Type SCORE_LIST_TYPE = new TypeToken<List<ScoreEntry>>() {}.getType();

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final HerbiboarHiscoreConfig config;

    @Inject
    public HerbiboarHiscoreClient(OkHttpClient httpClient, Gson gson, HerbiboarHiscoreConfig config)
    {
        this.httpClient = httpClient;
        this.gson = gson;
        this.config = config;
    }

    public void submitScore(String playerName, int harvestCount)
    {
        ScoreEntry payload = new ScoreEntry(playerName, harvestCount);
        RequestBody body = RequestBody.create(gson.toJson(payload), JSON);
        Request request = new Request.Builder()
            .url(config.apiBaseUrl() + "/scores")
            .post(body)
            .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                log.warn("Failed to submit score to API", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try (response)
                {
                    if (!response.isSuccessful())
                    {
                        log.warn("Score submission returned HTTP {}", response.code());
                    }
                }
            }
        });
    }

    public void getScoresAsync(Consumer<List<ScoreEntry>> callback)
    {
        Request request = new Request.Builder()
            .url(config.apiBaseUrl() + "/scores")
            .get()
            .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                log.warn("Failed to fetch scores from API", e);
                callback.accept(Collections.emptyList());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try (response)
                {
                    if (!response.isSuccessful() || response.body() == null)
                    {
                        log.warn("Fetch scores returned HTTP {}", response.code());
                        callback.accept(Collections.emptyList());
                        return;
                    }
                    List<ScoreEntry> scores = gson.fromJson(response.body().charStream(), SCORE_LIST_TYPE);
                    callback.accept(scores);
                }
            }
        });
    }
}


@Slf4j
public class HerbiboarHiscoreClient
{
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Type SCORE_LIST_TYPE = new TypeToken<List<ScoreEntry>>() {}.getType();

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final HerbiboarHiscoreConfig config;

    @Inject
    public HerbiboarHiscoreClient(OkHttpClient httpClient, Gson gson, HerbiboarHiscoreConfig config)
    {
        this.httpClient = httpClient;
        this.gson = gson;
        this.config = config;
    }

    public void submitScore(String playerName, int harvestCount)
    {
        ScoreEntry payload = new ScoreEntry(playerName, harvestCount);
        RequestBody body = RequestBody.create(gson.toJson(payload), JSON);
        Request request = new Request.Builder()
            .url(config.apiBaseUrl() + "/scores")
            .addHeader("Authorization", "Bearer " + config.apiKey())
            .post(body)
            .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                log.warn("Failed to submit score to API", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try (response)
                {
                    if (!response.isSuccessful())
                    {
                        log.warn("Score submission returned HTTP {}", response.code());
                    }
                }
            }
        });
    }

    public void getScoresAsync(Consumer<List<ScoreEntry>> callback)
    {
        Request request = new Request.Builder()
            .url(config.apiBaseUrl() + "/scores")
            .addHeader("Authorization", "Bearer " + config.apiKey())
            .get()
            .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                log.warn("Failed to fetch scores from API", e);
                callback.accept(Collections.emptyList());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try (response)
                {
                    if (!response.isSuccessful() || response.body() == null)
                    {
                        log.warn("Fetch scores returned HTTP {}", response.code());
                        callback.accept(Collections.emptyList());
                        return;
                    }
                    List<ScoreEntry> scores = gson.fromJson(response.body().charStream(), SCORE_LIST_TYPE);
                    callback.accept(scores);
                }
            }
        });
    }
}
