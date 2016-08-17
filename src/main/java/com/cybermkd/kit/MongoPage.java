package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/5/19
 * 文件描述:
 */
public class MongoPage {
    private int count;
    private int page;
    private int totalPage;
    private long totalRow;
    private boolean firstPage = false;
    private boolean lastPage = false;
    private List list = new ArrayList();

    public MongoPage(int count, int page, int totalPage, long totalRow, boolean firstPage,
                     boolean lastPage, List result) {

        this.count = count;
        this.page = page;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
        this.list = result;

    }

    public int getCount() {
        return count;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public long getTotalRow() {
        return totalRow;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public List getList() {
        return list;
    }
}
