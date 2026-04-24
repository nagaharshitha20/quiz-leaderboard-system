import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.*;

public class QuizLeaderboard {

    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private static final String REG_NO = "AP23110011324";

    static class Event {
        String roundId;
        String participant;
        int score;

        Event(String r, String p, int s) {
            roundId = r;
            participant = p;
            score = s;
        }
    }

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            System.out.println("Polling index: " + i);

            try {
                String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + i;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();

                System.out.println("Response: " + body);

                List<Event> events = parseEvents(body);

                for (Event e : events) {
                    String key = e.roundId + "_" + e.participant;

                    if (!seen.contains(key)) {
                        seen.add(key);
                        scores.put(e.participant,
                                scores.getOrDefault(e.participant, 0) + e.score);
                    }
                }

                if (i < 9) Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nFinal Scores: " + scores);

        // Sort leaderboard
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>(scores.entrySet());
        leaderboard.sort((a, b) -> b.getValue() - a.getValue());

        String json = buildSubmitJson(leaderboard);

        System.out.println("\nSubmitting Payload:\n" + json);

        try {
            HttpRequest submit = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/quiz/submit"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> res = client.send(submit, HttpResponse.BodyHandlers.ofString());

            System.out.println("\nFinal Response:");
            System.out.println(res.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- PARSER ----------------

    private static List<Event> parseEvents(String json) {
        List<Event> list = new ArrayList<>();

        Pattern p = Pattern.compile("\"events\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher m = p.matcher(json);

        if (m.find()) {
            String arr = m.group(1);

            Pattern obj = Pattern.compile("\\{(.*?)\\}");
            Matcher objMatch = obj.matcher(arr);

            while (objMatch.find()) {
                String item = objMatch.group(1);

                String roundId = extract(item, "roundId");
                String participant = extract(item, "participant");
                int score = extractInt(item, "score");

                list.add(new Event(roundId, participant, score));
            }
        }

        return list;
    }

    private static String extract(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"").matcher(json);
        return m.find() ? m.group(1) : "";
    }

    private static int extractInt(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)").matcher(json);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    // ---------------- JSON BUILDER ----------------

    private static String buildSubmitJson(List<Map.Entry<String, Integer>> leaderboard) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"regNo\":\"").append(REG_NO).append("\",\"leaderboard\":[");

        for (int i = 0; i < leaderboard.size(); i++) {
            Map.Entry<String, Integer> e = leaderboard.get(i);

            sb.append("{\"participant\":\"")
              .append(e.getKey())
              .append("\",\"totalScore\":")
              .append(e.getValue())
              .append("}");

            if (i < leaderboard.size() - 1) sb.append(",");
        }

        sb.append("]}");

        return sb.toString();
    }
}