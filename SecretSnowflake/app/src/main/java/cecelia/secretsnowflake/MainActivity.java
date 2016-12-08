package cecelia.secretsnowflake;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    String phoneNumber = "2025060820";
    String smsBody = "This is an SMS!";

    String SMS_SENT = "SMS_SENT";
    String SMS_DELIVERED = "SMS_DELIVERED";

    private final static int MY_PERMISSIONS_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent i = new Intent(Intent.ACTION_SEND);
        Log.d("BLAH", "another");
        Button button = (Button) findViewById(R.id.send);
        // Here, thisActivity is the current activity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BLAH", "button pressed");
                sendTextIfPermission();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Log.d("butts", "butts");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void sendSMS() {
        SmsManager manager = SmsManager.getDefault();

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        // For when the SMS has been sent
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SMS_SENT));

        // For when the SMS has been delivered
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SMS_DELIVERED));

        // Get the default instance of SmsManager
                SmsManager smsManager = SmsManager.getDefault();
        // Send a text based SMS
                smsManager.sendTextMessage(phoneNumber, null, smsBody, sentPendingIntent, deliveredPendingIntent);
            }

    private void sendTextIfPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST);

            }
        } else {
            sendSMS();
        }
    }
}
