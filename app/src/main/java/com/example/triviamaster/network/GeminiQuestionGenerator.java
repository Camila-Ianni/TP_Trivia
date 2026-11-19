package com.example.triviamaster.network;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Base64;
import com.example.triviamaster.data.QuestionTranslator;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.DbExecutor;
import com.example.triviamaster.models.Question;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GeminiQuestionGenerator {

    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    private static final int MAX_PDF_BYTES = 12 * 1024 * 1024;

    private final Context appContext;
    private final DbExecutor executor;

    public GeminiQuestionGenerator(Context context) {
        this.appContext = context.getApplicationContext();
        this.executor = DbExecutor.getInstance();
    }

    public void generateFromPdf(Uri pdfUri, String teacherPrompt, String apiKey, DbCallback<List<Question>> callback) {
        executor.execute(() -> {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalStateException("MISSING_API_KEY");
            }
            byte[] pdfBytes = readPdfBytes(pdfUri);
            String base64Pdf = Base64.encodeToString(pdfBytes, Base64.NO_WRAP);
            String lang = QuestionTranslator.getCurrentLanguage();
            String langName = "en".equals(lang) ? "English" : ("it".equals(lang) ? "Italian" : "Spanish");

            String systemInstruction = "You are a teacher assistant. Read the attached PDF and follow this teacher request: \""
                    + teacherPrompt.trim() + "\". Generate between 5 and 8 multiple-choice questions in " + langName
                    + ". Respond ONLY with a valid JSON array (no markdown fences) where each item has: "
                    + "\"question\" (string), \"category\" (short string), \"options\" (array of exactly 4 strings), "
                    + "\"correctIndex\" (integer 0-3), \"explanation\" (short educational string).";

            JSONObject inlineData = new JSONObject();
            inlineData.put("mime_type", "application/pdf");
            inlineData.put("data", base64Pdf);

            JSONObject pdfPart = new JSONObject();
            pdfPart.put("inline_data", inlineData);

            JSONObject textPart = new JSONObject();
            textPart.put("text", systemInstruction);

            JSONArray parts = new JSONArray();
            parts.put(pdfPart);
            parts.put(textPart);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject payload = new JSONObject();
            payload.put("contents", contents);

            String responseJson = postJson(ENDPOINT + apiKey.trim(), payload.toString());
            return parseQuestionsFromGeminiResponse(responseJson);
        }, callback);
    }

    private byte[] readPdfBytes(Uri uri) throws IOException {
        try (InputStream is = appContext.getContentResolver().openInputStream(uri)) {
            if (is == null) {
                throw new IOException("Cannot open PDF");
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            int read;
            int total = 0;
            while ((read = is.read(chunk)) != -1) {
                total += read;
                if (total > MAX_PDF_BYTES) {
                    throw new IOException("PDF_TOO_LARGE");
                }
                buffer.write(chunk, 0, read);
            }
            return buffer.toByteArray();
        }
    }

    private String postJson(String urlString, String body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setConnectTimeout(25000);
        conn.setReadTimeout(60000);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
        int status = conn.getResponseCode();
        InputStream stream = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();
        String responseText = "";
        if (stream != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = stream.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            responseText = new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
        conn.disconnect();
        if (status < 200 || status >= 300) {
            throw new IOException("HTTP_" + status + ": " + responseText);
        }
        return responseText;
    }

    private List<Question> parseQuestionsFromGeminiResponse(String responseJson) throws Exception {
        JSONObject root = new JSONObject(responseJson);
        JSONArray candidates = root.getJSONArray("candidates");
        String rawText = candidates.getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim();

        if (rawText.startsWith("```")) {
            int firstNewline = rawText.indexOf('\n');
            if (firstNewline > 0) {
                rawText = rawText.substring(firstNewline + 1);
            }
            if (rawText.endsWith("```")) {
                rawText = rawText.substring(0, rawText.length() - 3);
            }
            rawText = rawText.trim();
        }

        int startIdx = rawText.indexOf('[');
        int endIdx = rawText.lastIndexOf(']');
        if (startIdx >= 0 && endIdx > startIdx) {
            rawText = rawText.substring(startIdx, endIdx + 1);
        }

        JSONArray array = new JSONArray(rawText);
        List<Question> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String qText = obj.optString("question", "").trim();
            String category = obj.optString("category", "Clase").trim();
            JSONArray optsArr = obj.optJSONArray("options");
            if (qText.isEmpty() || optsArr == null || optsArr.length() < 4) {
                continue;
            }
            List<String> options = Arrays.asList(
                    optsArr.getString(0),
                    optsArr.getString(1),
                    optsArr.getString(2),
                    optsArr.getString(3)
            );
            int correctIdx = Math.max(0, Math.min(3, obj.optInt("correctIndex", 0)));
            String explanation = obj.optString("explanation", "");
            result.add(new Question(
                    0, 1, qText, category, Color.parseColor("#8E72DF"),
                    options, correctIdx, explanation, false, "", 0, 0, true
            ));
        }
        return result;
    }
}
