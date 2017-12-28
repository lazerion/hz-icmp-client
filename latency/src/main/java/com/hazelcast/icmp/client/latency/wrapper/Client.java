package com.hazelcast.icmp.client.latency.wrapper;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        ClientConfig cfg = new XmlClientConfigBuilder().build();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(cfg);

        IMap<String, String> map = client.getMap(RandomStringUtils.randomAlphabetic(42));
        logger.info("Get Map {}", map);
        logger.info("Started");
        List<Long> samples = new ArrayList<>();

        while (true) {
            int size = client.getCluster().getMembers().size();
            if (size != 3) {
                TimeUnit.MILLISECONDS.sleep(1000);
            } else {
                break;
            }
        }

        while (true) {
            long startTime = System.currentTimeMillis();
            IntStream.range(0, 1024).forEach(it -> {
                final String value = RandomStringUtils.randomAlphabetic(1024);
                final String key = RandomStringUtils.randomAlphabetic(42);

                map.set(key, value);
            });
            long elapsed = System.currentTimeMillis() - startTime;
            samples.add(elapsed);

            if (samples.size() % 100 == 0) {
                logger.info("Statistics {}", samples.stream().mapToLong(it -> it).summaryStatistics());
            }

            try {
                map.clear();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
}
