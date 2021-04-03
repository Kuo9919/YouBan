package com.example.poemzj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.poemzj.LoginActivity;
import com.example.poemzj.adapter.DBOpenHelper;
import com.example.poemzj.misc.Code;
import com.example.poemzj.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private String realCode;
    private DBOpenHelper mDBOpenHelper;
    private EditText mEtRegisterPhonecodes;
    private ImageView mIvRegisterShowcode;
    private Button mBtnRegisterOk;
    private EditText mEtRegisterName,mEtRegisterPd1,mEtRegisterPd2;
    private ImageView imageViewBack;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
       
        mIvRegisterShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }
    
    private void initView(){
        mEtRegisterPhonecodes = (EditText) findViewById(R.id.et_register_phonecodes);//验证码输入框
        mIvRegisterShowcode = (ImageView) findViewById(R.id.iv_register_showCode);//验证码图
        mBtnRegisterOk= (Button) findViewById(R.id.btn_register_ok);
        mEtRegisterName= (EditText) findViewById(R.id.et_register_name);
        mEtRegisterPd1= (EditText) findViewById(R.id.et_register_pd1);
        mEtRegisterPd2= (EditText) findViewById(R.id.et_register_pd2);
        imageViewBack= (ImageView) findViewById(R.id.iv_register_back);
        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        mBtnRegisterOk.setOnClickListener(this);
        mIvRegisterShowcode.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.iv_register_showCode:    //改变随机验证码的生成
                mIvRegisterShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.btn_register_ok:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisterName.getText().toString().trim();
                String password = mEtRegisterPd2.getText().toString().trim();
                String passwordt= mEtRegisterPd1.getText().toString().trim();
                String phoneCode = mEtRegisterPhonecodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode)&&!TextUtils.isEmpty(passwordt)) {
                    if (passwordt.equals(password)){
                        if (phoneCode.equals(realCode)) {
                            //将用户名和密码加入到数据库中
                            mDBOpenHelper.add(username, password);
                            Intent intent2 = new Intent(this, LoginActivity.class);
                            startActivity(intent2);
                            finish();
                            Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "验证信息错误,注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "密码不一致!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
