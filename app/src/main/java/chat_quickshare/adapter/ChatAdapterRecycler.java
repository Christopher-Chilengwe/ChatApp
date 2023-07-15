package chat_quickshare.adapter;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.marcinmoskala.videoplayview.VideoPlayView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import chat_quickshare.tabwithviewpager.AppController;

import chat_quickshare.tabwithviewpager.R;

import uk.co.jakelee.vidsta.VidstaPlayer;

public class ChatAdapterRecycler extends RecyclerView.Adapter<ChatAdapterRecycler.DataObjectHolder> {



    private static ProgressDialog progressDialog;

    Context cntext;

    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    AppController gObje;


    private MediaPlayer mp;

    private ArrayList<ChatModel> mDataset;

    String AudioURL = "https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3";

    MediaPlayer mediaplayer;
    FragmentManager fmg;


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        LinearLayout buddylay,youlay;

        BootstrapThumbnail postimgme,postfrnd;
        TextView txtBuddy,youName,txtYour;
        VideoView vdoplay;
        LinearLayout linearLayoutPlay;
        ImageView imageViewPlay;
        SeekBar seekBar;
        //VideoPlayView  vdoplay;


        public DataObjectHolder(View itemView) {
            super(itemView);
            txtBuddy=(TextView)itemView.findViewById(R.id.txtBuddy);
            txtYour=(TextView)itemView.findViewById(R.id.txtYour);
            postimgme=(BootstrapThumbnail)itemView.findViewById(R.id.postimgme);
            postfrnd=(BootstrapThumbnail)itemView.findViewById(R.id.postfrnd);
           vdoplay=(VideoView) itemView.findViewById(R.id.videoply);
            linearLayoutPlay=(LinearLayout) itemView.findViewById(R.id.linearLayoutPlay);
            imageViewPlay=(ImageView) itemView.findViewById(R.id.imageViewPlay);
            seekBar=(SeekBar) itemView.findViewById(R.id.seekBar);
            //vdoplay=(VideoPlayView) itemView.findViewById(R.id.videoply);


            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }

    }



    public ChatAdapterRecycler(ArrayList<ChatModel> myDataset) {
        mDataset = myDataset;

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);



    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        cntext=parent.getContext();
        gObje=(AppController)cntext.getApplicationContext();
        mpreferences = cntext.getSharedPreferences(String.format("%s_preferences", cntext.getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();

        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {


        String Sender =gObje.getUID();
       String dbUid=mDataset.get(position).getWhors();    //mDataset.get(position).getBuddyId();

       if(dbUid.equals("r"))
       {
           if(mDataset.get(position).getMesgtype().equals("2"))
           {
               holder.postfrnd.setVisibility(View.VISIBLE);
               holder.postimgme.setVisibility(View.GONE);
               Picasso.with(cntext)
                       .load(mDataset.get(position).getMultimideapath())
                       .into(holder.postfrnd);
               holder.txtBuddy.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.txtYour.setVisibility(View.GONE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
           }else if(mDataset.get(position).getMesgtype().equals("3"))
           {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
               holder.vdoplay.setBackgroundResource(R.drawable.media_radius);
               String urlStr=mDataset.get(position).getMultimideapath();
               Uri videoUri = Uri.parse(urlStr);
               holder.vdoplay.setVideoURI(videoUri);
               holder.vdoplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                   @Override
                   public void onPrepared(MediaPlayer mp) {
                       mp.setLooping(true);
                       holder.vdoplay.start();
                   }
               });

           }

           else if(mDataset.get(position).getMesgtype().equals("4"))
           {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.linearLayoutPlay.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                           AudioURL=mDataset.get(position).getMultimideapath();
                           mediaplayer.setDataSource(AudioURL);
                           mediaplayer.prepare();


                       } catch (IllegalArgumentException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (SecurityException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (IllegalStateException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (IOException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       }

                       mediaplayer.start();
                   }
               });

           }

           else {

               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.txtBuddy.setVisibility(View.VISIBLE);
               holder.txtYour.setVisibility(View.GONE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
               holder.txtBuddy.setText(mDataset.get(position).getMessageTxt());
           }
       }
       else {

           if(mDataset.get(position).getMesgtype().equals("2"))
           {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
               Picasso.with(cntext)
                       .load(mDataset.get(position).getMultimideapath())
                       .into(holder.postimgme);

               holder.txtBuddy.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.txtYour.setVisibility(View.GONE);
           }
           else if(mDataset.get(position).getMesgtype().equals("4"))
           {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.linearLayoutPlay.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                           AudioURL=mDataset.get(position).getMultimideapath();
                           mediaplayer.setDataSource(AudioURL);
                           mediaplayer.prepare();


                       } catch (IllegalArgumentException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (SecurityException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (IllegalStateException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       } catch (IOException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       }

                       mediaplayer.start();
                   }
               });

           }
           else if(mDataset.get(position).getMesgtype().equals("3"))
           {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
               holder.vdoplay.setBackgroundResource(R.drawable.media_radius);
               final String urlStr=mDataset.get(position).getMultimideapath();

//               holder.vdoplay.seekTo(100);
               holder.vdoplay.setOnTouchListener(new View.OnTouchListener() {
                   @Override
                   public boolean onTouch(View v, MotionEvent event) {

                       Uri videoUri = Uri.parse(urlStr);
                       holder.vdoplay.setVideoURI(videoUri);
                       holder.vdoplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                           @Override
                           public void onPrepared(MediaPlayer mp) {
                               mp.setLooping(true);
                               holder.vdoplay.start();
                           }
                       });
                       return false;
                   }
               });







           }
           else {
               holder.postfrnd.setVisibility(View.GONE);
               holder.postimgme.setVisibility(View.GONE);
               holder.txtBuddy.setVisibility(View.GONE);
               holder.vdoplay.setVisibility(View.GONE);
               holder.txtYour.setVisibility(View.VISIBLE);
               holder.linearLayoutPlay.setVisibility(View.GONE);
               holder.txtYour.setText(mDataset.get(position).getMessageTxt());
           }
       }

        holder.postimgme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog=new Dialog(cntext,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.videpics);
                ImageView pics=(ImageView)dialog.findViewById(R.id.pics);
                Picasso.with(cntext)
                        .load(mDataset.get(position).getMultimideapath())
                        .into(pics);
                dialog.show();

            }
        });

        holder.postfrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog=new Dialog(cntext,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.videpics);
                ImageView pics=(ImageView)dialog.findViewById(R.id.pics);
                Picasso.with(cntext)
                        .load(mDataset.get(position).getMultimideapath())
                        .into(pics);
                dialog.show();

            }
        });

    }



    public void addItem(ChatModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void setup() {
        try {
            progressDialog = ProgressDialog.show(cntext, "",
                    "Buffering audio...", true);
            progressDialog.setCancelable(true);
            mp = new MediaPlayer();
            mp.setDataSource("http://www.virginmegastore.me/Library/Music/CD_001214/Tracks/Track1.mp3");
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("first", "reached");
                    // mp.start();
                    progressDialog.dismiss();
                }
            });

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }




}