package mytechbus.hpie.com.mytechbus;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private SessionHandler session;
    String booking_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate());

        Button bookingBtn = findViewById(R.id.btnBooking);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            booking_message = bundle.getString(Constants.KEY_MESSAGE);

            if(!booking_message.equals(Constants.KEY_EMPTY)) {
                //Toast.makeText(getApplicationContext(), booking_message,Toast.LENGTH_LONG).show();
                showDialog(booking_message);
            }
        }

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}