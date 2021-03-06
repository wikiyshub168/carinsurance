package com.paulz.carinsurance.ui;


import com.paulz.carinsurance.HApplication;
import com.paulz.carinsurance.R;
import com.paulz.carinsurance.base.BaseFragmentActivity;
import com.paulz.carinsurance.common.GlobeFlags;
import com.paulz.carinsurance.common.IActions;
import com.paulz.carinsurance.ui.fragment.ActiveFragment;
import com.paulz.carinsurance.ui.fragment.CustomerFragment;
import com.paulz.carinsurance.ui.fragment.HomeFragment;
import com.paulz.carinsurance.ui.fragment.UserCenterFragment;
import com.paulz.carinsurance.update.UpdateUtil;
import com.paulz.carinsurance.utils.AppUtil;
import com.core.framework.app.MyApplication;
import com.core.framework.image.image13.Image13lLoader;
import com.core.framework.store.sharePer.PreferencesUtils;
import com.umeng.socialize.UMShareAPI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 首页
 *
 * @author paul'z
 *
 */
@RuntimePermissions
public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {
	private TextView titleTv;
	private TextView leftTv;
	private ImageView rightIv;
	private ImageView ivTab1Pic;
	private ImageView ivTab2Pic;
	private ImageView ivTab3Pic;
//	private ImageView ivTab4Pic;

	private LinearLayout layoutTab1;
	private LinearLayout layoutTab2;
	private LinearLayout layoutTab3;
//	private LinearLayout layoutTab4;
	private LinearLayout layoutTab1Selected;
	private LinearLayout layoutTab2Selected;
	private LinearLayout layoutTab3Selected;
//	private LinearLayout layoutTab4Selected;

	private FragmentTransaction fragmentTransaction;

	private boolean isFirst = true;

	public static final String TAB1 = "TAB1";
	public static final String TAB2 = "TAB2";
	public static final String TAB3 = "TAB3";
//	public static final String TAB4 = "TAB4";

	private String tag = TAB1;
	public static boolean isBackHome = false;

	private Handler mHandler = new Handler();
	private int mClickCount = 0;
//	UMShareAPI mShareAPI;


	private boolean isReInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(savedInstanceState!=null){
			tag=savedInstanceState.getString("cur_tag",TAB1);
			isReInstance=true;
		}
//		mShareAPI = UMShareAPI.get(this);
		initView();
		checkUpdate();

//		if (!PreferencesUtils.getBoolean(AppStatic.receiveMsg)) {
//			JPushInterface.stopPush(getApplicationContext());
//		}
		if(isReInstance){
			switchToFragment(tag);
			changeViewBackground(tag);
			isReInstance=false;
		}
		MainActivityPermissionsDispatcher.showPermissionsWithCheck(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent!=null){
			if(intent.getBooleanExtra("is_expired",false)){
				HApplication.getInstance().refreshSessionid();
				HApplication.getInstance().loadToken();
				UserLoginActivity.invoke(this);
				finish();
			}else {
				handleAction(intent);
			}
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Image13lLoader.getInstance().clearMemoryCache();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isBackHome) {
			tag = TAB1;
			switchToFragment(tag);
			changeViewBackground(tag);
			isBackHome = false;

		}
		// String tag1 = getIntent().getStringExtra("tag");
		// if (!TextUtils.isEmpty(tag1)) {
		// tag = tag1;
		// switchToFragment(tag);
		// changeViewBackground(tag);
		// }
	}

	/*
	 * private void initView() { layoutTab1 = (LinearLayout)
	 * findViewById(R.id.tab_main_tab1); layoutTab1.setOnClickListener(this);
	 * layoutTab2 = (LinearLayout) findViewById(R.id.tab_main_tab2);
	 * layoutTab2.setOnClickListener(this); layoutTab3 = (LinearLayout)
	 * findViewById(R.id.tab_main_tab3); layoutTab3.setOnClickListener(this);
	 * layoutTab4 = (LinearLayout) findViewById(R.id.tab_main_tab4);
	 * layoutTab4.setOnClickListener(this); titleTv = (TextView)
	 * findViewById(R.id.baseTitle_milddleTv); ivTab1Pic = (ImageView)
	 * findViewById(R.id.iv_main_tab1); tvTab1 = (TextView)
	 * findViewById(R.id.tv_main_tab1); ivTab2Pic = (ImageView)
	 * findViewById(R.id.iv_main_tab2); tvTab2 = (TextView)
	 * findViewById(R.id.tv_main_tab2); ivTab3Pic = (ImageView)
	 * findViewById(R.id.iv_main_tab3); tvTab3 = (TextView)
	 * findViewById(R.id.tv_main_tab3); ivTab4Pic = (ImageView)
	 * findViewById(R.id.iv_main_tab4); tvTab4 = (TextView)
	 * findViewById(R.id.tv_main_tab4);
	 *
	 * leftTv = (TextView) findViewById(R.id.baseTitle_leftTv); rightIv =
	 * (ImageView) findViewById(R.id.baseTitle_rightIv); //
	 * rightIv.setOnClickListener(rightListener); initFragment(); }
	 */

	private void initView() {
		layoutTab1 = (LinearLayout) findViewById(R.id.tab_main_tab1);
		layoutTab1.setOnClickListener(this);
		layoutTab2 = (LinearLayout) findViewById(R.id.tab_main_tab2);
		layoutTab2.setOnClickListener(this);
		layoutTab3 = (LinearLayout) findViewById(R.id.tab_main_tab3);
		layoutTab3.setOnClickListener(this);
//		layoutTab4 = (LinearLayout) findViewById(R.id.tab_main_tab4);
//		layoutTab4.setOnClickListener(this);

		layoutTab1Selected = (LinearLayout) findViewById(R.id.tab_main_tab1_selected);
		layoutTab2Selected = (LinearLayout) findViewById(R.id.tab_main_tab2_selected);
		layoutTab3Selected = (LinearLayout) findViewById(R.id.tab_main_tab3_selected);
//		layoutTab4Selected = (LinearLayout) findViewById(R.id.tab_main_tab4_selected);

		titleTv = (TextView) findViewById(R.id.baseTitle_milddleTv);
		ivTab1Pic = (ImageView) findViewById(R.id.iv_main_tab1);
		ivTab2Pic = (ImageView) findViewById(R.id.iv_main_tab2);
		ivTab3Pic = (ImageView) findViewById(R.id.iv_main_tab3);
//		ivTab4Pic = (ImageView) findViewById(R.id.iv_main_tab4);

		leftTv = (TextView) findViewById(R.id.baseTitle_leftTv);
		rightIv = (ImageView) findViewById(R.id.baseTitle_rightIv);
		// rightIv.setOnClickListener(rightListener);
		initFragment();

	}

	private void initFragment() {
		if (isFirst) {
			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			currentFrag = new HomeFragment();
			fragmentTransaction.add(R.id.main_fLayout, currentFrag, TAB1)
					.commit();
			isFirst = false;
			// changeViewBackground(HOME);
		}
	}

	Fragment currentFrag = null;

	/**
	 * fragment跳转
	 *
	 * @param Tag
	 */
	public void switchToFragment(String Tag) {
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Fragment findresult = null;
		findresult = getSupportFragmentManager().findFragmentByTag(Tag);
		if (currentFrag != null && currentFrag.getTag().equals(Tag)) {
			// 判断为相同fragment不切换
		} else {
			if (findresult != null) {
				fragmentTransaction.hide(currentFrag).show(findresult);
				adjustFragmentHindState(currentFrag.getTag(),Tag);
			} else {
				if (Tag.equals(TAB1)) {
					findresult = new HomeFragment();
				} else if (Tag.equals(TAB2)) {
					findresult = new CustomerFragment();
				} else if (Tag.equals(TAB3)) {
					findresult = new UserCenterFragment();
				}

				fragmentTransaction.hide(currentFrag)
						.add(R.id.main_fLayout, findresult, Tag).commit();
			}
		}
		currentFrag = findresult;
	}

	public void adjustFragmentHindState(String currentTab,String newTab){
		FragmentManager fm=getSupportFragmentManager();
		if(!currentTab.equals(TAB1)&&!newTab.equals(TAB1)){
			Fragment fg=fm.findFragmentByTag(TAB1);
			if(fg!=null){
				fragmentTransaction.hide(fg);
			}
		}
		if(!currentTab.equals(TAB2)&&!newTab.equals(TAB2)){
			Fragment fg=fm.findFragmentByTag(TAB2);
			if(fg!=null){
				fragmentTransaction.hide(fg);
			}
		}
		if(!currentTab.equals(TAB3)&&!newTab.equals(TAB3)){
			Fragment fg=fm.findFragmentByTag(TAB3);
			if(fg!=null){
				fragmentTransaction.hide(fg);
			}
		}

		fragmentTransaction.commit();
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// goodBusNumTv.setText(String.valueOf(AppStatic.goodBusNum));
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * public void changeViewBackground(String Tag) { if (Tag.equals(TAB1)) {
	 * ivTab1Pic.setImageResource(R.drawable.home2);
	 * ivTab2Pic.setImageResource(R.drawable.fashion);
	 * ivTab3Pic.setImageResource(R.drawable.classfiy);
	 * ivTab4Pic.setImageResource(R.drawable.user3);
	 *
	 * layoutTab1.setBackgroundResource(R.color.menu_press);
	 * layoutTab2.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab3.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab4.setBackgroundResource(R.color.menu_nomal);
	 *
	 * } else if (Tag.equals(TAB2)) {
	 * ivTab1Pic.setImageResource(R.drawable.home);
	 * ivTab2Pic.setImageResource(R.drawable.fashion2);
	 * ivTab3Pic.setImageResource(R.drawable.classfiy);
	 * ivTab4Pic.setImageResource(R.drawable.user3);
	 *
	 * layoutTab1.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab2.setBackgroundResource(R.color.menu_press);
	 * layoutTab3.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab4.setBackgroundResource(R.color.menu_nomal);
	 *
	 * } else if (Tag.equals(TAB3)) { //
	 * ivTab1Pic.setImageResource(R.drawable.home); //
	 * tvTab1.setTextColor(getResources().getColor(R.color.gray)); //
	 * ivTab2Pic.setImageResource(R.drawable.fashion); //
	 * tvTab2.setTextColor(getResources().getColor(R.color.gray)); //
	 * ivTab3Pic.setImageResource(R.drawable.classfiy2); //
	 * tvTab3.setTextColor(getResources().getColor(R.color.white)); //
	 * ivTab4Pic.setImageResource(R.drawable.user3); //
	 * tvTab4.setTextColor(getResources().getColor(R.color.gray)); // //
	 * layoutTab1.setBackgroundResource(R.color.menu_nomal); //
	 * layoutTab2.setBackgroundResource(R.color.menu_nomal); //
	 * layoutTab3.setBackgroundResource(R.color.menu_press); //
	 * layoutTab4.setBackgroundResource(R.color.menu_nomal);
	 *
	 *
	 * } else if (Tag.equals(TAB4)) {
	 * ivTab1Pic.setImageResource(R.drawable.home);
	 * ivTab2Pic.setImageResource(R.drawable.fashion);
	 * ivTab3Pic.setImageResource(R.drawable.classfiy);
	 * ivTab4Pic.setImageResource(R.drawable.user32);
	 *
	 * layoutTab1.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab2.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab3.setBackgroundResource(R.color.menu_nomal);
	 * layoutTab4.setBackgroundResource(R.color.menu_press); } }
	 */

	public void changeViewBackground(String Tag) {
		if (Tag.equals(TAB1)) {
			layoutTab1Selected.setVisibility(View.VISIBLE);
			layoutTab2Selected.setVisibility(View.INVISIBLE);
			layoutTab3Selected.setVisibility(View.INVISIBLE);
//			layoutTab4Selected.setVisibility(View.INVISIBLE);

		} else if (Tag.equals(TAB2)) {
			layoutTab1Selected.setVisibility(View.INVISIBLE);
			layoutTab2Selected.setVisibility(View.VISIBLE);
			layoutTab3Selected.setVisibility(View.INVISIBLE);
//			layoutTab4Selected.setVisibility(View.INVISIBLE);

		} else if (Tag.equals(TAB3)) {
			layoutTab1Selected.setVisibility(View.INVISIBLE);
			layoutTab2Selected.setVisibility(View.INVISIBLE);
			layoutTab3Selected.setVisibility(View.VISIBLE);
//			layoutTab4Selected.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
			case R.id.tab_main_tab1:
				tag = TAB1;
				break;
			case R.id.tab_main_tab2:
				tag = TAB2;
				break;
			case R.id.tab_main_tab3:
				tag = TAB3;
				break;

		}
		switchToFragment(tag);
		changeViewBackground(tag);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (mClickCount++ < 1) {
			AppUtil.showToast(this, "再按一次就退出"+getResources().getString(R.string.app_name));
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mClickCount = 0;
				}
			}, 2000);
			return;
		}


		mHandler.removeCallbacksAndMessages(null);
		HApplication.getInstance().exit();
		super.onBackPressed();
	}

	private void checkUpdate() {

		if (PreferencesUtils.getString(GlobeFlags.NO_UPDATE_NOTICE_TAG).equals(
				MyApplication.getInstance().getVersionName())) {
			UpdateUtil.checkBackgroundDate(this, false, false);
		} else {
			UpdateUtil.checkBackgroundDate(this, false, true);
		}
	}

	public static void invoke(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	public static void invokeWithAction(Context context,String action) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setAction(action);
		context.startActivity(intent);
	}

	public static void invoke(Context context,boolean isExpired) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("is_expired",isExpired);
		context.startActivity(intent);
	}

	private void handleAction(Intent data){
		String action=data.getAction();
		if(AppUtil.isNull(action))return;
		switch (action){
			case IActions.ACTION_TO_ODER_LIST:
				AccountActivity.invoke(this,1);
				break;
			case IActions.ACTION_TO_MAIN_USER_CENTER:

				break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		mShareAPI.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==101){//退出登录回来，需要关闭主页

				finish();
			}
		}
	}


	@NeedsPermission({Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE})
	void showPermissions() {

	}
}