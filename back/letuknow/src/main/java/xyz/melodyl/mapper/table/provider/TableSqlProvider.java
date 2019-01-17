package xyz.melodyl.mapper.table.provider;

import xyz.melodyl.pojo.table.TableSqlBuilder;
import xyz.melodyl.pojo.table.TableData;

import java.util.Map;

import static xyz.melodyl.pojo.table.TableSqlBuilder.*;

public class TableSqlProvider {

    public String createTable(TableSqlBuilder tableSqlBuilder) {
        return tableSqlBuilder.getCreateSql();
    }

    public String deleteTable(TableSqlBuilder tableSqlBuilder) {
        return tableSqlBuilder.getDropSql();
    }

    public String selectTable(TableSqlBuilder tableSqlBuilder, int limit, int offset) {
        return tableSqlBuilder.getSelectAllSql() +
                tableSqlBuilder.getAscOrderedSql() +
                mysqlPagingSql(limit, offset);
    }

    public String insertTable(String tableName, TableData tableData) {
        StringBuilder insertSqlMeta = new StringBuilder("INSERT INTO " + tableName + " ( ");
        StringBuilder insertValueSqlMeta = new StringBuilder(" VALUES (");

        for (Map.Entry<String, String> entry : tableData.getData().entrySet()) {
            insertSqlMeta.append(tableAttributePolish(entry.getKey()) + ",");
            insertValueSqlMeta.append(tableContentPolish(entry.getValue().toString()) + ",");
        }

        removeLastCharacter(insertSqlMeta);
        removeLastCharacter(insertValueSqlMeta);

        insertSqlMeta.append(")").append(insertValueSqlMeta).append(")");
        return insertSqlMeta.toString();
    }
}
