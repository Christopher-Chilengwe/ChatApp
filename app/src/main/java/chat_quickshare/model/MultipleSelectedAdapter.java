package chat_quickshare.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chat_quickshare.tabwithviewpager.AppController;
import chat_quickshare.tabwithviewpager.R;

/**

 */
public class MultipleSelectedAdapter extends RecyclerView.Adapter<MultipleSelectedAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<Model> imageModelArrayList;
    private Context ctx;
    ArrayList<String> getpossion =new ArrayList<String>();
    Typeface fontCustome;
    AppController obj;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;

    public MultipleSelectedAdapter(Context ctx, ArrayList<Model> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.ctx = ctx;
        fontCustome= Typeface.createFromAsset(ctx.getAssets(),"Raleway_Regular.ttf");
        obj=(AppController)ctx.getApplicationContext();
        mpreferences = ctx.getSharedPreferences(String.format("%s_preferences", ctx.getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();

    }

  
    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutcheck;
        protected CheckBox checkBox;
        private TextView contactName;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            contactName = (TextView) itemView.findViewById(R.id.contactName);
            layoutcheck = (LinearLayout) itemView.findViewById(R.id.layoutcheck);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}