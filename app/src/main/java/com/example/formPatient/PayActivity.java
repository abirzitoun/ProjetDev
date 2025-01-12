package com.example.formPatient;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.charset.Charset;

public class PayActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );


        IntentFilter nfcDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{nfcDetected};
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id = tag.getId();
            String tagId = bytesToHex(id);

            Log.d("NFC_TAG", "Tag ID: " + tagId);

             Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                try {
                    ndef.connect();
                    NdefMessage ndefMessage = ndef.getNdefMessage();


                    if (ndefMessage != null) {
                        NdefRecord[] records = ndefMessage.getRecords();
                        for (NdefRecord record : records) {
                            byte[] payload = record.getPayload();
                            String payloadData = new String(payload, Charset.forName("UTF-8"));


                            Log.d("NFC_PAYLOAD", "NFC Tag Payload: " + payloadData);
                            System.out.println(payloadData);
                        }
                    }

                    ndef.close();

                } catch (Exception e) {
                    Log.e("NFC_ERROR", "Error reading NFC tag: " + e.getMessage());
                }
            } else {

                Log.e("NFC_ERROR", "This NFC tag is not NDEF formatted.");


                String[] techList = tag.getTechList();
                Log.d("NFC_TAG", "Tag technologies: " + String.join(", ", techList));

                // Example: Handle MifareClassic or NfcA tags
                if (techList != null) {
                    for (String tech : techList) {
                        if (tech.equals("android.nfc.tech.MifareClassic")) {
                            Log.d("NFC_TAG", "MifareClassic tag detected");

                        } else if (tech.equals("android.nfc.tech.NfcA")) {
                            Log.d("NFC_TAG", "NfcA tag detected");

                        } else {
                            Log.d("NFC_TAG", "Unsupported tag technology: " + tech);

                        }
                    }
                }
            }


            Toast.makeText(this, "Processing Payment for Tag ID: " + tagId, Toast.LENGTH_SHORT).show();


            sendSuccessSms("+21626324848", "Payment Successful! Tag ID: " + tagId);


            Intent successIntent = new Intent(this, SuccessActivity.class);
            successIntent.putExtra("tagData", "Tag ID: " + tagId);
            startActivity(successIntent);

            finish();
        }
    }


    private void sendSuccessSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS Sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SMS_ERROR", "Failed to send SMS: " + e.getMessage());
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

     private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
