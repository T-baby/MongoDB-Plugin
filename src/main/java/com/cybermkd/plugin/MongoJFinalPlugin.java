package com.cybermkd.plugin;

import com.jfinal.plugin.IPlugin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.cybermkd.kit.MongoKit;

import java.util.logging.Logger;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:MongoDB for JFinal 插件
 */

public class MongoJFinalPlugin implements IPlugin {


    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    private MongoClient client;

    private MongoClientURI connectionString;

    private String host="mongodb://";

    private String database;



    public MongoJFinalPlugin(String database,String...host){

        for(int i=0;i<host.length;i++){
            this.host=this.host+host[i];
        }
        this.connectionString = new MongoClientURI(this.host);
        this.database=database;
    }
    @Override
    public boolean start() {

        try {
            client = new MongoClient(connectionString);
        } catch (Exception e) {
            throw new RuntimeException("can't connect mongodb, please check the host and port:"+this.host);
        }

        MongoKit.init(client, database);
        return true;
    }

    @Override
    public boolean stop() {
        if (client != null) {
            client.close();
        }
        return true;
    }


}
