package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/19
 * 文件描述:
 */
public class MongoIndex extends IndexOptions {

    private String collectionName;

    private Bson bson;

    private List<IndexModel> indexModels = new ArrayList<>();

    public MongoIndex(String collectionName) {
        this.collectionName = collectionName;
    }

    public Bson getBson() {
        return this.bson;
    }

    /*普通索引*/
    public MongoIndex ascending(String... keys) {
        this.bson = Indexes.ascending(keys);
        return this;
    }

    public MongoIndex descending(String... keys) {
        this.bson = Indexes.descending(keys);
        return this;
    }

    public MongoIndex geo2dsphere(String... keys) {
        this.bson = Indexes.geo2dsphere(keys);
        return this;
    }


    public MongoIndex geo2d(String key) {
        this.bson = Indexes.geo2d(key);
        return this;
    }

    public MongoIndex geoHaystack(String key, Bson additional) {
        this.bson = Indexes.geoHaystack(key, additional);
        return this;
    }

    public MongoIndex text(String key) {
        this.bson = Indexes.text(key);
        return this;
    }

    public MongoIndex hashed(String key) {
        this.bson = Indexes.hashed(key);
        return this;
    }

    public List<JSONObject> get() {
        return MongoKit.INSTANCE.getIndex(collectionName);
    }

    public String save() {
        return MongoKit.INSTANCE.setIndex(collectionName, bson);
    }

    public void delete() {
        MongoKit.INSTANCE.deleteIndex(collectionName, bson);
    }

    public void deleteAll() {
        MongoKit.INSTANCE.deleteIndex(collectionName);
    }

    /*组合索引*/
    public MongoIndex add(MongoIndex mongoIndex) {
        indexModels.add(new IndexModel(Indexes.compoundIndex(mongoIndex.getBson()), mongoIndex));
        return this;
    }

    public List<String> compound() {
        return MongoKit.INSTANCE.setIndex(collectionName, indexModels);
    }

    /*设置*/
    public MongoIndex setUnique(boolean unique) {
        unique(unique);
        return this;
    }


    public MongoIndex setBackground(boolean background) {
        background(background);
        return this;
    }


    public MongoIndex setSparse(boolean sparse) {
        sparse(sparse);
        return this;
    }

}
