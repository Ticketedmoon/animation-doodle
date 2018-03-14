package ca326.com.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ItemTwoFragment extends Fragment {

    private Button register;
    private Button sign_in;

    // Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //shared preferences
    public static final String PREFERENCE= "preference";
    public static final String PREF_ID = "id";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    // user id for profile
    public static Integer user_id;
    public Context mContext ;

    public static SharedPreferences mSharedPreference;

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_sign__in__screen, container,false);



        mSharedPreference = getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        // Register
        register = (Button) rootView.findViewById(R.id.email_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(rootView.getContext(), Register_Screen.class);
                startActivity(intent);
            }
        });


        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.email);
        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        // convert passord to asterix
        mPasswordView.setTransformationMethod(new changePasswordToAsterix());
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    String passwordView = mPasswordView.getText().toString();
                    String emailView = mEmailView.getText().toString();
                    Log.i("strings","are " + emailView + " " + passwordView);
                    attemptLogin(emailView,passwordView);
                    return true;
                }
                return false;
            }
        });



        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);

        // Sign In
        sign_in = (Button) rootView.findViewById(R.id.email_sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String passwordView = mPasswordView.getText().toString();
                String emailView = mEmailView.getText().toString();
                Log.i("strings","are " + emailView + " " + passwordView);
                attemptLogin(emailView,passwordView);
            }
        });

        return rootView;
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(String emailView, String passwordView) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView;
        String password = passwordView;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            new UserLoginTask(getActivity()).execute(email,password);
        }
    }

    public boolean isEmailValid(String email) {
        boolean filter = true;
        String filterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String regexEmail = filter ? filterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexEmail);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    // this regex will check that passwords contain at least one digit
    // a lower case letter at least once,an upper case letter, at least one special character
    // and is at least 5 characters long
    public boolean isPasswordValid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[A-Z]).{5,}";
        return password.matches(pattern);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void onBackPressed() {
        System.out.println("Back Button Pushed <Returning to Homescreen>");
        Intent startMain = new Intent(getContext(), Main_Menu_Screen.class);
        startActivity(startMain);
    }

    public class UserLoginTask extends AsyncTask<String, Void, String> {

        Activity instance;

        UserLoginTask(Activity instance) {
            this.instance = instance;

        }

        @Override
        protected String doInBackground(String... arg0) {

            String email = arg0[0];
            String password = arg0[1];
            String link;

            try {
                link = "http://animationdoodle2017.com/signin.php";
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                OutputStream out=con.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                String post_data= URLEncoder.encode("emailaddress","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                writer.write(post_data);
                writer.flush();
                writer.close();
                out.close();
                InputStream in=con.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"iso-8859-1"));
                String result="";
                String line="";
                while((line=br.readLine())!=null)
                {
                    result+=line;
                    System.out.println("result is " + result);
                }
                return result;

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            String jsonStr = result;
            Log.i("response",result);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    //System.out.println("string is " + query_result);
                    Integer user_result = jsonObj.getInt("user_result");
                    System.out.println("the user id is : " + user_result);
                    if (query_result.equals("User signed in")) {
                        user_id = user_result;
                        System.out.println("the user id is : " + user_id);
                        Toast.makeText(instance, "Welcome back!", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor mEditor = mSharedPreference.edit();
                        mEditor.putInt(PREF_ID,user_id);
                        mEditor.putString(PREF_EMAIL,email);
                        mEditor.putString(PREF_PASSWORD,password);
                        mEditor.apply();
                        System.out.println("shared  " + mSharedPreference.getAll());
                        Intent intent = new Intent(getContext(),Profile_Screen.class);
                        startActivity(intent);
                    }
                    else if (query_result.equals("FAILURE")) {
                        Toast.makeText(instance, "Login details incorrect.", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(instance, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(instance, "Login Details incorrect.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(instance, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
            mProgressView.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class changePasswordToAsterix extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new changePasswordToAsterix.GetCharSequence(source);
        }

        private class GetCharSequence implements CharSequence {
            private CharSequence mText;
            public GetCharSequence(CharSequence text) {
                mText = text;
            }
            public char charAt(int index) {
                return '*'; //Replace with an asterix here
            }
            public int length() {
                return mText.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mText.subSequence(start, end);
            }
        }
    }
    
}