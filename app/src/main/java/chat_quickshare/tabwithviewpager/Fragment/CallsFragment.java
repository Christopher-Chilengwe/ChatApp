package chat_quickshare.tabwithviewpager.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class CallsFragment extends Fragment {

    ImageView takepic;

    List<GroupContact> contacts;
    DatabaseHandler helperDB;
    ArrayList<GroupModel> results;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;

   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls,
                container, false);
							 }
}
