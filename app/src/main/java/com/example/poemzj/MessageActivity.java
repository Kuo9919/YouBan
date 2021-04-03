package com.example.poemzj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poemzj.msg.MessageBean;
import com.example.poemzj.msg.MessageUtils;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private long firstTime = 0;
    
    private Button btn_message,btn_discover,btn_self;
    private ImageButton ibtn_add;
    private ArrayList<MessageBean> mlist;
    private ListView lvMes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        mlist= MessageUtils.getAllMes(this);
        lvMes.setAdapter(new MesAdapter());

        
    }
    private void initView(){
        lvMes = (ListView) findViewById(R.id.lv_mes);
        lvMes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        lvMes.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,0,"置顶");
                contextMenu.add(0,1,1,"隐藏");
                contextMenu.add(0,2,2,"删除");
            }


        });

        btn_discover= (Button) findViewById(R.id.btn_todiscover_message);
        btn_message= (Button) findViewById(R.id.btn_tomessage_message);
        btn_self= (Button) findViewById(R.id.btn_toself_message);
        btn_self.setOnClickListener(this);
        btn_message.setOnClickListener(this);
        btn_discover.setOnClickListener(this);
        ibtn_add= (ImageButton) findViewById(R.id.ibtn_addmenu_message);
        ibtn_add.setOnClickListener(this);
    }

    //设置菜单内容和事件
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //获取点击的item的id
        String id = String.valueOf(info.id);
        switch(item.getItemId()){
            case 0:
                Toast.makeText(this, "这是消息框置顶按钮", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "这是消息框隐藏按钮", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "这是消息框删除按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class MesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int i) {
            return mlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view==null){
                holder=new ViewHolder();
                view=View.inflate(getApplicationContext(),R.layout.item_message,null);
                holder.tv_title= (TextView) view.findViewById(R.id.tv_title);
                holder.tv_content= (TextView) view.findViewById(R.id.tv_description);
                holder.iv_icon= (ImageView) view.findViewById(R.id.siv_icon);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            MessageBean item= (MessageBean) getItem(i);
            holder.tv_title.setText(item.title);
            holder.tv_content.setText(item.content);
            holder.iv_icon.setImageDrawable(item.icon);
            return view;
        }
        private class ViewHolder{
            TextView tv_title;
            TextView tv_content;
            ImageView iv_icon;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn_tomessage_message:
//                intent = new Intent(MessageActivity.this, MessageActivity.class);
//                MessageActivity.this.startActivity(intent);
//                finish();
                break;
            case R.id.btn_todiscover_message:
                intent = new Intent(MessageActivity.this, DiscoverActivity.class);
                MessageActivity.this.startActivity(intent);
                break;
            case R.id.btn_toself_message:
                intent = new Intent(MessageActivity.this, SelfActivity.class);
                MessageActivity.this.startActivity(intent);
                finish();
                break;
            case R.id.ibtn_addmenu_message:
                intent = new Intent(MessageActivity.this, AddActivity.class);
                MessageActivity.this.startActivity(intent);
            default:
                break;
            
        }
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MessageActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        }
        else {
            finish();
        }
    }
}