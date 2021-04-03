package com.example.poemzj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.poemzj.msg.MsgAdapter;
import com.example.poemzj.msg.Msg;
import com.example.poemzj.adapter.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;

    private ListView listView = null;
    private List<Msg> list = new ArrayList<Msg>();
    private Button sendBtn = null;
    private EditText input = null;
    private MsgAdapter msgAdapter = null;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        initMsgs();
        initView();

//        dbOpenHelper=new DBOpenHelper(this);
//        ArrayList<User> data=dbOpenHelper.getAllData();
//        User user=data.get(LoginActivity.IDNUM);
//        tv_title.setText(user.getName());

    }
    private void initView(){
        tv_title= (TextView) findViewById(R.id.tv_title_chat);

        listView = (ListView) findViewById(R.id.list_view);
        msgAdapter = new MsgAdapter(ChatActivity.this, R.layout.msg_item, list);
        sendBtn = (Button) findViewById(R.id.btn_send);
        input = (EditText) findViewById(R.id.edit_send);

        listView.setAdapter(msgAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = input.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    list.add(msg);
                    msgAdapter.notifyDataSetChanged(); // 刷新界面
                    listView.setSelection(list.size()); // 把发送的信息定位到最后一行
                    input.setText(""); // 发送框置空
                }
            }
        });
    }
    private void initMsgs() {
        Msg msg1 = new Msg("很高兴见到你，我叫Nicy", Msg.TYPE_RECE);
        list.add(msg1);
        Msg msg2 = new Msg("哦？ 你好， 我叫Tony", Msg.TYPE_SEND);
        list.add(msg2);
        Msg msg3 = new Msg("哈哈，请多指教！ ", Msg.TYPE_RECE);
        list.add(msg3);
    }
    public void onBack(View view){
        finish();
    }
}