package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String topic = req.getSourceName();
        String nameOrParam = req.getParam();
        String rsl = "";
        String status;
        if (Req.GET.equals(req.httpRequestType())) {
            String[] response = whenGet(topic, nameOrParam);
            rsl = response[0];
            status = response[1];
        } else {
            status = whenPost(topic, nameOrParam);
        }
        return new Resp(rsl, status);
    }

    private String[] whenGet(String topic, String nameOrParam) {
        String[] rsl = new String[2];
        if (topics.getOrDefault(topic, null) == null || topics.get(topic).get(nameOrParam) == null) {
            topics.putIfAbsent(topic, new ConcurrentHashMap<>());
            topics.get(topic).putIfAbsent(nameOrParam, new ConcurrentLinkedQueue<>());
            rsl[0] = Resp.BLANK;
            rsl[1] = Resp.FAIL;
        } else {
            rsl[0] = topics.get(topic).get(nameOrParam).poll();
            rsl[1] = Resp.SUCCESS;
        }
        return rsl;
    }

    private String whenPost(String topic, String nameOrParam) {
        String rsl = Resp.FAIL;
        if (topics.getOrDefault(topic, null) != null) {
            for (var themes : topics.keySet()) {
                for (var c : topics.get(themes).values()) {
                    c.add(nameOrParam);
                }
            }
            rsl = Resp.SUCCESS;
        }
        return rsl;
    }
}