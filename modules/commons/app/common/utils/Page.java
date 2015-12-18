package common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import common.models.AbstractModel;
import play.libs.Json;

import java.util.List;

public class Page<T extends AbstractModel> {
    private List<T> data;
    private Integer total;
    private Integer take;
    private Integer skip;

    public Page(List<T> data, Integer total, Integer take, Integer skip) {
        this.data = data;
        this.total = total;
        this.take = take;
        this.skip = skip;
    }

    public JsonNode getJson() {
        return Json.toJson(this);
    }
}
