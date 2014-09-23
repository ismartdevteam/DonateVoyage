package mn.ismartdev.donatevoyag.fragment;

import mn.ismartdev.donatevoyage.R;
import mn.ismartdev.donatevoyage.RestaurantMain;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenu extends Fragment {
	View v;
	private ListView menuList;
	private String menus[] = { "Нүүр хуудас", "Ресторан" };
	private int images[] = { R.drawable.home, R.drawable.restaurant };

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		menuList.setAdapter(new MyMenuAdapter(getActivity(), menus));
		menuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 1:
					startActivity(new Intent(getActivity(),
							RestaurantMain.class));
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.left_menu, container, false);
		menuList = (ListView) v.findViewById(R.id.menu_list);
		return v;
	}

	private class MyMenuAdapter extends ArrayAdapter<String> {
		Context mContext;

		public MyMenuAdapter(Context context, String[] objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			String title = getItem(position);
			Holder hol = null;
			if (v == null) {
				v = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.left_menu_list_item, parent, false);
				hol = new Holder();
				hol.image = (ImageView) v.findViewById(R.id.menu_image);
				hol.title = (TextView) v.findViewById(R.id.menu_title);
				v.setTag(hol);

			} else
				hol = (Holder) v.getTag();
			hol.image.setImageResource(images[position]);
			hol.title.setText(title);
			return v;
		}

		class Holder {
			ImageView image;
			TextView title;
		}
	}
}
