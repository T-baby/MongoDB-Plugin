package com.cybermkd.mongo.kit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cybermkd.mongo.kit.geospatial.MongoGeospatial;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.mongodb.client.model.*;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:方便增删查改
 */
public class MongoQuery {

    private String collectionName;
    private String join;
    private Document document = new Document();
    private List<Document> documents = new ArrayList<Document>();
    private List<Bson> query = new ArrayList<Bson>();
    private List<Bson> data = new ArrayList<Bson>();
    private Bson sort;
    private Bson projection;
    /*用于记录单挑插入时的id*/
    private String id;
    private int limit = 0;
    private int skip = 0;

    public String getCollectionName() {
        return collectionName;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }

    public Bson getSort() {
        return sort;
    }

    public MongoQuery use(String name) {
        this.collectionName = name;
        return this;
    }

    public static Bson and(List<Bson> q) {
        return q.size() == 0 ? new BsonDocument() : Filters.and((Iterable) q);
    }

    public MongoQuery or(MongoQuery... qs) {

        orQuery(Arrays.asList(qs));
        return this;
    }

    public MongoQuery orQuery(List<MongoQuery> qs) {
        List<Bson> bsons = new ArrayList<Bson>();
        for (MongoQuery q : qs) {
            bsons.add(and(q.getQuery()));
        }
        query.add(or(bsons));
        return this;
    }

    public static Bson or(List<Bson> q) {
        return q.size() == 0 ? new BsonDocument() : Filters.or((Iterable) q);
    }

    public static Bson or(Bson q) {
        return Filters.or(q);
    }

    public MongoQuery nor(MongoQuery q) {
        query.add(nor(q.getQuery()));
        return this;
    }

    public static Bson nor(List<Bson> query) {
        return query.size() == 0 ? new BsonDocument() : Filters.nor((Iterable) query);
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

    public MongoQuery setGeo(String key, Double x, Double y) {
        document.append(key, new Point(new Position(x, y)));
        return this;
    }

    public MongoQuery set(Object obj) {
        document = MongoDocumentKit.toDocument(obj);
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

    public MongoQuery regex(String key, String regex) {
        query.add(Filters.regex(key, regex));
        return this;
    }

    public MongoQuery type(String key, String type) {
        query.add(Filters.type(key, type));
        return this;
    }


    public MongoQuery mod(String key, long divisor, long remainder) {
        query.add(Filters.mod(key, divisor, remainder));
        return this;
    }


    public MongoQuery text(String search) {
        query.add(Filters.text(search));
        return this;
    }

    public MongoQuery text(String search, TextSearchOptions textSearchOptions) {
        query.add(Filters.text(search, textSearchOptions));
        return this;
    }

    public MongoQuery where(String javaScriptExpression) {
        query.add(Filters.where(javaScriptExpression));
        return this;
    }

    public MongoQuery elemMatch(String key, MongoQuery query) {
        this.query.add(Filters.elemMatch(key, this.and(query.getQuery())));
        return this;
    }

    public MongoQuery size(String key, int size) {
        query.add(Filters.size(key, size));
        return this;
    }

    public MongoQuery geo(MongoGeospatial geo) {
        query.add(geo.getQuery());
        return this;
    }

    //支持查询id
    public MongoQuery in(String key, List values) {
        if ("_id".equals(key)) {
            List<ObjectId> idList = new ArrayList<ObjectId>();

            Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Object value = (Object) iter.next();
                idList.add(new ObjectId(String.valueOf(value)));
            }
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
        data.add(Updates.set(key, MongoDocumentKit.toDocument(value)));
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

    public boolean save() {
        try {
            MongoKit.INSTANCE.insert(collectionName, document);
            this.id = this.document.getObjectId("_id").toString();
            document.clear();
            return true;
        } catch (RuntimeException e) {
            MongoKit.INSTANCE.error("MongoQuery.class", e.getMessage());
            return false;
        }
    }

    public boolean saveList() {
        return saveList(false);
    }

    /*快速插入用于插入不需要排序的文档，如一个人的账户等等*/
    public boolean saveList(boolean fast) {
        try {
            InsertManyOptions ops = new InsertManyOptions();
            if (fast) {
                ops.ordered(false);
            }
            MongoKit.INSTANCE.insert(collectionName, documents, ops);
            documents.clear();
            return true;
        } catch (RuntimeException e) {
            MongoKit.INSTANCE.error("MongoQuery.class", e.getMessage());
            return false;
        }
    }


    public MongoQuery projection(String... keys) {
        BasicDBObject dbObj = new BasicDBObject();
        for (String key : keys) {
            dbObj.append(key, 1);
        }

        this.projection = dbObj;
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
        return MongoKit.INSTANCE.find(collectionName, limit, skip, sort, projection, join);
    }

    public <T> List findAll(Class<T> clazz) {
        return MongoKit.INSTANCE.find(collectionName, limit, skip, sort, projection, join, clazz);
    }


    public JSONObject findOne() {
        return MongoKit.INSTANCE.findOne(collectionName, and(query), sort, join);
    }

    public <T> T findOne(Class<T> clazz) {
        return MongoKit.INSTANCE.findOne(collectionName, and(query), sort, join, clazz);
    }

    public List<JSONObject> find() {
        return MongoKit.INSTANCE.find(collectionName, and(query), sort, projection, limit, skip, join);
    }

    public <T> List find(Class<T> clazz) {
        return MongoKit.INSTANCE.find(collectionName, and(query), sort, projection, limit, skip, join, clazz);
    }

    public MongoQuery ascending(String... keys) {
        this.sort = Sorts.ascending(Arrays.asList(keys));
        return this;
    }

    public MongoQuery descending(String... keys) {
        this.sort = Sorts.descending(Arrays.asList(keys));
        return this;
    }

    public long count() {
        return MongoKit.INSTANCE.count(collectionName, and(query));
    }

    public JSONObject max(String key) {
        descending(key);
        return findOne();
    }

    public <T> T max(String key, Class<T> clazz) {
        descending(key);
        return findOne(clazz);
    }

    public JSONObject min(String key) {
        ascending(key);
        return findOne();
    }

    public <T> T min(String key, Class<T> clazz) {
        ascending(key);
        return findOne(clazz);
    }


    /*存在某个key*/
    public MongoQuery exist(String key) {
        set(Filters.exists(key));
        return this;
    }

    /*判断某个值是否存在*/
    public boolean exist() {
        return this.count() > 0;
    }

    public long update() {
        return MongoKit.INSTANCE.update(collectionName, and(query), Updates.combine(data));
    }

    public long updateOne() {
        return MongoKit.INSTANCE.updateOne(collectionName, and(query), Updates.combine(data));
    }


    public long replace(Object obj) {
        Document doc = MongoDocumentKit.toDocument(obj);
        doc.remove("_id");
        return MongoKit.INSTANCE.replaceOne(collectionName, and(query), doc);
    }

    public long delete() {
        return MongoKit.INSTANCE.delete(collectionName, and(query));
    }

    public long deleteOne() {
        return MongoKit.INSTANCE.deleteOne(collectionName, and(query));
    }

}