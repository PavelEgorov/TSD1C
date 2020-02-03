package com.egorovsoft.tsd1c.observers;

import com.egorovsoft.tsd1c.data.ScanItem;

import java.util.ArrayList;

public interface Observer {
   void updateList(ArrayList<ScanItem> item);
}
