package application.karim.com.androidphotos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
import application.karim.com.androidphotos.model.Album;
import application.karim.com.androidphotos.utils.BaseFragment;
import application.karim.com.androidphotos.utils.RecyclerItemClickListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private TextView textView;
    GridAlbumAdapter adapter;

    RecyclerView recyclerView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    List<Album> Albumlist;
    GridView gridView ;
    Album album;

    ArrayList<Object> alFBAlbum = new ArrayList<>();
    public FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();



            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/"+AccessToken.getCurrentAccessToken().getUserId()+"/albums",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d("TAG", "Facebook Albums: " + response.toString());
                            try {
                                if (response.getError() == null) {
                                    JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                    if (joMain.has("data")) {
                                        JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject

                                        Albumlist = new ArrayList<Album>();

                                        for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                            JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                            //GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album
                                            String name = joAlbum.getString("name");
                                            String id = joAlbum.getString("id");

                                            album = new Album(id , name) ;

                                                Log.d("Album_name", joAlbum.getString("name"));
                                                Log.d("Album_id", joAlbum.getString("id"));
                                               Albumlist.add(album);

                                            Log.d("alb", Albumlist.toString());

                                        }

                                        //////////////sort list////////////////

                                        if (Albumlist.size() > 0) {
                                            Collections.sort(Albumlist, new Comparator<Album>() {
                                                @Override
                                                public int compare(final Album object1, final Album object2) {
                                                    return object1.getName().compareTo(object2.getName());
                                                }
                                            } );
                                        }

                                    }
                                } else {
                                    Log.d("Test", response.getError().toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            recyclerView = (RecyclerView) getActivity().findViewById(R.id.albums_recycler_view);

                            adapter = new GridAlbumAdapter(getActivity() , Albumlist);

                            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 2);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(gridLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());



                         }

                    }

            ).executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public MainFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);



        loginButton.setReadPermissions("user_photos");

        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.albums_recycler_view);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        Album albumclicked = Albumlist.get(position);

                        Log.d("tttttttttt","sssssss");
                        loadFragment_home(new PhotosFragment(albumclicked.getId()));
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



    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }



    @Override
    public void onResume() {
        super.onResume();


    }
}
