package com.cleo.ringto;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terrin on 4/5/15.
 */
public class RingToAPI extends Fragment
{
    private String token = "";
    private String access_token = "";
    private String jb_token = "";
    private GetMessages messages = null;
    private JSONArray jobj_message = null;
//    protected RingToChatScreen activity = null;
    private boolean isTaskRunning = false;
    private ProgressDialog progressDialog = null;
    ActionBarActivity mActivity = null;
    ChatListThreadAdapter list = null;

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setAccessToken(String access_token)
    {
        this.access_token = token;
    }

    public void setList(ChatListThreadAdapter list)
    {
        this.list = list;
    }

    void attach(ActionBarActivity activity)
    {
        mActivity = activity;
    }

    void detach()
    {
        mActivity = null;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState)
//    {
//        super.onActivityCreated(savedInstanceState);
//        // If we are returning here from a screen orientation
//        // and the AsyncTask is still working, re-create and display the
//        // progress dialog.
//
//        if (isTaskRunning)
//        {
//            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
//        }
//    }
//    public RingToAPI(String token)
//    {
//        this.token = token;
//        this.messages = new GetMessages();
//    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public String getToken()
    {
        return token;
    }

    public String getAccessToken()
    {
        return access_token;
    }

    public void setActivity(ActionBarActivity activity)
    {
        this.mActivity = activity;
    }

    public void setAPI(ActionBarActivity activity, String token, ChatListThreadAdapter list)
    {
        this.mActivity = activity;
        this.token = token;
        new GetMessages(mActivity,token).execute();
        this.list = list;
    }

    public void setAPI(ActionBarActivity activity, String token)
    {
        this.mActivity = activity;
        this.token = token;
        new GetMessages(mActivity,token).execute();
    }

    public void setAPI(ActionBarActivity activity, String token, String access_token)
    {
        this.mActivity = activity;
        this.token = token;
        this.access_token = access_token;
        new GetMessages(mActivity,token).execute();
    }

    public void setAPI(ActionBarActivity activity, String token, String access_token, String jb_token)
    {
        this.mActivity = activity;
        this.token = token;
        this.access_token = access_token;
        this.jb_token = jb_token;
        new GetMessages(mActivity,token).execute();
        new GetNumbers(mActivity, token, jb_token).execute();
    }

    public void setAPI(ActionBarActivity activity, String token, String access_token, String current_number, ArrayList<String> media, String text, ArrayList<String> sent_number)
    {
        this.mActivity = activity;
        this.token = token;
        this.access_token = access_token;
        new SendMessage(mActivity, token, current_number, media, text, sent_number).execute();
    }

    public void onTaskStarted()
    {
        isTaskRunning = true;
//        progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
    }

    public void onTaskFinished(String result)
    {
        if (progressDialog != null)
        {
//            progressDialog.dismiss();
        }

        isTaskRunning = false;
    }

    @Override
    public void onDetach()
    {
        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        super.onDetach();
    }

    public void pullMessages(ActionBarActivity activity, String token)
    {
//        this.activity = activity;
//        messages.execute();
        messages = new GetMessages(activity, token);
        onTaskStarted();
        messages.execute();
    }

//    public JSONArray storeMessages(ActionBarActivity activity)
//    {
//        Log.d("Message Size", Integer.toString(jobj_message.length()));
//
//        for(int i = jobj_message.length()-1; i > -1; i--)
//        {
//            long server_id;
//            String from_number;
//            String to_number;
//            String direction;
//            String date;
//            String date_raw;
//            String text;
//            String incoming;
//            String status;
//            String read;
//            boolean left;
//
//            try
//            {
//                server_id = jobj_message.getJSONObject(i).getLong("id");
//                from_number = jobj_message.getJSONObject(i).getString("from");
//                to_number = jobj_message.getJSONObject(i).getString("to");
//                direction = jobj_message.getJSONObject(i).getString("direction");
//                date = jobj_message.getJSONObject(i).getString("date");
//                date_raw = jobj_message.getJSONObject(i).getString("date_raw");
//                text = jobj_message.getJSONObject(i).getString("text");
//                incoming = jobj_message.getJSONObject(i).getString("status_id");
//                status = jobj_message.getJSONObject(i).getString("status");
//                read = jobj_message.getJSONObject(i).getString("read");
//
//                if(direction.equals("Incoming"))
//                {
//                    left = true;
//                }
//
//                else
//                {
//                    left = false;
//                }
//
//                ((RingToMainContainer) activity).storeChatMessage(left, server_id, from_number, to_number, direction,
//                        date, date_raw, text, incoming, status, read);
//            }
//
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        return jobj_message;
//    }

