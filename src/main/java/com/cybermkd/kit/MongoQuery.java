package com.cybermkd.kit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
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
    Document document = new Document();
    List<Document> documents = new ArrayList<Document>();
    List<Bson> query = new ArrayList<Bson>();
    List<Bson> data = new ArrayList<Bson>();
    Bson sort;
    Bson projection;
    /*用于记录单挑插入时的id*/
    String id;
    int limit = 0;
    int skip = 0;


    public MongoQuery use(String name) {
        this.collectionName = name;
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

    /*支持java bean*/
    public MongoQuery set(Object obj) {
        document = Document.parse(JSON.toJSONString(obj));
        return this;
    }

    public MongoQuery add(MongoQuery query) {
        documents.add(query.getDocument());
        return this;
    }

    /*用于支持更多原生方法*/
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

    //支持查询id
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


    public MongoQuery gt(String key, Object value) {
        query.add(Filters.gt(key, value));
        return this;
    }

    public MongoQuery lt(String key, Object value) {
        query.add(Filters.lt(key, value));
        return this;
    }

    public MongoQuery gte(String key, Object value) {
        query.add(Filters.gte(key, value));
        return this;
    }

    public MongoQuery lte(String key, Object value) {
        query.add(Filters.lte(key, value));
        return this;
    }

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

    //1为以什么开头,2为以什么结尾
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
        return MongoKit.find(collectionName, limit, sort, projection);
    }

    public <T> List<JSONObject> findAll(Class<T> clazz) {
        return MongoKit.find(collectionName, limit, sort, projection, clazz);
    }


    public List<JSONObject> find() {
        return MongoKit.find(collectionName, MongoKit.and(query), sort, projection, limit, skip);
    }

    public <T> List find(Class<T> clazz) {
        return MongoKit.find(collectionName, MongoKit.and(query), sort, projection, limit, skip, clazz);
    }

    public MongoQuery ascending(String... fieldNames) {
        this.sort = Sorts.ascending(Arrays.asList(fieldNames));
        return this;
    }

    public MongoQuery descending(String... fieldNames) {
        this.sort = Sorts.descending(Arrays.asList(fieldNames));
        return this;
    }

    public long count() {
        return MongoKit.count(collectionName, MongoKit.and(query));
    }


    public long update() {
        return MongoKit.update(collectionName, MongoKit.and(query), Updates.combine(data));
    }

    public long delete() {
        return MongoKit.delete(collectionName, MongoKit.and(query));
    }


}
