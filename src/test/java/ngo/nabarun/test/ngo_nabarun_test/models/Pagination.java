package ngo.nabarun.test.ngo_nabarun_test.models;

import java.util.List;

import lombok.Data;

@Data
public class Pagination<T> {
    private int pageIndex;
    private Integer pageSize;
    private int totalSize;
    private int currentSize;
    private int totalPages;
    private List<T> content;
    private int nextPageIndex;
    private int prevPageIndex;
}