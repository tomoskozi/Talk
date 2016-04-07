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

    private static final String TAG = "Talk";
    AudioGroup m_AudioGroup;
    AudioStream m_AudioStream;
//
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
            String localIP = ((EditText) findViewById(R.id.editText2)).getText().toString();
            ((EditText) findViewById(R.id.editText2)).setText(InetAddress.getByAddress(getLocalIPAddress()).getHostAddress());
            m_AudioStream = new AudioStream(InetAddress.getByAddress(getLocalIPAddress()));
            int localPort = m_AudioStream.getLocalPort();
            m_AudioStream.setCodec(AudioCodec.PCMU);
            m_AudioStream.setMode(RtpStream.MODE_NORMAL);

            ((TextView) findViewById(R.id.lblLocalPort)).setText(String.valueOf(localPort));
            findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String remoteAddress = ((EditText) findViewById(R.id.editText3)).getText().toString();
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

            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    m_AudioStream.release();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public static byte[] getLocalIPAddress () {
        byte ip[]=null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        ip = inetAddress.getAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.i("SocketException ", ex.toString());
        }
        return ip;

    }
}
