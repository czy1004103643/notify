package xyz.melodyl.pojo.table;

//sql拼接中间部分，采用前部分空一格，结尾处不空格
//limit 仅适用于mysql
public class TableSqlBuilder {
    private static final int SQL_STR_INITIAL_SIZE = 256;
    private static final String PRIMARY_KEY_NAME = "id";

    private BaseTableContext baseTableContext;
    private Integer userID;

    public TableSqlBuilder(Integer userID, BaseTableContext baseTableContext) {
        this.baseTableContext = baseTableContext;
        this.userID = userID;
    }

    public String getCreateSql() {
        StringBuilder basicSqlStatement = new StringBuilder(SQL_STR_INITIAL_SIZE);
        basicSqlStatement.append("CREATE TABLE " + tableAttributePolish(getTableName()) + " ( ")
                .append(buildAutoIncrementIDSqlStr())
                .append(buildCreateTableColumnsSql())
                .append("PRIMARY KEY (" + tableAttributePolish(PRIMARY_KEY_NAME) + "))");

        return basicSqlStatement.toString();
    }

    public String getDropSql(){
        return "DROP TABLE " + getTableName();
    }

    public String getTableName() {
        return baseTableContext.buildRealTableName(userID);
    }

    public String getSelectAllSql() {
        String columns = PRIMARY_KEY_NAME + "," + buildSelectColumnStr();
        return "SELECT " + columns + " FROM " + getTableName();
    }

    public String getAscOrderedSql() {
        return " ORDER BY '" + PRIMARY_KEY_NAME + "' ASC";
    }

    private String buildAutoIncrementIDSqlStr() {
        return tableAttributePolish(PRIMARY_KEY_NAME) + " int UNSIGNED NOT NULL AUTO_INCREMENT,";
    }

    private String buildCreateTableColumnsSql() {
        StringBuilder tempStr = new StringBuilder(SQL_STR_INITIAL_SIZE);
        ColumnContext[] columnContexts = baseTableContext.getColumns();

        for (ColumnContext content : columnContexts) {
            tempStr.append(buildCreateColumnStr(content));
        }

        return tempStr.toString();
    }

    private String buildCreateColumnStr(ColumnContext content) {
        return content.getName() + " " + content.getType() +
                "(" + content.getLength() + ") NOT NULL, ";
    }

    private String buildSelectColumnStr() {
        StringBuilder temp = new StringBuilder(SQL_STR_INITIAL_SIZE);

        for (ColumnContext columnContext : baseTableContext.getColumns()) {
            temp.append(columnContext.getName() + ",");
        }
        removeLastCharacter(temp);

        return temp.toString();
    }

    public static String mysqlPagingSql(int limit, int offset) {
        return " LIMIT " + limit +
                " OFFSET " + offset;
    }

    public static void removeLastCharacter(StringBuilder str) {
        int length = str.length();
        str.replace(length - 1, length, "");
    }
    // 数据库、表、索引、列和别名用的是引用符是反勾号(‘`’)
    // 字符串 用的是 单引号( ' )或双引号
    public static String tableAttributePolish(String tableAttribute) {
        return "`" + tableAttribute + "`";
    }

    public static String tableContentPolish(String tableData) {
        return "'" + tableData + "'";
    }
}
