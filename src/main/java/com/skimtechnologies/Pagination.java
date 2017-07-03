package com.skimtechnologies;

public class Pagination {
    private int limit;
    private int total;
    private long startTime;
    private long endTime;
    private String next;

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getNext() {
        return next;
    }
}
