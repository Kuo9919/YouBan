package com.example.poemzj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poemzj.adapter.DBOpenHelper;
import com.example.poemzj.user.ForgetPWActivity;
import com.example.poemzj.user.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private DBOpenHelper dbOpenHelper;
    private EditText etName,etPd;
    private Button btnLogin;
    private TextView tvForget,tvRegister;
    public static int IDNUM;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initView();
        dbOpenHelper=new DBOpenHelper(this);
    }
    
    private void initView(){
        etName= (EditText) findViewById(R.id.login_et_username);
        etPd= (EditText) findViewById(R.id.login_et_userpd);
        btnLogin= (Button) findViewById(R.id.login_button_ok);
        tvForget= (TextView) findViewById(R.id.login_click_forget);
        tvRegister= (TextView) findViewById(R.id.login_click_register);
        
        btnLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_click_forget:
                startActivity(new Intent(this, ForgetPWActivity.class));
                break;
            case R.id.login_click_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_button_ok:
                String name = etName.getText().toString().trim();
                String password = etPd.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = dbOpenHelper.getAllData();
                    boolean match = false;
                    for (IDNUM = 0; IDNUM < data.size(); IDNUM++) {
                        User user = data.get(IDNUM);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, SelfActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        
    }
}
