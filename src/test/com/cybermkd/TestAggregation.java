package com.cybermkd;

import com.alibaba.fastjson.JSON;
import com.cybermkd.kit.MongoAccumulator;
import com.cybermkd.kit.MongoAggregation;
import com.cybermkd.kit.MongoQuery;
import org.junit.Test;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/26
 * 文件描述:
 */
public class TestAggregation extends TestMongodb {

    /*求出每个老师下所有男生平均成绩,输出到output集合中*/
    @Test
    public void avg(){
        MongoQuery query=new MongoQuery();
        //query.use("student").eq("sex",1).descending("age").limit(10);
        query.use("student").eq("sex",1).descending("score");
        MongoAggregation aggregation=new MongoAggregation(query);
        logger.info(JSON.toJSONString(aggregation
                //.include("name","sex","age","teacherName")
                //.projection()
                //.lookup("teacher","teacherName","name","teacher")

                .group("$teacherName",new MongoAccumulator().avg("result","$age"))

                //.out("_output_")
                .aggregate()));
    }



}
