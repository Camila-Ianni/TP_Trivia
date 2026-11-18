package com.example.triviamaster.network;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActiveDuelSession {

    public interface Listener {
        void onRemoteAnswer(int questionIndex, int selectedOptionIndex, int gainedScore);
        void onRemoteDisconnected();
    }

    private static ActiveDuelSession currentInstance;

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final boolean host;
    private final String localPlayerName;
    private final String remotePlayerName;
    private final List<Integer> questionIds;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService ioExecutor = Executors.newCachedThreadPool();
    private volatile boolean closed;

    public ActiveDuelSession(Socket socket, BufferedReader reader, PrintWriter writer,
                             boolean host, String localPlayerName, String remotePlayerName,
                             List<Integer> questionIds) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.host = host;
        this.localPlayerName = localPlayerName;
        this.remotePlayerName = remotePlayerName;
        this.questionIds = questionIds != null ? new ArrayList<>(questionIds) : new ArrayList<>();
    }

    public static synchronized void setCurrent(ActiveDuelSession session) {
        if (currentInstance != null && currentInstance != session) {
            currentInstance.close();
        }
        currentInstance = session;
    }

    public static synchronized ActiveDuelSession getCurrent() {
        return currentInstance;
    }

    public static synchronized void clearCurrent() {
        if (currentInstance != null) {
            currentInstance.close();
            currentInstance = null;
        }
    }

    public boolean isHost() {
        return host;
    }

    public String getLocalPlayerName() {
        return localPlayerName;
    }

    public String getRemotePlayerName() {
        return remotePlayerName;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void sendAnswer(int questionIndex, int selectedOptionIndex, int gainedScore) {
        if (closed) {
            return;
        }
        ioExecutor.execute(() -> {
            try {
                writer.println("ANSWER|" + questionIndex + "|" + selectedOptionIndex + "|" + gainedScore);
                writer.flush();
            } catch (Exception ignored) {
            }
        });
    }

    public void startListening(Listener listener) {
        ioExecutor.execute(() -> {
            try {
                String line;
                while (!closed && (line = reader.readLine()) != null) {
                    if (line.startsWith("ANSWER|")) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4) {
                            int qIdx = Integer.parseInt(parts[1]);
                            int optIdx = Integer.parseInt(parts[2]);
                            int score = Integer.parseInt(parts[3]);
                            mainHandler.post(() -> {
                                if (!closed && listener != null) {
                                    listener.onRemoteAnswer(qIdx, optIdx, score);
                                }
                            });
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            if (!closed) {
                mainHandler.post(() -> {
                    if (!closed && listener != null) {
                        listener.onRemoteDisconnected();
                    }
                });
            }
        });
    }

    public void close() {
        closed = true;
        ioExecutor.execute(() -> {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        });
        ioExecutor.shutdown();
    }

    public static BufferedReader createReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    }

    public static PrintWriter createWriter(Socket socket) throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }
}
