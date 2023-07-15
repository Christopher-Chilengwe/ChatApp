package chat_quickshare.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chat_quickshare.tabwithviewpager.R;

/**
 * Created by Lannet1 on 6/26/2018.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static String LOG_TAG = "FeedAdapter";
    private ArrayList<CommentModelObject> mDataset;
    private static CommentAdpaterdemo.MyClickListener myClickListener;
    Context cntxt;
    Typeface fontCustome;

    public MyRecyclerAdapter(ArrayList<CommentModelObject> myDataset, Context ct)
    {
        mDataset = myDataset;
        cntxt=ct;
        fontCustome= Typeface.createFromAsset(cntxt.getAssets(),"Raleway_Regular.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_contact, parent, false);
            return  new VHHeader(v);
        }
        else if(viewType == TYPE_ITEM)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private CommentModelObject getItem(int position)
    {
        return mDataset.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHHeader)
        {
            VHHeader VHheader = (VHHeader)holder;

            VHheader.contactnamebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(cntxt, "You clicked Add new contact", Toast.LENGTH_SHORT).show();
                }
            });
            VHheader.grpNamebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(cntxt, "You clicked on Create Group for QuickShare!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(holder instanceof VHItem)
        {
            CommentModelObject currentItem = getItem(position-1);
            VHItem VHitem = (VHItem)holder;
            VHitem.commentName.setText(mDataset.get(position).getCommentId());
            VHitem.commentName.setTypeface(fontCustome);
            VHitem.commenttext.setText("Hey there! I am using QuickshareApp");
            VHitem.commenttext.setTypeface(fontCustome);
        }
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return mDataset.size()+1;
    }

    class VHHeader extends RecyclerView.ViewHolder{
        TextView grpNamebtn,contactnamebtn;

        public VHHeader(View view) {
            super(view);
            grpNamebtn = (TextView) view.findViewById(R.id.grpNamebtn);
            contactnamebtn = (TextView) view.findViewById(R.id.contactnamebtn);
        }
    }

    class VHItem extends RecyclerView.ViewHolder{
        TextView commentName,commenttext;


        public VHItem(View itemView) {
            super(itemView);
            commentName = (TextView) itemView.findViewById(R.id.commentName);
            commenttext = (TextView) itemView.findViewById(R.id.textcomment);

        }
    }
}