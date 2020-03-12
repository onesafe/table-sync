package com._4paradigm.sage.tablesync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangyiping on 2020/3/11 6:24 PM.
 */
@Configuration
public class Constants {

    public static String canalUrl;
    public static Integer canalPort;
    public static String srcDBName;
    public static String srcDBTable;
    public static String destDBName;

    @Value("${canal.url}")
    public void setCanalUrl(String canalUrl) {
        Constants.canalUrl = canalUrl;
    }

    @Value("${canal.port}")
    public void setCanalPort(Integer canalPort) {
        Constants.canalPort = canalPort;
    }

    @Value("${srcdb.name}")
    public void setSrcDBName(String srcDBName) {
        Constants.srcDBName = srcDBName;
    }

    @Value("${srcdb.table}")
    public void setSrcDBTable(String srcDBTable) {
        Constants.srcDBTable = srcDBTable;
    }

    @Value("${destdb.name}")
    public void setDestDBName(String destDBName) {
        Constants.destDBName = destDBName;
    }
}
