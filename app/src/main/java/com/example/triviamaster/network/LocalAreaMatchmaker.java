package com.example.triviamaster.network;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocalAreaMatchmaker {

    public interface MatchCallback {
        void onMatchFound(ActiveDuelSession session);
        void onNoPlayersInArea();
    }

    private static final String SERVICE_TYPE = "_triviamaster._tcp.";
    private static final long SEARCH_TIMEOUT_MS = 4500L;

    private final NsdManager nsdManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final AtomicBoolean finished = new AtomicBoolean(false);

    private ServerSocket serverSocket;
    private NsdManager.RegistrationListener registrationListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private String ownServiceName;
    private String countryPrefix;

    public LocalAreaMatchmaker(Context context) {
        this.nsdManager = (NsdManager) context.getApplicationContext().getSystemService(Context.NSD_SERVICE);
    }

    public void startSearch(String localUsername, String countryCode, List<Integer> hostQuestionIds, MatchCallback callback) {
        finished.set(false);
        String safeUser = (localUsername == null ? "Jugador" : localUsername.replaceAll("[^A-Za-z0-9]", ""));
        if (safeUser.isEmpty()) {
            safeUser = "Jugador";
        }
        String safeCountry = (countryCode == null || countryCode.trim().isEmpty())
                ? "AR"
                : countryCode.trim().toUpperCase(java.util.Locale.US);
        countryPrefix = "TM_" + safeCountry + "_";
        String nonce = String.valueOf(1000 + (int) (System.currentTimeMillis() % 9000));
        ownServiceName = countryPrefix + safeUser + "_" + nonce;

        mainHandler.postDelayed(() -> {
            if (finished.compareAndSet(false, true)) {
                stop();
                callback.onNoPlayersInArea();
            }
        }, SEARCH_TIMEOUT_MS);

        executor.execute(() -> {
            try {
                serverSocket = new ServerSocket(0);
                serverSocket.setSoTimeout((int) SEARCH_TIMEOUT_MS);
                int localPort = serverSocket.getLocalPort();
                registerService(localPort);
                discoverPeers(localUsername, hostQuestionIds, callback);
                acceptIncomingConnection(localUsername, hostQuestionIds, callback);
            } catch (Exception e) {
                if (finished.compareAndSet(false, true)) {
                    stop();
                    mainHandler.post(callback::onNoPlayersInArea);
                }
            }
        });
    }

    private void registerService(int port) {
        if (nsdManager == null) {
            return;
        }
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(ownServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);

        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo info) {
                ownServiceName = info.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo info, int errorCode) {
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo info) {
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo info, int errorCode) {
            }
        };
        try {
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        } catch (Exception ignored) {
        }
    }

    private void discoverPeers(String localUsername, List<Integer> hostQuestionIds, MatchCallback callback) {
        if (nsdManager == null) {
            return;
        }
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                if (finished.get()) {
                    return;
                }
                String name = service.getServiceName();
                if (name == null || name.equals(ownServiceName) || !name.startsWith(countryPrefix)) {
                    return;
                }
                if (ownServiceName.compareTo(name) <= 0) {
                    return;
                }
                resolveAndConnect(service, localUsername, callback);
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            }
        };
        try {
            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        } catch (Exception ignored) {
        }
    }

    private void resolveAndConnect(NsdServiceInfo service, String localUsername, MatchCallback callback) {
        if (nsdManager == null || finished.get()) {
            return;
        }
        try {
            nsdManager.resolveService(service, new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    if (finished.get()) {
                        return;
                    }
                    InetAddress host = serviceInfo.getHost();
                    int port = serviceInfo.getPort();
                    if (host == null || port <= 0) {
                        return;
                    }
                    executor.execute(() -> connectAsClient(host, port, localUsername, callback));
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void acceptIncomingConnection(String localUsername, List<Integer> hostQuestionIds, MatchCallback callback) {
        try {
            Socket socket = serverSocket.accept();
            if (!finished.compareAndSet(false, true)) {
                socket.close();
                return;
            }
            BufferedReader reader = ActiveDuelSession.createReader(socket);
            PrintWriter writer = ActiveDuelSession.createWriter(socket);
            String hello = reader.readLine();
            String remoteName = parseRemoteName(hello);
            writer.println("WELCOME|" + sanitizeField(localUsername) + "|" + joinIds(hostQuestionIds));
            writer.flush();

            ActiveDuelSession session = new ActiveDuelSession(
                    socket, reader, writer, true, localUsername, remoteName, hostQuestionIds);
            stopNsdOnly();
            mainHandler.post(() -> callback.onMatchFound(session));
        } catch (IOException ignored) {
        }
    }

    private void connectAsClient(InetAddress host, int port, String localUsername, MatchCallback callback) {
        try {
            Socket socket = new Socket(host, port);
            if (!finished.compareAndSet(false, true)) {
                socket.close();
                return;
            }
            BufferedReader reader = ActiveDuelSession.createReader(socket);
            PrintWriter writer = ActiveDuelSession.createWriter(socket);
            writer.println("HELLO|" + sanitizeField(localUsername));
            writer.flush();

            String welcome = reader.readLine();
            String remoteName = "Rival";
            List<Integer> qIds = new ArrayList<>();
            if (welcome != null && welcome.startsWith("WELCOME|")) {
                String[] parts = welcome.split("\\|", -1);
                if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                    remoteName = parts[1].trim();
                }
                if (parts.length >= 3) {
                    qIds = parseIds(parts[2]);
                }
            }
            ActiveDuelSession session = new ActiveDuelSession(
                    socket, reader, writer, false, localUsername, remoteName, qIds);
            stopNsdOnly();
            mainHandler.post(() -> callback.onMatchFound(session));
        } catch (IOException ignored) {
        }
    }

    public void stop() {
        finished.set(true);
        stopNsdOnly();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
            serverSocket = null;
        }
    }

    private void stopNsdOnly() {
        if (nsdManager != null) {
            if (discoveryListener != null) {
                try {
                    nsdManager.stopServiceDiscovery(discoveryListener);
                } catch (Exception ignored) {
                }
                discoveryListener = null;
            }
            if (registrationListener != null) {
                try {
                    nsdManager.unregisterService(registrationListener);
                } catch (Exception ignored) {
                }
                registrationListener = null;
            }
        }
    }

    private String parseRemoteName(String line) {
        if (line != null && line.startsWith("HELLO|")) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                return parts[1].trim();
            }
        }
        return "Rival";
    }

    private String sanitizeField(String value) {
        return value == null ? "Jugador" : value.replace("|", "").trim();
    }

    private String joinIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        if (ids != null) {
            for (Integer id : ids) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(id);
            }
        }
        return sb.toString();
    }

    private List<Integer> parseIds(String raw) {
        List<Integer> ids = new ArrayList<>();
        if (raw == null || raw.trim().isEmpty()) {
            return ids;
        }
        for (String part : raw.split(",")) {
            try {
                ids.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return ids;
    }
}
