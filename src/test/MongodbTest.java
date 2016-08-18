import com.alibaba.fastjson.JSONObject;
import com.cybermkd.kit.MongoPage;
import com.cybermkd.kit.MongoPaginate;
import com.cybermkd.kit.MongoQuery;
import com.cybermkd.plugin.MongoJFinalPlugin;
import org.junit.Test;

import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:
 */
public class MongodbTest {

    public void init() {

        MongoJFinalPlugin jFinalPlugin = new MongoJFinalPlugin();
        jFinalPlugin.add("172.30.20.231", 27017);
        jFinalPlugin.setDatabase("cstore-mikey");
        jFinalPlugin.start();

    }

    @Test
    public void testInsert() {

        init();
        MongoQuery query = new MongoQuery();
        AccountBean bean = new AccountBean();
        bean.setUsername("xxxx");
        bean.setPassword("aaaa");
        System.out.println(query.use("item").set(bean).save());

    }

    @Test
    public void testInsertList() {

        init();
        MongoQuery query = new MongoQuery();
        query.use("item").add(new MongoQuery().set("多子属性测试", "父").set("b", "1"))
                .add(new MongoQuery().set("多子属性测试", "子").set("b", "3"))
                .saveList();

    }

    @Test
    public void testFindById() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").byId("5710a81ab73a87092e17a02b").find());
    }

    @Test
    public void testFindAll() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").findAll());
    }

    @Test
    public void testFind() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(JSONObject.toJSONString(query.use("item").eq("username", "xxxx").find(AccountBean.class)));
    }

    @Test
    public void testLike() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("a", "1").like("b", "a").find());
    }

    @Test
    public void testUpdate() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("username", "xxxx").modify("b", "3").update());
    }

    @Test
    public void testUpdateByObj() {
        init();
        MongoQuery query = new MongoQuery();
        AccountBean accountBean = new AccountBean();
        accountBean.setPassword("vvvv");
        System.out.println(query.use("item").eq("username", "xxxx").modify(accountBean).update());
    }

    @Test
    public void testUpById() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").byId("5710a81ab73a87092e17a02b").modify("b", 4).update());
    }

    @Test
    public void testDel() {
        init();
        MongoQuery query = new MongoQuery();
        System.out.println(query.use("item").eq("test", "2").delete());
    }

    @Test
    public void testNotNull() {
        init();
        AccountBean bean = new AccountBean();
        System.out.println(bean.validation("password", "username"));
    }

    @Test
    public void testChinese() {
        init();
        AccountBean bean = new AccountBean();
        bean.setUsername("ss");
        System.out.println(bean.validation("password", "username"));
        System.out.println(bean.getErrorMessage());
    }

    @Test
    public void testSafe() {
        init();
        AccountBean bean = new AccountBean();
        bean.setPassword("<script>sss</script>");
        System.out.println(bean.validation("password"));
        System.out.println(bean.getErrorMessage());
    }


    @Test
    public  void query() {
        init();
        MongoQuery query=new MongoQuery();
        List list = query.use("server").find();
        System.out.println(list.size());
//        System.out.println(q.byId("57b44c22283c9f2ca01f055c").find(Server.class));
    }

    @Test
    public  void queryPage() {
        init();
        MongoQuery query=new MongoQuery();
        MongoQuery q = query.use("server");
//        List list = q.find();
        MongoPaginate mongoPaginate = new MongoPaginate(q,10,10);
        MongoPage p  = mongoPaginate.find();
        System.out.println(p);
//        System.out.println(q.byId("57b44c22283c9f2ca01f055c").find(Server.class));
    }


}

