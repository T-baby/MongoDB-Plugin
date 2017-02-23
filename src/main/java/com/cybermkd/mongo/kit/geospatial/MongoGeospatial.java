package com.cybermkd.mongo.kit.geospatial;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 2016/12/13
 * 文件描述:
 */
public class MongoGeospatial {

    private Point point;

    private Bson query;

    private List<List<Double>> list = new ArrayList<List<Double>>();

    public MongoGeospatial() {

    }

    public MongoGeospatial(Double x, Double y) {
        set(x, y);
    }

    public MongoGeospatial set(Double x, Double y) {
        point = new Point(new Position(x, y));
        return this;
    }

    public MongoGeospatial add(Double x, Double y) {
        List<Double> temp = new ArrayList<Double>();
        temp.add(x);
        temp.add(y);
        list.add(temp);
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public Bson getQuery() {
        return query;
    }

    public List<List<Double>> getList() {
        return list;
    }

    public MongoGeospatial near(String filedName, Double maxDistance, Double minDistance) {
        query = Filters.near(filedName, point, maxDistance, minDistance);
        return this;
    }

    public MongoGeospatial nearSphere(String filedName, Double maxDistance, Double minDistance) {
        query = Filters.nearSphere(filedName, point, maxDistance, minDistance);
        return this;
    }

    public MongoGeospatial circle(String filedName, Double radius) {
        query = Filters.geoWithinCenter(filedName, point.getPosition().getValues().get(0),point.getPosition().getValues().get(1), radius);
        return this;
    }

    public MongoGeospatial circleSphere(String filedName, Double radius) {
        query = Filters.geoWithinCenterSphere(filedName, point.getPosition().getValues().get(0),point.getPosition().getValues().get(1), radius);
        return this;
    }

    public MongoGeospatial withinPolygon(String filedName) {
        query = Filters.geoWithinPolygon(filedName, list);
        return this;
    }

}
