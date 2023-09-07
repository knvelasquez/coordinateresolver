package application.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CoordinateService {
    private final HashMap<String, Coordinate> satellites;

    public CoordinateService(HashMap<String, String> satellites) {
        this.satellites = new HashMap<>();
        this.satellites.put("kenobi", new Coordinate(500, -200));
        this.satellites.put("skywalker", new Coordinate(100, -100));
        this.satellites.put("sato", new Coordinate(500, 100));
    }

    public Coordinate calculateFrom(Float distance, String name) {
        //formula=> distance = √((x2 - x1)^2 + (y2 - y1)^2)
        double x1 = satellites.get(name).getX();
        double y1 = satellites.get(name).getY();

        double x4 = satellites.get("skywalker").getX();
        //double x4 = x1 + Math.sqrt(Math.pow(distance, 2) - Math.pow(y4 - y1, 2));

        // Calculate the y-coordinate of the fourth object
        double y4 = Math.sqrt(Math.pow(distance, 2) - Math.pow(x4 - x1, 2)) - y1;

        return new Coordinate(x4, y4);
    }
}
