package com.kuycoding.moviecatalogue3.reminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.kuycoding.moviecatalogue3.MainActivity;
import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

class NewMoviesAlarmServices {
    private MutableLiveData<ArrayList<Movies>> listMovie = new MutableLiveData<>();
    private static final String API_KEY = "b123a5d9a362f614b34df7b54814fd36";
    private static int notifId = 100;
    private Context context;

    void getReleaaseToday(final Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        String NEW_MOVIES_TODAY = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + TODAY_DATE() + "&primary_release_date.lte=" + TODAY_DATE();

        client.get(NEW_MOVIES_TODAY, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<Movies> listItem = new ArrayList<>();
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray responseArray = responseObject.getJSONArray("results");

                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject jsonObject = responseArray.getJSONObject(i);
                        Movies movies = new Movies(jsonObject);
                        listItem.add(movies);
                    }
                    int jumlah = listItem.size();
                    String notify = "Hi, there are now " + jumlah + " new movie! Lets check it out.";

                    for (Movies iMovies: listItem) {
                        if (iMovies.getTitle() ==  listItem.get(listItem.size() - 1).getTitle()) {
                            notify = notify + iMovies.getTitle();
                        } else {
                            notify = notify + iMovies.getTitle() + ", ";
                        }
                    }

                    JSONObject movie = responseArray.getJSONObject(0);
                    Movies movieitem = new Movies(movie);
                    String title = movieitem.getTitle();
                    showAlarmNotification(context, title, notify, notifId);

                    listMovie.postValue(listItem);
                    Log.d("NEWMOVIES", "onSuccess: " + title);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    private static String TODAY_DATE() {
        Date nowDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(nowDate);
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "New Movie Alarm Channel";

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_tab_tv_gold)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
