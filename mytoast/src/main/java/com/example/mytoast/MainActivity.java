package com.example.mytoast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeadSetUtil.getInstance().setOnHeadSetListener(headSetListener);
        HeadSetUtil.getInstance().open(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeadSetUtil.getInstance().close(this);
    }
    HeadSetUtil.OnHeadSetListener headSetListener = new HeadSetUtil.OnHeadSetListener() {
        @Override
        public void onDoubleClick() {
            Toast.makeText(MainActivity.this,"123",Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "双击");
        }
        @Override
        public void onClick() {
            Toast.makeText(MainActivity.this,"1234",Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "单击");
        }
        @Override
        public void onThreeClick() {
            Toast.makeText(MainActivity.this,"1235",Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "三连击");
        }
    };
}
