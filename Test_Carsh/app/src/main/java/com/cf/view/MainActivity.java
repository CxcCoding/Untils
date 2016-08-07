package com.cf.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cf.untils.ExitAppUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitAppUtils.getInstance().addActivity(this);
        bt= (Button) findViewById(R.id.button1);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,TwoActivity.class);
                startActivity(intent);
                break;
        }

    }
}
