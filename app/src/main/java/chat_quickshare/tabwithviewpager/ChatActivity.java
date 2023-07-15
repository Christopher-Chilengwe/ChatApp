package chat_quickshare.tabwithviewpager;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import chat_quickshare.adapter.ChatAdapterRecycler;
import chat_quickshare.adapter.ChatModel;
import chat_quickshare.adapter.CommentAdpater;
import chat_quickshare.adapter.CommentModelObject;
import chat_quickshare.model.Upload;
import chat_quickshare.multipart.EndPoints;
import chat_quickshare.multipart.VolleyMultipartRequest;
import chat_quickshare.tabwithviewpager.ViewPager.TabWOIconActivity;
import chat_quickshare.tabwithviewpager.audiorecod.VoiceRecordingActivity;
import chat_quickshare.tabwithviewpager.notifi.ConfigurationsFCM;

import static android.Manifest.permission.CAMERA;

public class ChatActivity extends AppCompatActivity {


	private String selectedPath;
	private static final int SELECT_VIDEO = 3;
	Bitmap myBitmap;
	private static final String IMAGE_DIRECTORY = "/demonuts";
	private int GALLERY = 1;
	private ArrayList<String> permissionsToRequest;
	private ArrayList<String> permissionsRejected = new ArrayList<>();
	private ArrayList<String> permissions = new ArrayList<>();
	private static final int PERMISSION_REQUEST_CODE = 1;
	private final static int ALL_PERMISSIONS_RESULT = 107;
	private int mInterval = 5000; // 5 seconds by default, can be changed later
	private Handler mHandler;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView mRecyclerView;
	// label to display gcm messages
	//TextView lblMessage;
	LinearLayout bk;
	LinearLayout linearLayout;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	ArrayList<ChatModel> results;
	TextView textView1;
	TextView textView22;
	TextView textView33;
	public static String name;
	public static String email;
	ImageView chatbtn;
	EditText textmsg;
	SharedPreferences mpreferences;
	SharedPreferences.Editor settingDataPrefe;
	private RecyclerView.LayoutManager mLayoutManager;
	ArrayList<String> textMsg = new ArrayList<String>();
	ArrayList<String> frommy = new ArrayList<String>();
	private static final String TAG = ChatActivity.class.getSimpleName();
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	AppController object;
	TextView headetxt;
	String msgType="";
	Handler handler;
	ImageButton chatbtn1;
	LinearLayout callinglay;



	private void chooseVideo() {
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		object=(AppController)getApplicationContext();
	//	getSupportActionBar().setTitle(object.getChatUserName());
		callinglay=(LinearLayout)findViewById(R.id.callinglay);
		setContentView(R.layout.chatrow);
		ImageView  drawericon=(ImageView)findViewById(R.id.backicon);
		chatbtn1=(ImageButton)findViewById(R.id.chatbtn1);

		drawericon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mpreferences = getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
		settingDataPrefe = mpreferences.edit();
		mRecyclerView = (RecyclerView) findViewById(R.id.commentlist);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		headetxt=(TextView)findViewById(R.id.headetxt);
		headetxt.setText(object.getChatUserName());
		handler = new Handler();
		
	private void requestPermission() {

		if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
			Toast.makeText(ChatActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
		} else {
			ActivityCompat.requestPermissions(ChatActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
		}
	}
	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}
	private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
		ArrayList<String> result = new ArrayList<String>();

		for (String perm : wanted) {
			if (!hasPermission(perm)) {
				result.add(perm);
			}
		}

		return result;
	}

	private boolean hasPermission(String permission) {
		if (canMakeSmores()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				return (ChatActivity.this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);

			}
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);



