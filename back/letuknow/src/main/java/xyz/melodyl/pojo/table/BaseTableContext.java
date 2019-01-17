package xyz.melodyl.pojo.table;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public abstract class BaseTableContext {
    private static final String TABLE_SUFFIX = "dynamic";

    private Integer id;
    private String tableName;
    private String description;
    private String command;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    protected ColumnContext[] columnContexts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ColumnContext[] getColumns() {
        return columnContexts;
    }

    public String buildRealTableName(Integer userID) {
        return TABLE_SUFFIX + userID + "_" + getTableName();
    }

    public static boolean noIDInColumns(BaseTableContext baseTableContext) {
        boolean legality = true;

        for (ColumnContext columnContext : baseTableContext.getColumns()) {
            if (columnContext.getName().equals("id")) {
                legality = false;
                break;
            }
        }

        return legality;
    }
}
