package com.tbook.Utils;

//提示信息和判断操作是否执行正确
public class Tips {
    private String code;
    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Tips{" +
                "code=" + code +
                ", info='" + info + '\'' +
                '}';
    }
}
