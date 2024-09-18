package seemesave.businesshub.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import seemesave.businesshub.R;


public class ActionSheet extends Fragment implements View.OnClickListener,
		View.OnTouchListener {

	private static final String ARG_CANCEL_ITEM = "cancel_item";
	private static final String ARG_OTHER_ITEM = "other_item";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final int CANCEL_BUTTON_ID = 100;
	private static final int BG_VIEW_ID = 10;
	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;
	private static final String ARG_TEXT_SIZE = "text_size";
	private static final String ARG_CANCEL_MARGIN_TOP = "cancel_margintop";
	private static final String ARG_OTHER_ITEM_SPACING = "other_item_spacing";
	private static final String ARG_ICON_MARGIN_LEFT = "item_icon_margin_left";

	private boolean mDismissed = true;
	private ActionSheetListener mListener;
	private View mView;
	private LinearLayout mPanel;
	private ViewGroup mGroup;
	private View mBg;
	private boolean isCancel = true;

	public void show(FragmentManager manager, String tag) {
		// 显示fragment
		if (!mDismissed) {
			return;
		}
		mDismissed = false;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void dismiss() {
		// 移除fragment
		if (mDismissed) {
			return;
		}
		mDismissed = true;
		getFragmentManager().popBackStack();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(this);
		ft.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = getActivity().getCurrentFocus();
			if (focusView != null) {
				// 隐藏键盘
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}

		// 动态创建一个View
		mView = createView();

		// 得到当前调用ActionSheet的Activity的viewGroup
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();

		// 创建详细的按钮列表
		createItems();

		// 把创建的view放到viewgroup中
		mGroup.addView(mView);

		// 背景打开动画
		mBg.startAnimation(createAlphaInAnimation());

		// 按钮层打开动画
		mPanel.startAnimation(createTranslationInAnimation());

		// 通知父类
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	private Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}

	private View createView() {
		FrameLayout parent = new FrameLayout(getActivity());
		parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		mBg = new View(getActivity());
		mBg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setId(BG_VIEW_ID);

		// 设置背景点击监听
		mBg.setOnClickListener(this);

		mPanel = new LinearLayout(getActivity());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		params.setMargins(0,0,0, getNavigationBarSize());
		mPanel.setLayoutParams(params);
		mPanel.setOrientation(LinearLayout.VERTICAL);

		parent.addView(mBg);
		parent.addView(mPanel);
		return parent;
	}

	private int getNavigationBarSize(){
		Resources resources = getActivity().getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	private void createItems() {
		Item[] items = getOtherItem();

		Resources resources = getActivity().getResources();

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		if (items != null) {
			for (int i = 0; i < items.length; i++) {

				LinearLayout rl = new LinearLayout(getActivity());
				rl.setId(CANCEL_BUTTON_ID + i + 1);
				rl.setOnClickListener(this);
				//rl.setOnTouchListener(this);
				rl.setBackground(getOtherButtonBg(items, i)); 		
				GradientDrawable drawable = (GradientDrawable) rl
						.getBackground();

				TypedValue typedValue = new TypedValue();
				getActivity().getTheme().resolveAttribute(items[i].getItemBackgroundNormal(), typedValue, true);
				int backColor = ContextCompat.getColor(getActivity(), typedValue.resourceId);
				TypedValue typedValue_1 = new TypedValue();
				getActivity().getTheme().resolveAttribute(items[i].getItemTextColorNormal(), typedValue_1, true);
				int textColor = ContextCompat.getColor(getActivity(), typedValue_1.resourceId);

/*				drawable.setColor(resources.getColor(items[i]
						.getItemBackgroundNormal()));*/
				drawable.setColor(backColor);

				rl.setOrientation(LinearLayout.HORIZONTAL);
				rl.setGravity(Gravity.CENTER_VERTICAL);
				rl.setAlpha(items[i].getItemBackgroundAlpha());

				TextView textView = new TextView(getActivity());
				textView.setText(items[i].getItemText());
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
				textView.setGravity(Gravity.CENTER);
				/*textView.setTextColor(resources.getColor(items[i]
						.getItemTextColorNormal()));*/
				textView.setTextColor(textColor);

				LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				textParams.setMargins(50, 30, 0, 30);
				textView.setLayoutParams(textParams);
				rl.addView(textView);

				TypedValue typedValue_2 = new TypedValue();
				getActivity().getTheme().resolveAttribute(items[i].getItemIconNormal(), typedValue_2, true);

				ImageView imageView = new ImageView(getActivity());
				//imageView.setImageResource(items[i].getItemIconNormal());
				imageView.setImageResource(typedValue_2.resourceId);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
						50, ViewGroup.LayoutParams.MATCH_PARENT);
				//iconParams.setMargins(width / getIconMarginLeft(), 15, 0, 15);
				iconParams.setMargins(5, 5, 50, 5);
				imageView.setLayoutParams(iconParams);
				rl.addView(imageView);

				if (i > 0) {
					LinearLayout.LayoutParams params = createButtonLayoutParams();
					params.topMargin = getOtherItemSpacing();

					mPanel.addView(rl, params);
				} else {
					mPanel.addView(rl);
				}
			}
		}

		Button bt = new Button(getActivity());
		bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
		bt.setId(CANCEL_BUTTON_ID);

		bt.setBackground(resources
				.getDrawable(R.drawable.actionsheet_cancel_border));
		GradientDrawable drawable = (GradientDrawable) bt.getBackground();

		TypedValue typedValue = new TypedValue();
		getActivity().getTheme().resolveAttribute(getCancelItem().getItemBackgroundNormal(), typedValue, true);
		int backColor = ContextCompat.getColor(getActivity(), typedValue.resourceId);
		TypedValue typedValue_1 = new TypedValue();
		getActivity().getTheme().resolveAttribute(getCancelItem().getItemTextColorNormal(), typedValue_1, true);
		int textColor = ContextCompat.getColor(getActivity(), typedValue_1.resourceId);

		drawable.setColor(backColor);
		bt.setAlpha(getCancelItem().getItemBackgroundAlpha());
		bt.setText(getCancelItem().getItemText());
		bt.setTextColor(textColor);

		// 设置取消点击监听
		bt.setOnClickListener(this);
		//bt.setOnTouchListener(this);
		LinearLayout.LayoutParams params = createButtonLayoutParams();
		params.topMargin = getCancelButtonMarginTop();
		mPanel.addView(bt, params);

		mPanel.setBackgroundColor(Color.TRANSPARENT);
		mPanel.setPadding(20, 20, 20, 0);
	}

	public LinearLayout.LayoutParams createButtonLayoutParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		return params;
	}

	private Drawable getOtherButtonBg(Item[] items, int i) {
		Resources resources = getActivity().getResources();

		if (items.length == 1) {
			return resources.getDrawable(R.drawable.actionsheet_single_border);
		}
		if (items.length == 2) {
			switch (i) {
			case 0:
				return resources.getDrawable(R.drawable.actionsheet_top_border);
			case 1:
				return resources
						.getDrawable(R.drawable.actionsheet_bottom_border);
			}
		}
		if (items.length > 2) {
			if (i == 0) {
				return resources.getDrawable(R.drawable.actionsheet_top_border);
			}
			if (i == (items.length - 1)) {
				return resources
						.getDrawable(R.drawable.actionsheet_bottom_border);
			}
			return resources.getDrawable(R.drawable.actionsheet_middle_border);
		}
		return null;
	}

	@Override
	public void onDestroyView() {
		// 操作按钮面板消失动画
		mPanel.startAnimation(createTranslationOutAnimation());

		// 操作的背景消失动画
		mBg.startAnimation(createAlphaOutAnimation());

		// android View上面的postDelayed方法用于延迟UI操作(单位毫秒)
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 从viewGroup中移除整个弹出的view
				mGroup.removeView(mView);
			}
		}, ALPHA_DURATION);
		if (mListener != null) {
			// 移除之后调用移除方法，相当于回调
			mListener.onDismiss(this, isCancel);
		}
		// 调用fragment的注销方法
		super.onDestroyView();
	}

	private Item getCancelItem() {
		// 这里的getArguments()获取的是Bundle, 这个方法只有在fragment中有
		return getArguments().getParcelable(ARG_CANCEL_ITEM);
	}

	private Item[] getOtherItem() {
		return (Item[]) getArguments().getParcelableArray(ARG_OTHER_ITEM);
	}

	private int getTextSize() {
		return getArguments().getInt(ARG_TEXT_SIZE);
	}

	private int getCancelButtonMarginTop() {
		return getArguments().getInt(ARG_CANCEL_MARGIN_TOP);
	}

	private int getOtherItemSpacing() {
		return getArguments().getInt(ARG_OTHER_ITEM_SPACING);
	}
	private int getIconMarginLeft() {
		return getArguments().getInt(ARG_ICON_MARGIN_LEFT);
	}
	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	public void setActionSheetListener(ActionSheetListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == BG_VIEW_ID && !getCancelableOnTouchOutside()) {
			return;
		}
		dismiss();

		if (v.getId() != CANCEL_BUTTON_ID && v.getId() != BG_VIEW_ID) {
			if (mListener != null) {
				Resources resources = getActivity().getResources();
				LinearLayout layout = (LinearLayout) getActivity()
						.findViewById(v.getId());

				ImageView iv = null;
				TextView tv = null;
				View v1, v2 = null;
				if (layout.getChildCount() == 1) {
					v1 = layout.getChildAt(0);
				} else {
					v1 = layout.getChildAt(0);
					v2 = layout.getChildAt(1);
				}

				if (v1 != null) {
					if (v1 instanceof ImageView) {
						iv = (ImageView) v1;
					} else {
						tv = (TextView) v1;
					}
				}

				if (v2 != null) {
					if (v2 instanceof TextView) {
						tv = (TextView) v2;
					} else {
						iv = (ImageView) v2;
					}
				}
				mListener.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
						- 1, tv.getText().toString());
			}
			isCancel = false;
		}
	}

	public static Builder createBuilder(Context context,
			FragmentManager fragmentManager) {
		return new Builder(context, fragmentManager);
	}

	public static class Builder {

		private Context mContext;
		private FragmentManager mFragmentManager;
		private Item mCancelItem;
		private Item[] mOtherItems;
		private String mTag = "actionSheet";
		private boolean mCancelableOnTouchOutside;
		private ActionSheetListener mListener;
		private int mTextSize=30;
		private int mIconMarginLeft=3;
		private int mCancelButtonMarginTop=20;
		private int mOtherItemSpacing=5;

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		public Builder setCancelItem(Item item) {
			mCancelItem = item;
			return this;
		}

		public Builder setmOtherItems(Item... items) {
			mOtherItems = items;
			return this;
		}
		public Builder setmIconMarginLeft(int marginLeft) {
			mIconMarginLeft = marginLeft;
			return this;
		}

		public Builder setmTextSize(int size) {

			mTextSize = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources()
							.getDisplayMetrics());
			return this;
		}

		public Builder setmCancelButtonMarginTop(int marginTop) {
			mCancelButtonMarginTop = marginTop;
			return this;
		}

		public Builder setmOtherItemSpacing(int itemSpacing) {
			mOtherItemSpacing = itemSpacing;
			return this;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		public Builder setListener(ActionSheetListener listener) {
			this.mListener = listener;
			return this;
		}

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putParcelable(ARG_CANCEL_ITEM, mCancelItem);
			bundle.putParcelableArray(ARG_OTHER_ITEM, mOtherItems);
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE,
					mCancelableOnTouchOutside);
			bundle.putInt(ARG_TEXT_SIZE, mTextSize);
			bundle.putInt(ARG_CANCEL_MARGIN_TOP, mCancelButtonMarginTop);
			bundle.putInt(ARG_OTHER_ITEM_SPACING, mOtherItemSpacing);
			bundle.putInt(ARG_ICON_MARGIN_LEFT, mIconMarginLeft);
			return bundle;
		}

		public ActionSheet show() {
			ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
					mContext, ActionSheet.class.getName(), prepareArguments());
			// 这个监听事件，Builder里面set方法是漏给外面的，ActionSheet的setActionSheetListener才是真正的
			actionSheet.setActionSheetListener(mListener);
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}
	}

	public static class Item implements Parcelable {
		private int itemBackgroundNormal;
		private int itemBackgroundPressed;
		private int itemIconNormal;
		private int itemIconPressed;
		private int itemTextColorNormal;
		private int itemTextColorPressed;
		private float itemBackgroundAlpha=0.7f;
		private String itemText;

		protected Item(Parcel in) {
			itemBackgroundNormal = in.readInt();
			itemBackgroundPressed = in.readInt();
			itemIconNormal = in.readInt();
			itemIconPressed = in.readInt();
			itemTextColorNormal = in.readInt();
			itemTextColorPressed = in.readInt();
			itemBackgroundAlpha = in.readFloat();
			itemText = in.readString();
		}

		public static final Creator<Item> CREATOR = new Creator<Item>() {
			@Override
			public Item createFromParcel(Parcel in) {
				return new Item(in);
			}

			@Override
			public Item[] newArray(int size) {
				return new Item[size];
			}
		};

		public float getItemBackgroundAlpha() {
			return itemBackgroundAlpha;
		}

		public void setItemBackgroundAlpha(float itemBackgroundAlpha) {
			this.itemBackgroundAlpha = itemBackgroundAlpha;
		}
		public int getItemBackgroundNormal() {
			return itemBackgroundNormal;
		}

		public int getItemBackgroundPressed() {
			return itemBackgroundPressed;
		}

		public int getItemIconNormal() {
			return itemIconNormal;
		}

		public int getItemIconPressed() {
			return itemIconPressed;
		}

		public int getItemTextColorNormal() {
			return itemTextColorNormal;
		}

		public int getItemTextColorPressed() {
			return itemTextColorPressed;
		}

		public String getItemText() {
			return itemText;
		}

		public Item() {
		}

		public Item(int itemBackgroundNormal, int itemBackgroundPressed,
				int itemIconNormal, int itemIconPressed,
				int itemTextColorNormal, int itemTextColorPressed,
				String itemText) {
			this.itemText = itemText;
			this.itemTextColorNormal = itemTextColorNormal;
			this.itemTextColorPressed = itemTextColorPressed;
			this.itemIconNormal = itemIconNormal;
			this.itemIconPressed = itemIconPressed;
			this.itemBackgroundNormal = itemBackgroundNormal;
			this.itemBackgroundPressed = itemBackgroundPressed;
		}
		
		public Item(int itemBackgroundNormal, int itemBackgroundPressed,
				int itemIconNormal, int itemIconPressed,
				int itemTextColorNormal, int itemTextColorPressed,
				String itemText, float itemBackgroundAlpha) {
			this.itemText = itemText;
			this.itemTextColorNormal = itemTextColorNormal;
			this.itemTextColorPressed = itemTextColorPressed;
			this.itemIconNormal = itemIconNormal;
			this.itemIconPressed = itemIconPressed;
			this.itemBackgroundNormal = itemBackgroundNormal;
			this.itemBackgroundPressed = itemBackgroundPressed;
			this.itemBackgroundAlpha = itemBackgroundAlpha;
		}

		public void setItemBackgroundNormal(int itemBackgroundNormal) {
			this.itemBackgroundNormal = itemBackgroundNormal;
		}

		public void setItemBackgroundPressed(int itemBackgroundPressed) {
			this.itemBackgroundPressed = itemBackgroundPressed;
		}

		public void setItemIconNormal(int itemIconNormal) {
			this.itemIconNormal = itemIconNormal;
		}

		public void setItemIconPressed(int itemIconPressed) {
			this.itemIconPressed = itemIconPressed;
		}

		public void setItemTextColorNormal(int itemTextColorNormal) {
			this.itemTextColorNormal = itemTextColorNormal;
		}

		public void setItemTextColorPressed(int itemTextColorPressed) {
			this.itemTextColorPressed = itemTextColorPressed;
		}

		public void setItemText(String itemText) {
			this.itemText = itemText;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(itemBackgroundNormal);
			dest.writeInt(itemBackgroundPressed);
			dest.writeInt(itemIconNormal);
			dest.writeInt(itemIconPressed);
			dest.writeInt(itemTextColorNormal);
			dest.writeInt(itemTextColorPressed);
			dest.writeString(itemText);
		}

	}

	public static interface ActionSheetListener {

		void onDismiss(ActionSheet actionSheet, boolean isCancel);

		void onOtherButtonClick(ActionSheet actionSheet, int index, String itemStr);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (getActivity() != null) {

			if (v.getId() != CANCEL_BUTTON_ID) {
				Resources resources = getActivity().getResources();
				LinearLayout layout = (LinearLayout) getActivity()
						.findViewById(v.getId());

				ImageView iv = null;
				TextView tv = null;
				View v1, v2 = null;
				if (layout.getChildCount() == 1) {
					v1 = layout.getChildAt(0);
				} else {
					v1 = layout.getChildAt(0);
					v2 = layout.getChildAt(1);
				}

				if (v1 != null) {
					if (v1 instanceof ImageView) {
						iv = (ImageView) v1;
					} else {
						tv = (TextView) v1;
					}
				}

				if (v2 != null) {
					if (v2 instanceof TextView) {
						tv = (TextView) v2;
					} else {
						iv = (ImageView) v2;
					}
				}

				Item[] items = getOtherItem();

				int i = v.getId() - CANCEL_BUTTON_ID - 1;

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (iv != null && items[i].getItemIconPressed() != 0) {
						iv.setImageResource(items[i].getItemIconPressed());
					}
					if (tv != null && items[i].getItemTextColorPressed() != 0) {
						tv.setTextColor(getResources().getColor(
								items[i].getItemTextColorPressed()));
					}

					if (items[i].getItemBackgroundPressed() != 0) {
						layout.setBackground(getOtherButtonBg(items, i)); 
						GradientDrawable drawable = (GradientDrawable) layout
								.getBackground();
						drawable.setColor(resources.getColor(items[i]
								.getItemBackgroundPressed()));
					}

					break;
				case MotionEvent.ACTION_UP:

					if (iv != null && items[i].getItemIconNormal() != 0) {
						iv.setImageDrawable(getResources().getDrawable(
								items[i].getItemIconNormal()));
					}
					if (tv != null && items[i].getItemTextColorNormal() != 0) {
						tv.setTextColor(getResources().getColor(
								items[i].getItemTextColorNormal()));
					}

					if (items[i].getItemBackgroundNormal() != 0) {
						layout.setBackground(getOtherButtonBg(items, i)); 
						GradientDrawable drawable = (GradientDrawable) layout
								.getBackground();
						drawable.setColor(resources.getColor(items[i]
								.getItemBackgroundNormal()));
					}

					break;
				case MotionEvent.ACTION_MOVE:
					if (event.getY() < 0 || event.getY() > layout.getHeight()
							|| event.getX() < 0
							|| event.getX() > layout.getWidth()) {
						if (iv != null && items[i].getItemIconNormal() != 0) {
							iv.setImageDrawable(getResources().getDrawable(
									items[i].getItemIconNormal()));
						}
						if (tv != null
								&& items[i].getItemTextColorNormal() != 0) {
							tv.setTextColor(getResources().getColor(
									items[i].getItemTextColorNormal()));
						}

						if (items[i].getItemBackgroundNormal() != 0) {
							layout.setBackground(getOtherButtonBg(items, i)); 
							GradientDrawable drawable = (GradientDrawable) layout
									.getBackground();
							drawable.setColor(resources.getColor(items[i]
									.getItemBackgroundNormal()));
						}
					}
					break;
				}
			} else {

				Button button = (Button) getActivity().findViewById(
						CANCEL_BUTTON_ID);
				Item cancelItem = getCancelItem();
				Resources resources = getActivity().getResources();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (cancelItem.getItemBackgroundPressed() != 0) {
						button.setBackground(resources
								.getDrawable(R.drawable.actionsheet_cancel_border));
						GradientDrawable drawable = (GradientDrawable) button
								.getBackground();
						drawable.setColor(resources.getColor(getCancelItem()
								.getItemBackgroundPressed()));
					}
					if (cancelItem.getItemTextColorPressed() != 0) {
						button.setTextColor(resources.getColor(cancelItem
								.getItemTextColorPressed()));
					}

					break;
				case MotionEvent.ACTION_UP:

					if (cancelItem.getItemBackgroundNormal() != 0) {
						button.setBackground(resources
								.getDrawable(R.drawable.actionsheet_cancel_border));
						GradientDrawable drawable = (GradientDrawable) button
								.getBackground();
						drawable.setColor(resources.getColor(getCancelItem()
								.getItemBackgroundNormal()));
					}
					if (cancelItem.getItemTextColorNormal() != 0) {
						button.setTextColor(resources.getColor(cancelItem
								.getItemTextColorNormal()));
					}

					break;
				case MotionEvent.ACTION_MOVE:
					if (event.getY() < 0 || event.getY() > button.getHeight()
							|| event.getX() < 0
							|| event.getX() > button.getWidth()) {

						if (cancelItem.getItemBackgroundNormal() != 0) {
							button.setBackground(resources
									.getDrawable(R.drawable.actionsheet_cancel_border));
							GradientDrawable drawable = (GradientDrawable) button
									.getBackground();
							drawable.setColor(resources
									.getColor(getCancelItem()
											.getItemBackgroundNormal()));
						}
						if (cancelItem.getItemTextColorNormal() != 0) {
							button.setTextColor(resources.getColor(cancelItem
									.getItemTextColorNormal()));
						}
					}
					break;
				}
			}
		}

		return false;
	}

}