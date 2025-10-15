package com.yogesh.spring_boot_kafka;

import com.launchdarkly.eventsource.EventSource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WikiMediaChangesProducer {

    private static final String WIKIMEDIA_STREAM_URL = "https://stream.wikimedia.org/v2/stream/recentchange";
    private static final String TOPIC_NAME = "wikimedia_recentchange";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private EventSource eventSource;

    public WikiMediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Starts reading Wikimedia live stream and sends data to Kafka.
     * This method runs asynchronously (Spring @Async).
     */
    @Async
    public void sendMessage() throws InterruptedException {
        log.info("Preparing to connect to Wikimedia stream...");

        // Create handler that will publish events to Kafka
        WikiMediaChangesHandles eventHandler = new WikiMediaChangesHandles(kafkaTemplate, TOPIC_NAME);

        // Build OkHttpClient that adds required User-Agent header (Wikimedia requires identification)
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request requestWithUserAgent = original.newBuilder()
                                .header("User-Agent", "WikiMediaKafkaProducer/1.0 (yogesh.choudhar@gmail.com)")
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    }
                })
                // for streaming, it's common to have no read timeout or a long one:
                .readTimeout(0, TimeUnit.MILLISECONDS) // disable read timeout for SSE stream
                .build();

        // Build EventSource with custom OkHttpClient
        eventSource = new EventSource.Builder(eventHandler, URI.create(WIKIMEDIA_STREAM_URL))
                .client(okHttpClient)
                .build();

        // Start the EventSource (it runs its own thread to receive events)
        eventSource.start();
        log.info("Wikimedia stream started and listening...");

        // Keep this async method alive for demonstration; remove or change for production
        // so that the application doesn't immediately finish the async task.
        TimeUnit.MINUTES.sleep(10);

        // Optionally stop after demo interval:
        stop();
    }

    /**
     * Gracefully closes the EventSource connection.
     */
    public void stop() {
        if (eventSource != null) {
            try {
                eventSource.close();
                log.info("Wikimedia event stream stopped.");
            } catch (Exception e) {
                log.warn("Exception while closing event source: {}", e.getMessage(), e);
            }
        }
    }
}
