package com.example.poemzj.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.poemzj.misc.Code;
import com.example.poemzj.R;

public class ForgetPWActivity extends AppCompatActivity {
    private ImageView mIvRegisteractivityShowcode;
    private String realCode;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);
    
        mIvRegisteractivityShowcode = (ImageView) findViewById(R.id.iv_registeractivity_showCode);
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    
    }
    public void onReset(View view){
    
    }
    public void onBack(View view){
        finish();
    }
    public void onVerify(View view){
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }
}