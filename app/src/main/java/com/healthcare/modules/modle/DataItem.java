package com.healthcare.modules.modle;

import java.util.List;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/8
 */
public class DataItem {

    public String tag;
    public int count;

    public DataItem(String tag, int count) {
        this.tag = tag;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public int getCount() {
        return count;
    }

    public static List<DataItem> loadDataItems(){
       List<DataItem> dataItems =  SqliteHelper.getInstance().getDataItemList();
       return dataItems;
    }
}
