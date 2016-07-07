package application.karim.com.androidphotos.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.List;

import application.karim.com.androidphotos.R;
import application.karim.com.androidphotos.model.Album;
import application.karim.com.androidphotos.model.Photo;

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ViewHolder>
         {

    private  Context context;
    private List<Photo> Photos;





     public ListPhotoAdapter(Context context, List<Photo> Photos)
             {

                 this.context=context;
                 this.Photos=Photos;

             }

    public void setAlbums( List<Photo> Photos) {
        this.Photos = Photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Photos == null ? 0 : Photos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_photo_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = Photos.get(position);


        //Picasso.with(context).load("https://z-1-scontent.xx.fbcdn.net/v/t1.0-9/1920107_443742095766304_9208875944953535603_n.jpg?oh=1bdf29f44e911e0ba3519ac6e7ee0dcc&oe=57F27BB9").into(holder.image);
        //holder.namealbum.setText(album.getName());
        Glide.with(context).load(photo.getSource()).into(holder.image);


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
             {
                 //TextView namealbum ;
                 ImageView image ;
                 int position;
                 Photo current ;


        public ViewHolder( View itemView) {
            super(itemView);

             //namealbum = (TextView) itemView.findViewById(R.id.albumname);

            image = (ImageView) itemView.findViewById(R.id.imageview);


        }




                 public void setData(Photo current , int position )
                 {

                     this.position = position;
                     this.current = current;
                     //this.namealbum.setText(current.getName());
                 }


                 @Override
                 public void onClick(View v) {
                     Toast.makeText(v.getContext() , "jjjjjjjj" , Toast.LENGTH_LONG ).show();
                 }
             }
}
