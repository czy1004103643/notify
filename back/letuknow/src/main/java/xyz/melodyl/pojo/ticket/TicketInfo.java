package xyz.melodyl.pojo.ticket;

public class TicketInfo {
    private Integer userID;
    private String tableName;

    public TicketInfo() {
    }

    public TicketInfo(Integer userID, String tableName) {
        this.userID = userID;
        this.tableName = tableName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
