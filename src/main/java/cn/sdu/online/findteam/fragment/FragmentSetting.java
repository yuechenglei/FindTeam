/**   
 * @Title: FragmentSetting.java 
 * @Package com.example.shareseed 
 * @Description: TODO 
 * @author YBZ   
 * @date 2014-5-14 涓嬪崍10:58:59 
 * @version V1.0   
 */
package cn.sdu.online.findteam.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.sdu.online.findteam.R;



public class FragmentSetting extends Fragment implements OnClickListener {
	private LinearLayout llModifyPwd;
	private LinearLayout llCompleteInfo;
	private LinearLayout llLogout;
	private LinearLayout llUpdate;
	private LinearLayout llAbout;
	private LinearLayout llShareSoft;
	private SharedPreferences sp;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting, container, false);
//		sp = getActivity().getSharedPreferences("userInfo",
//				Context.MODE_WORLD_READABLE);
		FindViews();
		AddListeners();
		return view;
}

	private void FindViews() {
		llModifyPwd = (LinearLayout) view.findViewById(R.id.ll_modify_pwd);
		llCompleteInfo = (LinearLayout) view
				.findViewById(R.id.ll_complete_info);
		llLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
		llUpdate = (LinearLayout) view.findViewById(R.id.ll_update);
		llAbout = (LinearLayout) view.findViewById(R.id.ll_about);
		llShareSoft = (LinearLayout) view.findViewById(R.id.ll_shareSoft);
	}

	private void AddListeners() {
		llModifyPwd.setOnClickListener(this);
		llCompleteInfo.setOnClickListener(this);
		llLogout.setOnClickListener(this);
		llUpdate.setOnClickListener(this);
		llAbout.setOnClickListener(this);
		llShareSoft.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_complete_info: {
//			Intent intent = new Intent(getActivity(),
//					ActivityModifyUserinfo.class);
//			startActivity(intent);
			break;
		}
		case R.id.ll_modify_pwd: {
//			Intent intent = new Intent(getActivity(), ActivityModifyPwd.class);
//			startActivity(intent);
			break;
		}

		case R.id.ll_logout: {
//			SharedPreferences.Editor mEditor = sp.edit();
//			mEditor.putBoolean("auto_login", false);
//			mEditor.commit();
//			Intent i = new Intent(getActivity(), ActivityLogin.class);
//			startActivity(i);
//			getActivity().finish();
			break;
		}
		case R.id.ll_about: {
//			Intent intent = new Intent(getActivity(), ActivityAbout.class);
//			startActivity(intent);
//			break;
		}
		case R.id.ll_shareSoft: {
//			Intent intent = new Intent(Intent.ACTION_SEND);
//
//			intent.setType("text/plain");
//			intent.putExtra(Intent.EXTRA_SUBJECT, "");
//			intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share));
//			startActivity(Intent.createChooser(intent, getTag()));
			break;
		}
		}
	}

//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onPageStart("Setting"); // 统计页面
//	}
//
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd("Setting");
//	}
}
