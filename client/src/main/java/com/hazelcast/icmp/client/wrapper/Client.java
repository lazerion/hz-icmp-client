package com.hazelcast.icmp.client.wrapper;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        ClientConfig cfg = new XmlClientConfigBuilder().build();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(cfg);

        IQueue<String> queue = client.getQueue(RandomStringUtils.randomAlphabetic(42));
        logger.info("Get Queue {}", queue);
        logger.info("Started");

        while (true) {
            IntStream.range(0, 1024).forEach(it -> {
                final String value = RandomStringUtils.randomAlphabetic(1024);
                try {
                    queue.put(value);
                    queue.take();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            });
            logger.info("Running queue");
            TimeUnit.SECONDS.sleep(5);
            try {
                queue.clear();
            } catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
