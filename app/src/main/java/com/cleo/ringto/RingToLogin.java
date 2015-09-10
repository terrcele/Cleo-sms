package com.cleo.ringto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// The email and password must take the user's entry. Changed for debugging.
public class RingToLogin extends Activity
{
    EditText username = null;
    EditText password = null;
    String retSrc = "";
    static boolean login_started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        username   = (EditText)findViewById(R.id.email_editbox);
        password   = (EditText)findViewById(R.id.password_editbox);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void signIn(View v)
    {
        if(!login_started)
        {
            login_started = true;
            Login auth = new Login();
            auth.execute();
//            auth2.execute();
        }
    }

    class Login_Old extends AsyncTask<Void, Void, Void>
    {
        boolean login = false;
        String uuid = "";
        String access_token = "";
        String jb_token = "";
        JSONObject jObj_returned = null;
        String email = username.getText().toString();
        String mpassword = password.getText().toString();
        String URL = "https://ring.to/api/user";
        Activity mActivity;

        public Login_Old()
        {}

        @Override
        protected Void doInBackground(Void... params)
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            try
            {
                // RingTo API to obtain token
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "l"));
                nameValuePairs.add(new BasicNameValuePair("password", mpassword));
                nameValuePairs.add(new BasicNameValuePair("token", "t"));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null)
                {
                    retSrc = EntityUtils.toString(httpEntity);
                    jObj_returned = new JSONObject(retSrc);
                    jObj_returned = (JSONObject) jObj_returned.get("user");
                    retSrc = (String) jObj_returned.get("token");
                    jb_token = retSrc;
                    RingToApplication singleton = (RingToApplication) getApplicationContext();
                    singleton.setJBToken(jb_token);
                    Log.d("all old login", jObj_returned.toString());
                    Log.d("jbtoken login", jb_token);
                }

                login = true;
            }

            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("Error", "Cannot Estabilish Connection");
            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

//            if (login)
//            {
//                Intent intent = new Intent(RingToLogin.this, RingToMainContainer.class);
//                Bundle token = new Bundle();
//                token.putString("token", uuid);
//                token.putString("messages", retSrc);
//                intent.putExtras(token);
//                startActivity(intent);
//            }
        }
    }

    // Communicates with the RingTo server by inputing the user credentials and
    // obtaining the a token used for every subsequent function.
    class Login extends AsyncTask<Void, Void, Void>
    {
        boolean login = false;
        String uuid = "";
        String access_token = "";

        public Login()
        {}

        @Override
        protected Void doInBackground(Void... params)
        {
            JSONObject jObj_returned = null;
            String email = username.getText().toString();
            String mpassword = password.getText().toString();
            String URL = "https://partners.ring.to/api/oauth/token";

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            try
            {
                // RingTo API to obtain token
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
                nameValuePairs.add(new BasicNameValuePair("username", "terrincelestin@gmail.com"));
                nameValuePairs.add(new BasicNameValuePair("password", "5523parabal"));
                nameValuePairs.add(new BasicNameValuePair("client_id", "ringto"));
                nameValuePairs.add(new BasicNameValuePair("client_secret", "1"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null)
                {
                    retSrc = EntityUtils.toString(httpEntity);
                    jObj_returned = new JSONObject(retSrc);
                    Log.d("Tag", jObj_returned.toString());
                    access_token = (String) jObj_returned.get("access_token");
                    jObj_returned = (JSONObject) jObj_returned.get("extra_data");
                    jObj_returned = (JSONObject) jObj_returned.get("user");
                    retSrc = (String) jObj_returned.get("uuid");
                    uuid = retSrc;
                    RingToApplication singleton = (RingToApplication) getApplicationContext();
                    singleton.setToken(uuid);
                    singleton.setAccessToken(access_token);
                    Log.d("Access Token", access_token);
                    Log.d("uuid login", uuid);
                }

                login = true;
            }

            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("Error", "Cannot Estabilish Connection");
            }

            URL = "https://ring.to/api/user";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(URL);

            try
            {
                // RingTo API to obtain token
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "l"));
                nameValuePairs.add(new BasicNameValuePair("password", "5523parabal"));
                nameValuePairs.add(new BasicNameValuePair("token", "t"));
                nameValuePairs.add(new BasicNameValuePair("email", "terrincelestin@gmail.com"));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null)
                {
                    String jb_token = "";
                    retSrc = EntityUtils.toString(httpEntity);
                    jObj_returned = new JSONObject(retSrc);
                    jObj_returned = (JSONObject) jObj_returned.get("user");
                    retSrc = (String) jObj_returned.get("token");
                    jb_token = retSrc;
                    RingToApplication singleton = (RingToApplication) getApplicationContext();
                    singleton.setJBToken(jb_token);
                    Log.d("all old login", jObj_returned.toString());
                    Log.d("jbtoken login", jb_token);
                }

                login = true;
            }

            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("Error", "Cannot Estabilish Connection");
            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(login)
            {
                Intent intent = new Intent(RingToLogin.this, RingToMainContainer.class);
                Bundle token = new Bundle();
                token.putString("token", uuid);
                token.putString("access_token", access_token);
                token.putString("messages", retSrc);
                intent.putExtras(token);
                startActivity(intent);
            }
        }
    }
}
