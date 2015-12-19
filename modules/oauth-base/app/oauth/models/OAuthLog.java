package oauth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import common.models.AbstractModel;
import org.joda.time.DateTime;
import play.libs.Json;

import javax.persistence.PostLoad;

public class OAuthLog extends AbstractModel {
    private Long id;
    private String type;
    @JsonIgnore
    private String data;
    private JsonNode jsonData;
    private DateTime dateTime;

    @PostLoad
    void postLoad() {
        jsonData = Json.parse(data);
    }

    public OAuthLog() {
    }

    public OAuthLog(String type, String data, DateTime dateTime) {
        this.type = type;
        this.data = data;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
