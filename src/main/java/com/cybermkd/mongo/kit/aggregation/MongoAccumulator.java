package com.cybermkd.mongo.kit.aggregation;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.BsonField;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/23
 * 文件描述:
 */
public class MongoAccumulator {

    private List<BsonField> accumulators = new ArrayList<BsonField>();


    public List<BsonField> getAccumulators() {
        return accumulators;
    }

    public MongoAccumulator setAccumulators(List<BsonField> accumulators) {
        this.accumulators = accumulators;
        return this;
    }

    public MongoAccumulator sum(String fieldName, String expression) {
        accumulators.add(Accumulators.sum(fieldName, expression));
        return this;
    }

    public MongoAccumulator avg(String fieldName, String expression) {
        accumulators.add(Accumulators.avg(fieldName, expression));
        return this;
    }

    public MongoAccumulator first(String fieldName, String expression) {
        accumulators.add(Accumulators.first(fieldName, expression));
        return this;
    }

    public MongoAccumulator last(String fieldName, String expression) {
        accumulators.add(Accumulators.last(fieldName, expression));
        return this;
    }

    public MongoAccumulator max(String fieldName, String expression) {
        accumulators.add(Accumulators.max(fieldName, expression));
        return this;
    }

    public MongoAccumulator min(String fieldName, String expression) {
        accumulators.add(Accumulators.min(fieldName, expression));
        return this;
    }

    public MongoAccumulator push(String fieldName, String expression) {
        accumulators.add(Accumulators.push(fieldName, expression));
        return this;
    }

    public MongoAccumulator addToSet(String fieldName, String expression) {
        accumulators.add(Accumulators.addToSet(fieldName, expression));
        return this;
    }

    public MongoAccumulator stdDevPop(String fieldName, String expression) {
        accumulators.add(Accumulators.stdDevPop(fieldName, expression));
        return this;
    }

    public MongoAccumulator stdDevSamp(String fieldName, String expression) {
        accumulators.add(Accumulators.stdDevSamp(fieldName, expression));
        return this;
    }


}
