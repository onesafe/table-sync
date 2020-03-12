package com._4paradigm.sage.tablesync.manager;

import com._4paradigm.sage.tablesync.config.Constants;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Created by wangyiping on 2020/3/12 12:03 AM.
 */
@Slf4j
@Component
public class CanalManager {

    public static void processData(JdbcTemplate jdbcTemplate) throws Exception {

        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(Constants.canalUrl, Constants.canalPort),
                "example",
                "",
                ""
        );

        int batchSize = 1000;

        try {
            connector.connect();
            connector.subscribe();
            //connector.subscribe(".*\\..*");
            connector.rollback();

            while (true) {
                try {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();

                    if (batchId == -1 || size == 0) {
                        Thread.sleep(1000);
                    } else {
                        // log.info(String.format("message [ batchId=%s, size=%s ] \n", batchId, size));
                        DataManager.HandleData(message.getEntries(), jdbcTemplate);
                    }

                    connector.ack(batchId); // 提交确认
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            connector.disconnect();
        }
    }
}
