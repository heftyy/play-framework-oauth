package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import common.models.AbstractModel;

public interface LoggingService {
    void saveLog(String type, String data);
    void saveLog(String type, JsonNode data);
    void saveLog(String type, AbstractModel data);
}
