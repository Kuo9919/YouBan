package com.example.poemzj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();

    }
    private void initView(){
        btn_search= (Button) findViewById(R.id.btn_search_add);
        btn_search.setOnClickListener(this);
    }

    public void onBack(View view){
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search_add:
                Toast.makeText(this,"正在查找好友...",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}