package com.cybermkd;

import com.alibaba.fastjson.JSON;
import com.cybermkd.kit.MongoQuery;
import org.junit.Test;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/26
 * 文件描述:
 */
public class TestCRUD extends TestMongodb{

    private static String id;

    @Test
    public void insert(){
        MongoQuery query=new MongoQuery();
        Teacher teacher=new Teacher();
        teacher.setName("踢宝宝");
        query.use("teacher").set(teacher).save();
        id=query.getId();
        logger.info(id);
    }

    @Test
    public void findOne(){
        MongoQuery query=new MongoQuery();
        logger.info(query.use("teacher").byId(id).findOne().toJSONString());
    }

    @Test
    public void findOr(){
        MongoQuery query=new MongoQuery();
        logger.info(JSON.toJSONString(query.use("teacher").or(new MongoQuery().eq("name","卫越相"),new MongoQuery().eq("name","危兴修")).find()));
    }

    @Test
    public void findAndJoin(){
        MongoQuery query=new MongoQuery();
        logger.info(JSON.toJSONString(query.use("student").join("teacher").findOne(Student.class)));
    }

    @Test
    public void update(){
        logger.info(new MongoQuery().use("teacher").eq("name","卫越相").findOne().toJSONString());
        logger.info(String.valueOf(new MongoQuery().use("teacher").eq("name","卫越相").modify("name","兔宝宝").update()));
        logger.info(new MongoQuery().use("teacher").eq("name","兔宝宝").findOne().toJSONString());
    }

    @Test
    public void delete(){
        Teacher teacher=new MongoQuery().use("teacher").eq("name","兔宝宝").findOne(Teacher.class);
        logger.info(String.valueOf(new MongoQuery().use("teacher").byId(teacher.getId()).deleteOne()));
    }

    @Test
    public void sort(){
        logger.info(JSON.toJSONString(new MongoQuery().use("student").descending("age").join("teacher").findAll()));
    }

    @Test
    public void count(){
        logger.info(JSON.toJSONString(new MongoQuery().use("student").count()));
    }

    @Test
    public void max(){
        logger.info(JSON.toJSONString(new MongoQuery().use("student").join("teacher").max("age")));
    }

    @Test
    public void min(){
        logger.info(JSON.toJSONString(new MongoQuery().use("student").join("teacher").min("age")));
    }

    @Test
    public void exit(){
        logger.info(String.valueOf(new MongoQuery().use("student").eq("name","贝承载").exist()));
    }

}
