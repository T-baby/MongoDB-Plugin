package com.cybermkd.kit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:方便增删查改
 */
public class MongoQuery {

    String collectionName;
    String join;
    Document document = new Document();
    List<Document> documents = new ArrayList<Document>();
    List<Bson> query = new ArrayList<Bson>();
    List<Bson> data = new ArrayList<Bson>();
    Bson sort;
    Bson projection;

    /**
     * 用于记录单挑插入时的id
     */
    String id;
    int limit = 0;
    int skip = 0;

    public MongoQuery use(String name) {
        this.collectionName = name;
        return this;
    }

    public static Bson and(List<Bson> query) {
        return query.size() == 0 ? new BsonDocument() : Filters.and((Iterable) query);
    }

    public MongoQuery or(MongoQuery query) {
        query.add(or(query.getQuery()));
        return this;
    }

    public static Bson or(List<Bson> query) {
        return query.size() == 0 ? new BsonDocument() : Filters.or((Iterable) query);
    }

    public MongoQuery nor(MongoQuery query) {
        query.add(nor(query.getQuery()));
        return this;
    }

    public static Bson nor(List<Bson> query) {
        return query.size() == 0 ? new BsonDocument() : Filters.nor((Iterable) query);
    }

    public MongoQuery join(String key, String collectionName, List<String> ids) {
        List<ObjectId> oids = new ArrayList<ObjectId>();
        ids.forEach((final String id) -> {
            oids.add(new ObjectId(id));
        });
        DBRef ref = new DBRef(collectionName, oids);
        document.append(key, ref);
        return this;
    }

    public MongoQuery join(String key, String collectionName, String id) {
        DBRef ref = new DBRef(collectionName, new ObjectId(id));
        document.append(key, ref);
        return this;
    }

