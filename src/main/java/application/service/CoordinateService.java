package application.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoordinateService {
    private final String KENOBI = "kenobi";
    private final String SKYWALKER = "skywalker";
    private final String SATO = "sato";
    private final Map<String, Coordinate> satellites = Map.of(
            KENOBI, new Coordinate(500, -200),
            SKYWALKER, new Coordinate(100, -100),
            SATO, new Coordinate(500, 100)
    );

    public Coordinate calculate(Float distanceFromShipToSatellite, String satelliteName) {
        Coordinate satellite1 = satellites.get(satelliteName);
        Coordinate satellite2 = obtainFirstFrom(exclude(satelliteName));

        double distanceFromSatellite1ToSatellite2 = distanceFrom(satellite1, satellite2);
        double differenceX = xDifferenceFromSatellite1ToShip(satellite1, satellite2, distanceFromShipToSatellite, distanceFromSatellite1ToSatellite2);
        double differenceY = yDifferenceFromSatellite1ToShip(satellite1, satellite2, distanceFromShipToSatellite, distanceFromSatellite1ToSatellite2);

        // Calculate coordinates of the ship
        double shipX = satellite1.getX() + differenceX;
        double shipY = satellite1.getY() + differenceY;

        return new Coordinate(shipX, shipY);
    }

    private double xDifferenceFromSatellite1ToShip(Coordinate satellite1, Coordinate satellite2, Float distanceFromShipToSatellite1, double distanceFromSatellite1ToSatellite2) {
        double x1 = satellite1.getX();
        double x2 = satellite2.getX();
        return (distanceFromShipToSatellite1 * (x2 - x1)) / distanceFromSatellite1ToSatellite2;
    }

    private double yDifferenceFromSatellite1ToShip(Coordinate satellite1, Coordinate satellite2, Float distanceFromShipToSatellite1, double distanceFromSatellite1ToSatellite2) {
        double y1 = satellite1.getY();
        double y2 = satellite2.getY();
        return (distanceFromShipToSatellite1 * (y2 - y1)) / distanceFromSatellite1ToSatellite2;
    }

    private double distanceFrom(Coordinate satellite1, Coordinate satellite2) {
        double x1 = satellite1.getX();
        double y1 = satellite1.getY();

        double x2 = satellite2.getX();
        double y2 = satellite2.getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private Map<String, Coordinate> exclude(String satelliteName) {
        return satellites.entrySet()
                .stream()
                .filter(entry -> !satelliteName.equals(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Coordinate obtainFirstFrom(Map<String, Coordinate> satellites) {
        return satellites.entrySet()
                .stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
