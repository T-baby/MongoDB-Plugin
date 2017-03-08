package com.cybermkd.mongo.kit;

import com.cybermkd.mongo.kit.geospatial.MongoGeospatial;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人:T-baby 创建日期: 2016/12/9 文件描述:
 */
public class MongoDocumentKit {

    public static Document toDocument(Object obj) {

        Map<String, Object> map;

        if (Document.class.isInstance(obj)) {
            return (Document) obj;
        }

        if (!Map.class.isInstance(obj)) {
            map = MongoKit.INSTANCE.toMap(obj);
        } else {
            map = (Map<String, Object>) obj;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() instanceof MongoGeospatial) {
                map.put(entry.getKey(), ((MongoGeospatial) entry.getValue()).getPoint());
            }

            if (entry.getValue() instanceof MongoBean) {
                Document doc = toDocument((MongoBean) entry.getValue());
                map.put(entry.getKey(), doc);
            }

            if (entry.getValue() instanceof List) {
                try {
                    List<MongoBean> list = (List<MongoBean>) entry.getValue();
                    List<Document> docList = new ArrayList<Document>();
                    for (int i = 0; i < list.size(); i++) {
                        Document doc = toDocument(list.get(i));
                        docList.add(doc);
                    }
                    map.put(entry.getKey(), docList);
                } catch (RuntimeException e) {
                    MongoKit.INSTANCE.error("MongoDocumentKit.class",
                            "The list must be List<MongoBean> inserted into the database.");
                }

            }
        }
        map.remove("id");
        return new Document(map);
    }

    public static Document toDocument(MongoBean bean) {
        return new Document(bean.toMap());
    }

    /* 用于判断是否是基本类型和JSON对象，如果是的话不需要进行转换 */
    public static boolean conversionValidation(Object obj) {
        if (String.class.isInstance(obj) || Integer.class.isInstance(obj)
                || Double.class.isInstance(obj) || Boolean.class.isInstance(obj)
                || Float.class.isInstance(obj) || Character.class.isInstance(obj)
                || Long.class.isInstance(obj) || Byte.class.isInstance(obj)
                || Short.class.isInstance(obj) || Date.class.isInstance(obj)
                || Map.class.isInstance(obj)) {
            return false;
        }

        if (obj instanceof Object) {
            return true;
        }

        return false;

    }


}


