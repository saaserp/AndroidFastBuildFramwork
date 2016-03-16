package com.wcgh.login;

import java.util.ArrayList;

import com.wcgh.framwork.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
OnItemClickListener, OnDismissListener {
	protected static final String TAG = "LoginActivity";
	private LinearLayout mLoginLinearLayout; // ��¼���ݵ�����
	private LinearLayout mUserIdLinearLayout; // ���������������ڴ������·���ʾ
	private Animation mTranslate; // λ�ƶ���
	private Dialog mLoginingDlg; // ��ʾ���ڵ�¼��Dialog
	private EditText mIdEditText; // ��¼ID�༭��
	private EditText mPwdEditText; // ��¼����༭��
	private ImageView mMoreUser; // ����ͼ��
	private Button mLoginButton; // ��¼��ť
	private ImageView mLoginMoreUserView; // ���������������İ�ť
	private String mIdString;
	private String mPwdString;
	private ArrayList<User> mUsers; // �û��б�
	private ListView mUserIdListView; // ������������ʾ��ListView����
	private MyAapter mAdapter; // ListView�ļ�����
	private PopupWindow mPop; // ����������

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		setListener();
		mLoginLinearLayout.startAnimation(mTranslate); // Y��ˮƽ�ƶ�

		/* ��ȡ�Ѿ�����õ��û����� */
		mUsers = Utils.getUserList(LoginActivity.this);

		if (mUsers.size() > 0) {
			/* ���б��еĵ�һ��user��ʾ�ڱ༭�� */
			mIdEditText.setText(mUsers.get(0).getId());
			mPwdEditText.setText(mUsers.get(0).getPwd());
		}

		LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
				R.layout.userifo_listview, null);
		mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
		parent.removeView(mUserIdListView); // �������븸�ӹ�ϵ,��Ȼ�ᱨ��
		mUserIdListView.setOnItemClickListener(this); // ���õ����
		mAdapter = new MyAapter(mUsers);
		mUserIdListView.setAdapter(mAdapter);

	}

	/* ListView�������� */
	class MyAapter extends ArrayAdapter<User> {

		public MyAapter(ArrayList<User> users) {
			super(LoginActivity.this, 0, users);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.listview_item, null);
			}

			TextView userIdText = (TextView) convertView
					.findViewById(R.id.listview_userid);
			userIdText.setText(getItem(position).getId());

			ImageView deleteUser = (ImageView) convertView
					.findViewById(R.id.login_delete_user);
			deleteUser.setOnClickListener(new OnClickListener() {
				// ���ɾ��deleteUserʱ,��mUsers��ɾ��ѡ�е�Ԫ��
				@Override
				public void onClick(View v) {

					if (getItem(position).getId().equals(mIdString)) {
						// ���Ҫɾ�����û�Id��Id�༭��ǰֵ��ȣ������
						mIdString = "";
						mPwdString = "";
						mIdEditText.setText(mIdString);
						mPwdEditText.setText(mPwdString);
					}
					mUsers.remove(getItem(position));
					mAdapter.notifyDataSetChanged(); // ����ListView
				}
			});
			return convertView;
		}

	}

	private void setListener() {
		mIdEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mIdString = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		mPwdEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mPwdString = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		mLoginButton.setOnClickListener(this);
		mLoginMoreUserView.setOnClickListener(this);
	}

	private void initView() {
		mIdEditText = (EditText) findViewById(R.id.login_edtId);
		mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
		mMoreUser = (ImageView) findViewById(R.id.login_more_user);
		mLoginButton = (Button) findViewById(R.id.login_btnLogin);
		mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
		mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
		mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
		mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // ��ʼ����������
		initLoginingDlg();
	}

	public void initPop() {
		int width = mUserIdLinearLayout.getWidth() - 4;
		int height = LayoutParams.WRAP_CONTENT;
		mPop = new PopupWindow(mUserIdListView, width, height, true);
		mPop.setOnDismissListener(this);// ���õ���������ʧʱ������

		// ע��Ҫ�������룬�������������������Ż��ô�����ʧ
		mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

	}

	/* ��ʼ�����ڵ�¼�Ի��� */
	private void initLoginingDlg() {

		mLoginingDlg = new Dialog(this, R.style.loginingDlg);
		mLoginingDlg.setContentView(R.layout.logining_dlg);

		Window window = mLoginingDlg.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		// ��ȡ��mLoginingDlg�����ĵ�ǰ���ڵ����ԣ��Ӷ�����������Ļ����ʾ��λ��

		// ��ȡ��Ļ�ĸ߿�
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int cxScreen = dm.widthPixels;
		int cyScreen = dm.heightPixels;

		int height = (int) getResources().getDimension(
				R.dimen.loginingdlg_height);// ��42dp
		int lrMargin = (int) getResources().getDimension(
				R.dimen.loginingdlg_lr_margin); // ���ұ���10dp
		int topMargin = (int) getResources().getDimension(
				R.dimen.loginingdlg_top_margin); // ����20dp

		params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		/* �Ի���Ĭ��λ������Ļ����,����x,y��ʾ�˿ؼ���"��Ļ����"��ƫ���� */

		params.width = cxScreen;
		params.height = height;
		// width,height��ʾmLoginingDlg��ʵ�ʴ�С

		mLoginingDlg.setCanceledOnTouchOutside(true); // ���õ��Dialog�ⲿ��������ر�Dialog
	}

	/* ��ʾ���ڵ�¼�Ի��� */
	private void showLoginingDlg() {
		if (mLoginingDlg != null)
			mLoginingDlg.show();
	}

	/* �ر����ڵ�¼�Ի��� */
	private void closeLoginingDlg() {
		if (mLoginingDlg != null && mLoginingDlg.isShowing())
			mLoginingDlg.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnLogin:
			// ������¼
			showLoginingDlg(); // ��ʾ"���ڵ�¼"�Ի���,��Ϊ��Demoû�е�¼��web������,����Ч�����ܿ�����.���Խ�����ʹ��
			Log.i(TAG, mIdString + "  " + mPwdString);
			if (mIdString == null || mIdString.equals("")) { // �˺�Ϊ��ʱ
				Toast.makeText(LoginActivity.this, "�������˺�", Toast.LENGTH_SHORT)
				.show();
			} else if (mPwdString == null || mPwdString.equals("")) {// ����Ϊ��ʱ
				Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_SHORT)
				.show();
			} else {// �˺ź����붼��Ϊ��ʱ
				boolean mIsSave = true;
				try {
					Log.i(TAG, "�����û��б�");
					for (User user : mUsers) { // �жϱ����ĵ��Ƿ��д�ID�û�
						if (user.getId().equals(mIdString)) {
							mIsSave = false;
							break;
						}
					}
					if (mIsSave) { 
						User user = new User(mIdString, mPwdString);
						mUsers.add(user);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				closeLoginingDlg(); 
				Toast.makeText(this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		case R.id.login_more_user:  
			if (mPop == null) {
				initPop();
			}
			if (!mPop.isShowing() && mUsers.size() > 0) {
			 
				mMoreUser.setImageResource(R.drawable.login_more_down);  
				mPop.showAsDropDown(mUserIdLinearLayout, 2, 1);  
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mIdEditText.setText(mUsers.get(position).getId());
		mPwdEditText.setText(mUsers.get(position).getPwd());
		mPop.dismiss();
	}

	 
	@Override
	public void onDismiss() {
	 
		mMoreUser.setImageResource(R.drawable.login_more_up);
	}

	 
	@Override
	public void onPause() {
		super.onPause();
		try {
			Utils.saveUserList(LoginActivity.this, mUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
