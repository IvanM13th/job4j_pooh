package ru.job4j.pooh;

public class Req {

    public static final String POST = "POST";
    public static final String GET = "GET";
    private static final String QUEUE = "queue";

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] rsl = content.split("[\r\n/ ]");
        String type = rsl[0];
        String mode = rsl[2];
        String source = rsl[3];
        String param;
        if (POST.equals(type)) {
            param = rsl[rsl.length - 1];
        } else {
            param = QUEUE.equals(mode)
                    ? "" : rsl[4];
        }
        return new Req(type, mode, source, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
