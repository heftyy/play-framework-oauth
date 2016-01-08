package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import common.models.JsonSerializable;

public interface LoggingService {
    void saveLog(String type, String data);
    void saveLog(String type, JsonNode data);
    void saveLog(String type, JsonSerializable data);
}
