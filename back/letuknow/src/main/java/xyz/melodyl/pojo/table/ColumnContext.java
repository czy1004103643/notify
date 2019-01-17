package xyz.melodyl.pojo.table;

import java.sql.JDBCType;

public class ColumnContext {
    private static final int COLUMN_DEFAULT_LENGTH = 255;

    private String name;
    private String type;
    private Integer length;

    public static ColumnContext createDefault(String name) {
        return new ColumnContext(name, JDBCType.VARCHAR.getName(),COLUMN_DEFAULT_LENGTH);
    }

    public ColumnContext(){}

    public ColumnContext(String name, String type, Integer length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
