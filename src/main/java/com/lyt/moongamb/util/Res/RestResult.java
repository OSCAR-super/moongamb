package com.lyt.moongamb.util.Res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResult {
    private int code;
    private String msg;
    private Object data;

    @SuppressWarnings("unchecked")
    public void put(String key, Object data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        ((Map) this.data).put(key, data);
    }

    @SuppressWarnings("unchecked")
    public void putData(Object data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        ((Map) this.data).put("data", data);
    }

    @SuppressWarnings("unchecked")
    public void putTotal(Object total) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        ((Map) this.data).put("total", total);
    }
}
