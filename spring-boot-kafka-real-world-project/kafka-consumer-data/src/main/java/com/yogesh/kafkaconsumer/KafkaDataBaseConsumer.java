package com.yogesh.kafkaconsumer;

import com.yogesh.kafkaconsumer.entity.WikimediaFeeds;
import com.yogesh.kafkaconsumer.repository.WikiMediaDataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaDataBaseConsumer {


    private  final WikiMediaDataStore wikiMediaDataStore;

    public KafkaDataBaseConsumer(WikiMediaDataStore wikiMediaDataStore) {
        this.wikiMediaDataStore = wikiMediaDataStore;
    }

    @KafkaListener(topics = "wikimedia_recentchange", groupId = "wiki_media_group")
    public void listen(String message) {
        log.info(String.format("Received Message: %s", message));
       WikimediaFeeds wikimediaFeeds = new WikimediaFeeds();   // create a new object of WikimediaFeeds
       wikimediaFeeds.setEventData(message);    // Set the event data to setter method
       wikiMediaDataStore.save(wikimediaFeeds);   // Save the object to the database
    }
}
