package com.mobike.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mobike.library.Ball;
import com.mobike.library.Mobike;
import com.mobike.library.MobikeView;

public class MainActivity extends AppCompatActivity {

    private MobikeView mobikeView;
    private Button btnTest, btnTest2, btnLeft1, btnLeft2, btnRight1, btnRight2;
    private  int[] imgs = {
            R.drawable.share_fb,
            R.drawable.share_kongjian,
            R.drawable.share_pyq,
            R.drawable.share_qq,
            R.drawable.share_tw,
            R.drawable.share_wechat,
            R.drawable.share_weibo,
            R.drawable.shape,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobikeView = findViewById(R.id.mobike_view);
        btnTest = findViewById(R.id.test);
        btnTest2 = findViewById(R.id.test2);
        btnLeft1 = findViewById(R.id.left1);
        btnLeft2 = findViewById(R.id.left2);
        btnRight1 = findViewById(R.id.right1);
        btnRight2 = findViewById(R.id.right2);

        initViews();

        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mobikeView.getmMobike().random();
            }
        });
        btnTest2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Mobike mobike = mobikeView.getmMobike();
                boolean enable = mobike.getEnable();
                btnTest2.setText(enable ? "开启" : "停止");
                mobike.setEnable(!enable);
            }
        });
        btnLeft1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobikeView.getBallList().get(mobikeView.getBallList().size() - 1).setAddLeft(10);
            }
        });
        btnLeft2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobikeView.getBallList().get(mobikeView.getBallList().size() - 1).setAddLeft(-10);
            }
        });
        btnRight1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobikeView.getBallList().get(mobikeView.getBallList().size() - 1).setAddRight(10);
            }
        });
        btnRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobikeView.getBallList().get(mobikeView.getBallList().size() - 1).setAddRight(-10);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mobikeView.getmMobike().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mobikeView.getmMobike().onStop();
    }

    private void initViews() {
        for(int i = 0; i < imgs.length  ; i ++){
            if(i == imgs.length - 1){
                mobikeView.addBall(new Ball(this, 300, 400, imgs[i], false));
            }else{
                mobikeView.addBall(new Ball(this, 100, 200, imgs[i], true));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 110 && data != null){
            float density = data.getFloatExtra("density",-1);
            float friction = data.getFloatExtra("friction",-1);
            float restitution = data.getFloatExtra("restitution",-1);
            float ratio = data.getFloatExtra("ratio",-1);
            mobikeView.getmMobike().setDensity(density);
            mobikeView.getmMobike().setFriction(friction);
            mobikeView.getmMobike().setRestitution(restitution);
            mobikeView.getmMobike().setRatio(ratio);
            mobikeView.getmMobike().update();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editParams){
            Intent intent = new Intent(this, ParamsActivity.class);
            intent.putExtra("density",mobikeView.getmMobike().getDensity());
            intent.putExtra("friction",mobikeView.getmMobike().getFriction());
            intent.putExtra("restitution",mobikeView.getmMobike().getRestitution());
            intent.putExtra("ratio",mobikeView.getmMobike().getRatio());
            startActivityForResult(intent,110);
        }else if(item.getItemId() == R.id.enable){

        }else if(item.getItemId() == R.id.mobikedemo){
            startActivity(new Intent(this,MobikeDemo.class));
        }else if(item.getItemId() == R.id.about){
            startActivity(new Intent(this,AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
