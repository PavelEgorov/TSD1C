package com.egorovsoft.tsd1c.data;

import java.util.ArrayList;

public class ScanItems {
    private ArrayList<ScanItem> list;

    public ScanItems(){
        list = new ArrayList<>();
    }

    public ArrayList<ScanItem> getList() {
        return list;
    }

    public void setList(ArrayList<ScanItem> list) {
        this.list = list;
    }
}
