package com.mobike.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.mobike.library.MyView;

public class Main2Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    final MyView myView = findViewById(R.id.myview);
    findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        myView.moveLeftHand(-1);
      }
    });
    findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        myView.moveLeftHand(1);
      }
    });
    findViewById(R.id.btn3).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        myView.moveRightHand(-1);
      }
    });
    findViewById(R.id.btn4).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        myView.moveRightHand(1);
      }
    });
  }
}
