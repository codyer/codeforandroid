package com.pao123.contrl.menu.ui;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.DisplayMetrics;
//import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import com.pao123.contrl.menu.MenuActivity;
import com.pao123.www.R;
import com.nineoldandroids.view.ViewHelper;

public class ResideMenu extends FrameLayout {

	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	private static final int PRESSED_MOVE_HORIZONTAL = 2;
	private static final int PRESSED_DOWN = 3;
	private static final int PRESSED_DONE = 4;
	private static final int PRESSED_MOVE_VERTICAL = 5;

//	private ImageView imageViewShadow;
	private ImageView imageViewBackground;
	private LinearLayout layoutLeftMenu;
	private LinearLayout layoutTempLayout;
	private ScrollView scrollViewLeftMenu;
//	private ScrollView scrollViewMenu;
	/** Current attaching activity. */
	private Activity activity;
	/** The DecorView of current activity. */
	private ViewGroup viewDecor;
	private TouchDisableView viewActivity;
	/** The flag of menu opening status. */
	private boolean isOpened;
	// private float shadowAdjustScaleX;
	// private float shadowAdjustScaleY;
	/** Views which need stop to intercept touch events. */
	private List<View> ignoredViews;
	private List<LinearLayout> leftMenuItems;
	// private List<LinearLayout> rightMenuItems;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private OnMenuListener menuListener;
	private float lastRawX;
	private boolean isInIgnoredView = false;
	private int transDirection = DIRECTION_LEFT;
	// private int scaleDirection = DIRECTION_LEFT;
	private int pressedState = PRESSED_DOWN;
	private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
	// Valid scale factor is between 0.0f and 1.0f.
	// private float mScaleValue = 0.5f;
	private int mTransValue = 200;
	private float mTransScale = 0.8f;
	private LinearLayout profile_item;
	private LinearLayout startrun_item;
	private LinearLayout history_item;
	private LinearLayout realtime_item;
	private LinearLayout friends_item;
	private LinearLayout group_item;
	private LinearLayout settings_item;

	public ResideMenu(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu, this);
		scrollViewLeftMenu = (ScrollView) findViewById(R.id.sv_left_menu);
//		imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
		layoutLeftMenu = (LinearLayout) findViewById(R.id.layout_left_menu);
		// (LinearLayout) findViewById(R.id.layout_right_menu);
		imageViewBackground = (ImageView) findViewById(R.id.iv_background);

