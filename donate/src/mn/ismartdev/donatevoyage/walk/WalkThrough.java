package mn.ismartdev.donatevoyage.walk;

import java.io.IOException;
import java.io.InputStream;

import mn.ismartdev.donatevoyage.MainActivity;
import mn.ismartdev.donatevoyage.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

public class WalkThrough extends FragmentActivity {
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	private WalkAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walkthrough);
		mPager = (ViewPager) findViewById(R.id.walk_pager);
		adapter = new WalkAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mIndicator = (CirclePageIndicator) findViewById(R.id.walk_indicator);
		mIndicator.setViewPager(mPager);
	}

	public class WalkAdapter extends FragmentPagerAdapter {

		public WalkAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {

			return WalkFragment.newInstance(pos);

		}

		@Override
		public int getCount() {
			return 3;
		}
	}

	public static class WalkFragment extends Fragment {
		private String images[] = { "walk/1.png", "walk/2.png", "walk/3.png" };
		ImageView _imageView;
		Button skip;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.walk_item, container, false);
			int pos = getArguments().getInt("pos");
			_imageView = (ImageView) v.findViewById(R.id.walk_item_image);
			skip = (Button) v.findViewById(R.id.walk_item_skip);
			_imageView.setImageBitmap(getBitmapFromAsset(getActivity(),
					images[pos]));
			if (pos == 2) {
				skip.setVisibility(View.VISIBLE);
				skip.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getActivity().finish();
						startActivity(new Intent(getActivity(),
								MainActivity.class));
					}
				});
			} else
				skip.setVisibility(View.GONE);
			// TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
			// tv.setText();

			return v;
		}

		public static WalkFragment newInstance(int mNum) {

			WalkFragment f = new WalkFragment();
			Bundle b = new Bundle();
			b.putInt("pos", mNum);

			f.setArguments(b);

			return f;
		}
	}

	private static Bitmap getBitmapFromAsset(Context context, String strName) {
		AssetManager assetManager = context.getAssets();
		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			return null;
		}
		return bitmap;
	}
}
