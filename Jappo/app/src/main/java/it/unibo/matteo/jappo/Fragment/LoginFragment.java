package it.unibo.matteo.jappo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class LoginFragment extends Fragment {

    private SharedPreferencesManager spManager;

    private UserLoginTask mAuthTask = null;
    private static String startingMail;

    public TextView mPasswordText;
    public TextView mMailText;

    private View mProgressView;

    public LoginFragment(){}

    public static LoginFragment newInstance(String mail) {
        LoginFragment fragment = new LoginFragment();
        startingMail = mail;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spManager = new SharedPreferencesManager("Default", getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  mainView = inflater.inflate(R.layout.fragment_login, container, false);

        mPasswordText = (TextView) mainView.findViewById(R.id.login_password_text);
        mMailText = (TextView) mainView.findViewById(R.id.login_mail_text);
        mProgressView = mainView.findViewById(R.id.login_progress);

        mMailText.setText(startingMail);

        return mainView;
    }

    private void showProgress(final boolean show) {
        mMailText.setVisibility(show ? View.GONE : View.VISIBLE);
        mPasswordText.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mMailText.setError(null);
        mPasswordText.setError(null);

        // Store values at the time of the login attempt.
        String email = mMailText.getText().toString();
        String password = mPasswordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mMailText.setError(getString(R.string.error_field_required));
            focusView = mMailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mMailText.setError(getString(R.string.error_invalid_email));
            focusView = mMailText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final RequestType requestType = RequestType.LOGIN;

        private final String mEmail;
        private final String mPassword;

        private HashMap<String, String> loginParams;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;

            loginParams = new HashMap<>();
            loginParams.put(RequestType.getDefault(), requestType.getValue());
            loginParams.put("email", mEmail);
            loginParams.put("password", mPassword);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loginParams);
            boolean isCorrect = JSONHelper.parseResponse(response, requestType);

            if (isCorrect) {
                loginParams = new HashMap<>();
                loginParams.put(RequestType.getDefault(), RequestType.GET_USER.getValue());
                loginParams.put("email", mEmail);
                loginParams.put("password", mPassword);

                response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, loginParams);

                DataModel dm = new DataModel(getContext());
                User loggedUser = JSONHelper.parseUser(response);
                dm.login(loggedUser);
                dm.load();
                dm.save();
            }
            return isCorrect;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Activity currentActivity = getActivity();

            if (success) {
                startActivity(new Intent(currentActivity.getApplicationContext(), MainActivity.class));
                currentActivity.finish();
            } else {
                mPasswordText.setError(getString(R.string.error_incorrect_password));
                mPasswordText.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
