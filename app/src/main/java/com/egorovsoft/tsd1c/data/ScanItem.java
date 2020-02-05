package com.egorovsoft.tsd1c.data;

public class ScanItem {
    private int systemCount;
    private int count;
    private String id;
    private String name;
    private String scanCode;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public int getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(int systemCount) {
        this.systemCount = systemCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ScanItem{" +
                "count=" + count +
                ", name='" + name + '\'' +
                ", scanCode='" + scanCode + '\'' +
                ", systemCount='" + systemCount + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
