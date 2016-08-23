package com.cybermkd.plugin;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/5/13
 * 文件描述:支持更多特性的MongoPlugin
 */
public class MongoPlugin {

    final Logger logger = LoggerFactory.getLogger(MongoPlugin.class);

    private MongoClientOptions.Builder options = MongoClientOptions.builder();

    private List<ServerAddress> hostList = new ArrayList<>();

    private List<MongoCredential> authList = new ArrayList<>();

    /*数据库名*/
    private String database;

    public MongoPlugin add(String host) {
        this.hostList.add(new ServerAddress(host, 27017));
        return this;
    }

    public MongoPlugin add(String host, int port) {
        this.hostList.add(new ServerAddress(host, port));
        return this;
    }

    public MongoPlugin setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getDatabase() {
        return this.database;
    }

    /**
     * 数据库授权
     */
    public MongoPlugin auth(MongoCredential mongoCredential) {
        this.authList.add(mongoCredential);
        return this;
    }

    /**
     * 数据库授权
     */
    public MongoPlugin auth(String username, String password) {
        this.authList.add(MongoCredential.createCredential(username, this.database, password.toCharArray()));
        return this;
    }

    /**
     * 使用SSL连接
     */
    public MongoPlugin ssl() {
        this.options.sslEnabled(true);
        return this;
    }

    public MongoPlugin connectTimeout(int connectTimeout) {
        this.options.connectTimeout(connectTimeout);
        return this;
    }

    public MongoPlugin opition(MongoClientOptions.Builder opitions) {
        this.options = opitions;
        return this;
    }

    /**
     * 自定义读写分离
     * <p>
     * primary: 主节点,默认模式,读操作只在主节点,如果主节点不可用,报错或者抛出异常。
     * <p>
     * primaryPreferred: 首选主节点,大多情况下读操作在主节点,如果主节点不可用,如故障转移,读操作在从节点。
     * <p>
     * secondary: 从节点,读操作只在从节点, 如果从节点不可用,报错或者抛出异常。
     * <p>
     * secondaryPreferred: 首选从节点,大多情况下读操作在从节点,特殊情况（如单主节点架构）读操作在主节点。
     * <p>
     * nearest: 最邻近节点,读操作在最邻近的成员,可能是主节点或者从节点,根据ping时间最短
     */
    public MongoPlugin readPreference(ReadPreference readPreference) {
        this.options.readPreference(readPreference);
        return this;
    }

    public MongoPlugin readPreference() {
        this.options.readPreference(ReadPreference.secondaryPreferred());
        return this;
    }

    public MongoPlugin writeConcern(WriteConcern writeConcern) {
        this.options.writeConcern(writeConcern);
        return this;
    }

    /*前几次修改操作仍然被记录在journal中,可以被还原也可以被撤销,避免数据不一致或弄脏的情况,写成功后再执行到真的数据集中*/
    public MongoPlugin writeConcern() {
        this.options.writeConcern(WriteConcern.JOURNALED);
        return this;
    }

    public MongoPlugin writeSafe() {
        this.options.writeConcern(WriteConcern.SAFE);
        return this;
    }

    public MongoClient getMongoClient() {
        try {
            return new MongoClient(hostList, authList, options.build());
        } catch (Exception e) {
            throw new RuntimeException("无法连接mongodb,请检查配置!");
        }
    }
}