			if (resultCode == this.RESULT_CANCELED) {
				return;
			}
			if (requestCode == GALLERY) {
				if (data != null) {
					Uri contentURI = data.getData();
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
						myBitmap = getResizedBitmap(bitmap, 500);
						String path = saveImage(bitmap);
						uploadBitmap(myBitmap);
						//showDialog(myBitmap);
						//  Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
						//   imageview.setImageBitmap(bitmap);

					} catch (IOException e) {
						e.printStackTrace();
						/// Toast.makeText(SectionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
					}
				}

			} else if (requestCode == 2) {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				myBitmap = getResizedBitmap(thumbnail, 500);
				//imageview.setImageBitmap(thumbnail);
				saveImage(thumbnail);
				uploadBitmap(myBitmap);
				//showDialog(myBitmap);
				//Toast.makeText(SectionActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
			}

			if (resultCode == RESULT_OK) {
				if (requestCode == SELECT_VIDEO) {
					System.out.println("SELECT_VIDEO");
					Uri selectedImageUri = data.getData();
					selectedPath = getPath(selectedImageUri);

					uploadVideo();
					//textView.setText(selectedPath);
				}
			}





		}
	public String getPath(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = getContentResolver().query(
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
		cursor.close();

		return path;
	}
	public String saveImage(Bitmap myBitmap) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		File wallpaperDirectory = new File(
				Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
		// have the object build the directory structure, if needed.
		if (!wallpaperDirectory.exists()) {
			wallpaperDirectory.mkdirs();
		}

		try {
			File f = new File(wallpaperDirectory, Calendar.getInstance()
					.getTimeInMillis() + ".jpg");
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			MediaScannerConnection.scanFile(this,
					new String[]{f.getPath()},
					new String[]{"image/jpeg"}, null);
			fo.close();
			Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

			return f.getAbsolutePath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	private boolean canMakeSmores() {
		return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
	}

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch (requestCode) {

			case ALL_PERMISSIONS_RESULT:
				for (String perms : permissionsToRequest) {
					if (hasPermission(perms)) {
						// Toast.makeText(getActivity(),"has perm 1",Toast.LENGTH_LONG).show();
					} else {
						// Toast.makeText(getActivity(),"has perm 0",Toast.LENGTH_LONG).show();
						permissionsRejected.add(perms);
					}
				}

				if (permissionsRejected.size() > 0) {


					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
							showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

												//Log.d("API123", "permisionrejected " + permissionsRejected.size());

												requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
											}
										}
									});
							return;
						}
					}

				}
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.e("value", "Permission Granted, Now you can use local drive .");
				} else {
					Log.e("value", "Permission Denied, You cannot use local drive .");
				}
				break;

		}

	}
	private void showPictureDialog(){
		AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ChatActivity.this);
		pictureDialog.setTitle("Select Action");
		String[] pictureDialogItems = {
				"Select photo from gallery",
				"Take picture from camera",
				"Select video from gallery"
		};
		pictureDialog.setItems(pictureDialogItems,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								choosePhotoFromGallary();

								break;
							case 1:
								takePhotoFromCamera();

								break;
							case 2:

								if (Build.VERSION.SDK_INT >= 23)
								{
									if (checkPermission())
									{
										// Code for above or equal 23 API Oriented Device
										// Your Permission granted already .Do next code
										chooseVideo();
									} else {
										requestPermission(); // Code for permission
									}
								}
								else
								{

									// Code for Below 23 API Oriented Device
									// Do next code
								}



								break;
						}
					}
				});
		pictureDialog.show();
	}
	public void choosePhotoFromGallary() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(galleryIntent, GALLERY);
	}

	private void takePhotoFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 2);
	}
	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new android.support.v7.app.AlertDialog.Builder(ChatActivity.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}
	// Fetches reg id from shared preferences
	// and displays on the screen
	private void displayFirebaseRegId() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigurationsFCM.SHARED_PREF, 0);
		String regId = pref.getString("regId", null);

		Log.e("tag", "Firebase reg id: " + regId);

			uuid=uuId;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			String Rid=object.getMobileNoSendOnMessage();

			ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();

			param1.add(new BasicNameValuePair("sID", uuid));
			param1.add(new BasicNameValuePair("rID", Rid));


			try {
				respo=CustomHttpClient.executeHttpPost(AppController.baseURL+AppController.getChat,param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respo;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);


			try{
				JSONObject jObje=new JSONObject(s);
				JSONObject jObjectName=jObje.getJSONObject("response");
				String msg=jObjectName.getString("message");
				String flg=jObjectName.getString("status");
				results = new ArrayList<ChatModel>();
				results.clear();
				if(flg.equals("1")){
					JSONArray jArr=jObjectName.getJSONArray("data");

					for(int i=0;i<jArr.length();i++)
					{
						JSONObject jIndexObj=jArr.getJSONObject(i);
						ChatModel obj = new ChatModel(jIndexObj.getString("receiver_id"),
								jIndexObj.getString("sender_id"),
								// jIndexObj.getString("Unreaded"),
								jIndexObj.getString("message"),jIndexObj.getString("whoReceive")
								,jIndexObj.getString("message_type"),jIndexObj.getString("multimideaMessage"));
						results.add(i, obj);
					}

					ChatAdapterRecycler	mAdapter1 = new ChatAdapterRecycler(results);
					mRecyclerView.setAdapter(mAdapter1);
					mRecyclerView.scrollToPosition(mAdapter1.getItemCount()-1);
				}else {
					//Toast.makeText(getActivity(),"Data not found",Toast.LENGTH_LONG).show();

				}

				// CreateDB();
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	// recvd message
class Recvd extends FirebaseMessagingService
{
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Log.e(TAG, "From: " + remoteMessage.getFrom());

		if (remoteMessage == null)
			return;

		// Check if message contains a notification payload.
		if (remoteMessage.getNotification() != null) {
			Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

		}

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

			try {
				JSONObject json = new JSONObject(remoteMessage.getData().toString());

			} catch (Exception e) {
				Log.e(TAG, "Exception: " + e.getMessage());
			}
		}
	}
}


	private void uploadBitmap(final Bitmap bitmap) {



		//our custom volley request
		VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.ROOT_URL,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						//pd.dismiss();
						Toast.makeText(ChatActivity.this, "" + response, Toast.LENGTH_LONG).show();

						Intent intent = getIntent();
						finish();
						startActivity(intent);

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */


			@Override
			protected Map<String, String> getParams() throws AuthFailureError {


				String Sid= mpreferences.getString("uid","0");  //object.getUID();
				String Rid=object.getMobileNoSendOnMessage();
				Map<String, String> params = new HashMap<>();

				params.put("sid", ""+Sid);
				params.put("rid", ""+Rid);

				return params;
			}

			/*
            * Here we are passing image by renaming it with a unique name
            * */
			@Override
			protected Map<String, DataPart> getByteData() {
				Map<String, DataPart> params = new HashMap<>();
				long imagename = System.currentTimeMillis();
				params.put("photoimg", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
				return params;
			}
		};

		//adding the request to volley
		Volley.newRequestQueue(this).add(volleyMultipartRequest);
	}

	public byte[] getFileDataFromDrawable(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}


	private void uploadVideo() {
		class UploadVideo extends AsyncTask<Void, Void, String> {

			ProgressDialog uploading;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				uploading = ProgressDialog.show(ChatActivity.this, "Uploading File", "Please wait...", false, false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				uploading.dismiss();
			//	textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
			//	textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
			}

			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			protected String doInBackground(Void... params) {
				String Sid= mpreferences.getString("uid","0");  //object.getUID();
				String Rid=object.getMobileNoSendOnMessage();
				Upload u = new Upload();
				String msg = u.uploadVideo(selectedPath,Sid,Rid);
				return msg;
			}
		}
		UploadVideo uv = new UploadVideo();
		uv.execute();
	}


}
