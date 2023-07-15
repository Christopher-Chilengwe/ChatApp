package chat_quickshare.tabwithviewpager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chat_quickshare.adapter.CommentAdpater;
import chat_quickshare.adapter.CommentModelObject;
import chat_quickshare.dbhelper.Contact;
import chat_quickshare.dbhelper.DatabaseHandler;
import chat_quickshare.model.My_Service;

/**
 * Created by Lannet1 on 6/22/2018.
 */

public class ContactListActivity extends AppCompatActivity {


    private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

    private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

    @SuppressLint("InlinedApi")
    private final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };


    Cursor phones, email;
    ContentResolver resolver;
    private RecyclerView recyclerView;
    CommentAdpater mAdapter;
    ArrayList<CommentModelObject> results;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final int REQUEST_READ_CONTACTS = 79;
    ProgressDialog pd;
    DatabaseHandler helperDB;
    List<Contact> contacts;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    String jsonPrestring="{\"Data\":[";
    String jsonPostString="]}";
    StringBuilder jsonAppend=new StringBuilder();
    String totaltxt="";
    AppController gObject;
    //TextView cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setTitle("Verify your mobile number");
      // getSupportActionBar().setTitle("Select Contact");
        setContentView(R.layout.contact_list);
        helperDB=new DatabaseHandler(ContactListActivity.this);
        gObject=(AppController)getApplicationContext();
        ImageView  drawericon=(ImageView)findViewById(R.id.backicon);
        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mpreferences = getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();

        EditText searchitem=(EditText)findViewById(R.id.searchitem);
        searchitem. addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s);
                // TODO Auto-generated method stub
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.commentlist);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
        String noCouter=mpreferences.getString("totalContact","0");
        if(Integer.parseInt(noCouter)<=0) {

    new OrgerPlaced().execute();
    /*new AsyncTask<Void, Void, Void>() {
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(ContactListActivity.this,
                    "Loading..", "Please Wait", true, false);
        }// End of onPreExecute method

        @Override
        protected Void doInBackground(Void... params) {
            getContacts();

            return null;
        }// End of doInBackground method

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();

//                    CommentAdpater mAdapter = new CommentAdpater(results,ContactListActivity.this);
//                    recyclerView.setAdapter(mAdapter);
//            String uuid=mpreferences.getString("uid","0");
//            new ContactList(uuid).execute();

        }//End of onPostExecute method
    }.execute((Void[]) null);*/

}
else
{
    String uuid=mpreferences.getString("uid","0");
    new ContactList(uuid).execute();

  // getCartData();
}


        } else {
            requestLocationPermission();
        }



        Intent i = new Intent(this, My_Service.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        final Calendar time = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC, 1000,pendingIntent );
        alarmManager.setRepeating(AlarmManager.RTC, 500
                , 500, pendingIntent);


    }
    protected void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String noCouter=mpreferences.getString("totalContact","0");
                    if(Integer.parseInt(noCouter)<=0) {

                        new OrgerPlaced().execute();

                      /*  new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                pd = ProgressDialog.show(ContactListActivity.this,
                                        "Loading..", "Please Wait", true, false);
                            }// End of onPreExecute method

                            @Override
                            protected Void doInBackground(Void... params) {
                                getContacts();

                                return null;
                            }// End of doInBackground method

                            @Override
                            protected void onPostExecute(Void result) {
                                pd.dismiss();
//                                CommentAdpater mAdapter = new CommentAdpater(results,ContactListActivity.this);
//                                recyclerView.setAdapter(mAdapter);

//                                String uuid=mpreferences.getString("uid","0");
//                                new ContactList(uuid).execute();


                            }//End of onPostExecute method
                        }.execute((Void[]) null);*/
                    }
                    else
                    {

                        String uuid=mpreferences.getString("uid","0");
                        new ContactList(uuid).execute();
                       //getCartData();
                    }

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
    public void getCartData()
    {

        int i=0;
        results=new ArrayList<CommentModelObject>();
         contacts= helperDB.getAllContacts();
        // List<Contact> contacts = dbhep.getAllContacts(uidstr);
        for (Contact cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);

            String nameTemp = cn.getName();
            String phoneNumberTemp = cn.getPhoneNumber();
            String uIdTemp = mpreferences.getString("uid","0");
            String addedByid =mpreferences.getString("uid","0");;


            jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

            Toast.makeText(ContactListActivity.this,""+cn.getName(),Toast.LENGTH_LONG).show();
//            CommentModelObject obj = new CommentModelObject(
//                    cn.getName(),cn.getPhoneNumber());
//            results.add(i, obj);

i++;
        }
        String templnf=jsonPrestring+jsonAppend+jsonPostString;
        totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
      //  totaltxt =jsonPrestring+"{\"added_by_id\":\"1\",\"contact_name\":\"Gyan Yadav\", \"contact_mobile\":\"+918750680670\", \"user_id\":\"1\"},{\"added_by_id\":\"1\",\"contact_name\":\"isoft\", \"contact_mobile\":\"+911204336443\", \"user_id\":\"1\"},"+jsonPostString;
        //  jsonString= list.toString();
        try {
            JSONObject objectcheck = new JSONObject(totaltxt);
            JSONArray jCheckArr=objectcheck.getJSONArray("Data");
            if(jCheckArr.length()>0)
            {
                //  new SendPostRequest().execute();
                // datastr =totaltxt.substring(0,totaltxt.length()-3)+jsonPostString;
                boolean checkConnection=gObject.isNetworkAvailable();
                if(checkConnection) {
                    Log.e("msg",totaltxt);
                    new OrgerPlaced1(totaltxt).execute();
                }
                else
                {


                    Toast.makeText(ContactListActivity.this,"Internet Connection Not Available",Toast.LENGTH_SHORT).show();
                }

                // new HttpAsyncTask().execute(AppController.baseURL+AppController.getbuddyURL);
            }
            else
            {
                Toast.makeText(ContactListActivity.this,"No Contact",Toast.LENGTH_SHORT).show();
                // alertDisplay("Cart is Empty, please add deal(s) ");
            }
        }catch (Exception e)
        {
            e.printStackTrace();

        }

