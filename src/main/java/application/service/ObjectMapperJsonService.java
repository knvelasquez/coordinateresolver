package application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.JsonService;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperJsonService implements JsonService {
    private final ObjectMapper objectMapper;

    public ObjectMapperJsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String mapToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
