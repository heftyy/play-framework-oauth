package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import common.models.AbstractModel;
import oauth.models.OAuthLog;
import org.joda.time.DateTime;
import play.db.jpa.JPA;
import play.libs.Json;

public class HibernateLoggingService implements LoggingService {
    @Override
    public void saveLog(String type, String data) {
        OAuthLog log = new OAuthLog(type, data, DateTime.now());
        JPA.em().persist(log);
    }

    @Override
    public void saveLog(String type, JsonNode data) {
        saveLog(type, Json.toJson(data).toString());
    }

    @Override
    public void saveLog(String type, AbstractModel data) {
        saveLog(type, data.getJson().toString());
    }
}
