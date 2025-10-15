package com.yogesh.kafkaconsumer.repository;

import com.yogesh.kafkaconsumer.entity.WikimediaFeeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WikiMediaDataStore extends JpaRepository<WikimediaFeeds, Long> {
}
