package com.binarywalllabs.sendit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.binarywalllabs.sendit.BuildConfig;
import com.binarywalllabs.sendit.R;
import com.binarywalllabs.sendit.managers.FbConnectHelper;
import com.binarywalllabs.sendit.managers.GooglePlusSignInHelper;
import com.binarywalllabs.sendit.managers.SharedPreferenceManager;
import com.binarywalllabs.sendit.managers.gcm.RegistrationIntentService;
import com.binarywalllabs.sendit.models.UserModel;
import com.binarywalllabs.sendit.utils.Apputility;
import com.facebook.GraphResponse;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GooglePlusSignInHelper.OnGoogleSignInListener, FbConnectHelper.OnFbSignInListener {

    private GooglePlusSignInHelper gSignInHelper;
    private FbConnectHelper fbConnectHelper;

    @Bind(R.id.root_layout)
    RelativeLayout view;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        gSignInHelper = new GooglePlusSignInHelper(this, this);
        fbConnectHelper = new FbConnectHelper(this,this);
        fbConnectHelper.printKeyHash(this);

        if (Apputility.checkPlayServices(this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

            checkIfUserIsSignedIn();
        }

        Log.e("Server Url", BuildConfig.SERVER_URL);
    }

    private void checkIfUserIsSignedIn() {
        UserModel userModel = SharedPreferenceManager.getSharedInstance().getUserModelFromPreferences();
        if(userModel!=null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(UserModel.class.getSimpleName(), userModel);
            startActivity(intent);
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        gSignInHelper.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_fb_sign_in)
    public void onFBSignin(View view)
    {
        fbConnectHelper.connect();
        setBackground(R.color.fb_color);
    }

    @OnClick(R.id.btn_google_sign_in)
    public void onGoogleSignin(View view)
    {
        gSignInHelper.connect();
        setBackground(R.color.g_color);
    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        UserModel userModel = getUserModelFromGraphResponse(graphResponse);
        if(userModel!=null) {
            SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
            startHomeActivity(userModel);
        }
    }

    private UserModel getUserModelFromGraphResponse(GraphResponse graphResponse)
    {
        UserModel userModel = new UserModel();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.userName = jsonObject.getString("name");
            userModel.userEmail = jsonObject.getString("email");
            String id = jsonObject.getString("id");
            String profileImg = "http://graph.facebook.com/"+ id+ "/picture?type=large";
            userModel.profilePic = profileImg;
            Log.i(LoginActivity.class.getSimpleName(), profileImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    @Override
    public void OnFbError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }

    @Override
    public void OnGSignSuccess(Person mPerson, String emailAddress) {
        UserModel userModel = new UserModel();
        userModel.userName = mPerson.getDisplayName();
        userModel.userEmail = emailAddress;
        userModel.profilePic = mPerson.getImage().getUrl();

        SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
        startHomeActivity(userModel);
    }

    @Override
    public void OnGSignError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }

    private void startHomeActivity(UserModel userModel)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(UserModel.class.getSimpleName(), userModel);
        startActivity(intent);
        finishAffinity();
    }

    private void setBackground(int colorId)
    {
        view.setBackgroundColor(getResources().getColor(colorId));
        progressBar.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    private void resetToDefaultView(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}
