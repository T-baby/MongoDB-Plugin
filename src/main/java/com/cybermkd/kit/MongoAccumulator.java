package com.cybermkd.kit;

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
    private String expression = "$quantity";

    public String getExpression() {
        return expression;
    }

    public MongoAccumulator setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public List<BsonField> getAccumulators() {
        return accumulators;
    }

    public MongoAccumulator setAccumulators(List<BsonField> accumulators) {
        this.accumulators = accumulators;
        return this;
    }

    public MongoAccumulator sum(String fieldName) {
        accumulators.add(Accumulators.sum(fieldName, expression));
        return this;
    }

    public MongoAccumulator avg(String fieldName) {
        accumulators.add(Accumulators.avg(fieldName, expression));
        return this;
    }

    public MongoAccumulator first(String fieldName) {
        accumulators.add(Accumulators.first(fieldName, expression));
        return this;
    }

    public MongoAccumulator last(String fieldName) {
        accumulators.add(Accumulators.last(fieldName, expression));
        return this;
    }

    public MongoAccumulator max(String fieldName) {
        accumulators.add(Accumulators.max(fieldName, expression));
        return this;
    }

    public MongoAccumulator min(String fieldName) {
        accumulators.add(Accumulators.min(fieldName, expression));
        return this;
    }

    public MongoAccumulator push(String fieldName) {
        accumulators.add(Accumulators.push(fieldName, expression));
        return this;
    }

    public MongoAccumulator addToSet(String fieldName) {
        accumulators.add(Accumulators.addToSet(fieldName, expression));
        return this;
    }

    public MongoAccumulator stdDevPop(String fieldName) {
        accumulators.add(Accumulators.stdDevPop(fieldName, expression));
        return this;
    }

    public MongoAccumulator stdDevSamp(String fieldName) {
        accumulators.add(Accumulators.stdDevSamp(fieldName, expression));
        return this;
    }

    public MongoAccumulator quantity() {
        expression = "$quantity";
        return this;
    }

    public MongoAccumulator totalQuantity() {
        expression = "$totalQuantity";
        return this;
    }

    public MongoAccumulator averageQuantity() {
        expression = "$averageQuantity";
        return this;
    }


}
