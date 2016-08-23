package com.cybermkd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cybermkd.kit.*;
import com.cybermkd.plugin.MongoPlugin;
import com.mongodb.MongoClient;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Random;

public class MongodbTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        MongoPlugin plugin = new MongoPlugin();
        plugin.add("127.0.0.1", 27017).setDatabase("test");
        MongoClient client = plugin.getMongoClient();
        MongoKit.init(client, plugin.getDatabase());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsert() {
        MongoQuery query = new MongoQuery();
        AccountBean bean = new AccountBean();
        bean.setUsername("mongodb-" + new Random().nextInt(1000));
        bean.setPassword("ddd-bbb");
        bean.setAge(18);
        bean.setCreateDate(System.currentTimeMillis());
        long count = query.use("item").set(bean).save();
        Assert.assertEquals(1L, count);
    }

    public void testInsertList() {
        MongoQuery query = new MongoQuery();
        long count = query.use("item")
                .add(new MongoQuery().set("username", "name-001").set("password", "123").set("level", "AAA"))
                .add(new MongoQuery().set("username", "name-002").set("password", "456").set("level", "BBB"))
                .saveList();
        Assert.assertEquals(2L, count);
    }

    public void testFindById() {
        MongoQuery query = new MongoQuery();
        query.use("item").set("username", "name-before").set("password", "12345").save();
        AccountBean findBean = new MongoQuery().use("item").eq("username", "name-before").findOne(AccountBean.class);

        AccountBean bean = query.use("item").byId(findBean.getId()).findOne(AccountBean.class);
        Assert.assertEquals("12345", bean.getPassword());
    }

    public void testFindAll() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").findAll());
        System.out.println(JSON.toJSONString(query.use("item").findAll(AccountBean.class)));
    }

    public void testPageFindAll() {
        MongoQuery query = new MongoQuery().use("item");
        MongoPaginate paginate = new MongoPaginate(query, 1, 2);
        MongoPage page = paginate.findAll(AccountBean.class);
        System.out.println(JSON.toJSONString(page));
    }

    public void testFind() {
        MongoQuery query = new MongoQuery();
        System.out.println(JSONObject.toJSONString(query.use("item").eq("username", "name-001").find(AccountBean.class)));
    }

    public void testLike() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("password", "ddd-bbb").like("username", "mongo").find());
    }

    public void testUpdate() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("username", "name-before").modify("username", "name-after").update());
    }

    public void testUpdateByObj() {
        MongoQuery query = new MongoQuery();
        AccountBean accountBean = new AccountBean();
        accountBean.setPassword("vvvv");
        System.out.println(query.use("item").eq("username", "name-001").modify(accountBean).update());
    }

    public void testUpById() {
        MongoQuery query = new MongoQuery();
        query.use("item").set("username", "name-update").set("password", "12345").save();
        AccountBean findBean = new MongoQuery().use("item").eq("username", "name-update").findOne(AccountBean.class);

        System.out.println(query.use("item").byId(findBean.getId()).modify("password", "password-updated").update());
    }

    public void testDel() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("username", "name-002").delete());
    }

    public void testNotNull() {
        AccountBean bean = new AccountBean();
        System.out.println(bean.validation("password", "username"));
    }

    public void testChinese() {
        AccountBean bean = new AccountBean();
        bean.setUsername("ss");
        System.out.println(bean.validation("password", "username"));
        System.out.println(bean.errorMessage());
    }

    public void testSafe() {
        AccountBean bean = new AccountBean();
        bean.setPassword("<script>sss</script>");
        System.out.println(bean.validation("password"));
        System.out.println(bean.errorMessage());
    }

    public void testType() {
        AccountBean bean = new AccountBean();
        bean.setId("12.2");
        System.out.println(bean.validation("id"));
        System.out.println(bean.errorMessage());
    }

    public void testInside() {
        AccountBean bean = new AccountBean();
        bean.setUsername("mongo3");
        System.out.println(bean.validation("username"));
        System.out.println(bean.errorMessage());
    }

    public void testIndex() {
        MongoIndex index = new MongoIndex("item");
        System.out.println(index.get());
        index.ascending("username");
        System.out.println(index.save());
        System.out.println(index.get());
        index.delete();
        System.out.println(index.get());
    }

    public void testCompoundIndex() {
        MongoIndex index = new MongoIndex("item");
        System.out.println(index.get());

        index.add(new MongoIndex("item")
                .ascending("username")
                .setUnique(true))
                .add(new MongoIndex("item").ascending("password"));

        System.out.println(index.compound());
        System.out.println(index.get());
        index.deleteAll();
        System.out.println(index.get());
    }
}

