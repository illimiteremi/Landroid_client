package com.example.landroidclient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.erz.joysticklibrary.JoyStick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "[LANDROID][CLIENT]";

    private static final byte INITIAL_SPEED = 0;
    private static final byte MAX_SPEED = 100;

    private static final String HOST_KEY  = "HOST";
    private static final String PORT_KEY  = "KEY";

    private Button bStart, bStop, bTest, bHalt;
    private ImageButton bAloneMode;

    private TextView message;
    private JoyStick joyStick;

    private EditText host, port;
    private TextView modeAuto;
    private String hostName;
    private Integer portValue;

    private Socket      connexion = null;
    private Boolean     stayPut   = false;
    private Boolean     aloneMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joyStick   = findViewById(R.id.joyStick);
        message    = findViewById(R.id.textMessage);
        host       = findViewById(R.id.editHost);
        port       = findViewById(R.id.editPort);
        bStart     = findViewById(R.id.buttonConnect);
        bStop      = findViewById(R.id.buttonClose);
        bTest      = findViewById(R.id.buttonTest);
        bAloneMode = findViewById(R.id.autoMode);
        bHalt      = findViewById(R.id.buttonHalt);
        modeAuto = findViewById(R.id.textModeAuto);

        port.setText(DataStore.getString(getApplicationContext(), PORT_KEY, "8080"));
        host.setText(DataStore.getString(getApplicationContext(), HOST_KEY, "192.168.x.x"));

        createListener();
        DataStore.realRxPermisssion(getApplicationContext());
   }


    private void sendCommande(final byte direction, final byte speed) {
       Log.d(TAG, "sendCommande: " + direction + " - " + speed);
        hostName  = host.getText().toString();
        portValue = Integer.parseInt(port.getText().toString());
        Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               clientConnexion(hostName, portValue, direction, speed);
           }
       });
       thread.start();
   }

    private void clientConnexion(String host, int port, byte direction, byte speed){
        try {
            connexion = new Socket();
            connexion.connect(new InetSocketAddress(host, port), 3000);
            DataOutputStream out = new DataOutputStream(connexion.getOutputStream());
            DataInputStream in = new DataInputStream(connexion.getInputStream());

            // On envoie la commande au serveur
            out.write(direction);
            out.write(speed);
            out.flush();
            Log.d(TAG, message + " envoyée au serveur");

            // On attend la réponse
            byte response = in.readByte();
            String result = "Réponse reçue : " + Constants.RESPONSE.getValue(response).getMessage();
            if (direction == Constants.COMMANDE.TEST_LANDROID.getCode()) {
                message.setTextColor(Color.BLACK);
                message.setText(result);
            }
            Log.d(TAG,result);
        } catch (final Exception e) {
            if (direction == Constants.COMMANDE.TEST_LANDROID.getCode()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.setText(e.getMessage());
                    }
                });
            }
            Log.e(TAG, "Erreur : ", e);
        }
    }

    private void createListener() {

        host.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DataStore.putString(getApplicationContext(), HOST_KEY, s.toString());

            }
        });

        port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DataStore.putString(getApplicationContext(), PORT_KEY, s.toString());
            }
        });

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommande(Constants.COMMANDE.BRUSH_START.getCode(), MAX_SPEED);
                message.setText(Constants.COMMANDE.BRUSH_START.getMessage());
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommande(Constants.COMMANDE.BRUSH_STOP.getCode(), INITIAL_SPEED);
                message.setText(Constants.COMMANDE.BRUSH_STOP.getMessage());
            }
        });

        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommande(Constants.COMMANDE.TEST_LANDROID.getCode(), INITIAL_SPEED);
                message.setText(Constants.COMMANDE.TEST_LANDROID.getMessage());
            }
        });

        bHalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommande(Constants.COMMANDE.HALT_SYSTEM.getCode(), INITIAL_SPEED);
                message.setText(Constants.COMMANDE.HALT_SYSTEM.getMessage());
            }
        });

        bAloneMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aloneMode) {
                    message.setText(Constants.COMMANDE.STOP_ALONE_MODE.getMessage());
                    modeAuto.setTextColor(Color.BLACK);
                    sendCommande(Constants.COMMANDE.STOP_ALONE_MODE.getCode(), INITIAL_SPEED);
                    aloneMode  = false;
                } else {
                    message.setText(Constants.COMMANDE.START_ALONE_MODE.getMessage());
                    modeAuto.setTextColor(Color.GREEN);
                    sendCommande(Constants.COMMANDE.START_ALONE_MODE.getCode(), INITIAL_SPEED);
                    aloneMode  = true;
                }
            }
        });

        JoyStick.JoyStickListener joyStickListener = new JoyStick.JoyStickListener() {
            @Override
            public void onMove(JoyStick joyStick, double angle, double power, int direction) {
                byte landroidDirection = (byte)direction;
                byte landroidSpeed     = (byte) power;
                message.setText(Constants.COMMANDE.getValue(landroidDirection).getMessage() + " - " + landroidSpeed + "%");
                sendCommande(landroidDirection, landroidSpeed);
                Log.d(TAG, "onMove: " + Constants.COMMANDE.getValue(landroidDirection) + " - " + landroidSpeed);

                // Disable alone mode
                if (aloneMode) {
                    sendCommande(Constants.COMMANDE.STOP_ALONE_MODE.getCode(), (byte)0);
                    aloneMode  = false;
                }
            }

            @Override
            public void onTap() {
                Log.d(TAG, "onTap: ");
            }

            @Override
            public void onDoubleTap() {
                Log.d(TAG, "onDoubleTap: ");
                if (stayPut) {
                    joyStick.enableStayPut(false);
                    stayPut = false;
                } else {
                    joyStick.enableStayPut(true);
                    stayPut = true;
                }
            }
        };
        joyStick.setListener(joyStickListener);
    }
}
