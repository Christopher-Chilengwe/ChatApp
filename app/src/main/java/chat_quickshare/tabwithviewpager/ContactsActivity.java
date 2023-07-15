package chat_quickshare.tabwithviewpager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import chat_quickshare.dbhelper.DatabaseHandler;
import chat_quickshare.dbhelper.GroupContact;
import chat_quickshare.model.Model;
import chat_quickshare.model.MultipleSelectedAdapter;
import chat_quickshare.tabwithviewpager.ViewPager.TabWOIconActivity;

public class ContactsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<Model> modelArrayList;
    private MultipleSelectedAdapter customAdapter;
    private Button btnselect, btndeselect, btnnext;
    private  String[] animallist = new String[]{"Lion", "Tiger", "Leopard", "Cat"};
    private RecyclerView.LayoutManager mLayoutManager;
    Typeface fontCustome;
    ImageView drawericon;
    AppController obj;
    ImageView creategrp;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    DatabaseHandler helperDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setTitle("Create Group");
        setContentView(R.layout.contact_activity);
        helperDB=new DatabaseHandler(ContactsActivity.this);
        fontCustome= Typeface.createFromAsset(getAssets(),"Raleway_Regular.ttf");
        obj=(AppController)getApplicationContext();
        mpreferences =getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();

        ImageView  drawericon=(ImageView)findViewById(R.id.backicon);

        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        creategrp=(ImageView)findViewById(R.id.creategrp);
        creategrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ContactsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout_ask);
                final EditText grpnametext=(EditText)dialog.findViewById(R.id.grpnametext);
                RelativeLayout yesbtn=(RelativeLayout)dialog.findViewById(R.id.yesbtn);
                RelativeLayout nobtn=(RelativeLayout)dialog.findViewById(R.id.nobtn);
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupContact obj1=null;
                        String gname=grpnametext.getText().toString();
                        String getSubStr=obj.getSelectedContact().substring(0,obj.getSelectedContact().length()-1);
                        String uidStr=mpreferences.getString("uid","0");
                        Toast.makeText(ContactsActivity.this,""+getSubStr,Toast.LENGTH_LONG).show();
                        String grpUserId[]=getSubStr.split(",");
                        for(int i=0;i<grpUserId.length;i++)
                        {
                            String uuuId=grpUserId[i].toString();
                            obj1= new GroupContact(gname,uuuId,uidStr,uidStr);
                            helperDB.addContact(obj1);

                        }
                        new CreateGRP(gname).execute();
                        dialog.dismiss();
                    }
                });

                dialog.show();





            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.contcList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        new ContactList().execute();
//        RecyclerView.ItemDecoration itemDecoration =
//                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);
       //modelArrayList = getModel(false);




    }

    private ArrayList<Model> getModel(boolean isSelect){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){

            Model model = new Model();
            model.setSelected(isSelect);
            model.setAnimal(animallist[i]);
            list.add(model);
        }
        return list;
    }



    class ContactList extends AsyncTask<String, String, String> {

        String respo;
        ProgressDialog pd;
        String mobNo;



        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ContactsActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub +"state_id="+citytem;



            String uid=mpreferences.getString("uid","0");
            String url =AppController.baseURL+AppController.getMapContact+"uid="+uid;
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

                if(stus.equals("1")) {

                    JSONArray jArrObject = jRes.getJSONArray("data");
                    modelArrayList= new ArrayList<>();
                    for (int i = 0; i < jArrObject.length(); i++) {
                        JSONObject jIndexObj = jArrObject.getJSONObject(i);

                        Model model = new Model();
                        model.setSelected(false);
                        model.setAnimal(jIndexObj.getString("name"));
                        model.setId(jIndexObj.getString("contact_id"));
                        modelArrayList.add(model);
                    }
                    customAdapter = new MultipleSelectedAdapter(ContactsActivity.this,modelArrayList);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));



                 /*   myCustomPagerAdapter = new MyCustomPagerAdapter(GalleryActivity.this, imgList1);
                    viewPager.setAdapter(myCustomPagerAdapter);
*/
                    // new ImageGET1().execute();
                }else {


                }

                // CreateDB();,
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class CreateGRP extends AsyncTask<String,String,String>
    {

        String response;
        ProgressDialog pg;
        String gname;
        CreateGRP(String GRPName)
        {
            gname=GRPName;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg=new ProgressDialog(ContactsActivity.this);
            pg.setMessage("Please wait...");
            pg.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getSubStr=obj.getSelectedContact().substring(0,obj.getSelectedContact().length()-1);
            String uid=mpreferences.getString("uid","0");
            String url=AppController.baseURL+AppController.createGRP;
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("creatorId",uid));
            nameValuePair.add(new BasicNameValuePair("name",gname));
            nameValuePair.add(new BasicNameValuePair("userIds",getSubStr));

            try {
                response=CustomHttpClient.executeHttpPost(url,nameValuePair);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pg.dismiss();
            startActivity(new Intent(ContactsActivity.this,TabWOIconActivity.class));
            finish();
        }

    }
}