		layoutTempLayout = (LinearLayout) inflater.inflate(
				R.layout.residemenu_content, null);
		profile_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_profile_item);
		startrun_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_startrun_item);
		history_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_history_item);
		realtime_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_realtime_item);
		friends_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_friends_item);
		group_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_group_item);
		settings_item = (LinearLayout) layoutTempLayout
				.findViewById(R.id.menu_settings_item);
		initItem(profile_item, R.drawable.menu_user, "游客");
		initItem(startrun_item, R.drawable.menu_startrun, "开始跑步");
		initItem(history_item, R.drawable.menu_history, "跑步历史");
		initItem(realtime_item, R.drawable.menu_realtime, "实时跟踪");
		initItem(friends_item, R.drawable.menu_friends, "我的好友");
		initItem(group_item, R.drawable.menu_group, "我的团");
		initItem(settings_item, R.drawable.menu_setting, "设置");
	}

	private void initItem(LinearLayout v, int icon, String title) {
//		int margin = mTransValue/11;
//		Log.e("initItem", "margin="+margin);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
//		lp.setMargins(margin,margin,margin,margin);
//		v.setLayoutParams(lp);
//		v.setGravity(Gravity.CENTER);
//		v.setOrientation(LinearLayout.VERTICAL);
//		v.setWeightSum(1);
//		v.setPadding(margin/2, margin/2, margin/2, margin/2);
		ImageView iv_icon = (ImageView) v
				.findViewById(R.id.iv_icon);
		TextView tv_title = (TextView) v
				.findViewById(R.id.tv_title);
		iv_icon.setImageResource(icon);
		tv_title.setText(title);
	}

	@Override
	protected boolean fitSystemWindows(Rect insets) {
		// Applies the content insets to the view's padding, consuming that
		// content (modifying the insets to be 0),
		// and returning true. This behavior is off by default and can be
		// enabled through setFitsSystemWindows(boolean)
		// in api14+ devices.
		this.setPadding(viewActivity.getPaddingLeft() + insets.left,
				viewActivity.getPaddingTop() + insets.top,
				viewActivity.getPaddingRight() + insets.right,
				viewActivity.getPaddingBottom() + insets.bottom);
		insets.left = insets.top = insets.right = insets.bottom = 0;
		return true;
	}

	/**
	 * Set up the activity;
	 *
	 * @param activity
	 */
	public void attachToActivity(Activity activity) {
		// setShadowAdjustScaleXByOrientation();
		initValue(activity);
	}

	private void initValue(Activity activity) {
		this.activity = activity;
		leftMenuItems = new ArrayList<LinearLayout>();
		// rightMenuItems = new ArrayList<LinearLayout>();
		ignoredViews = new ArrayList<View>();
		viewDecor = (ViewGroup) activity.getWindow().getDecorView();
		viewActivity = new TouchDisableView(this.activity);

		View mContent = viewDecor.getChildAt(0);
		viewDecor.removeViewAt(0);
		viewActivity.setContent(mContent);
		viewDecor.addView(this, 0);
		addView(viewActivity);

		ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
		parent.removeView(scrollViewLeftMenu);
		mTransValue = (int) (mTransScale * getScreenWidth());
	}

	/**
	 * Set the background image of menu;
	 *
	 * @param imageResource
	 */
	public void setBackground(int imageResource) {
		imageViewBackground.setImageResource(imageResource);
	}

	/**
	 * The visibility of the shadow under the activity;
	 *
	 * @param isVisible
	 */
	public void setShadowVisible(boolean isVisible) {
//		if (isVisible)
//			imageViewShadow.setBackgroundResource(R.drawable.shadow);
//		else
//			imageViewShadow.setBackgroundResource(0);
	}

	public void addMenuItems(MenuActivity menuActivity) {
		this.leftMenuItems.add(profile_item);
		this.leftMenuItems.add(startrun_item);
		this.leftMenuItems.add(history_item);
		this.leftMenuItems.add(realtime_item);
		this.leftMenuItems.add(friends_item);
		this.leftMenuItems.add(group_item);
		this.leftMenuItems.add(settings_item);
		profile_item.setOnClickListener(menuActivity);
		startrun_item.setOnClickListener(menuActivity);
		history_item.setOnClickListener(menuActivity);
		realtime_item.setOnClickListener(menuActivity);
		friends_item.setOnClickListener(menuActivity);
		group_item.setOnClickListener(menuActivity);
		settings_item.setOnClickListener(menuActivity);
		layoutLeftMenu.addView(layoutTempLayout);
	}

	/**
	 * If you need to do something on closing or opening menu, set a listener
	 * here.
	 *
	 * @return
	 */
	public void setMenuListener(OnMenuListener menuListener) {
		this.menuListener = menuListener;
	}

	/**
	 * Show the menu;
	 */
	public void openMenu(int direction) {

		setTransDirection(direction);

		isOpened = true;
		AnimatorSet transOut_activity = buildTransOutAnimation(viewActivity,
				mTransValue, mTransValue);
//		AnimatorSet transOut_shadow = buildTransOutAnimation(imageViewShadow,
//				mTransValue, mTransValue);
		AnimatorSet alpha_menu = buildMenuAnimation(scrollViewLeftMenu, 1.0f);
		transOut_activity.addListener(animationListener);
//		transOut_activity.playTogether(transOut_shadow);
		transOut_activity.playTogether(alpha_menu);
		transOut_activity.start();
	}

	/**
	 * Close the menu;
	 */
	public void closeMenu() {
		isOpened = false;
		AnimatorSet TransIn_activity = buildTransInAnimation(viewActivity, 0, 0);
//		AnimatorSet TransIn_shadow = buildTransInAnimation(imageViewShadow, 0,
//				0);
		AnimatorSet alpha_menu = buildMenuAnimation(scrollViewLeftMenu, 0.0f);
		TransIn_activity.addListener(animationListener);
//		TransIn_activity.playTogether(TransIn_shadow);
		TransIn_activity.playTogether(alpha_menu);
		TransIn_activity.start();
	}

	public void setSwipeDirectionDisable(int direction) {
		disabledSwipeDirection.add(direction);
	}

	private boolean isInDisableDirection(int direction) {
		return disabledSwipeDirection.contains(direction);
	}

	private void setTransDirection(int direction) {
		// float pivotX;
//		 scrollViewMenu = scrollViewLeftMenu;
		// pivotX = mTransValue;
		// ViewHelper.setTranslationX(viewActivity, pivotX);
		// ViewHelper.setTranslationX(imageViewShadow, pivotX);
		transDirection = direction;
	}

	/**
	 * return the flag of menu status;
	 *
	 * @return
	 */
	public boolean isOpened() {
		return isOpened;
	}

	private OnClickListener viewActivityOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (isOpened())
				closeMenu();
		}
	};

	private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
		@Override
		public void onAnimationStart(Animator animation) {
			if (isOpened()) {
				showScrollViewMenu(scrollViewLeftMenu);
				if (menuListener != null)
					menuListener.openMenu();
			}
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// reset the view;
			if (isOpened()) {
				viewActivity.setTouchDisable(true);
				viewActivity.setOnClickListener(viewActivityOnClickListener);
			} else {
				viewActivity.setTouchDisable(false);
				viewActivity.setOnClickListener(null);
				hideScrollViewMenu(scrollViewLeftMenu);
				if (menuListener != null)
					menuListener.closeMenu();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}
	};

	/**
	 * A helper method to build scale down animation;
	 *
	 * @param target
	 * @param targetScaleX
	 * @param targetScaleY
	 * @return
	 */
	private AnimatorSet buildTransOutAnimation(View target, float translationX,
			float targettransY) {
		AnimatorSet trans = new AnimatorSet();
		trans.playTogether(ObjectAnimator.ofFloat(target, "translationX",
				translationX));
		trans.setInterpolator(AnimationUtils.loadInterpolator(activity,
				android.R.anim.decelerate_interpolator));
		trans.setDuration(250);
		return trans;
	}

	private AnimatorSet buildTransInAnimation(View target, int targetTransX,
			int targetTransY) {
		AnimatorSet transIn = new AnimatorSet();
		transIn.playTogether(ObjectAnimator.ofFloat(target, "translationX",
				targetTransX));
		transIn.setDuration(250);
		return transIn;
	}

	private AnimatorSet buildMenuAnimation(View target, float alpha) {
		AnimatorSet alphaAnimation = new AnimatorSet();
		alphaAnimation.playTogether(ObjectAnimator.ofFloat(target, "alpha",
				alpha));
		alphaAnimation.setDuration(250);
		return alphaAnimation;
	}

	/**
	 * If there were some view you don't want reside menu to intercept their
	 * touch event, you could add it to ignored views.
	 *
	 * @param v
	 */
	public void addIgnoredView(View v) {
		ignoredViews.add(v);
	}

	/**
	 * Remove a view from ignored views;
	 * 
	 * @param v
	 */
	public void removeIgnoredView(View v) {
		ignoredViews.remove(v);
	}

	/**
	 * Clear the ignored view list;
	 */
	public void clearIgnoredViewList() {
		ignoredViews.clear();
	}

	/**
	 * If the motion event was relative to the view which in ignored view
	 * list,return true;
	 *
	 * @param ev
	 * @return
	 */
	private boolean isInIgnoredView(MotionEvent ev) {
		Rect rect = new Rect();
		for (View v : ignoredViews) {
			v.getGlobalVisibleRect(rect);
			if (rect.contains((int) ev.getX(), (int) ev.getY()))
				return true;
		}
		return false;
	}

	private void setTransDirectionByRawX(float currentRawX) {
		if (currentRawX < lastRawX)
			setTransDirection(DIRECTION_RIGHT);
		else
			setTransDirection(DIRECTION_LEFT);
	}

	private float getTargetTrans(float currentRawX) {
		float transFloatX = currentRawX - lastRawX;
		// transFloatX = transDirection == DIRECTION_LEFT ? -transFloatX
		// : transFloatX;

//		Log.e("getTargetTrans", "getTargetTrans--transFloatX=" + transFloatX);
//		Log.e("getTargetTrans",
//				"getTargetTrans--getTranslationX="
//						+ ViewHelper.getTranslationX(viewActivity));
		float targetScale = ViewHelper.getTranslationX(viewActivity)
				+ transFloatX;
		return targetScale;
	}

	private float lastActionDownX, lastActionDownY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float currentActivityTransX = ViewHelper.getTranslationX(viewActivity);
