package mn.ismartdev.donatevoyage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.kmshack.newsstand.SampleListFragment;
import com.kmshack.newsstand.ScrollTabHolder;
import com.kmshack.newsstand.ScrollTabHolderFragment;
import com.nineoldandroids.view.ViewHelper;

public class GuideMain extends ActionBarActivity implements ScrollTabHolder,
		ViewPager.OnPageChangeListener {

	private KenBurnsSupportView mHeaderPicture;
	private View mHeader;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;

	private TypedValue mTypedValue = new TypedValue();
	private SpannableString mSpannableString;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMinHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		setContentView(R.layout.guide_main);
		mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
		mHeaderPicture.setResourceIds(R.drawable.guide_one,
				R.drawable.guide_two, R.drawable.guide_three);
		mHeader = findViewById(R.id.header);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(4);

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPagerAdapter.setTabHolderScrollingContent(this);

		mViewPager.setAdapter(mPagerAdapter);

		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setOnPageChangeListener(this);
		mSpannableString = new SpannableString("Restaurant");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);

		ViewHelper.setAlpha(getActionBarIconView(), 0f);

		getSupportActionBar().setBackgroundDrawable(null);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// nothing
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// nothing
	}

	@Override
	public void onPageSelected(int position) {
		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter
				.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

		currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper
				.getTranslationY(mHeader)));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		if (mViewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
			ViewHelper.setTranslationY(mHeader,
					Math.max(-scrollY, mMinHeaderTranslation));
			float ratio = clamp(ViewHelper.getTranslationY(mHeader)
					/ mMinHeaderTranslation, 0.0f, 1.0f);
			setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		}
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// nothing
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getTheme().resolveAttribute(android.R.attr.actionBarSize,
					mTypedValue, true);
		} else {
			getTheme()
					.resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
		}

		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());

		return mActionBarHeight;
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(mSpannableString);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private ImageView getActionBarIconView() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return (ImageView) findViewById(android.R.id.home);
		}

		return (ImageView) findViewById(android.support.v7.appcompat.R.id.home);
	}

	public class PagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private final String[] TITLES = { "Attractions", "Shopping",
				"Restaurants", "Night Life", "Hotel Motel", "Others" };
		private ScrollTabHolder mListener;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		}

		public void setTabHolderScrollingContent(ScrollTabHolder listener) {
			mListener = listener;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) SampleListFragment
					.newInstance(position);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}

			return fragment;
		}

		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.guide_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		// if (id == R.id.action_search_ad) {
		// mPagerAdapter.filterCar();
		// }
		if (id == android.R.id.home)
			onBackPressed();
		return true;
	}

}
