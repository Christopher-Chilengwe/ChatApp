package chat_quickshare.tabwithviewpager.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chat_quickshare.adapter.ChatAdapterRecycler;
import chat_quickshare.adapter.ChatModel;
import chat_quickshare.adapter.GroupAdpater;
import chat_quickshare.adapter.GroupModel;
import chat_quickshare.dbhelper.DatabaseHandler;
import chat_quickshare.dbhelper.GroupContact;
import chat_quickshare.tabwithviewpager.AppController;
import chat_quickshare.tabwithviewpager.CustomHttpClient;
import chat_quickshare.tabwithviewpager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    List<GroupContact> contacts;
    DatabaseHandler helperDB;
    ArrayList<GroupModel> results;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    public ContactsFragment() {
        // Required empty public constructor
    }


   

}
