package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.fragment.BuildTeamFragment;

public class EditTeamSettingActivity extends BaseFragmentActivity implements View.OnClickListener{

    private BuildTeamFragment buildTeamFragment;
    Button back_Btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.editteamsetting_actionbar);
        setContentView(R.layout.editteamsetting_layout);
        buildTeamFragment = new BuildTeamFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.editactivity_container,
                buildTeamFragment).commit();

        back_Btn = (Button) findViewById(R.id.editteamsetting_back_btn);
        back_Btn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BuildTeamFragment.PHOTO_REQUEST:// 相册返回
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        buildTeamFragment.setHeaderImgAlbum();
                        break;
                }
                break;

            case BuildTeamFragment.CAMERA_REQUEST:// 照相返回

                switch (resultCode) {
                    case Activity.RESULT_OK:// 照相完成点击确定
                        buildTeamFragment.getHeaderImgCamera();
                        break;

                    case Activity.RESULT_CANCELED:// 取消
                        break;
                }
                break;

            case BuildTeamFragment.CAMERA_CUT_REQUEST:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        buildTeamFragment.setHeaderImgCamera(data);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editteamsetting_back_btn:
                EditTeamSettingActivity.this.finish();
                break;

            default:
                break;
        }
    }
}