//		Log.e("dispatchTouchEvent", "currentActivityTransX="
//				+ currentActivityTransX + ",mTransValue=" + mTransValue);
		if (currentActivityTransX > mTransValue)
			setTransDirectionByRawX(ev.getRawX());

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastActionDownX = ev.getX();
			lastActionDownY = ev.getY();
			isInIgnoredView = isInIgnoredView(ev) && !isOpened();
			pressedState = PRESSED_DOWN;
			break;

		case MotionEvent.ACTION_MOVE:
			if (isInIgnoredView || isInDisableDirection(transDirection)
					|| currentActivityTransX < 0)
				break;

			if (pressedState != PRESSED_DOWN
					&& pressedState != PRESSED_MOVE_HORIZONTAL)
				break;

			int xOffset = (int) (ev.getX() - lastActionDownX);
			int yOffset = (int) (ev.getY() - lastActionDownY);

			if (pressedState == PRESSED_DOWN) {
				if (yOffset > 25 || yOffset < -25) {
					pressedState = PRESSED_MOVE_VERTICAL;
					break;
				}
				if (xOffset < -50 || xOffset > 50) {
					pressedState = PRESSED_MOVE_HORIZONTAL;
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
				if (currentActivityTransX > mTransValue)
					showScrollViewMenu(scrollViewLeftMenu);

				float targetTrans = getTargetTrans(ev.getRawX());
				ViewHelper.setTranslationX(viewActivity, targetTrans);
//				ViewHelper.setTranslationX(imageViewShadow, targetTrans);
				float al=currentActivityTransX/mTransValue;
				ViewHelper.setAlpha(scrollViewLeftMenu, al);

				showScrollViewMenu(scrollViewLeftMenu);
				lastRawX = ev.getRawX();
				return true;
			}

			break;

		case MotionEvent.ACTION_UP:

			if (isInIgnoredView)
				break;
			if (pressedState != PRESSED_MOVE_HORIZONTAL)
				break;

			pressedState = PRESSED_DONE;
			if (isOpened()) {
				if (currentActivityTransX < 0.7 * mTransValue)
					closeMenu();
				else
					openMenu(transDirection);
			} else {
				if (currentActivityTransX > 0.3 * mTransValue) {
					openMenu(transDirection);
				} else {
					closeMenu();
				}
			}
			break;

		}
		lastRawX = ev.getRawX();
		return super.dispatchTouchEvent(ev);
	}

	public int getScreenHeight() {
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	// public void setScaleValue(float scaleValue) {
	// this.mScaleValue = scaleValue;
	// }

	public interface OnMenuListener {

		/**
		 * This method will be called at the finished time of opening menu
		 * animations.
		 */
		public void openMenu();

		/**
		 * This method will be called at the finished time of closing menu
		 * animations.
		 */
		public void closeMenu();
	}

	private void showScrollViewMenu(ScrollView scrollViewMenu) {
		if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
			addView(scrollViewMenu);
		}
	}

	private void hideScrollViewMenu(ScrollView scrollViewMenu) {
		if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
			removeView(scrollViewMenu);
		}
	}

	public void setTransScaleValue(float f) {
		mTransScale = f;
	}

}
