package model;

public class MessageModel {
    private String name;
    private Float distance;
    private String[] message;

    public MessageModel() {
    }

    public MessageModel(String name, Float distance, String[] message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public Float getDistance() {
        return distance;
    }

    public String[] getMessage() {
        return message;
    }
}