    private class SendMessage extends AsyncTask<Void, Void, Void>
    {
        String token = "";
        String current_number = "";
        ArrayList<String> media = new ArrayList<String>();
        String text = "";
        ArrayList<String> sent_number = new ArrayList<String>();
        int[] message_ids;
        String[] status;
        JSONObject obj;
        boolean completion = false;

        public SendMessage(ActionBarActivity activity, String token, String current_number, ArrayList<String> media, String text, ArrayList<String> sent_number)
        {
            attach(activity);
            this.token = token;
            this.current_number = current_number;
            this.media = media;
            this.text = text;
            this.sent_number = sent_number;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.d("Starting", "AsyncTask starting.");
            JSONObject jObj = null;
            JSONArray jObj_returned = null;
            String retSrc = "";
//            String URL = "https://ring.to/api/messages?";
            RingToApplication application = new RingToApplication();

            String URL = "https://partners.ring.to/api/users/" + token + "/messages?access_token="
                    + access_token;

//            String URL = "https://ring.to/api/users/" + getToken();

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            try
            {
                // RingTo API to obtain token
                obj = new JSONObject();
                JSONArray m = new JSONArray();

                for(int i = 0; i < media.size(); i++)
                {
                    m.put(media.get(i));
                }

                JSONArray sn = new JSONArray();

                for(int i = 0; i < sent_number.size(); i++)
                {
                    sn.put("+" + sent_number.get(i));
                }

                obj.put("from", "+" + current_number);
                obj.put("media", m);
                obj.put("text", text);
                obj.put("to", sn);

//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//                nameValuePairs.add(new BasicNameValuePair("from", "+" + current_number));
//                nameValuePairs.add(new BasicNameValuePair("media", "5523parabal"));
//                nameValuePairs.add(new BasicNameValuePair("text", text));
//                nameValuePairs.add(new BasicNameValuePair("to", "terrincelestin@gmail.com"));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpPost.setEntity(new ByteArrayEntity(obj.toString().getBytes("UTF8")));
                httpPost.setHeader("Content-Type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null)
                {
                    retSrc = EntityUtils.toString(httpEntity);
                    jObj_returned = new JSONArray(retSrc);
                    Log.d("String url", URL);
                    Log.d("JSON Send Message", jObj_returned.toString());
                    Log.d("JSON Object", obj.toString());

                    message_ids = new int[jObj_returned.length()];
                    status = new String[jObj_returned.length()];

                    for(int i = 0; i < jObj_returned.length(); i++)
                    {
                        message_ids[i] = (int) ((JSONObject) jObj_returned.get(i)).get("message_id");
                        status[i] = (String) ((JSONObject) jObj_returned.get(i)).get("status");

                        Log.d("status", Integer.toString(message_ids[i]));
                        Log.d("status", status[i]);
                    }
                }
            }

            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("Error", "Cannot Estabilish Connection");
                completion = false;
            }

            completion = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(completion)
            {
                try
                {
                    ((RingToChatScreen) mActivity).storeChatMessage(false, message_ids[0], obj.getString("from"), obj.getString("to"), "out", Message.getDateAndTime(), text, status[0], "true");
                }

                catch(JSONException e)
                {
                    Log.e("RingToAPI", "JSON object did not have the correct attributes");
                    Log.d("RingToAPI Object Attrib", obj.toString());
                }
            }
            else
            {
                Log.e("RingToAPI", "GetMessages did not complete the getMessage HTTPConnection.");
            }
        }
    }

    private class GetNumbers extends AsyncTask<Void, Integer, Void>
    {
        boolean login = false;
        String token = "";
        String jb_token = "";
        ArrayList<Message> temp = null;
        ProgressDialog progressDialog;

