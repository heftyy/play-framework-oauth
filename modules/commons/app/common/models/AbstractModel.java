package common.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class AbstractModel {
    @Override
    @JsonIgnore
    public String toString() {
        return this.getJson().toString();
    }

    @JsonIgnore
    public JsonNode getJson() {
        return Json.toJson(this);
    }
}