    public MongoQuery join(String key) {
        this.join = key;
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public List<Bson> getQuery() {
        return this.query;
    }

    public String getId() {
        return id;
    }

    public List<Bson> data() {
        return this.data;
    }

    public MongoQuery set(String key, Object value) {
        document.append(key, value);
        return this;
    }

    /**
     * 支持java bean
     *
     * @param obj
     * @return
     */
    public MongoQuery set(Object obj) {
        document = Document.parse(JSON.toJSONString(obj));
        return this;
    }

    public MongoQuery add(MongoQuery query) {
        documents.add(query.getDocument());
        return this;
    }

    /**
     * 用于支持更多原生方法
     *
     * @param bson
     * @return
     */
    public MongoQuery add(Bson bson) {
        data.add(bson);
        return this;
    }

    public MongoQuery filter(Bson filter) {
        query.add(filter);
        return this;
    }

    public MongoQuery eq(String key, Object value) {
        query.add(Filters.eq(key, value));
        return this;
    }

    public MongoQuery ne(String key, Object value) {
        query.add(Filters.ne(key, value));
        return this;
    }

    /**
     * 支持查询id
     *
     * @param key
     * @param values
     * @return
     */
    public MongoQuery in(String key, List values) {
        if ("_id".equals(key)) {
            List<ObjectId> idList = new ArrayList<ObjectId>();
            values.forEach(value -> {
                idList.add(new ObjectId(String.valueOf(value)));
            });
            query.add(Filters.in(key, idList));
        } else {
            query.add(Filters.in(key, values));
        }

        return this;
    }

    /**
     * 大于 >
     */
    public MongoQuery gt(String key, Object value) {
        query.add(Filters.gt(key, value));
        return this;
    }

    /**
     * 小于 <
     */
    public MongoQuery lt(String key, Object value) {
        query.add(Filters.lt(key, value));
        return this;
    }

    /**
     * 大于等于 >=
     */
    public MongoQuery gte(String key, Object value) {
        query.add(Filters.gte(key, value));
        return this;
    }

    /**
     * 小于等于 <=
     */
    public MongoQuery lte(String key, Object value) {
        query.add(Filters.lte(key, value));
        return this;
    }

    /**
     * 更新属性,若key不存在,则新建属性
     */
    public MongoQuery modify(String key, MongoQuery query) {
        this.modify(key, query.getDocument());
        return this;
    }

    public MongoQuery modify(Object obj) {
        JSONObject json = (JSONObject) JSON.toJSON(obj);
        for (String key : json.keySet()) {
            if (json.getString(key) != null && !json.getString(key).isEmpty()) {
                this.modify(key, json.getString(key));
            }
        }
        return this;
    }

    public MongoQuery modify(String key, Object value) {
        data.add(Updates.set(key, value));
        return this;
    }

    public MongoQuery inc(String key, Number value) {
        data.add(Updates.inc(key, value));
        return this;
    }

    public MongoQuery like(String key, String value) {
        Pattern pattern = Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        query.add(Filters.regex(key, pattern));
        return this;
    }

    /**
     * 模糊条件查询
     *
     * @param type: 1-以什么开头,2-以什么结尾
     * @param key
     * @param value
     * @return
     */
    public MongoQuery like(int type, String key, String value) {
        if (type == 1) {
            Pattern pattern = Pattern.compile("^" + value + ".*$", Pattern.CASE_INSENSITIVE);
            query.add(Filters.regex(key, pattern));
        } else {
            Pattern pattern = Pattern.compile("^.*" + value + "$", Pattern.CASE_INSENSITIVE);
            query.add(Filters.regex(key, pattern));
        }
        return this;
    }

    public MongoQuery byId(String id) {
        query.add(new Document("_id", new ObjectId(id)));
        return this;
    }

    public long save() {
        long row = MongoKit.insert(collectionName, document);
        this.id = this.document.getObjectId("_id").toString();
        document.clear();
        return row;
    }

    public long saveList() {
        long row = MongoKit.insert(collectionName, documents);
        documents.clear();
        return row;
    }

    public MongoQuery projection(String... keys) {
        for (String key : keys) {
            this.projection = new BasicDBObject().append(key, 1);
        }
        return this;
    }

    public MongoQuery limit(int i) {
        this.limit = i;
        return this;
    }

    public MongoQuery skip(int i) {
        this.skip = i;
        return this;
    }

    public List<JSONObject> findAll() {
        return MongoKit.find(collectionName, limit, skip, sort, projection, join);
    }

    public <T> List findAll(Class<T> clazz) {
        return MongoKit.find(collectionName, limit, skip, sort, projection, join, clazz);
    }

    public JSONObject findOne() {
        return MongoKit.findOne(collectionName, and(query), join);
    }

    public <T> T findOne(Class<T> clazz) {
        return MongoKit.findOne(collectionName, and(query), join, clazz);
    }

    public List<JSONObject> find() {
        return MongoKit.find(collectionName, and(query), sort, projection, limit, skip, join);
    }

    public <T> List find(Class<T> clazz) {
        return MongoKit.find(collectionName, and(query), sort, projection, limit, skip, join, clazz);
    }

    /**
     * 升序排序
     *
     * @param fieldNames
     * @return
     */
    public MongoQuery ascending(String... fieldNames) {
        this.sort = Sorts.ascending(Arrays.asList(fieldNames));
        return this;
    }

    /**
     * 降序排序
     *
     * @param fieldNames
     * @return
     */
    public MongoQuery descending(String... fieldNames) {
        this.sort = Sorts.descending(Arrays.asList(fieldNames));
        return this;
    }

    public long count() {
        return MongoKit.count(collectionName, and(query));
    }

    /**
     * 判断key是否存在
     */
    public MongoQuery exist(String key) {
        set(Filters.exists(key));
        return this;
    }

    /**
     * 判断某个值是否存在
     */
    public boolean exist() {
        return this.count() > 0;
    }

    public long update() {
        return MongoKit.update(collectionName, and(query), Updates.combine(data));
    }

    public long updateOne() {
        return MongoKit.updateOne(collectionName, and(query), Updates.combine(data));
    }

    public long delete() {
        return MongoKit.delete(collectionName, and(query));
    }

    public long deleteOne() {
        return MongoKit.deleteOne(collectionName, and(query));
    }
}
