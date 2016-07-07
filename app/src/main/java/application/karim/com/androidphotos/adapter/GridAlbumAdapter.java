package application.karim.com.androidphotos.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import application.karim.com.androidphotos.R;
import application.karim.com.androidphotos.model.Album;

import static android.content.ContentValues.TAG;

public class GridAlbumAdapter extends RecyclerView.Adapter<GridAlbumAdapter.ViewHolder>
         {


    private List<Album> Albums;





     public  GridAlbumAdapter(Context context, List<Album> Albums)
             {

                 this.Albums=Albums;

             }

    public void setAlbums( List<Album> Albums) {
        this.Albums = Albums;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Albums == null ? 0 : Albums.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_album_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Album album = Albums.get(position);


        holder.namealbum.setText(album.getName());


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
             {
                 TextView namealbum ;
                 int position;
                 Album current ;


        public ViewHolder( View itemView) {
            super(itemView);

             namealbum = (TextView) itemView.findViewById(R.id.albumname);

        }




                 public void setData(Album current , int position )
                 {

                     this.position = position;
                     this.current = current;
                     this.namealbum.setText(current.getName());
                 }


                 @Override
                 public void onClick(View v) {
                     Toast.makeText(v.getContext() , "jjjjjjjj" , Toast.LENGTH_LONG ).show();
                 }
             }
}
