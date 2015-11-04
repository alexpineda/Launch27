package com.example.alexpineda.launch27;

import android.os.AsyncTask;
import android.os.HandlerThread;

import com.example.alexpineda.launch27.WebServices.Dto.LoginDto;
import com.example.alexpineda.launch27.WebServices.Dto.LoginUserDtoResponse;
import com.example.alexpineda.launch27.WebServices.Dto.ServiceDtoResponse;
import com.example.alexpineda.launch27.WebServices.Dto.ServicesDtoResponse;
import com.example.alexpineda.launch27.WebServices.Dto.SpotDtoResponse;
import com.example.alexpineda.launch27.WebServices.Dto.SpotsDtoResponse;
import com.example.alexpineda.launch27.WebServices.Dto.StatesDtoContainerResponse;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexpineda77 on 2015-11-03.
 */
public class AppServices {

    public final static String APP_PREFS_NAME = "LAUNCH27";

    public final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public final  MediaType FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private final String LAUNCH27_URL_LOGIN = "https://login.launch27.com/login";
    private static String LAUNCH27_URL_SUBDOMAIN = "";
    private static String LAUNCH27_API_KEY = "";

    private static OkHttpClient httpClient;

    public boolean Login(String email, String password){
        httpClient = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.setCookieHandler(cookieManager);
        String data = "user_session[email]="+email+"&user_session[password]="+password;
        RequestBody body = RequestBody.create(FORM_URLENCODED,data);
        Request request = new Request.Builder()
                .url(LAUNCH27_URL_LOGIN)
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies){
                if (cookie.getName().equals("user_credentials")){
                    LAUNCH27_URL_SUBDOMAIN = "https://"+response.request().url().getHost();
                    LAUNCH27_API_KEY = crawlAPIToken();
                    if (LAUNCH27_API_KEY.length()==0){
                        return false;
                    }
                    return true;
                }
            }

            return false;
        } catch (java.io.IOException e){
            return false;
        }
    }

    public String crawlAPIToken(){

        Request request = new Request.Builder()
                .url(_buildUrlApiAccess())
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            String html = response.body().string();

            String pattern = "(.*)(\\d+)(.*)";
            Pattern r = Pattern.compile("\\b(live_[\\w]+)\\s");
            Matcher m = r.matcher(html);

            if (m.find()) {
                return m.group(0).trim();
            } else {
                return "";
            }

        } catch (java.io.IOException e){
            return "";
        }

    }

    public void getStates(){
        new getStatesAsync().execute();
    }

    class getStatesAsync extends AsyncTask<String, Void, List<String>> {
        private Exception exception;

        protected List<String> doInBackground(String... urls) {
            Request.Builder builder = new Request.Builder()
                    .url(_buildUrlApiStates());
            _addLaunchHeaders(builder);
            Request request = builder.build();

            try {
                Response response = httpClient.newCall(request).execute();
                String str = response.body().string();
                Gson gson = new Gson();
                StatesDtoContainerResponse states = gson.fromJson(str, StatesDtoContainerResponse.class);
                return states.getStates();
            } catch (java.io.IOException e){
                return null;
            }
        }

        protected void onPostExecute(List<String> feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    public void getServices(){
        new getServicesAsync().execute();
    }

    class getServicesAsync extends AsyncTask<String, Void, List<ServiceDtoResponse>> {
        private Exception exception;

        protected List<ServiceDtoResponse> doInBackground(String... urls) {
            Request.Builder builder = new Request.Builder()
                    .url(_buildUrlApiServices());
            _addLaunchHeaders(builder);
            Request request = builder.build();

            try {
                Response response = httpClient.newCall(request).execute();
                String str = response.body().string();
                Gson gson = new Gson();
                ServicesDtoResponse services = gson.fromJson(str, ServicesDtoResponse.class);
                return services.services;
            } catch (java.io.IOException e){
                return null;
            }
        }

        protected void onPostExecute(List<ServiceDtoResponse> feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

        public void getSpots(Date date){
            new getSpotsAsync().execute(date);
        }

    class getSpotsAsync extends AsyncTask<Date, Void, List<SpotDtoResponse>> {
        private Exception exception;

        protected List<SpotDtoResponse> doInBackground(Date... date) {
            String fdate = new SimpleDateFormat("yyyy-MM-dd").format(date[0]);

            Request.Builder builder = new Request.Builder()
                    .url(_buildUrlApiGetSpots(fdate));
            _addLaunchHeaders(builder);
            Request request = builder.build();

            try {
                Response response = httpClient.newCall(request).execute();
                String str = response.body().string();
                Gson gson = new Gson();
                SpotsDtoResponse spots = gson.fromJson(str, SpotsDtoResponse.class);
                return spots.spots;
            } catch (java.io.IOException e){
                return null;
            }
        }

        protected void onPostExecute(List<SpotDtoResponse> feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }



    private String _buildUrlApiGetSpots(String date){
        return LAUNCH27_URL_SUBDOMAIN + "/api/spots/"+date;
    }

    private String _buildUrlApiAccess(){
        return LAUNCH27_URL_SUBDOMAIN + "/admin/clients/setup?tab=api_access";
    }

    private String _buildUrlApiStates(){
        return LAUNCH27_URL_SUBDOMAIN + "/api/states";
    }

    private String _buildUrlApiServices(){
        return LAUNCH27_URL_SUBDOMAIN + "/api/services";
    }

    private void _addLaunchHeaders(Request.Builder builder ){
        builder.addHeader("X-API-Key", LAUNCH27_API_KEY);
        builder.addHeader("Accept", "application/launch27.v2");
    }

}
