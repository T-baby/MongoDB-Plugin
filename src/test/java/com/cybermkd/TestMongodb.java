package com.cybermkd;

import com.alibaba.fastjson.JSONArray;
import com.cybermkd.log.Logger;
import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.kit.MongoQuery;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.mongodb.MongoClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Random;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/25
 * 文件描述:
 */
public class TestMongodb {

    private static final Random random = new Random();

    public static Logger logger = Logger.getLogger(TestMongodb.class);

    @BeforeClass
    public static void init() {
        MongoPlugin plugin = new MongoPlugin();
        plugin.add("127.0.0.1", 27017).setDatabase("training");
        MongoClient client = plugin.getMongoClient();
        MongoKit.INSTANCE.init(client, plugin.getDatabase());

        MongoKit.INSTANCE.getCollection("_output_").drop();
        MongoKit.INSTANCE.getCollection("student").drop();
        MongoKit.INSTANCE.getCollection("teacher").drop();

        /*开始模拟真实数据*/
        JSONArray studentNames = ReadTextTool.readTxtFile(TestMongodb.class.getResource("/student.txt").getPath());
        JSONArray teacherNames = ReadTextTool.readTxtFile(TestMongodb.class.getResource("/teacher.txt").getPath());

        /*每个老师带十个学生*/
        for (int i = 0; i < 100; i++) {
             /*初始化批量插入用的对象*/
            MongoQuery teacherQuery = new MongoQuery().use("teacher");
            MongoQuery studentQuery = new MongoQuery().use("student");

            Teacher teacher = new Teacher();
            teacher.setName(spaceFliter(teacherNames.getString(i)));
            teacherQuery.set(teacher);
            teacherQuery.save();

            for (int j = 0; j < 10; j++) {
                Student student = new Student();
                student.setName(spaceFliter(studentNames.getString(10 * i + j)));
                student.setAge(number(18, 30));
                student.setSex(sex(student.getAge()));
                student.setScore(number(0,100));
                student.setTeacherName(teacher.getName());
                studentQuery.add(new MongoQuery().set(student).join("teacher", "teacher", teacherQuery.getId()));

            }
            studentQuery.saveList();

        }

    }


    @AfterClass
    public static void clean() {
        MongoKit.INSTANCE.getCollection("_output_").drop();
        MongoKit.INSTANCE.getCollection("student").drop();
        MongoKit.INSTANCE.getCollection("teacher").drop();
        MongoKit.INSTANCE.getClient().close();
    }


    public static int number(int min, int max) {
        return min + random.nextInt(max - min);
    }

    public static int sex(int age) {
        if (age % 2 == 0) {
            return 1;
        }else {
            return 2;
        }
    }

    //用于去掉空格
    public static String spaceFliter(String text) {
        text = text.replaceAll(" ", "");
        return text;
    }


}
