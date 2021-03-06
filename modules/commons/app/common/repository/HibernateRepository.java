package common.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableMap;
import com.google.inject.TypeLiteral;
import common.json.DataSourceOptions;
import common.models.AbstractModel;
import common.utils.Page;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import play.db.jpa.JPA;
import play.libs.Json;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

public class HibernateRepository<T extends AbstractModel> implements Repository<T> {

    private Class<T> type;

    @Override
    public List<T> parseJson(JsonNode jn) {
        ArrayNode models = (ArrayNode) jn.findPath("models");
        List<T> result = new ArrayList<>();
        for (JsonNode jsonObject : models) {
            T obj = Json.fromJson(jsonObject, type);
            result.add(obj);
        }
        return result;
    }

    @Override
    public ArrayNode deleteWithJson(JsonNode jn) {
        ArrayNode models = (ArrayNode) jn.findPath("models");
        ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
        for (JsonNode jsonObject : models) {
            AbstractModel dataObject = Json.fromJson(jsonObject, type);
            JPA.em().remove(dataObject);
            result.add(dataObject.getJson());
        }
        return result;
    }

    @Override
    public ArrayNode updateWithJson(JsonNode jn) {
        ArrayNode models = (ArrayNode) jn.findPath("models");
        ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
        for (JsonNode jsonObject : models) {
            AbstractModel dataObject = Json.fromJson(jsonObject, type);
            dataObject.save();
            result.add(dataObject.getJson());
        }
        return result;
    }

    @Override
    public T delete(Long id) {
        T obj = JPA.em().find(type, id);
        if(obj != null) {
            JPA.em().remove(obj);
        }
        return obj;
    }

    @Inject
    @SuppressWarnings("unchecked")
    public HibernateRepository(TypeLiteral<T> type) {
        this.type = (Class<T>) type.getRawType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Criteria criteria = getSession().createCriteria(type);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<T> find(DataSourceOptions ds, String... aliases) {
        Map<String, JoinType> map = new HashMap<>();
        Stream.of(aliases).forEach(alias -> map.put(alias, JoinType.LEFT_OUTER_JOIN));
        return find(ds, map);
    }

    @Override
    public List<T> findWithRestrictions(List<?> restrictions, String... aliases) {
        Map<String, JoinType> map = new HashMap<>();
        Stream.of(aliases).forEach(alias -> map.put(alias, JoinType.LEFT_OUTER_JOIN));
        return findWithRestrictions(restrictions, map);
    }

    @Override
    public List<T> findWithRestrictions(List<?> restrictions, Map<String, JoinType> aliases) {
        Criteria criteria = getSession().createCriteria(type);

        addRestrictions(criteria, restrictions);
        addAliases(criteria, aliases);

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Override
    public List<T> find(DataSourceOptions ds, Map<String, JoinType> aliases) {
        Criteria criteria = getSession().createCriteria(type);

        addAliases(criteria, aliases);

        ds.addRestrictionsCriteria(criteria, type);
        ds.addOrderCriteria(criteria);
        ds.paginateQuery(criteria);

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public Page<T> paginate(Integer take, Integer skip, String... aliases) {
        Map<String, JoinType> map = new HashMap<>();
        Stream.of(aliases).forEach(alias -> map.put(alias, JoinType.LEFT_OUTER_JOIN));
        return paginate(take, skip, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> paginate(Integer take, Integer skip, Map<String, JoinType> aliases) {
        Criteria criteria = getSession().createCriteria(type);

        addAliases(criteria, aliases);

        Integer total = (Integer) criteria.setProjection(Projections.projectionList().add(Projections.rowCount())).uniqueResult();
        criteria.setProjection(null);

        if (skip != null) criteria.setFirstResult(skip);
        if (take != null) criteria.setMaxResults(take);

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return new Page<>(criteria.list(), total, take, skip);
    }

    @Override
    public Page<T> findAndPaginate(DataSourceOptions ds, String... aliases) {
        Map<String, JoinType> map = new HashMap<>();
        Stream.of(aliases).forEach(alias -> map.put(alias, JoinType.LEFT_OUTER_JOIN));
        return findAndPaginate(ds, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAndPaginate(DataSourceOptions ds, Map<String, JoinType> aliases) {
        Criteria criteria = getSession().createCriteria(type);

        addAliases(criteria, aliases);

        ds.addRestrictionsCriteria(criteria, type);

        Integer total = (Integer) criteria.setProjection(Projections.projectionList().add(Projections.rowCount())).uniqueResult();
        criteria.setProjection(null);

        ds.addOrderCriteria(criteria);
        ds.paginateQuery(criteria);

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return new Page<>(criteria.list(), total, ds.getTake(), ds.getSkip());
    }

    @Override
    public T findById(Long id) {
        return JPA.em().find(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findByField(String field, Object value, String... aliases) {
        return this.findByFields(ImmutableMap.of(field, value), aliases);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findByFields(Map<String, ?> restrictions, String... aliases) {
        Criteria criteria = getSession().createCriteria(type);
        restrictions.forEach((key, value) -> criteria.add(Restrictions.eq(key, value)));

        addAliases(criteria, aliases);

        Object result = criteria.uniqueResult();
        return result == null ? null : (T) result;
    }

    @Override
    public T findOneWithRestrictions(List<?> restrictions, String... aliases) {
        Criteria criteria = getSession().createCriteria(type);

        addRestrictions(criteria, restrictions);
        addAliases(criteria, aliases);

        Object result = criteria.uniqueResult();
        return result == null ? null : (T) result;
    }

    private void addRestrictions(Criteria criteria, List<?> restrictions) {
        restrictions.stream()
                .filter(restriction -> restriction instanceof Criterion)
                .forEach(restriction -> criteria.add((Criterion) restriction));
    }

    private void addAliases(Criteria criteria, Map<String, JoinType> aliases) {
        if (aliases == null) return;
        aliases.forEach((k, v) -> {
            // if the alias contains a dot use the full alias as the left side and the part after the dot as the
            // right side, this lets you join multiple tables with references to other tables
            // eg. assignment.socket.devices
            if (k.contains(".")) {
                String rhs = k.split("\\.")[1];
                criteria.createAlias(k, rhs, v);
            } else criteria.createAlias(k, k, v);
        });
    }

    private void addAliases(Criteria criteria, String... aliases) {
        if (aliases == null) return;
        Arrays.stream(aliases).forEach(alias -> {
            // if the alias contains a dot use the full alias as the left side and the part after the dot as the
            // right side, this lets you join multiple tables with references to other tables
            // eg. assignment.socket.devices
            if (alias.contains(".")) {
                String rhs = alias.split("\\.")[1];
                criteria.createAlias(alias, rhs, JoinType.LEFT_OUTER_JOIN);
            } else criteria.createAlias(alias, alias, JoinType.LEFT_OUTER_JOIN);
        });
    }
}
