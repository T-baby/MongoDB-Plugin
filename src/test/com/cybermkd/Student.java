package com.cybermkd;

import com.cybermkd.constraints.Chinese;
import com.cybermkd.constraints.Exist;
import com.cybermkd.constraints.Inside;
import com.cybermkd.constraints.Type;
import com.cybermkd.kit.MongoValidate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/25
 * 文件描述:
 */
public class Student extends MongoValidate {

    /**
     * Student是一个英语培训机构的学生类,因为MongoDB本身没有约束,故使用class来约束
     *
     * @param id Objectid
     * @param name 学生姓名
     * @param age 学生年龄
     * @param sex 学生性别,1为男生,2为女生
     * @param score 几次考试总数
     * @param teacher 教课的老师
     */


    @Exist(value = false, collectionName = "student", key = "id")
    private String id;

    @Chinese(value = true)
    @Size(min = 1, max = 4)
    @SafeHtml
    private String name;

    @Type(value = "int")
    @Size(min = 1, max = 100)
    private int age;

    @Type(value = "int")
    @Inside(value = {"1", "2"})
    private int sex;

    @Length(max = 100)
    private int score;

    private Teacher teacher;

    private String teacherName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
