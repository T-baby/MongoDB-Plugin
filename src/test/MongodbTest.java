import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cybermkd.kit.MongoIndex;
import com.cybermkd.kit.MongoPage;
import com.cybermkd.kit.MongoPaginate;
import com.cybermkd.kit.MongoQuery;
import com.cybermkd.plugin.MongoIceRestPlugin;
import org.junit.Before;
import org.junit.Test;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:
 */
public class MongodbTest {

    @Before
    public void init() {

        MongoIceRestPlugin iceRestPlugin = new MongoIceRestPlugin();
        iceRestPlugin.add("127.0.0.1", 27017);
        iceRestPlugin.setDatabase("test");
        iceRestPlugin.start();

    }

    @Test
    public void testInsert() {

        MongoQuery query = new MongoQuery();
        AccountBean bean = new AccountBean();
        bean.setUsername("xxxx");
        bean.setPassword("aaaa");
        System.out.println(query.use("item").set(bean).save());

    }

    @Test
    public void testInsertList() {

        MongoQuery query = new MongoQuery();
        query.use("item").add(new MongoQuery().set("多子属性测试", "父").set("b", "1"))
                .add(new MongoQuery().set("多子属性测试", "子").set("b", "3"))
                .saveList();

    }

    @Test
    public void testFindById() {
        MongoQuery query = new MongoQuery();
        AccountBean bean= query.use("item").byId("57b2c197a5e5d009d01f335f").findOne(AccountBean.class);
        System.out.println(JSON.toJSONString(bean));
    }

    @Test
    public void testFindAll() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").findAll());
        System.out.println(JSON.toJSONString(query.use("item").findAll(AccountBean.class)));
    }

    @Test
    public void testPageFindAll() {
        MongoQuery query = new MongoQuery().use("item");
        MongoPaginate painate=new MongoPaginate(query,1,2);
        MongoPage page=painate.findAll(AccountBean.class);
        System.out.println(JSON.toJSONString(page));
    }



    @Test
    public void testFind() {
        MongoQuery query = new MongoQuery();
        System.out.println(JSONObject.toJSONString(query.use("item").eq("username", "xxxx").find(AccountBean.class)));
    }

    @Test
    public void testLike() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("a", "1").like("b", "a").find());
    }

    @Test
    public void testUpdate() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("username", "xxxx").modify("b", "3").update());
    }

    @Test
    public void testUpdateByObj() {
        MongoQuery query = new MongoQuery();
        AccountBean accountBean = new AccountBean();
        accountBean.setPassword("vvvv");
        System.out.println(query.use("item").eq("username", "xxxx").modify(accountBean).update());
    }

    @Test
    public void testUpById() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").byId("5710a81ab73a87092e17a02b").modify("b", 4).update());
    }

    @Test
    public void testDel() {
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("test", "2").delete());
    }

    @Test
    public void testNotNull() {
        AccountBean bean = new AccountBean();
        System.out.println(bean.validation("password", "username"));
    }

    @Test
    public void testChinese() {
        AccountBean bean = new AccountBean();
        bean.setUsername("ss");
        System.out.println(bean.validation("password", "username"));
        System.out.println(bean.errorMessage());
    }

    @Test
    public void testSafe() {
        AccountBean bean = new AccountBean();
        bean.setPassword("<script>sss</script>");
        System.out.println(bean.validation("password"));
        System.out.println(bean.errorMessage());
    }


    @Test
    public void testType() {
        AccountBean bean = new AccountBean();
        bean.setId("12.2");
        System.out.println(bean.validation("id"));
        System.out.println(bean.errorMessage());
    }

    @Test
    public void testInside() {
        AccountBean bean = new AccountBean();
        bean.setUsername("xxxx");
        System.out.println(bean.validation("username"));
        System.out.println(bean.errorMessage());
    }

    @Test
    public void testIndex(){
        MongoIndex index=new MongoIndex("item");
        System.out.println(index.get());
        index.ascending("username");
        System.out.println(index.save());
        System.out.println(index.get());
        index.delete();
        System.out.println(index.get());
    }

    @Test
    public void testCompoundIndex(){
        MongoIndex index=new MongoIndex("item");
        System.out.println(index.get());
        index.add(new MongoIndex("item").ascending("username").setUnique(true))
                .add(new MongoIndex("item").ascending("password"))
                ;
        System.out.println(index.compound());
        System.out.println(index.get());
        index.deleteAll();
        System.out.println(index.get());
    }




}

