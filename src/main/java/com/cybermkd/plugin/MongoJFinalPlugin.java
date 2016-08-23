package com.cybermkd.plugin;

import com.cybermkd.kit.MongoKit;
import com.jfinal.plugin.IPlugin;
import com.mongodb.MongoClient;

import java.util.logging.Logger;

/**
 * 创建人:T-baby
 * 创建日期: 16/4/15
 * 文件描述:MongoDB for JFinal 插件
 */

public class MongoJFinalPlugin extends MongoPlugin implements IPlugin {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    private MongoClient client;

    @Override
    public boolean start() {
        client = getMongoClient();
        MongoKit.init(client, getDatabase());
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
