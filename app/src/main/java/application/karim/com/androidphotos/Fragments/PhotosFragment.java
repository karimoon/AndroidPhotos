package application.karim.com.androidphotos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.karim.com.androidphotos.R;
import application.karim.com.androidphotos.adapter.GridAlbumAdapter;
import application.karim.com.androidphotos.adapter.ListPhotoAdapter;
import application.karim.com.androidphotos.model.Album;
import application.karim.com.androidphotos.model.Photo;
import application.karim.com.androidphotos.utils.BaseFragment;
import application.karim.com.androidphotos.utils.RecyclerItemClickListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class PhotosFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private TextView textView;
    ListPhotoAdapter adapter;

    RecyclerView recyclerView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    List<Photo> Photolist;
    GridView gridView ;
    Photo photo;
    String albumid ;

    ArrayList<Object> alFBAlbum = new ArrayList<>();






    public PhotosFragment() {

    }



    public PhotosFragment(String albumid) {

        this.albumid=albumid;
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumid + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);


                        if (response.getError() == null) {


                            JSONObject joMain = response.getJSONObject();
                            if (joMain.has("data")) {
                                JSONArray jaData = joMain.optJSONArray("data");
                                //lstFBImages = new ArrayList<>();

                                Photolist = new ArrayList<Photo>();
                                for (int i = 0; i < jaData.length(); i++)//Get no. of images {
                                    try {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages=joAlbum.getJSONArray("images");
                                        if(jaImages.length()>0)
                                        {
                                            int height = jaImages.getJSONObject(0).getInt("height");
                                            int width = jaImages.getJSONObject(0).getInt("width");
                                            String source = jaImages.getJSONObject(0).getString("source");
                                            photo = new Photo(height, width, source);
                                            Photolist.add(photo);
                                            Log.d("source :" , source);
                                            String id = joAlbum.getString("id");
                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                            }

                            //set your adapter here

                            recyclerView = (RecyclerView) getActivity().findViewById(R.id.photos_recycler_view);

                            adapter = new ListPhotoAdapter(getActivity() , Photolist);

                            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

/*
                            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 2);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(gridLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());*/
                        }
                        else {
                            Log.v("TAG", response.getError().toString());
                        }
                    }

                }

        ).executeAsync();


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.photos_recycler_view);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        Photo Photoclicked = Photolist.get(position);

                        CheckBox ck = (CheckBox) view.findViewById(R.id.checkBox);

                        ck.setChecked(!ck.isChecked());
                    }
                })
        );


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile){
        if(profile != null){
            textView.setText(profile.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }



    @Override
    public void onResume() {
        super.onResume();

    }
}
