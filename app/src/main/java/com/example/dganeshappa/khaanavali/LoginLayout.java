package com.example.dganeshappa.khaanavali;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginLayout extends AppCompatActivity {

    EditText un,pw;
    TextView error;
    Button ok;
    // Session Manager Class
    SessionManager session;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        un=(EditText)findViewById(R.id.et_un);
        pw=(EditText)findViewById(R.id.et_pw);
        ok=(Button)findViewById(R.id.btn_login);
        error=(TextView)findViewById(R.id.tv_error);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("username", un.getText().toString()));
                postParameters.add(new BasicNameValuePair("password", pw.getText().toString()));

                String response = null;
                try {


            //      new JSONAsyncTask().execute("http://10.239.54.7:3000/v1/vendor/order/summary/x@gmail.com");
                 //   new JSONAsyncTask().execute("http://10.239.54.7:3000/v1/m/login", un.getText().toString(),pw.getText().toString());
                    new JSONAsyncTask().execute("http://oota.herokuapp.com/v1/m/login", un.getText().toString(),pw.getText().toString());
//                    response = CustomHttpClient.executeHttpGet("http://oota.herokuapp.com/v1/vendor/city?Chennai");
//                    String res=response.toString();
//                    res= res.replaceAll("\\s+","");
//                    if(res.equals("1"))
//                        error.setText("Correct Username or Password");
//                    else
//                        error.setText("Sorry!! Incorrect Username or Password");
                } catch (Exception e) {
                    un.setText(e.toString());
                }

            }
        });
    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginLayout.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
//                HttpGet httppost = new HttpGet(urls[0]);
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response = httpclient.execute(httppost);

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("email", urls[1]));
                postParameters.add(new BasicNameValuePair("password", urls[2]));
                postParameters.add(new BasicNameValuePair("role", "vendor"));

                HttpPost request = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);


                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                   if(data.equals("1"))
                   {
                       session.createLoginSession("Knvl", urls[1]);
                       Intent intent = new Intent("com.example.dganeshappa.khaanavali.MainActivity");
                       startActivity(intent);
                     //  Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       //Toast.makeText(getApplicationContext(), "error in logged in", Toast.LENGTH_LONG).show();
                   }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            //adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();

        }
    }
}

