package com.cy.okhttpniubility_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        try {
//            run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    run_();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }


                    System.out.println(responseBody.string());
                    responseBody.close();
                }
            }
        });
    }


    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * 09-09 17:06:01.434 19584-19734/com.cy.okhttpniubility I/System.out: 0.00 Executing call.
     * 09-09 17:06:01.459 19584-19734/com.cy.okhttpniubility I/System: core_booster, getBoosterConfig = false
     * 09-09 17:06:01.509 19584-19737/com.cy.okhttpniubility I/OpenGLRenderer: Initialized EGL, version 1.4
     * 09-09 17:06:02.434 19584-19735/com.cy.okhttpniubility I/System.out: 1.00 Canceling call.
     * 09-09 17:06:02.435 19584-19735/com.cy.okhttpniubility I/System.out: 1.00 Canceled call.
     * 09-09 17:06:02.436 19584-19734/com.cy.okhttpniubility I/System.out: 1.00 Call failed as expected: java.io.IOException: Canceled
     * @throws Exception
     */
    public void run_() throws Exception {
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                .build();

        final long startNanos = System.nanoTime();
        final Call call = client.newCall(request);

        // Schedule a job to cancel the call in 1 second.
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                call.cancel();
                System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
            }
        }, 1, TimeUnit.SECONDS);

        System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
        try (Response response = call.execute()) {
            System.out.printf("%.2f Call was expected to fail, but completed: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, response);
        } catch (IOException e) {
            System.out.printf("%.2f Call failed as expected: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, e);
        }
    }




    public void run2() throws Exception {
        HttpUrl.Builder urlBuilder =HttpUrl.parse("https://api.weibo.com/2/users/show.json")
                .newBuilder();
        urlBuilder.addQueryParameter("access_token", oauth2AccessToken.getToken());


        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }
}
