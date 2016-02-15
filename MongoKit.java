package plugin.mongodb;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建人:T-baby
 * 创建日期: 16/2/12
 * 文件描述:MongoDB操作工具类
 */
public class MongoKit {


    protected static Logger logger = Logger.getLogger(MongoKit.class);

    private static MongoClient client;
    private static DB defaultDb;


    public static void init(MongoClient client, String database) {
        MongoKit.client = client;
        MongoKit.defaultDb = client.getDB(database);

    }


    public static void updateFirst(String collectionName, MongoQuery q, MongoQuery o) {
        MongoKit.getCollection(collectionName).findAndModify(q.get(), o.get());
    }

    public static int removeAll(String collectionName) {
        return MongoKit.getCollection(collectionName).remove(new BasicDBObject()).getN();
    }

    public static int remove(String collectionName,  MongoQuery q) {
        return MongoKit.getCollection(collectionName).remove(q.get()).getN();
    }



    public static int save(String collectionName, List<MongoQuery> querys) {
        List<DBObject> objs = new ArrayList<DBObject>();
        for (MongoQuery query : querys) {
            objs.add(query.get());
        }
        return MongoKit.getCollection(collectionName).insert(objs).getN();

    }

    public static int save(String collectionName, MongoQuery q) {
        return MongoKit.getCollection(collectionName).save(q.get()).getN();
    }

    public static int save(String collectionName, Record record) {
        return MongoKit.getCollection(collectionName).save(toDbObject(record)).getN();
    }

    public static Record findFirst(String collectionName) {
        return toRecord(MongoKit.getCollection(collectionName).findOne());
    }

    //根据ID查找
    public static List<DBObject> findById(String collectionName, String id) {
        DBCollection c = MongoKit.getCollection(collectionName);
        return c.find(new BasicDBObject("_id", new ObjectId(id))).toArray();
    }

    //根据条件对象查找
    public static List<DBObject> findByQuery(String collectionName, MongoQuery query) {
        DBCollection c = MongoKit.getCollection(collectionName);
        return c.find(query.get()).toArray();
    }


    //根据条件对象查找并只要多少数量
    public static List<DBObject> findByQuery(String collectionName, MongoQuery query, int limit) {
        DBCollection c = MongoKit.getCollection(collectionName);
        return c.find(query.get()).limit(limit).toArray();
    }

    //根据条件对象查找并只要多少数量和根据什么排序
    public static List<DBObject> findByQuery(String collectionName, MongoQuery query, int limit, BasicDBObject b) {
        DBCollection c = MongoKit.getCollection(collectionName);
        return c.find(query.get()).limit(limit).sort(b).toArray();
    }

    //根据条件对象查找并删除
    public static DBObject findAndRemove(String collectionName, MongoQuery query) {
        DBCollection c = MongoKit.getCollection(collectionName);
        return c.findAndRemove(query.get());
    }


    @SuppressWarnings("unchecked")
    public static Record toRecord(DBObject dbObject) {
        Record record = new Record();
        record.setColumns(dbObject.toMap());
        return record;
    }


    public static DB getDB() {
        return defaultDb;
    }

    public static DB getDB(String dbName) {
        return client.getDB(dbName);
    }

    public static DBCollection getCollection(String name) {
        return defaultDb.getCollection(name);
    }

    public static DBCollection getDBCollection(String dbName, String collectionName) {
        return getDB(dbName).getCollection(collectionName);
    }

    public static MongoClient getClient() {
        return client;
    }

    public static void setMongoClient(MongoClient client) {
        MongoKit.client = client;
    }




    private static BasicDBObject toDBObject(Map<String, Object> map) {
        BasicDBObject dbObject = new BasicDBObject();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object val = entry.getValue();
            dbObject.append(key, val);
        }
        return dbObject;
    }

    private static BasicDBObject toDbObject(Record record) {
        BasicDBObject object = new BasicDBObject();
        for (Map.Entry<String, Object> e : record.getColumns().entrySet()) {
            object.append(e.getKey(), e.getValue());
        }
        return object;
    }
}




