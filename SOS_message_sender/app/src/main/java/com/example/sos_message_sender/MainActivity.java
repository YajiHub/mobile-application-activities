package com.example.sos_message_sender;

import android.content.Context;
import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.content.SharedPreferences;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import java.util.*;

import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Set<String> contacts;
    Spinner simSpinner;
    TextView contactListText;


    double latitude = 0;
    double longitude = 0;

    public LocationManager manager;
    public GPSReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences("sos_prefs", MODE_PRIVATE);
        contacts = prefs.getStringSet("contacts", new HashSet<>());

        simSpinner = findViewById(R.id.simSpinner);
        contactListText = findViewById(R.id.contactListText);

        ArrayAdapter<String> simAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("SIM 1", "SIM 2"));
        simAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        simSpinner.setAdapter(simAdapter);

        updateContactDisplay();

        myButtonListenerMethod();
        receiver = new GPSReceiver();
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L, 1.0F, receiver);

    //end of oncreate method
    }

    void updateContactDisplay() {
        if (contacts.isEmpty()) {
            contactListText.setText("Contacts: None");
        } else {
            contactListText.setText("Contacts: " + String.join(", ", contacts));
        }
    }

    public class GPSReceiver implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(getApplicationContext(),
                        "READY TO SEND!!!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "NOT READY YET...", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getApplicationContext(), "GPS Enabled!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(), "Please enable GPS!", Toast.LENGTH_LONG).show();
        }

    }

    public void myButtonListenerMethod() {
        Button button = findViewById(R.id.sendSOS);

        // Short click = send to random
        button.setOnClickListener(v -> {
            if (contacts.isEmpty()) {
                Toast.makeText(this, "No contacts saved!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> list = new ArrayList<>(contacts);
            String randomContact = list.get(new Random().nextInt(list.size()));
            sendSMS(randomContact);
        });

        // Long click = manage contacts
        button.setOnLongClickListener(v -> {
            showContactManager();
            return true;
        });
    }

    void sendSMS(String number) {
//        logAndToastSelectedSim();

        int simIndex = simSpinner.getSelectedItemPosition(); // 0 = SIM 1, 1 = SIM 2
        String message = "SOS! My location: https://maps.google.com/?q=" + latitude + "," + longitude;

        try {
            SmsManager smsManager;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                List<?> subs = subManager.getActiveSubscriptionInfoList();
                if (subs != null && subs.size() > simIndex) {
                    int subId = subManager.getActiveSubscriptionInfoList().get(simIndex).getSubscriptionId();
                    smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
                } else {
                    smsManager = SmsManager.getDefault();
                }
            } else {
                smsManager = SmsManager.getDefault();
            }

            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(this, "SOS sent to " + number, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

    void showContactManager() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint("Enter number (e.g. 09631338357)");

        new AlertDialog.Builder(this)
                .setTitle("Manage Contacts")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String num = input.getText().toString().trim();
                    if (!num.isEmpty()) {
                        contacts.add(num);
                        prefs.edit().putStringSet("contacts", contacts).apply();
                        updateContactDisplay();
                    }
                })
                .setNeutralButton("View/Delete", (dialog, which) -> {
                    if (contacts.isEmpty()) {
                        Toast.makeText(this, "No contacts to delete", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String[] items = contacts.toArray(new String[0]);
                    new AlertDialog.Builder(this)
                            .setTitle("Tap to delete")
                            .setItems(items, (d, i) -> {
                                contacts.remove(items[i]);
                                prefs.edit().putStringSet("contacts", contacts).apply();
                                updateContactDisplay();
                            })
                            .show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }






//    void logAndToastSelectedSim() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//            List<SubscriptionInfo> subs = subManager.getActiveSubscriptionInfoList();
//
//            if (subs != null && subs.size() > simSpinner.getSelectedItemPosition()) {
//                SubscriptionInfo info = subs.get(simSpinner.getSelectedItemPosition());
//                String simName = "SIM " + (simSpinner.getSelectedItemPosition() + 1) + " - " + info.getCarrierName();
//                Toast.makeText(this, "Using: " + simName, Toast.LENGTH_SHORT).show();
//                Log.d("SIM_DEBUG", "Selected SIM: " + simName + ", subId: " + info.getSubscriptionId());
//            } else {
//                Toast.makeText(this, "Only 1 SIM or none detected", Toast.LENGTH_SHORT).show();
//                Log.d("SIM_DEBUG", "Fallback to default SIM");
//            }
//        } else {
//            Toast.makeText(this, "SIM selection not supported on this Android version", Toast.LENGTH_SHORT).show();
//        }
//    }


//end of main method
}