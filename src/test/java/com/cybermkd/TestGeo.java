package com.cybermkd;

import com.alibaba.fastjson.JSON;
import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.kit.MongoQuery;
import com.cybermkd.mongo.kit.geospatial.MongoGeospatial;
import com.cybermkd.mongo.kit.index.MongoIndex;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 创建人:T-baby
 * 创建日期: 2016/12/13
 * 文件描述:
 */
public class TestGeo {

    @Before
    public void init() {
        MongoPlugin plugin = new MongoPlugin();
        plugin.add("127.0.0.1", 27017).setDatabase("map");
        MongoClient client = plugin.getMongoClient();
        MongoKit.INSTANCE.init(client, plugin.getDatabase());
    }

    @Test
    public void testInsert() {
        MongoQuery query = new MongoQuery().use("point");
        query.add(new MongoQuery().set("name", "深圳大学").setGeo("geo", 113.9365065988, 22.5350151812));
        query.add(new MongoQuery().set("name", "豪方精品酒店公寓").setGeo("geo", 113.9341400000, 22.5311710000));
        query.add(new MongoQuery().set("name", "深圳市深大制冷电器有限公司").setGeo("geo", 113.9214880000, 22.5332620000));
        query.add(new MongoQuery().set("name", "深圳职业技术学院").setGeo("geo", 113.9466750000, 22.5873010000));
        System.out.println(query.saveList(true));
    }

    @Test
    public void testIndex() {
        MongoIndex index = new MongoIndex("point");
        System.out.println(index.geo2dsphere("geo").save());
    }

    @Test
    public void testNear() {
        MongoQuery query = new MongoQuery().use("point");
        MongoGeospatial geo = new MongoGeospatial(113.9365065988, 22.5350151812).circleSphere("geo", 5000.0);
        query.geo(geo);
        System.out.println(JSON.toJSONString(query.find()));
    }

    @After
    public void close() {
        MongoKit.INSTANCE.getClient().close();
    }
    

}
