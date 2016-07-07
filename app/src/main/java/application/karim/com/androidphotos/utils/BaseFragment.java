package application.karim.com.androidphotos.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import application.karim.com.androidphotos.R;


public class BaseFragment extends Fragment {



    public void loadFragment_home(android.support.v4.app.Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.content_home_frame, fragment)
                .commit();
    }

    public void loadFragment_home_with_value_phone(android.support.v4.app.Fragment fragment , String value) {

        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction().addToBackStack(null);
        Bundle args = new Bundle();
        args.putString("root", value);
        fragment.setArguments(args);
        ft.replace(R.id.content_home_frame, fragment);
        ft.commit();

    }



}
