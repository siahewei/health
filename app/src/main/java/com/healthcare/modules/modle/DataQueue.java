package com.healthcare.modules.modle;

import com.healthcare.common.utils.TaskExcuteUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * project     healthcare
 *  缓冲器存入数据库的缓冲器
 *
 * @author hewei
 * @verstion 15/12/12
 */
public class DataQueue <T>{

    private List<T> dataList;
    private AbstractDao dao;
    private int size;


    public DataQueue(AbstractDao dao, int size) {
        this.dao = dao;
        this.size = size;
        dataList = new ArrayList<T>();
    }

    public void add(final T data){
        if (data != null){
            dataList.add(data);
        }

        // triger to save data
        if (dataList.size() == size){

            final List<T> tempDatas = new ArrayList<T>();
            tempDatas.addAll(dataList);

            TaskExcuteUtil.getInstance().excute(new Runnable() {
                @Override
                public void run() {
                    dao.insertInTx(tempDatas);
                }
            });

            dataList.clear();
        }
    }


}
