package com.yogesh.kafkaconsumer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "WikimediaFeeds")
public class WikimediaFeeds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String eventData;

    public WikimediaFeeds() {
    }

    public WikimediaFeeds(Long id, String eventData) {
        this.id = id;
        this.eventData = eventData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