        public GetNumbers(ActionBarActivity activity, String token, String jb_token)
        {
            attach(activity);
            this.token = token;
            this.jb_token = jb_token;
//            progressDialog = ProgressDialog.show(activity, "Downloading Messages",
            // "dialog message", true);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

        @Override
//        protected Void doInBackground(Void... params)
        protected Void doInBackground(Void... params)
        {
            Log.d("Starting", "AsyncTask starting.");
            JSONObject jObj_returned = null;
            String URL = "https://ring.to/api/numbers";

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);
            Log.d("jb token thing", jb_token);
            httpPost.setHeader("JB-Token", jb_token);
            ArrayList<String> numbers = new ArrayList<String>();

            try
            {
                Log.d("RingTo jb-token", jb_token);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                String retSrc = "";

                if (httpEntity != null)
                {
                    retSrc = EntityUtils.toString(httpEntity);
                    jObj_returned = new JSONObject(retSrc);
                    JSONArray number = (JSONArray) jObj_returned.get("numbers");

                    for(int i=0; i < number.length(); i++)
                    {
                        numbers.add(number.getJSONObject(i).getString("number"));
                    }

                    Log.d("Numbers", numbers.get(0));
                }
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
        protected void onProgressUpdate(Integer... values)
        {
            if(values[0].longValue() < 20)
            {
                progressDialog.setMessage("Parsing Through Messages");
            }

            else if(values[0].longValue() > 20)
            {
                progressDialog.setMessage("Storing Messages In Local Database");
            }

//            getMessages(mActivity);
        }

        @Override
        protected void onPostExecute(Void params)
        {
            super.onPostExecute(params);

            if (mActivity != null)
            {
//                storeMessages(mActivity);
                ArrayList<Message> temp = new ArrayList<Message>();
                temp.addAll(((RingToMainContainer) mActivity).getDataSource().getRecentChatThreads());

                if(list != null)
                {
                    list.addMessageThreads(temp);
                    list.notifyDataSetChanged();
                }

                progressDialog.cancel();
                ((RingToMainContainer) mActivity).printStuff(temp);
                Toast.makeText(mActivity, "AsyncTask finished", Toast.LENGTH_LONG).show();
                Log.d("Done", "AsyncTask finished while Activity was attached.");
                RingToApplication singleton = (RingToApplication) mActivity.getApplicationContext();
                singleton.setInitialPullMessage(true);
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                detach();
            }

            else
            {
                Log.d("Done", "AsyncTask finished while no Activity was attached.");
            }

        }
    }

    private class GetMessages extends AsyncTask<Void, Integer, Void>
    {
        boolean login = false;
        String token = "";
        ArrayList<Message> temp = null;
        ProgressDialog progressDialog;

        public GetMessages(ActionBarActivity activity, String token)
        {
            attach(activity);
            this.token = token;
//            progressDialog = ProgressDialog.show(activity, "Downloading Messages",
                   // "dialog message", true);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Downloading All Messages");
            progressDialog.setMessage("Pulling Messages From Server");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
//        protected Void doInBackground(Void... params)
        protected Void doInBackground(Void... params)
        {
            Log.d("Starting", "AsyncTask starting.");
            JSONObject jObj = null;
            JSONObject jObj_returned = null;
            String retSrc = "";
//            String URL = "https://ring.to/api/messages?";
            RingToApplication application = new RingToApplication();

            String URL = "https://partners.ring.to/api/users/" + token + "/messages/conversations?access_token="
                    + access_token + "&messages=10&page=1&page_size=30";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();

            try
            {
                Log.d("RingTo uuid", token);
                Log.d("RingTo uuid", application.getAccessToken());

                HttpGet httpGet = new HttpGet(URL);
//                httpGet.setHeader("JB-Token", token);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;

                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }

                JSONObject obj = new JSONObject(builder.toString());
                Log.d("Output", obj.toString());
                List<String> list = new ArrayList<String>();
                JSONArray array1 = null;

                try
                {
                    array1 = obj.getJSONArray("data");
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                int array_count = 0;
                int prev_array_length = 0;

                for(int message_count = 0; message_count < array1.length(); message_count++)
                {
                    JSONObject obj2 = array1.getJSONObject(message_count);
                    JSONArray array = obj2.getJSONArray("messages");
                    array_count = array_count + array.length();
                    Log.d("Length of Array1", Integer.toString(array_count));
                }

                int myProgress = 0;

                for(int index = 0; index < array1.length(); index++)
                {
                    JSONObject obj2 = array1.getJSONObject(index);
                    JSONArray array = obj2.getJSONArray("messages");

                    jobj_message = array;

                    progressDialog.setProgress(myProgress);
                    publishProgress(myProgress);

                    for (int i = 0; i < array.length(); i++)
                    {
                        list.add(array.getJSONObject(i).getString("text"));
                        prev_array_length = prev_array_length + 1;
                        myProgress = ((prev_array_length * 100) / (2 * array_count)); //jobj_message.length()));
                        progressDialog.setProgress(myProgress);
                        publishProgress(myProgress);
                        Log.d("array length", Integer.toString(prev_array_length));
                        Log.d("20 my progress", Integer.toString(myProgress));
                    }

//                for (int j = 0; j < list.size(); j++) {
//                    Log.d("Text", list.get(j));
//                }

                    if (mActivity != null)
                    {
                        Log.d("Message Size", Integer.toString(jobj_message.length()));

                        for (int i = jobj_message.length() - 1; i > -1; i--)
                        {
                            long message_id;
                            String direction;
                            String from_number;
                            String to_number;
                            String text;
                            String time;
//                        String date_raw;
//                        String incoming;
                            String read;
                            String status;
                            boolean left;

                            try
                            {
                                message_id = jobj_message.getJSONObject(i).getLong("message_id");
                                from_number = jobj_message.getJSONObject(i).getString("from");
                                to_number = jobj_message.getJSONObject(i).getString("to");
                                direction = jobj_message.getJSONObject(i).getString("direction");
                                time = jobj_message.getJSONObject(i).getString("time");
                                //date_raw = jobj_message.getJSONObject(i).getString("date_raw");
                                text = jobj_message.getJSONObject(i).getString("text");
//                            incoming = jobj_message.getJSONObject(i).getString("status");
                                status = jobj_message.getJSONObject(i).getString("status");
                                read = jobj_message.getJSONObject(i).getString("read");

//                            if(direction.equals("Incoming"))
                                if (direction.equals("in"))
                                {
                                    left = true;
                                }

                                else
                                {
                                    left = false;
                                }

                                ((RingToMainContainer) mActivity).storeChatMessage(left, message_id, from_number, to_number, direction,
                                        time, text, status, read);
                            }

                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            prev_array_length = prev_array_length + 1; //(jobj_message.length() - i);
                            myProgress = ((prev_array_length * 100)/ (2 * array_count));
                            publishProgress(myProgress);
                            progressDialog.setProgress(myProgress);
                            Log.d("array length", Integer.toString(prev_array_length));
                            Log.d("20 my progress", Integer.toString(myProgress));
                        }
                    }
                }
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
        protected void onProgressUpdate(Integer... values)
        {
            if(values[0].longValue() < 20)
            {
                progressDialog.setMessage("Parsing Through Messages");
            }

            else if(values[0].longValue() > 20)
            {
                progressDialog.setMessage("Storing Messages In Local Database");
            }

//            getMessages(mActivity);
        }

        @Override
        protected void onPostExecute(Void params)
        {
            super.onPostExecute(params);

            if (mActivity != null)
            {
//                storeMessages(mActivity);
                ArrayList<Message> temp = new ArrayList<Message>();
                temp.addAll(((RingToMainContainer) mActivity).getDataSource().getRecentChatThreads());

                if(list != null)
                {
                    list.addMessageThreads(temp);
                    list.notifyDataSetChanged();
                }

                progressDialog.cancel();
                ((RingToMainContainer) mActivity).printStuff(temp);
                Toast.makeText(mActivity, "AsyncTask finished", Toast.LENGTH_LONG).show();
                Log.d("Done", "AsyncTask finished while Activity was attached.");
                RingToApplication singleton = (RingToApplication) mActivity.getApplicationContext();
                singleton.setInitialPullMessage(true);
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                detach();
            }

            else
            {
                Log.d("Done", "AsyncTask finished while no Activity was attached.");
            }

        }
    }
}
