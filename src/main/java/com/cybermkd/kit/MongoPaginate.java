package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/5/17
 * 文件描述:用于分页
 */
public class MongoPaginate {

    private int count;
    private int page;
    private int totalPage;
    private long totalRow;
    private MongoQuery query;
    private boolean firstPage = false;
    private boolean lastPage = false;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public MongoPaginate(MongoQuery query, int count, int page) {
        this.query = query;
        this.count = count;
        this.page = page;
        this.totalRow = count();
        this.totalPage = (int) (totalRow / (long) count);
        if (totalRow % (long) count != 0L) {
            ++totalPage;
        }

        if (count <= 0 || count > totalRow || page <= 0 || page > totalPage || page * count > totalRow) {
            new RuntimeException("MongPage tips: (づ￣ 3￣)づ count or page is error !");
        }

        this.firstPage = this.page == 1;
        this.lastPage = this.page == this.totalPage;

    }


    public MongoPage find() {
        this.list = query.limit(count).skip(skip()).find();
        return new MongoPage(count, page, totalPage, totalRow, firstPage, lastPage, list);
    }


    private int skip() {
        if (firstPage) {
            return 0;
        } else {
            return (page - 1) * count;
        }
    }

    public int count() {
        return (int) this.query.count();
    }


}
