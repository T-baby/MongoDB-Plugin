package com.cybermkd.mongo.kit;

import com.cybermkd.mongo.kit.geospatial.MongoGeospatial;
import org.bson.Document;

import java.util.Map;

/**
 * 创建人:T-baby
 * 创建日期: 2016/12/9
 * 文件描述:
 */
public class MongoDocumentKit {

    public static Document toDocument(Object obj) {
        Map<String, Object> map = MongoKit.INSTANCE.toMap(obj);
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() instanceof MongoGeospatial) {
                map.put(entry.getKey(), ((MongoGeospatial) entry.getValue()).getPoint());
            }

            if (entry.getValue() instanceof MongoBean) {
                Document doc = toDocument((MongoBean) entry.getValue());
                map.put(entry.getKey(), doc);
            }
        }
        return new Document(map);
    }

    public static Document toDocument(MongoBean bean) {
        return new Document(bean.toMap());
    }

}


