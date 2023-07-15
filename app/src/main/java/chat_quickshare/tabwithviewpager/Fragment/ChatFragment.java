package chat_quickshare.tabwithviewpager.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import chat_quickshare.adapter.GroupAdpater;
import chat_quickshare.adapter.GroupModel;
import chat_quickshare.dbhelper.DatabaseHandler;
import chat_quickshare.dbhelper.GroupContact;
import chat_quickshare.tabwithviewpager.R;

public class ChatFragment extends Fragment {
    List<GroupContact> contacts;
    DatabaseHandler helperDB;
    ArrayList<GroupModel> results;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    public ChatFragment() {

    }

   
}
