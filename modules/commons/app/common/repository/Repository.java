package common.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import common.json.DataSourceOptions;
import common.utils.Page;
import common.models.AbstractModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.sql.JoinType;
import play.db.jpa.JPA;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Repository<T extends AbstractModel> {

    default Session getSession() {
        return (Session) JPA.em().getDelegate();
    }

    List<T> parseJson(JsonNode jn);
    ArrayNode removeFromJson(JsonNode jn);
    ArrayNode updateFromJson(JsonNode jn);

    T findById(Long id);
    T findByField(String field, Object value, String... aliases);
    T findByFields(Map<String, Object> restrictions, String... aliases);
    T findOneWithRestrictions(List<Object> restrictions, String... aliases);
    Page<T> paginate(Integer take, Integer skip, Map<String, JoinType> joins);
    Page<T> paginate(Integer take, Integer skip, String... aliases);
    Page<T> findAndPaginate(DataSourceOptions ds, Map<String, JoinType> aliases);
    Page<T> findAndPaginate(DataSourceOptions ds, String... aliases);
    List<T> find(DataSourceOptions ds, Map<String, JoinType> aliases);
    List<T> find(DataSourceOptions ds, String... aliases);
    List<T> findWithRestrictions(List<Object> restrictions, Map<String, JoinType> aliases);
    List<T> findWithRestrictions(List<Object> restrictions, String... aliases);
    List<T> findAll();

}
