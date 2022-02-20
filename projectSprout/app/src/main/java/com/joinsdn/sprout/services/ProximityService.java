package com.joinsdn.sprout.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.joinsdn.sprout.MainActivity;
import com.joinsdn.sprout.R;
import com.joinsdn.sprout.model.User;
import com.joinsdn.sprout.util.TokenGenerator;

public class ProximityService extends Service {
    public static final String SERVICE_CHAN_ID = "ProximityServiceChannel";

    public static final String[] REQUIRED_PERMISSIONS;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    };
        } else {
            REQUIRED_PERMISSIONS =
                    new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    };
        }
    }

    public static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;

    private ConnectionsClient connectionsClient;
    private NotificationManagerCompat notificationManager;

    private final String codeName = TokenGenerator.generate();
    private String matchEndpointID;
    private User match;

    // call backs
    // PayloadCallback, EndpointDiscoveryCallback, ConnectionLifeCycleCallback
    // TODO: Fill Callback Objects
    private PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
                    // decipher profile
                    // check profile compatibility
                    // determine if match or not
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                    // i do not know what this method means
                }
            };

    private ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
                    connectionsClient.acceptConnection(s, payloadCallback);
                }

                @Override
                public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution result) {
                    //
                    if (result.getStatus().isSuccess()) {
                        // we don't want to stop discovery, we are p2pcluster so we want as many as possible
//                        connectionsClient.stopDiscovery();
//                        connectionsClient.stopAdvertising();
                        Notification connectNotif = new Notification.Builder(getBaseContext(), SERVICE_CHAN_ID)
                                .setContentTitle("Match found!")
                                .setContentText("Click here to see more")
                                .build();
                        int notificationId = 10;

                        notificationManager.notify(notificationId, connectNotif);

                        matchEndpointID = s;
                    }
                }

                @Override
                public void onDisconnected(@NonNull String s) {
                    // handle disconnect
                }
            };

    private EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                    connectionsClient.requestConnection(codeName, s, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(@NonNull String s) { }
            };


    @Override
    public void onCreate() {
        super.onCreate();

        // Nearby Connections
        connectionsClient = Nearby.getConnectionsClient(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        createNotificationChannel();

        // create notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHAN_ID)
                .setContentTitle(getText(R.string.proxy_noti_title))
                .setContentText(getText(R.string.proxy_noti_msg))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build();

        startForeground(1, notification);

        // initialize notification manager
        notificationManager = NotificationManagerCompat.from(this);

        // start nearby connections
        startDiscovery();
        startAdvertising();

        // do work

        // stop self

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // no binding
        return null;
    }

    @Override
    public void onDestroy() {
        connectionsClient.stopAllEndpoints();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel proximityChannel = new NotificationChannel(
                    SERVICE_CHAN_ID,
                    "Proximity Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(proximityChannel);
        }
    }

    // handle advertising and discovery
    private void startDiscovery() {
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build());
    }

    private void startAdvertising() {
        connectionsClient.startAdvertising(
                codeName, getPackageName(), connectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build());
    }

    private void disconnect() {
        connectionsClient.disconnectFromEndpoint(matchEndpointID);
        // end and handle whatever needs to be handled
    }

}
