package it.unibo.matteo.jappo.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import it.unibo.matteo.jappo.Activity.LoginActivity;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;

public class RegisterFragment extends Fragment {

    private RegisterTask mRegisterTask = null;

    private TextView mPasswordText;
    private TextView mMailText;
    private TextView mNameText;

    private View mProgressView;

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  mainView = inflater.inflate(R.layout.fragment_register, container, false);

        mNameText = (TextView) mainView.findViewById(R.id.register_name_text);
        mMailText = (TextView) mainView.findViewById(R.id.register_mail_text);
        mPasswordText = (TextView) mainView.findViewById(R.id.register_password_text);
        mProgressView = mainView.findViewById(R.id.register_progress);

        return mainView;
    }

    private void showProgress(final boolean show) {
        mNameText.setVisibility(show ? View.GONE : View.VISIBLE);
        mMailText.setVisibility(show ? View.GONE : View.VISIBLE);
        mPasswordText.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isNameValid(String name) {
        return name.length() > 0;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void attemptRegistration() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mMailText.setError(null);
        mPasswordText.setError(null);
        mNameText.setError(null);

        // Store values at the time of the login attempt.
        String email = mMailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String name = mNameText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordText;
            cancel = true;
        }

        if (!TextUtils.isEmpty(name) && !isNameValid(name)) {
            mNameText.setError(getString(R.string.error_invalid_name));
            focusView = mNameText;
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
            mRegisterTask = new RegisterTask(name, email, password);
            mRegisterTask.execute((Void) null);
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final RequestType requestType = RequestType.REGISTER;

        private final String mEmail;
        private final String mPassword;
        private final String mName;

        private HashMap<String, String> registerParams;

        RegisterTask(String name, String email, String password) {
            mEmail = email;
            mPassword = password;
            mName = name;

            registerParams = new HashMap<>();
            registerParams.put(RequestType.getDefault(), requestType.getValue());
            registerParams.put("name", mName);
            registerParams.put("email", mEmail);
            registerParams.put("password", mPassword);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String response = HTTPHelper.connectPost(HTTPHelper.REST_BACKEND, registerParams);
            boolean isCorrect = JSONHelper.parseResponse(response, requestType);
            return isCorrect;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            LoginActivity currentLoginActivity = (LoginActivity) getActivity();

            if (success) {
                currentLoginActivity.setupLoginFragment(mMailText.getText().toString());

                View v = currentLoginActivity.findViewById(R.id.input_container);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
                params.weight *= 2;
                v.setLayoutParams(params);

                currentLoginActivity.mMainButton.setText(R.string.login);
                currentLoginActivity.mRegisterLabel.setVisibility(View.VISIBLE);
            }

            //TODO Handle insuccess
        }
        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}
