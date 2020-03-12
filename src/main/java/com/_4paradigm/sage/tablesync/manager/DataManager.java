package com._4paradigm.sage.tablesync.manager;

import com._4paradigm.sage.tablesync.config.Constants;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangyiping on 2020/3/12 12:00 AM.
 */
@Slf4j
@Service
public class DataManager {

    public static void HandleData(List<CanalEntry.Entry> entrys, JdbcTemplate jdbcTemplate) throws Exception {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException(
                        "ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e
                );
            }

            CanalEntry.EventType eventType = rowChage.getEventType();

            // 同步指定的表
            if(Constants.srcDBName.equals(entry.getHeader().getSchemaName())
                    && Constants.srcDBTable.equals(entry.getHeader().getTableName())) {

                String binlogInfo = String.format(
                        "binlog:[%s], offset:[%s], eventLength:[%s], SchemaName:[%s], TableName:[%s], eventType:[%s]",
                        entry.getHeader().getLogfileName(),
                        entry.getHeader().getLogfileOffset(),
                        entry.getHeader().getEventLength(),
                        entry.getHeader().getSchemaName(),
                        entry.getHeader().getTableName(),
                        eventType);
                log.info(binlogInfo);

                log.info(String.format("开始同步数据库表: %s", entry.getHeader().getTableName()));

                String sql = rowChage.getSql();
                log.info(String.format("Origianl SQL: %s", sql));
                String newSql = sql.replace(Constants.srcDBName, Constants.destDBName);
                log.info(String.format("New SQL: %s", newSql));

                if(
                        eventType == CanalEntry.EventType.DELETE ||
                        eventType == CanalEntry.EventType.UPDATE ||
                        eventType == CanalEntry.EventType.INSERT
                        ) {
                    log.info("execute new sql");
                    jdbcTemplate.execute(newSql);
                }
            }
        }
    }
}
