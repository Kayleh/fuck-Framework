package com.kayleh.config;

/**
 * sql的类型、方法名、sql语句、返回类型和参数类型
 *
 * @Author: Kayleh
 * @Date: 2021/4/28 0:34
 */
public class Function {
    private String sqltype;
    private String funcName;
    private String sql;
    private Object resultType;
    private String parameterType;

    public String getSqltype() {
        return sqltype;
    }

    public void setSqltype(String sqltype) {
        this.sqltype = sqltype;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getResultType() {
        return resultType;
    }

    public void setResultType(Object resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}

