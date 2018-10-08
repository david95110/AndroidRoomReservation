package dk.easj.anbo.roomreservationokhttp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView messageView;
    private static final String uri = "https://anbo-roomreservation.azurewebsites.net/api/reservations/room/1"; //"https://reqres.in/api/users/2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.main_message_textview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GetReservationsTask okHttpHandler = new GetReservationsTask();
        okHttpHandler.execute(uri);
    }

    public void addReservationButtonClicked(View view) {
        Intent intent = new Intent(this, AddReservationActivity.class);
        startActivity(intent);
    }

    private class GetReservationsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Log.d(CommonStuff.TAG, jsonString);
                return jsonString;
            } catch (Exception e) {
                Log.d("SHIT", e.getMessage());
                cancel(true);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            Gson gson = new GsonBuilder().create();
            final Reservation[] reservations = gson.fromJson(jsonString, Reservation[].class);
            Log.d(CommonStuff.TAG, Arrays.toString(reservations));
            ArrayAdapter<Reservation> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, reservations);

            ListView listView = findViewById(R.id.mainListView);
            listView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled(String message) {
            messageView.setText(message);
        }
    }
}
