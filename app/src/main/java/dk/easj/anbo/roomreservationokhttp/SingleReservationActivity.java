package dk.easj.anbo.roomreservationokhttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SingleReservationActivity extends AppCompatActivity {
    
    public static final String RESERVATION = "reservation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_reservation);
        Intent intent = getIntent();
        Reservation reservation = (Reservation) intent.getSerializableExtra(RESERVATION);
        Log.d(CommonStuff.TAG, reservation.toString());

        TextView userIdView = findViewById(R.id.singleReservation_userId_textview);
        userIdView.setText("user: " + reservation.getUserId());

        TextView purposeView = findViewById(R.id.singleReservation_purpose_textview);
        purposeView.setText("purpose: " + reservation.getPurpose());

    }
}
