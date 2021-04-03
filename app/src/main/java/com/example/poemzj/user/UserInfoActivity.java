package com.example.poemzj.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poemzj.LoginActivity;
import com.example.poemzj.R;
import com.example.poemzj.adapter.DBOpenHelper;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvUserName;
    DBOpenHelper dbOpenHelper;
    private TextView date;
    private EditText etUserName,etUserSign;
    private RadioGroup rgUserSex;
    private Button btn_save;
    
    User user=null;
    private String msex=null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        dbOpenHelper=new DBOpenHelper(this);
        ArrayList<User> data = dbOpenHelper.getAllData();
        user=data.get(LoginActivity.IDNUM);
        initInfo(user);
        
        
    }
    private void initView(){
        tvUserName= (TextView) findViewById(R.id.tv_userinfo_name);
        date= (TextView) findViewById(R.id.date_userinfo);
        etUserName= (EditText) findViewById(R.id.et_userinfo_name);
        etUserSign= (EditText) findViewById(R.id.et_userinfo_sign);
        rgUserSex= (RadioGroup) findViewById(R.id.rg_sex_userinfo);
        btn_save= (Button) findViewById(R.id.btn_userinfo_save);
        
        
        btn_save.setOnClickListener(this);
        rgUserSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rd_userinfo_man)
                    msex="男";
                else
                    msex="女";
            }
        });
    }
    
    private void initInfo(User user){
        tvUserName.setText(user.getName());
    }

    public void onGetDate(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserInfoActivity.this);
        View view1=getLayoutInflater().inflate(R.layout.layout_datepicker_dialog,null);
        final DatePicker datePicker= (DatePicker) view1.findViewById(R.id.date_picker);
        datePicker.setCalendarViewShown(false);
        builder.setView(view1);
        builder.setTitle("选择出生日期");
        builder.setPositiveButton(" 确认 ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year=datePicker.getYear();
                int month=datePicker.getMonth();
                int dayOfMonth=datePicker.getDayOfMonth();
                month++;
                date.setText(year+" - "+ month +" - "+dayOfMonth);
                dialogInterface.cancel();
        
            }
        });
        builder.setNegativeButton(" 取消 ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
        
    }
    public void saveDate(){
        user.setDate(date.getText().toString().trim());
        user.setName(etUserName.getText().toString().trim());
        user.setSex(msex);
        user.setSign(etUserSign.getText().toString().trim());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_userinfo_save:
                saveDate();
                finish();
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void onBack(View view){
        finish();
    }
}