//        CommentAdpater mAdapter = new CommentAdpater(results,ContactListActivity.this);
//        recyclerView.setAdapter(mAdapter);

    }

    public void getContacts() {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        results=new ArrayList<CommentModelObject>();
        // Loop for every contact in the phone
        int i=0;
        settingDataPrefe.putString("totalContact",""+cursor.getCount());
        settingDataPrefe.commit();
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    // Query and loop for every phone number of the contact

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);


                        String nameTemp = name;
                        String phoneNumberTemp = phoneNumber;
                        String uIdTemp = mpreferences.getString("uid","0");
                        String addedByid =mpreferences.getString("uid","0");;


                        jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                        Contact obj1= new Contact(name, phoneNumber);
                        helperDB.addContact(obj1);
//                            CommentModelObject obj = new CommentModelObject(
//                                    name,phoneNumber);
//                            results.add(i, obj);
                        i++;
                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\nEmail:" + email);

                    }

                    emailCursor.close();
                }
                String templnf=jsonPrestring+jsonAppend+jsonPostString;
                totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
                new OrgerPlaced1(totaltxt).execute();
               // totaltxt =jsonPrestring+jsonAppend+jsonPostString;
               // totaltxt =jsonPrestring+jsonAppend+jsonPostString;
                //  jsonString= list.toString();
                try {
                    JSONObject objectcheck = new JSONObject(totaltxt);
                    JSONArray jCheckArr=objectcheck.getJSONArray("Data");
                    if(jCheckArr.length()>0)
                    {
                        //  new SendPostRequest().execute();
                        // datastr =totaltxt.substring(0,totaltxt.length()-3)+jsonPostString;
                        boolean checkConnection=gObject.isNetworkAvailable();
                        if(checkConnection) {

                            new OrgerPlaced1(totaltxt).execute();
                        }
                        else
                        {


                            Toast.makeText(ContactListActivity.this,"Internet Connection Not Available",Toast.LENGTH_SHORT).show();
                        }

                        // new HttpAsyncTask().execute(AppController.baseURL+AppController.getbuddyURL);
                    }
                    else
                    {
                        Toast.makeText(ContactListActivity.this,"No Contact",Toast.LENGTH_SHORT).show();
                        // alertDisplay("Cart is Empty, please add deal(s) ");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();

                }
                output.append("\n");
            }

           // cnt.setText(output.toString());
        }



    }



    class OrgerPlaced extends AsyncTask<String,String,String> {

        String orden = "";
        String datastr;
        String respo;
        ProgressDialog pd;
        String paytyp;
        String totamot;

       /* OrgerPlaced(String dataString) {

            datastr =dataString;        //dataString.substring(0, dataString.length() - 3) + jsonPostString;
            Log.e("msg",datastr);
        }*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ContactListActivity.this);
            pd.setMessage("Preparing contact list.....");
            pd.setCancelable(true);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {



                String phoneNumber = null;
                String email = null;

                Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
                String _ID = ContactsContract.Contacts._ID;
                String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

                Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
                String DATA = ContactsContract.CommonDataKinds.Email.DATA;

                StringBuffer output = new StringBuffer();

                ContentResolver contentResolver = getContentResolver();

                Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
                results=new ArrayList<CommentModelObject>();
                // Loop for every contact in the phone
                int i=0;
                settingDataPrefe.putString("totalContact",""+cursor.getCount());
                settingDataPrefe.commit();
                int totalno=cursor.getCount();
                if ( totalno> 0) {

                    while (cursor.moveToNext()) {

                        String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                        String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                        if (hasPhoneNumber > 0) {

                            output.append("\n First Name:" + name);

                            // Query and loop for every phone number of the contact

                            Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                                output.append("\n Phone number:" + phoneNumber);


                                String nameTemp = name;
                                String phoneNumberTemp = phoneNumber;
                                String uIdTemp = mpreferences.getString("uid","0");
                                String addedByid =mpreferences.getString("uid","0");;


                                jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                                Contact obj1= new Contact(name, phoneNumber);
                                helperDB.addContact(obj1);
//                            CommentModelObject obj = new CommentModelObject(
//                                    name,phoneNumber);
//                            results.add(i, obj);
                                i++;
                            }

                            phoneCursor.close();

                            // Query and loop for every email of the contact
                            Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                            while (emailCursor.moveToNext()) {

                                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                                output.append("\nEmail:" + email);

                            }

                            emailCursor.close();

                        }

                        String templnf=jsonPrestring+jsonAppend+jsonPostString;
                        totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
                        String uuid=mpreferences.getString("uid","0");
                        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                        nameValuePair.add(new BasicNameValuePair("rawData", totaltxt));
                        nameValuePair.add(new BasicNameValuePair("uid", uuid));

                        String baseurl = AppController.baseURL+AppController.mapContact;
                        respo = CustomHttpClient.executeHttpPost(baseurl, nameValuePair);
                      //  cursor.close();

                        output.append("\n");
                    }



                    // cnt.setText(output.toString());
                }










            } catch (Exception e) {
                e.printStackTrace();
            }
            return respo;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            try {
                JSONObject jObject = new JSONObject(s);
                JSONObject jObjectName=jObject.getJSONObject("response");
                String msg=jObjectName.getString("message");
                String flg=jObjectName.getString("status");
                if(flg.equals("1"))
                {

                    JSONObject jObj=new JSONObject(s);
                    JSONObject jRes=jObj.getJSONObject("response");
                    String stus=jRes.getString("status");
                    results=new ArrayList<CommentModelObject>();
                    if(stus.equals("1")) {

                        JSONArray jArrObject = jRes.getJSONArray("data");

                        for (int i = 0; i < jArrObject.length(); i++) {
                            JSONObject jIndexObj = jArrObject.getJSONObject(i);

                            CommentModelObject obj = new CommentModelObject(
                                    jIndexObj.getString("name"),jIndexObj.getString("contact_mobile"),jIndexObj.getString("contact_status"));
                            results.add(i, obj);
                        }

                        mAdapter = new CommentAdpater(results,ContactListActivity.this);
                        recyclerView.setAdapter(mAdapter);

                    }else {


                    }
                }
                else
                {

                }

                // Toast.makeText( getActivity(),  " "+jObject, Toast.LENGTH_LONG ).show();
                //orden = jObject.getString("orderno");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class OrgerPlaced1 extends AsyncTask<String,String,String> {

        String orden = "";
        String datastr;
        String respo;
        ProgressDialog pd;
        String paytyp;
        String totamot;
         OrgerPlaced1(String dataString) {

             datastr =dataString;        //dataString.substring(0, dataString.length() - 3) + jsonPostString;
             Log.e("msg",datastr);
         }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ContactListActivity.this);
            pd.setMessage("Please wait.....");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                if (phones != null) {
                    Log.e("count", "" + phones.getCount());
                    if (phones.getCount() == 0) {
                        Toast.makeText(ContactListActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                    }

                    while (phones.moveToNext()) {
                        Bitmap bit_thumb = null;
                        String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                        String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                        String nameTemp = name;
                        String phoneNumberTemp = phoneNumber;
                        String uIdTemp = mpreferences.getString("uid","0");
                        String addedByid =mpreferences.getString("uid","0");;


                        jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                        Contact obj1= new Contact(name, phoneNumber);
                        helperDB.addContact(obj1);


                        try {
                            if (image_thumb != null) {
                                bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                            } else {
                                Log.e("No Image Thumb", "--------------");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    String templnf=jsonPrestring+jsonAppend+jsonPostString;
                    totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
                } else {
                    Log.e("Cursor close 1", "----------------");
                }

          //      new LoadContact().execute();
               // getAllContacts();





/*


                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        // get the contact's information
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                        Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        // get the user's email address
                        String email = null;
                        Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (ce != null && ce.moveToFirst()) {
                            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ce.close();
                        }

                        // get the user's phone number
                        String phone = null;
                        if (hasPhone > 0) {
                            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (cp != null && cp.moveToFirst()) {
                                phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                cp.close();
                            }
                        }

                        // if the user user has an email or phone then add it to contacts
                        if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {
                          */
/*  Contact contact = new Contact();
                            contact.name = name;
                            contact.email = email;
                            contact.phoneNumber = phone;
                            contacts.add(contact);*//*


                            String nameTemp = name;
                            String phoneNumberTemp = phone;
                            String uIdTemp = mpreferences.getString("uid","0");
                            String addedByid =mpreferences.getString("uid","0");;


                            jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                            Contact obj1= new Contact(name, phoneNumberTemp);
                            helperDB.addContact(obj1);


                        }




                    } while (cursor.moveToNext());
                    String templnf=jsonPrestring+jsonAppend+jsonPostString;
                    totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
                    // clean up cursor
                    cursor.close();
                }

*/




                String uuid=mpreferences.getString("uid","0");
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("rawData", totaltxt));
                nameValuePair.add(new BasicNameValuePair("uid", uuid));

                String baseurl = AppController.baseURL+AppController.mapContact;
                respo = CustomHttpClient.executeHttpPost(baseurl, nameValuePair);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return respo;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            try {
                JSONObject jObject = new JSONObject(s);
                JSONObject jObjectName=jObject.getJSONObject("response");
                String msg=jObjectName.getString("message");
                String flg=jObjectName.getString("status");
                if(flg.equals("1"))
                {

                    JSONObject jObj=new JSONObject(s);
                    JSONObject jRes=jObj.getJSONObject("response");
                    String stus=jRes.getString("status");
                    results=new ArrayList<CommentModelObject>();
                    if(stus.equals("1")) {

                        JSONArray jArrObject = jRes.getJSONArray("data");

                        for (int i = 0; i < jArrObject.length(); i++) {
                            JSONObject jIndexObj = jArrObject.getJSONObject(i);

                            CommentModelObject obj = new CommentModelObject(
                                    jIndexObj.getString("name"),jIndexObj.getString("contact_mobile"),jIndexObj.getString("contact_status"));
                            results.add(i, obj);
                        }

                        CommentAdpater mAdapter = new CommentAdpater(results,ContactListActivity.this);
                        recyclerView.setAdapter(mAdapter);

                    }else {


                    }
                }
                else
                {

                }

                // Toast.makeText( getActivity(),  " "+jObject, Toast.LENGTH_LONG ).show();
                //orden = jObject.getString("orderno");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    class ContactList extends AsyncTask<String, String, String> {

        String respo;
        ProgressDialog pd;
        String mobNo;
        String uIdTemp;
        ContactList(String uid)
        {
            uIdTemp=uid;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ContactListActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub +"state_id="+citytem;



            String url =AppController.baseURL+AppController.getMapContact+"uid="+uIdTemp;
            try {
                respo = CustomHttpClient.urlincoding(url);
                // jRespons = CustomHttpClient.executeHttpGet(url);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return respo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try{


                JSONObject jObj=new JSONObject(s);
                JSONObject jRes=jObj.getJSONObject("response");
                String stus=jRes.getString("status");
                results=new ArrayList<CommentModelObject>();
                if(stus.equals("1")) {

                    JSONArray jArrObject = jRes.getJSONArray("data");

                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject jIndexObj = jArrObject.getJSONObject(i);

                        CommentModelObject obj = new CommentModelObject(
                                jIndexObj.getString("name"),jIndexObj.getString("contact_mobile"),jIndexObj.getString("contact_status"));
                        results.add(i, obj);
                    }

                     mAdapter = new CommentAdpater(results,ContactListActivity.this);
                    recyclerView.setAdapter(mAdapter);

                }else {


                }

                // CreateDB();,
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }





    private void getAllContacts() {
        String phoneNumber="";
        String name="";
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));



                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }

                }
                String nameTemp = name;
                String phoneNumberTemp = phoneNumber;
                String uIdTemp = mpreferences.getString("uid","0");
                String addedByid =mpreferences.getString("uid","0");;


                jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                Contact obj1= new Contact(name, phoneNumber);
                helperDB.addContact(obj1);
            }


          /*  AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getApplicationContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);*/
        }
        String templnf=jsonPrestring+jsonAppend+jsonPostString;
        totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
    }


    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(ContactListActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    String nameTemp = name;
                    String phoneNumberTemp = phoneNumber;
                    String uIdTemp = mpreferences.getString("uid","0");
                    String addedByid =mpreferences.getString("uid","0");;


                    jsonAppend.append("{\"added_by_id\":\""+addedByid+"\",\"contact_name\":\""+nameTemp+"\", \"contact_mobile\":\""+phoneNumberTemp+"\", \"user_id\":\""+uIdTemp+"\"},");

                    Contact obj1= new Contact(name, phoneNumber);
                    helperDB.addContact(obj1);


                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                   /* SelectUser selectUser = new SelectUser();
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(id);
                    selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);*/
                }
                String templnf=jsonPrestring+jsonAppend+jsonPostString;
                totaltxt =templnf.substring(0, templnf.length() - 3)+jsonPostString ;
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          /*  adapter = new SelectUserAdapter(selectUsers, MainActivity.this);
            listView.setAdapter(adapter);

            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    SelectUser data = selectUsers.get(i);
                }
            });

            listView.setFastScrollEnabled(true);*/
        }
    }

/*
    private class FetchContacts extends AsyncTask<Void, Void, ArrayList<Contact>> {

        private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        @SuppressLint("InlinedApi")
        private final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        @Override
        protected ArrayList<LearnSaveContact> doInBackground(Void... params) {
            try {
                ArrayList<Contact> contacts = new ArrayList<>();

                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        // get the contact's information
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                        Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        // get the user's email address
                        String email = null;
                        Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (ce != null && ce.moveToFirst()) {
                            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ce.close();
                        }

                        // get the user's phone number
                        String phone = null;
                        if (hasPhone > 0) {
                            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (cp != null && cp.moveToFirst()) {
                                phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                cp.close();
                            }
                        }

                        // if the user user has an email or phone then add it to contacts
                        if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {
                            Contact contact = new Contact();
                            contact.name = name;
                            contact.email = email;
                            contact.phoneNumber = phone;
                            contacts.add(contact);
                        }

                    } while (cursor.moveToNext());

                    // clean up cursor
                    cursor.close();
                }

                return contacts;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            if (contacts != null) {
                // success
                mContacts = contacts;
            } else {
                // show failure
                // syncFailed();
            }
        }
    }*/
}
