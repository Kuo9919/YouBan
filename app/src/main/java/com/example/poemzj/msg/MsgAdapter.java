package com.example.poemzj.msg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.poemzj.msg.Msg;
import com.example.poemzj.R;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {
    
    private int resourceId;
    
    public MsgAdapter(Context context, int resource, List<Msg> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view
                    .findViewById(R.id.msg_left);
            viewHolder.rightLayout = (LinearLayout) view
                    .findViewById(R.id.msg_right);
            viewHolder.leftTextView = (TextView) view
                    .findViewById(R.id.left_tv);
            viewHolder.rightTextView = (TextView) view
                    .findViewById(R.id.right_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        
        if (msg.getType() == Msg.TYPE_RECE) {   //如果是接收信息
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftTextView.setText(msg.getContent());
        } else {  // 如果是发送信息
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightTextView.setText(msg.getContent());
        }
        return view;
    }
    
    private class ViewHolder {
        
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        
        TextView leftTextView;
        TextView rightTextView;
        
    }
    
}
