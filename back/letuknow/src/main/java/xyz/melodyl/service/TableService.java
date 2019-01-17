package xyz.melodyl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.melodyl.mapper.table.TableMapper;
import xyz.melodyl.pojo.table.*;

import java.util.List;
import java.util.Map;

@Service("TableService")
public class TableService {

    @Autowired
    @Qualifier("TableMapper")
    private TableMapper tableMapper;

    // create table cannot trigger roll-back,
    // so we need simulate roll-back
    @Transactional(rollbackFor = Exception.class)
    public void createTableAndAddRecord(Integer userID, BaseTableContext baseTableContext) {
        tableMapper.createTable(createTableSqlBuilder(userID, baseTableContext));

        try {
            tableMapper.insertUserTableMapping(userID, baseTableContext);
            tableMapper.insertColumnTableMapping(baseTableContext.getId(), baseTableContext.getColumns());
        } catch (Exception e) {
            tableMapper.dropTable(createTableSqlBuilder(userID, baseTableContext));
            throw e;//触发insert的事务
        }
    }

    public BaseTableContext getTableContextNoCmd(Integer userID, String tableName) {
        BaseTableContext std = getTableContext(userID, tableName);
        std.setCommand(null);
        return std;
    }

    public BaseTableContext getTableContext(Integer userID, String tableName) {
        return tableMapper.selectUserTableMapping(userID, tableName);
    }

    public List<ComplicatedTableContext> getTableContextList(Integer userID, int limit, int offset) {
        return tableMapper.selectUserTableMappingList(userID, limit, offset);
    }

    public List<Map<String, String>> getTableContent(Integer userID, BaseTableContext baseTableContext, int limit, int offset) {
        return tableMapper.selectTableData(createTableSqlBuilder(userID, baseTableContext), limit, offset);
    }

    @Transactional(rollbackFor = Exception.class)
    public int addTableData(String tableName, TableData tableData) {
        return tableMapper.insertTableData(tableName, tableData);
    }

    private void deleteTableUserMapping(Integer userID, BaseTableContext baseTableContext) {
        tableMapper.deleteUserTableMapping(userID, baseTableContext.getTableName());
    }

    private TableSqlBuilder createTableSqlBuilder(Integer userID, BaseTableContext baseTableContext) {
        return new TableSqlBuilder(userID, baseTableContext);
    }

}
