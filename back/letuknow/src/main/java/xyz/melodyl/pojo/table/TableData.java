package xyz.melodyl.pojo.table;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class TableData {
    private String command;
    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
