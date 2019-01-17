package xyz.melodyl.pojo.table;

public class SimpleTableContext extends BaseTableContext {
    private static final String SIMPLE_TABLE_SEPARATOR = ",";

    public void setColumnsStr(String columnsStr) {
        String[] columnNameList = columnsStr.split(SIMPLE_TABLE_SEPARATOR);
        int columnLength = columnNameList.length;

        columnContexts = new ColumnContext[columnLength];
        for (int i = 0; i < columnLength; i++) {
            columnContexts[i] = ColumnContext.createDefault(columnNameList[i]);
        }
    }
}
