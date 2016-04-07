package com.mtomoskozi.talk;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MainActivity extends Activity {

    AudioGroup m_AudioGroup;
    AudioStream m_AudioStream;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
            m_AudioGroup = new AudioGroup();
            m_AudioGroup.setMode(AudioGroup.MODE_NORMAL);
            m_AudioStream = new AudioStream(InetAddress.getByAddress(getLocalIPAddress()));
            int localPort = m_AudioStream.getLocalPort();
            m_AudioStream.setCodec(AudioCodec.PCMU);
            m_AudioStream.setMode(RtpStream.MODE_NORMAL);

            ((TextView) findViewById(R.id.lblLocalPort)).setText(String.valueOf(localPort));

            ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String remoteAddress = ((EditText) findViewById(R.id.editText2)).getText().toString();
                    String remotePort = ((EditText) findViewById(R.id.editText1)).getText().toString();

                    try {
                        m_AudioStream.associate(InetAddress.getByName(remoteAddress), Integer.parseInt(remotePort));
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    m_AudioStream.join(m_AudioGroup);
                }
            });

            ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    m_AudioStream.release();
                }
            });

        } catch (Exception e) {
            Log.e("----------------------", e.toString());
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static byte[] getLocalIPAddress() {
        byte ip[] = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.i("SocketException ", ex.toString());
        }
        return ip;

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mtomoskozi.talk/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mtomoskozi.talk/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
