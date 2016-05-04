package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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

    public MongoQuery use(String name){
        this.collectionName=name;
        return this;
    }

    private Document getDocument(){
        return this.document;
    }

    public MongoQuery set(String key, Object value){
        document.append(key, value);
        return this;
    }

    public MongoQuery add(MongoQuery query){
        documents.add(query.getDocument());
        return this;
    }

    public MongoQuery filter(Bson filter){
        query.add(filter);
        return this;
    }

    public MongoQuery eq(String key, Object value){
        query.add(Filters.eq(key, value));
        return this;
    }

    public MongoQuery ne(String key, Object value){
        query.add(Filters.ne(key, value));
        return this;
    }

    public MongoQuery gt(String key, Object value){
        query.add(Filters.gt(key, value));
        return this;
    }

    public MongoQuery lt(String key, Object value){
        query.add(Filters.lt(key, value));
        return this;
    }

    public MongoQuery gte(String key, Object value){
        query.add(Filters.gte(key, value));
        return this;
    }

    public MongoQuery lte(String key, Object value){
        query.add(Filters.lte(key, value));
        return this;
    }

    public MongoQuery modify(String key, Object value){
        data.add(Updates.set(key,value));
        return this;
    }


    public MongoQuery like(String key, String value) {
        Pattern pattern = Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        query.add(Filters.regex(key,pattern));
        return this;
    }

    //1为以什么开头,2为以什么结尾
    public MongoQuery like(int type,String key, String value) {

        if (type==1){
            Pattern pattern = Pattern.compile("^"+value+".*$", Pattern.CASE_INSENSITIVE);
            query.add(Filters.regex(key, pattern));
        }else {
            Pattern pattern = Pattern.compile("^.*"+value+"$", Pattern.CASE_INSENSITIVE);
            query.add(Filters.regex(key, pattern));
        }
        return this;
    }
    public MongoQuery byId(String id){
        query.add(new Document("_id", new ObjectId(id)));
        return this;
    }


    public long save(){
        return MongoKit.insert(collectionName,document);
    }

    public long saveList(){
        return MongoKit.insert(collectionName,documents);
    }

    public List<JSONObject> findAll(){
        return MongoKit.find(collectionName);
    }

    public List<JSONObject> find(){
        return MongoKit.find(collectionName,Filters.and((Iterable) query));
    }

    public List<JSONObject> find(Bson sort){
        return MongoKit.find(collectionName,Filters.and((Iterable) query),sort);
    }


    public List<JSONObject> find(Bson sort,int limit){
        return MongoKit.find(collectionName,Filters.and((Iterable) query),sort,limit);
    }


    public long update(){
        return MongoKit.update(collectionName, Filters.and((Iterable) query), Updates.combine(data));
    }

    public long delete(){
        return MongoKit.delete(collectionName, Filters.and((Iterable) query));
    }


}
