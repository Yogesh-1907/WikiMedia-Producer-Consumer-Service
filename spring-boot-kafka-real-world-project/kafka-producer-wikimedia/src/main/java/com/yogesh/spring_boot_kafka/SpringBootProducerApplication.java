package com.yogesh.spring_boot_kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootProducerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootProducerApplication.class, args);
    }


    @Autowired
    private WikiMediaChangesProducer wikiMediaChangesProducer;
    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
       // WikiMediaChangesProducer wikiMediaChangesProducer = new WikiMediaChangesProducer();
        wikiMediaChangesProducer.sendMessage();
    }
}
