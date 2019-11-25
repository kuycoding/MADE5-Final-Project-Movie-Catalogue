package com.kuycoding.moviecatalogue3.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.reminder.DailyReminder;
import com.kuycoding.moviecatalogue3.reminder.ReleaseReminder;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String DAILY = "daily";
    private static final String RELEASE = "release";

    Switch dailyCheck, upcomingCheck;
    ReleaseReminder newMoviesAlarm;
    DailyReminder dailyCheckerAlarm;
    SharedPreferences dailyAlarmPrefer, newMoviePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dailyCheck = findViewById(R.id.daily_setting);
        upcomingCheck = findViewById(R.id.release_setting);

        dailyCheck.setOnCheckedChangeListener(this);
        upcomingCheck.setOnCheckedChangeListener(this);

        dailyCheckerAlarm  = new DailyReminder();
        newMoviesAlarm = new ReleaseReminder();

        dailyAlarmPrefer = getSharedPreferences(DAILY, MODE_PRIVATE);
        dailyCheck.setChecked(dailyAlarmPrefer.getBoolean(DAILY, false));

        newMoviePref = getSharedPreferences(RELEASE, MODE_PRIVATE);
        upcomingCheck.setChecked(newMoviePref.getBoolean(RELEASE, false));

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChacked) {
        switch (compoundButton.getId()) {
            case (R.id.daily_setting):
                if (isChacked) {
                    String time = "07:00";
                    dailyCheckerAlarm.setDailyAlarm(getApplicationContext(), time);
                    SharedPreferences.Editor editor = getSharedPreferences(DAILY, MODE_PRIVATE).edit();
                    editor.putBoolean(DAILY, true);
                    editor.apply();
                    Toast.makeText(this,"Daily reminder has turn on",Toast.LENGTH_SHORT).show();
                } else {
                    dailyCheckerAlarm.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(DAILY, MODE_PRIVATE).edit();
                    editor.putBoolean(DAILY, false);
                    editor.apply();
                    Toast.makeText(this,"Daily reminder has turn off",Toast.LENGTH_SHORT).show();
                }
                break;

            case (R.id.release_setting):
                if (isChacked) {
                    String time = "08:00";
                    newMoviesAlarm.setDailyNewMovieAlarm(getApplicationContext(), time);
                    SharedPreferences.Editor editor = getSharedPreferences(RELEASE, MODE_PRIVATE).edit();
                    editor.putBoolean(RELEASE, true);
                    editor.apply();
                    Toast.makeText(this,"Release reminder has turn on",Toast.LENGTH_SHORT).show();
                } else {
                    newMoviesAlarm.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(RELEASE, MODE_PRIVATE).edit();
                    editor.putBoolean(RELEASE, false);
                    editor.apply();
                    Toast.makeText(this,"Release reminder has turn off",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
