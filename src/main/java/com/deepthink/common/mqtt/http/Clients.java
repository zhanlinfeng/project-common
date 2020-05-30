package com.deepthink.common.mqtt.http;

import java.util.List;

/**
 * @author td
 */
public class Clients {

    private Integer currentPage;

    private Integer pageSize;

    private Integer totalSize;

    private Integer totalPage;

    private List<Client> objects;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<Client> getObjects() {
        return objects;
    }

    public void setObjects(List<Client> objects) {
        this.objects = objects;
    }
}
