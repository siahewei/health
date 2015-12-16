package com.healthcare.modules.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.healthcare.R;
import com.healthcare.common.base.BaseListAdapter;
import com.healthcare.common.base.BaseViewHolder;
import com.healthcare.modules.modle.DataItem;

import java.util.List;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/8
 */
public class DataAdapter extends BaseListAdapter<DataItem>{
    public DataAdapter(List<DataItem> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void setItem(BaseViewHolder viewHolder, int postion) {

        if (viewHolder != null && viewHolder instanceof ViewHolder){

            DataItem dataItem = dataList.get(postion);
            if (dataItem != null){
                ((ViewHolder) viewHolder).tvCount.setText(dataItem.getCount() + "");
                ((ViewHolder) viewHolder).tvTagName.setText(dataItem.getTag());
            }
        }
    }

    class ViewHolder extends BaseViewHolder{

        public TextView tvTagName;
        public TextView tvCount;

        @Override
        public View getView() {
            if (context != null && view == null){
                view = LayoutInflater.from(context).inflate(R.layout.item_adapter, null);

                tvCount = (TextView) view.findViewById(R.id.tv_count);
                tvTagName = (TextView) view.findViewById(R.id.tv_tag);
            }
            return view;
        }
    }

}
