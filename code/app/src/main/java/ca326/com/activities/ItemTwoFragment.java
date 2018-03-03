package ca326.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ItemTwoFragment extends Fragment {

    private Button register;
    private Button sign_in;

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_sign__in__screen, container,false);

        // Register
        register = (Button) rootView.findViewById(R.id.email_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(rootView.getContext(), Register_Screen.class);
                startActivity(intent);
            }
        });

        // Sign In
        sign_in = (Button) rootView.findViewById(R.id.email_sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Sign_In_Screen screen = new Sign_In_Screen();
                screen.attemptLogin();
            }
        });

        return rootView;
    }
    
}