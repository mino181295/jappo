package it.unibo.matteo.jappo.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.matteo.jappo.Activity.LoginActivity;
import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.VibratorManager;

public class LoginFragment extends Fragment {

    private UserLoginTask mAuthTask = null;
    private static String startingMail;

    private TextView mPasswordText;
    private TextView mMailText;
    private View mainView;

    private ProgressBar mProgressView;

    public LoginFragment(){}

    public static LoginFragment newInstance(String mail) {
        LoginFragment fragment = new LoginFragment();
        startingMail = mail;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_login, container, false);

        mPasswordText = (TextView) mainView.findViewById(R.id.login_password_text);
        mMailText = (TextView) mainView.findViewById(R.id.login_mail_text);
        mMailText.setText(startingMail);

        mProgressView = (ProgressBar) mainView.findViewById(R.id.login_progress);

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
        mMailText.setError(null);
        mPasswordText.setError(null);

        String email = mMailText.getText().toString();
        String password = mPasswordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordText;
            cancel = true;
        }

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
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

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
                VibratorManager.makeBuzz(getContext(), VibratorManager.MEDIUM);
                startActivity(new Intent(currentActivity.getApplicationContext(), MainActivity.class));
                currentActivity.finish();
            } else {
                VibratorManager.makeBuzz(getContext(), VibratorManager.LONG);
                Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake_animation);
                mainView.setAnimation(shakeAnimation);
                mPasswordText.setError(getString(R.string.error_invalid_password));
                mMailText.setError(getString(R.string.error_invalid_email));
                mMailText.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
