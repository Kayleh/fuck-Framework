package com.kayleh.config;

import java.util.List;

/**
 * @Author: Kayleh
 * @Date: 2021/4/28 0:35
 */
public class MapperBean {
    private String interfaceName; //接口名
    private List<Function> list; //接口下所有方法

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}
