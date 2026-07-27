package com.lc.system.dto;

import lombok.Data;

/**
 * 分页请求基类。
 */
@Data
public class PageRequest {
    private int page = 1;
    private int size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }
}
