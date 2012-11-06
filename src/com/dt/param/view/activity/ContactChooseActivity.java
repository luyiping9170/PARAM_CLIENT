package com.dt.param.view.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import com.dt.bean.Contact;
import com.dt.bean.Group;
import com.dt.bean.IndexPair;
import com.dt.listadapter.SimpleExpandableListAdapter;
import com.dt.listadapter.SingleContactPickerListAdapter;
import com.dt.param.R;
import com.dt.param.util.StringUtil;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ContactChooseActivity extends ParamView {

	private ListView contactList;
	private ExpandableListView groupList;
	private Button switcher;
	private TextView letterIndicator;
	private EditText searchTxtBox;
	private LinearLayout listContainer;
	private LinearLayout pinyinSelector;
	private TableLayout pickedContactContainer;
	private FrameLayout frameContainer;
	private RelativeLayout pinyinContainer;

	private List<Group> groups;
	private List<Contact> allContacts;
	private List<TextView> letters;

	private BaseAdapter contactAdapter;
	private BaseExpandableListAdapter groupAdapter;

	private enum State {
		STATE_CONTACT, STATE_GROUP, STATE_ACPT
	}

	private int lastLetter = 0;
	private State current = State.STATE_CONTACT;
	private State previous = State.STATE_CONTACT;

	private static final String[] alphabet = { "#", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_picker);

		// / Init UI Components
		switcher = (Button) this
				.findViewById(R.id.contact_picker_state_switcher);
		// / Init pinyin selector
		pinyinSelector = (LinearLayout) this
				.findViewById(R.id.contact_picker_pinyin_selector);
		letterIndicator = (TextView) this
				.findViewById(R.id.contact_picker_letter_indicator);
		letterIndicator.setTextSize(40);
		letters = new ArrayList<TextView>();

		for (String letter : alphabet) {
			TextView tv = new TextView(this);
			tv.setText(letter);
			tv.setTextSize(8);
			letters.add(tv);
			pinyinSelector.addView(tv, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		letters.get(lastLetter).setBackgroundColor(Color.BLUE);
		pinyinSelector.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					letterIndicator.setVisibility(View.VISIBLE);
				case MotionEvent.ACTION_MOVE:
					int height = pinyinSelector.getHeight();
					int index = (int) (event.getY() / height * alphabet.length);
					if (index < alphabet.length && index >= 0) {
						setSelectedLetter(index);
						letterIndicator.setText(alphabet[index]);
					}
					break;
				case MotionEvent.ACTION_UP:
					letterIndicator.setVisibility(View.INVISIBLE);
					if(lastLetter<=0)
						contactList.setSelection(0);
					else{
						for(int i=0;i<allContacts.size();i++)
							if(allContacts.get(i).key.toUpperCase().startsWith(alphabet[lastLetter])){
								contactList.setSelection(i);
								break;
							}
					}
						
					break;
				}
				return true;
			}
		});

		searchTxtBox = (EditText) this.findViewById(R.id.contact_picker_search_box);
		frameContainer = (FrameLayout) this
				.findViewById(R.id.contact_picker_frame_container);
		pinyinContainer = (RelativeLayout) this
				.findViewById(R.id.contact_picker_pinyin_container);
		pickedContactContainer = (TableLayout) this
				.findViewById(R.id.contact_picker_picked_contact_container);

		// / Init contact list
		Cursor phoneCursor = this.managedQuery(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone._ID,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone.NUMBER }, null,
				null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " ASC");

		allContacts = new LinkedList<Contact>();
		long time1 = System.currentTimeMillis();
		while (phoneCursor.moveToNext()) {
		
			String contact_id = phoneCursor
					.getString(phoneCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			String phone_id = phoneCursor
					.getString(phoneCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
			String name = phoneCursor
					.getString(phoneCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String number = phoneCursor
					.getString(phoneCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			Contact c = new Contact(contact_id, phone_id, name,number);
			int i = allContacts.size();
			while(i-1 >= 0&&c.compareTo(allContacts.get(i-1))<0)
				i --;
			allContacts.add(i, c);
		}
		long time2 = System.currentTimeMillis();
				
		// Setup contact picker list
		contactList = new ListView(this);
		contactAdapter = new SingleContactPickerListAdapter(this, allContacts);
		contactList.setAdapter(contactAdapter);
		contactList.setCacheColorHint(Color.TRANSPARENT);

		// / Setup groupCursor
		Cursor groupCursor = managedQuery(Groups.CONTENT_URI, new String[] {
				Groups.SYSTEM_ID, Groups._ID, Groups.TITLE }, Groups.SYSTEM_ID
				+ " NOT NULL", null, null);

		this.groups = new LinkedList<Group>();
		long time3 = System.currentTimeMillis();
		while (groupCursor.moveToNext()) {
			String groupId = groupCursor.getString(groupCursor
					.getColumnIndex(Groups._ID));
			String title = groupCursor.getString(groupCursor
					.getColumnIndex(Groups.TITLE));

			Cursor c = this.managedQuery(Data.CONTENT_URI, new String[] {
					Contacts.DISPLAY_NAME, Contacts._ID },
					GroupMembership.GROUP_ROW_ID + " = ?",
					new String[] { groupId }, Contacts.DISPLAY_NAME + " ASC");

			List<IndexPair> pairs = new LinkedList<IndexPair>();
			while (c.moveToNext()) {
				String name = c.getString(c
						.getColumnIndex(Contacts.DISPLAY_NAME));
				String id = c.getString(c.getColumnIndex(Contacts._ID));
				//Log.d("name and id:", name + " : " + id);
				int location = binarySearch(allContacts, new Contact(null,null,name,null), 0,
						allContacts.size() - 1);
				List<Integer> locations = new LinkedList<Integer>();
				if (location >= 0) {
					int temp = location;
					// Look downward
					while (temp >= 0 && allContacts.get(temp).name.equals(name)) {
						// if(allContacts.get(temp).contact_id.equals(id))
						locations.add(0, temp);
						allContacts.get(temp).group = groups.size(); // set the
																		// contact's
																		// group
																		// index
						// else
						// break;
						temp--;
					}
					// Look upward
					temp = location + 1;
					while (temp < allContacts.size()
							&& allContacts.get(temp).name.equals(name)) {
						// if(allContacts.get(temp).contact_id.equals(id))
						locations.add(temp);
						allContacts.get(temp).group = groups.size();
						// else
						// break;
						temp++;
					}
					if (locations.size() > 0) {
						pairs.add(new IndexPair(locations.get(0), locations
								.get(locations.size() - 1)));
				//		Log.d("low : upper",
				//				locations.get(0) + ":"
				//						+ locations.get(locations.size() - 1));
					}
				}
			}
			Collections.sort(pairs);
			groups.add(new Group(groupId, title, pairs));
		}
		long time4 = System.currentTimeMillis();
		Log.d("Iterate over all groups", time4 - time3 + " ms");
		
		// Sort by group title
		// Collections.sort(groups);

		// Setup group picker list
		groupList = new ExpandableListView(this);
		groupAdapter = new SimpleExpandableListAdapter(this, groups,
				allContacts);
		groupList.setAdapter(groupAdapter);
		groupList.setCacheColorHint(Color.TRANSPARENT);

		// Setup list view
		listContainer = (LinearLayout) this
				.findViewById(R.id.contact_picker_list_container);
		listContainer.addView(contactList, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		// Setup listeners
		initListener();

	}

	private int binarySearch(List<Contact> contacts, Contact key, int l, int u) {
		if (l > u)
			return -1;

		int m = (l + u) / 2;
		int c = contacts.get(m).compareTo(key);

		if (c == 0)
			return m;
		else if (c > 0)
			return binarySearch(contacts, key, l, m - 1);
		else
			return binarySearch(contacts, key, m + 1, u);
	}

	/**
	 * 初始化事件监听
	 */
	private void initListener() {
		this.contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Contact c = allContacts.get(arg2);
				c.chosen = !c.chosen;
				if (!c.chosen)
					groups.get(c.group).chosen = false;
				addContact();
				groupAdapter.notifyDataSetChanged();
				contactAdapter.notifyDataSetChanged();
			}

		});

		this.groupList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Contact c = allContacts.get(groups.get(groupPosition).getIndex(
						childPosition));
				c.chosen = !c.chosen;
				if (!c.chosen)
					groups.get(c.group).chosen = false;
				addContact();
				groupAdapter.notifyDataSetChanged();
				contactAdapter.notifyDataSetChanged();
				return false;
			}
		});

		this.switcher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (current) {
				case STATE_CONTACT: {
					listContainer.removeView(contactList);
					listContainer
							.addView(groupList, new LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT));
					frameContainer.removeView(pinyinContainer);
					previous = current;
					current = State.STATE_GROUP;
					break;
				}
				case STATE_GROUP: {
					listContainer.removeView(groupList);
					listContainer
							.addView(contactList, new LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT));
					frameContainer.addView(pinyinContainer);
					previous = current;
					current = State.STATE_CONTACT;
					break;
				}
				case STATE_ACPT: {
					// TODO
					break;
				}
				}
			}

		});
		
		this.contactList.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub0
				char[] key_c = allContacts.get(firstVisibleItem).key_arr;
				if(key_c[0]>='a'&&key_c[0]<='z')
					setSelectedLetter(key_c[0] - 'a' + 1);
				else
					setSelectedLetter(0);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	
	/**
	 * 将选中的联系人添加到复选文本框中
	 */
	private void addContact() {
		pickedContactContainer.removeAllViews();
		searchTxtBox = new EditText(this);
		int i = 0;
		TableRow row = null;
		for (Contact c : allContacts) {
			if (c.chosen) {
				if (i % 3 == 0) {
					row = new TableRow(this);
					pickedContactContainer.addView(row);
				}
				TextView tv = new TextView(this);
				tv.setText(c.name);
				row.addView(tv);
				i++;
			}
		}
		if(row==null||i%3==0){
			row = new TableRow(this);
			pickedContactContainer.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
			i++;
		}
		row.addView(this.searchTxtBox);
		int rows = i/3 + 1;
	} 
	
	/**
	 * 设置当前被选中的字母
	 * @param position
	 */
	private void setSelectedLetter(int position){
		
		this.letters.get(lastLetter).setBackgroundColor(Color.TRANSPARENT);
		this.letters.get(position).setBackgroundColor(Color.BLUE);
		this.lastLetter = position;
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}