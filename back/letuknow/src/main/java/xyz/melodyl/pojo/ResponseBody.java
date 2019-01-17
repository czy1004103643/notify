package xyz.melodyl.pojo;

import com.alibaba.fastjson.JSON;

public class ResponseBody {
    private Object data;

    public ResponseBody() {
    }

    public ResponseBody(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}