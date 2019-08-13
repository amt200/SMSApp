package sg.edu.rp.c346.smsapp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button buttonSend, buttonLaunch;
    EditText etRecipient, etContent;
    BroadcastReceiver rc;

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(rc);
        super.onDestroy();
    }
    private void send_to_multiple_recipients(String[] arr, String content) {
        SmsManager smsManager = SmsManager.getDefault();
        for (int i = 0; i < arr.length; i++) {
            smsManager.sendTextMessage(arr[i], null, content, null, null);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = findViewById(R.id.buttonSend);
        buttonLaunch = findViewById(R.id.buttonMessage);
        etRecipient = findViewById(R.id.editTextRecipient);
        etContent = findViewById(R.id.editTextContent);
        rc = new Receiver();
        IntentFilter itFilter = new IntentFilter();
        final String content = etContent.getText().toString();

        final String destNumber = etRecipient.getText().toString();

        itFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(rc, itFilter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content == null || destNumber == null) {
                    Toast.makeText(MainActivity.this, "Please fill up all the fields", Toast.LENGTH_LONG);
                    return;
                } else {
                    if (destNumber.contains(",")) {
                        String[] numbersArr = destNumber.split(", ");
                        send_to_multiple_recipients(numbersArr, content);
                    } else {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(destNumber, null, content, null, null);
                        Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG);
                        etContent.setText("");
                    }
                }
            }
        });
        buttonLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content == null || destNumber == null) {
                    Toast.makeText(MainActivity.this, "Please fill up all the fields", Toast.LENGTH_LONG);
                    return;
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("smsto: "+destNumber));
                    intent.putExtra("sms_body", content);
                    startActivity(intent);
                }
            }
        });
    }
}
