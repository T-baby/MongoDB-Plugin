package com.cybermkd;

import com.alibaba.fastjson.JSON;
import com.cybermkd.kit.MongoPaginate;
import com.cybermkd.kit.MongoQuery;
import org.junit.Test;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/26
 * 文件描述:
 */
public class TestPage extends TestMongodb {

    @Test
    public void page(){
        MongoPaginate page=new MongoPaginate(new MongoQuery().use("student").descending("age").join("teacher"),20,1);
        logger.info(JSON.toJSONString(page.findAll(Student.class)));
    }

}
