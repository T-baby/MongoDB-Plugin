package com.cybermkd.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;


/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:MongoDB操作工具类
 */
public class MongoKit {


    final org.slf4j.Logger logger = LoggerFactory.getLogger(MongoKit.class);

    private static MongoClient client;
    private static MongoDatabase defaultDb;
    private static Validator validator;

    public static void init(MongoClient client, String database) {
        MongoKit.client = client;
        MongoKit.defaultDb = client.getDatabase(database);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return defaultDb.getCollection(collectionName);
    }

    public static long insert(String collectionName, List<Document> docs) {
        long before = getCollection(collectionName).count();
        getCollection(collectionName).insertMany(docs);
        return getCollection(collectionName).count() - before;
    }


    public static long insert(String collectionName, Document doc) {
        long before = getCollection(collectionName).count();
        getCollection(collectionName).insertOne(doc);
        return getCollection(collectionName).count() - before;
    }


    public static List<JSONObject> find(String collectionName, Bson projection) {
        return find(collectionName, new BsonDocument(), projection, new BsonDocument(), 0, 0);
    }

    public static List<JSONObject> find(String collectionName, int limit, Bson sort, Bson projection) {
        return find(collectionName, new BsonDocument(), projection, sort, limit, 0);
    }

    public static <T> List<JSONObject> find(String collectionName, int limit, Bson sort, Bson projection, Class<T> clazz) {
        return find(collectionName, new BsonDocument(), projection, sort, limit, 0,clazz);
    }


    public static List<JSONObject> find(String collectionName, Bson query, Bson projection) {
        return find(collectionName, query, projection, new BsonDocument(), 0, 0);
    }


    public static long count(String collectionName, Bson query) {
        return getCollection(collectionName).count(query);
    }

    public static long count(String collectionName) {
        return getCollection(collectionName).count();
    }


    public static List<JSONObject> find(String collectionName, Bson query, Bson projection, Bson sort, int limit, int skip) {

        final List<JSONObject> list = new ArrayList<JSONObject>();

        Block<Document> block = new Block<Document>() {

            public void apply(final Document document) {
                document.put("id", document.get("_id").toString());
                list.add(JSONObject.parseObject(document.toJson()));
            }
        };

        getCollection(collectionName).find(query).projection(projection).sort(sort).limit(limit).skip(skip).forEach(block);

        return list;

    }

    public static <T> List find(String collectionName, Bson query, Bson projection, Bson sort, int limit, int skip, Class<T> clazz) {

        final List list=new ArrayList();

        Block<Document> block = new Block<Document>() {

            public void apply(final Document document) {
                document.put("id", document.get("_id").toString());
                list.add(JSON.parseObject(document.toJson(),clazz));
            }
        };

        getCollection(collectionName).find(query).projection(projection).sort(sort).limit(limit).skip(skip).forEach(block);

        return list;

    }


    public static long update(String collectionName, Bson queue, Bson data) {
        UpdateResult updateResult = getCollection(collectionName).updateMany(queue, data);
        return updateResult.getModifiedCount();
    }


    public static long delete(String collectionName, Bson queue) {
        DeleteResult deleteResult = getCollection(collectionName).deleteMany(queue);
        return deleteResult.getDeletedCount();
    }

    public static String validation(Object obj){

        StringBuffer buffer = new StringBuffer(64);//用于存储验证后的错误信息

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(obj);//验证某个对象,其实也可以只验证其中的某一个属性的

        constraintViolations.forEach((ConstraintViolation c) -> buffer.append(c.getMessage()));

        return buffer.toString();
    }

    //校验单个属性
    public static String validation(Object obj,String[] keys){

        StringBuffer buffer = new StringBuffer(64);//用于存储验证后的错误信息

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();

        for (String key:keys){
            Iterator<ConstraintViolation<Object>> it=validator.validateProperty(obj,key).iterator();
            if (it.hasNext()){
                constraintViolations.add(it.next());
            }

        }


        constraintViolations.forEach((ConstraintViolation c) -> buffer.append(c.getMessage()));

        return buffer.toString();
    }

}




