package application.karim.com.androidphotos.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import application.karim.com.androidphotos.R;
import application.karim.com.androidphotos.adapter.GridAlbumAdapter;
import application.karim.com.androidphotos.adapter.ListPhotoAdapter;
import application.karim.com.androidphotos.model.Album;
import application.karim.com.androidphotos.model.Photo;
import application.karim.com.androidphotos.utils.BaseFragment;
import application.karim.com.androidphotos.utils.RecyclerItemClickListener;
import application.karim.com.androidphotos.utils.RequestHandler;


/**
 * A placeholder fragment containing a simple view.
 */
public class PhotosFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private TextView textView;
    ListPhotoAdapter adapter;

    Button uplaodbtn;
    RecyclerView recyclerView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    List<Photo> Photolist;
    List<Photo> Photolistdownload;
    GridView gridView ;
    Photo photo;
    String albumid ;

    ArrayList<Object> alFBAlbum = new ArrayList<>();


    public static final String UPLOAD_URL = "http://10.0.3.2/androidphotos/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private Bitmap bitmap;
    List<Bitmap> listbitmap;
    ProgressBar progressBar;
    TextView txtview;


    public PhotosFragment() {

    }



    public PhotosFragment(String albumid) {

        this.albumid=albumid;
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<List<Bitmap>,Integer,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                ((TextView) getActivity().findViewById(R.id.txupload)).setText("Uploading...");

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                ((RelativeLayout) getActivity().findViewById(R.id.relativelayoutmessage)).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ((TextView) getActivity().findViewById(R.id.txupload)).setText("Done");
                //loading.dismiss();

            }

            @Override
            protected String doInBackground(List<Bitmap>... params) {

                String result = null;

                List<Bitmap> lisbmp = params[0];
                if(lisbmp!=null)
                {

                for (int i = 0; i < lisbmp.size(); i++)

                {
                    String uploadImage = getStringImage(lisbmp.get(i));

                    HashMap<String,String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, uploadImage);

                     result = rh.sendPostRequest(UPLOAD_URL,data);
                    publishProgress(((i+1)*100)/lisbmp.size());
                }
                }


                return result;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(listbitmap);
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



    private class DownloadImages extends AsyncTask<List<Photo>, Void, Void> {
        @Override
        protected Void doInBackground(List<Photo>... params) {
            // we use the OkHttp library from https://github.com/square/okhttp
            //bitmap = getBitmapFromURL("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");

            List<Photo> array = params[0];
            //String s = array.get(1);

            listbitmap = new ArrayList<Bitmap>();

            for (int i = 0; i < array.size(); i++)

            {

                try {
                    bitmap = Glide.
                            with(getActivity()).
                            load(array.get(i).getSource()).
                            asBitmap().
                            into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

                    listbitmap.add(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }


            return null;
            //return "Download failed";
        }

        @Override
        protected void onPostExecute(Void result) {

            //imageView.setImageBitmap(listbitmap.get(0));
            uploadImage();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);



         Photolistdownload = new ArrayList<Photo>();

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

                        if(ck.isChecked())
                        {
                            Photolistdownload.add(Photoclicked);
                        }
                        else
                        {
                            Photolistdownload.remove(Photoclicked);
                        }
                    }
                })
        );

         uplaodbtn = (Button) getActivity().findViewById(R.id.buttonupload);

        uplaodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImages().execute(Photolistdownload);


            }
        });

        //new v().execute(Photolistdownload);

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
