package adapter.presentation;

import application.service.Coordinate;
import application.service.CoordinateService;
import application.service.EncryptService;
import application.service.JwtService;
import domain.JsonService;
import domain.Producer;
import model.CoordinateModel;
import model.MessageModel;
import model.SecurityKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/coordinate")
public class CoordinateRest {
    private final JwtService jwtService;
    private final EncryptService encryptService;
    private final JsonService jsonService;
    private final Producer kafka;

    private final CoordinateService coordinateService;

    public CoordinateRest(JwtService jwtService, EncryptService encryptService, JsonService jsonService, Producer kafka, CoordinateService coordinateService) {
        this.jwtService = jwtService;
        this.encryptService = encryptService;
        this.jsonService = jsonService;
        this.kafka = kafka;
        this.coordinateService = coordinateService;
    }

    @GetMapping(value = "/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("version", "v2.0");
        response.put("status", "healthy");
        response.put("details", "Coordinate Resolver Service is up and running.");
        response.put("date", new Date(System.currentTimeMillis()).toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/resolver")
    public CoordinateModel execute(@RequestBody MessageModel messageModel,
                                   @RequestHeader(value = "Authorization") String jwt) {
        SecurityKey key = jwtService.decode(jwt.replace("Bearer ", ""));
        Coordinate coordinate = coordinateService.calculateFrom(messageModel.getDistance(), messageModel.getName());
        CoordinateModel coordinates = new CoordinateModel(coordinate.getX(), coordinate.getY(), stringFrom(messageModel.getMessage()));
        String message = jsonService.mapToString(coordinates);
        String encrypted = encryptService.from(message, key.getPrivateKey());
        kafka.publish(encrypted);
        return coordinates;
    }

    private String stringFrom(String[] message) {
        StringBuilder result = new StringBuilder();

        for (String word : message) {
            if (!word.isEmpty()) {
                result.append(word).append(" ");
            }
        }
        //Remove extra trailing white space, if any
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
