package com.example.petadoption.common;

import java.util.List;

/**
 * 分页结果封装
 * 把 MyBatis-Plus 的分页对象转为前端更易读的格式
 *
 * @param <T> 数据类型
 */
public class PageResult<T> {

    /** 总记录数 */
    private long total;

    /** 当前页码 */
    private long current;

    /** 每页大小 */
    private long size;

    /** 数据列表 */
    private List<T> records;

    public PageResult() {}

    public PageResult(long total, long current, long size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.records = records;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public long getCurrent() { return current; }
    public void setCurrent(long current) { this.current = current; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }
}
