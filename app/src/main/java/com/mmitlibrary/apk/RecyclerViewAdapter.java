package com.mmitlibrary.apk;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mmitlibrary.apk.R;

/**
 * Created by JUNED on 6/16/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private ProgressDialog  progressDialog;
    public static final int progress_bar_type = 0;
    private long enqueue;
    private DownloadManager dm;
    private List<GetDataAdapter> filteredTitleList;

    //public String file;

    Context context;

    List<GetDataAdapter> getDataAdapter;

    ImageLoader imageLoader1;

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        this.filteredTitleList=getDataAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.fade);
        v.startAnimation(animation);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        GetDataAdapter getDataAdapter1 =  getDataAdapter.get(position);

        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        Viewholder.networkImageView,//Server Image
                        R.drawable.icon,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.networkImageView.setImageUrl(getDataAdapter1.getImageServerUrl(), imageLoader1);

        Viewholder.title.setText(getDataAdapter1.getImageTitleName2());

        Viewholder.size.setText(getDataAdapter1.getImageTitleName3());

        Viewholder.des.setText(getDataAdapter1.getImageTitleName4());



      //  final String vd_name=getDataAdapter1.getImageTitleName2();

        final String link=getDataAdapter1.getImageTitleName1();
        final String img_link=getDataAdapter1.getImageServerUrl();
        final String title=getDataAdapter1.getImageTitleName2();
        final String size=getDataAdapter1.getImageTitleName3();
        final String book_name=getDataAdapter1.getImageTitleName5();

      final File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/ITLibrary").getPath() + File.separator + book_name);


        if (file2.exists()) {
            Viewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are You Sure You Want To Delete")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                      dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                           file2.delete();
                                        }
                                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }
            });
        }

        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewActivity.class);
                intent.putExtra("img_link",img_link);
                intent.putExtra("link",link);
                intent.putExtra("name",title);
                intent.putExtra("book_name",book_name);
                context.startActivity(intent);

            }
        });




    }


    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }
    /////////////
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    getDataAdapter = filteredTitleList;
                } else {
                    List<GetDataAdapter> filteredList = new ArrayList<>();
                    for (GetDataAdapter name : getDataAdapter) {
                        if (name.getImageTitleName2().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        getDataAdapter = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values =  getDataAdapter;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                getDataAdapter = (List<GetDataAdapter>) results.values;
                notifyDataSetChanged();
            }
        };
    }
//////////////////////////////

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView size;
        public TextView des;
        public NetworkImageView networkImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title) ;
            size = (TextView) itemView.findViewById(R.id.size) ;
            des = (TextView) itemView.findViewById(R.id.des) ;

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;

        }
    }


}
