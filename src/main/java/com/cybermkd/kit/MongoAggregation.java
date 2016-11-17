package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/23
 * 文件描述:
 */
public class MongoAggregation {


    private MongoQuery query;
    private List<Bson> pipeline = new ArrayList<Bson>();
    private List<Bson> projections = new ArrayList<Bson>();
    private UnwindOptions unwindOptions = new UnwindOptions();
    private boolean allowDiskUse = true;

    public MongoAggregation(MongoQuery query) {

        /*复用MongoQuery*/

        this.query = query;

        if (query.getQuery() != null && !query.getQuery().isEmpty()) {
            pipeline.add(Aggregates.match(Filters.and(query.getQuery())));
        }

        if (query.getSort() != null) {
            pipeline.add(Aggregates.sort(query.getSort()));
        }

        if (query.getSkip() > 0) {
            pipeline.add(Aggregates.skip(query.getSkip()));
        }

        if (query.getLimit() > 0) {
            pipeline.add(Aggregates.limit(query.getLimit()));
        }

    }

    public MongoQuery getQuery() {
        return query;
    }

    public void setQuery(MongoQuery query) {
        this.query = query;
    }

    public void preserveNullAndEmptyArrays(Boolean preserveNullAndEmptyArrays) {
        unwindOptions.preserveNullAndEmptyArrays(preserveNullAndEmptyArrays);
    }

    public MongoAggregation includeArrayIndex(String arrayIndexFieldName) {
        unwindOptions.includeArrayIndex(arrayIndexFieldName);
        return this;
    }

    public MongoAggregation unwind(String field) {
        pipeline.add(Aggregates.unwind(field, unwindOptions));
        return this;
    }

    public MongoAggregation unwind(String field, UnwindOptions unwindOptions) {
        pipeline.add(Aggregates.unwind(field, unwindOptions));
        return this;
    }


    public MongoAggregation projection() {
        pipeline.add(Aggregates.project(Projections.fields(projections)));
        return this;
    }

    public MongoAggregation include(String... fieldNames) {
        projections.add(Projections.include(fieldNames));
        return this;
    }

    public MongoAggregation exclude(String... fieldNames) {
        projections.add(Projections.exclude(fieldNames));
        return this;
    }

    public MongoAggregation excludeId() {
        projections.add(Projections.excludeId());
        return this;
    }

    public MongoAggregation sample(int size) {
        pipeline.add(Aggregates.sample(size));
        return this;
    }


    public MongoAggregation lookup(String from, String localField, String foreignField, String as) {
        pipeline.add(Aggregates.lookup(from, localField, foreignField, as));
        return this;
    }

    public MongoAggregation out(String collectionName) {
        pipeline.add(Aggregates.out(collectionName));
        return this;
    }

    public MongoAggregation group(String fieldName, MongoAccumulator accumulator) {
        pipeline.add(Aggregates.group(fieldName, accumulator.getAccumulators()));
        return this;
    }


    public MongoAggregation group(Bson bson) {
        pipeline.add(Aggregates.group(bson));
        return this;
    }

    public MongoAggregation allowDiskUse(boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    public List<JSONObject> aggregate() {
        return MongoKit.INSTANCE.aggregate(query.getCollectionName(), pipeline, allowDiskUse);
    }

    public <T> List aggregate(Class<T> clazz) {
        return MongoKit.INSTANCE.aggregate(query.getCollectionName(), pipeline, allowDiskUse, clazz);
    }


}
