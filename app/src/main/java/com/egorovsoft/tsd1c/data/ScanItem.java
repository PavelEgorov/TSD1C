package com.egorovsoft.tsd1c.data;

public class ScanItem {
    private int count;
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

    @Override
    public String toString() {
        return "ScanItem{" +
                "count=" + count +
                ", name='" + name + '\'' +
                ", scanCode='" + scanCode + '\'' +
                '}';
    }
}
