package dk.easj.anbo.roomreservationokhttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddReservationActivity extends AppCompatActivity {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
    }

    public void addReservationButtonClicked(View view) {
        EditText userIdField = findViewById(R.id.addReservation_userId_edittext);
        EditText roomIdField = findViewById(R.id.addReservation_roomId_edittext);
        EditText fromTimeField = findViewById(R.id.addReservation_fromTime_edittext);
        EditText toFimeField = findViewById(R.id.addReservation_toTime_edittext);
        EditText purposeField = findViewById(R.id.addReservation_purpose_edittext);

        String userId = userIdField.getText().toString();
        String roomId = roomIdField.getText().toString();
        String fromTime = fromTimeField.getText().toString();
        String toTime = toFimeField.getText().toString();
        String purpose = purposeField.getText().toString();

        TextView messageView = findViewById(R.id.addReservation_message_textview);

        try { // create JSON document
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("roomId", roomId);
            jsonObject.put("fromTimeString", fromTime);
            jsonObject.put("toTimeString", toTime);
            jsonObject.put("purpose", purpose);
            String jsonDocument = jsonObject.toString();
            messageView.setText(jsonDocument);
            AddReservationTask task = new AddReservationTask();
            task.execute("https://anbo-roomreservation.azurewebsites.net/api/reservations", jsonDocument);
        } catch (JSONException ex) {
            messageView.setText(ex.getMessage());
        }
    }

    private void done() {
        finish();
    }

    private class AddReservationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            String jsonString = strings[1];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException ex) {
                Log.e(CommonStuff.TAG, ex.getMessage());
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(CommonStuff.TAG, "Reservation added");
            done();
        }

        @Override
        protected void onCancelled(String s) {
            Log.d(CommonStuff.TAG, "Problem: Reservation add");
        }
    }

}
