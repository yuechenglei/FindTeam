package cn.sdu.online.findteam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.view.GestureImageView;

public class ImgShowerActivity extends Activity {

    private GestureImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imageshower);
        imageView = (GestureImageView) findViewById(R.id.show_bigimg);

        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.zoomout);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.zoomout);
        super.onBackPressed();
    }
}


