package com.veloeye.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.veloeye.Singleton.ArrayResponseCallback;
import com.veloeye.Singleton.ObjectResponseCallback;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Connection {
        public static final String ACCESS_TOKEN = "access-token";
        //public JsonObject params;
        HashMap<String, List<String>> params = new HashMap<String, List<String>>();
        private Context context;
        private Method method;
        private String page;

        public static Connection with(Context context, String page) {
            Connection connection = new Connection();
            connection.context = context;
            connection.method = Method.POST;
            connection.page = page;
            return connection;
        }

        public Connection setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Connection addParameter(String key, String value) {
            //this.params.addProperty(key, value);
            this.params.put(key, Collections.singletonList(value));
            return this;
        }

        private String handleError(Response<?> result) {
            String error = null;
            if (result.getHeaders().code() == 404) {
                error = "Method does not exist";
            } else if (result.getHeaders().code() == 401) {
                //error = result.getResult().get("description").toString();
                error = "Unauthorized";
            } else if (result.getHeaders().code() != 200) {
                error = "Error occured";
            }
            return error;
        }

        public void performNetworkCallForImage(final ObjectResponseCallback callback, final boolean showProgress, String auth, String filename, File
        file, final String contentType, String type, final AttachPhoto attachPhoto) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setMultipartFile("File", file)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                              //  RealmController.with(context).storeImageTempInLocalDatabase(attachPhoto);
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallForArray(final ArrayResponseCallback callback, final boolean showProgress) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallToPostDataToServer(final ObjectResponseCallback callback, final boolean showProgress, String auth, JsonObject jsonObject) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), "https://cz.leanpoint.com/Api/Forms/SaveForms")
                    .setHeader("Authorization", auth)
                    .setHeader("Content-Type", "application/json")
                    .setTimeout(13000)
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }
        public void performNetworkCallForArrayForWareHouse(final ArrayResponseCallback callback, final boolean showProgress, String auth) {
            final ProgressDialog dialog;
            if (showProgress) {
//            dialog = new ProgressDialog(context);
//            dialog.setMessage("Please wait...");
//            dialog.setIndeterminate(false);
//            dialog.setCancelable(false);
//            dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            //if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }
        public void performNetworkCallForArrayForWareHouseForSave(final ArrayResponseCallback callback, String auth) {
            final ProgressDialog dialog;
//        if (showProgress) {
////            dialog = new ProgressDialog(context);
////            dialog.setMessage("Please wait...");
////            dialog.setIndeterminate(false);
////            dialog.setCancelable(false);
////            dialog.show();
//        } else {
//            dialog = null;
//        }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            //if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }
        public void performNetworkCall(final ObjectResponseCallback callback) {
            final ProgressDialog dialog;
            if (true) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (true) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }


        public void performNetworkCallForToolWarehouse(final ObjectResponseCallback callback, final boolean showProgress, String auth) {
            final ProgressDialog dialog;
            if (showProgress) {
//            dialog = new ProgressDialog(context);
//            dialog.setMessage("Please wait...");
//            dialog.setIndeterminate(false);
//            dialog.setCancelable(false);
//            dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            // if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallToolList(final ObjectResponseCallback callback, final boolean showProgress, String auth) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setHeader("Content-Type", "application/json")
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallToPostData(final ObjectResponseCallback callback, final boolean showProgress, String auth, JsonObject jsonObject) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallToPostDataforMoveItem(final ObjectResponseCallback callback, final boolean showProgress, String auth, JsonObject jsonObject) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setHeader("Content-Type","application/json")
                    .setTimeout(13000)
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }



        public void performNetworkCallToPostDataforOngoingFormula(final ObjectResponseCallback callback, final boolean showProgress, String auth, JsonObject jsonObject) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }


        public void performNetworkCallToPostDataforGetGuid(final ObjectResponseCallback callback, final boolean showProgress, String auth) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    // .setJsonObjectBody(jsonObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        public void performNetworkCallForArrayOnGoing(final ArrayResponseCallback callback, final boolean showProgress, String auth) {
            final ProgressDialog dialog;
            if (showProgress) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog = null;
            }

            try {

                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
// Create an ssl socket factory with our all-trusting manager
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
                Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("PARAMS", params.toString());
            Ion.with(context)
                    .load(method.toString(), this.page)
                    .setHeader("Authorization", auth)
                    .setTimeout(13000)
                    .setBodyParameters(params)
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            if (showProgress) dialog.dismiss();

                            String error = null;
                            if (e != null) {
                                error = "Exception occured: " + e.getMessage();
                            } else {
                                error = handleError(result);
                            }

                            if (error != null) {
                                callback.onFailure(error);
                            } else {
                                callback.onSuccess(result.getResult());
                            }
                        }
                    });
        }

        private HostnameVerifier getHostnameVerifier() {
            return new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                    // or the following:
                    // HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    // return hv.verify("www.yourserver.com", session);
                }
            };
        }


        public enum Method {
            GET,
            POST,
            PUT,
            DELETE
        }

    }


