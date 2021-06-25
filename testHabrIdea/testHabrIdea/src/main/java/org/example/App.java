package org.example;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class App {
    public static void main(String[] args) throws IOException {

        String path = "src/main/java/org/example/tickets.json";

        JsonObject mainObject = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
        JsonArray pItem = mainObject.getAsJsonArray("tickets");

        ArrayList ticketsTime = new ArrayList();
        String currentDest = "vladivostok-telAviv";

        for (JsonElement ticket : pItem) {
            JsonObject userObject = ticket.getAsJsonObject();
            if (userObject.get("destination").getAsString().equals(currentDest)) {
                ticketsTime.add(userObject.get("flightTime").getAsInt());
            }
        }

        int tmpTime = ticketsTime.stream().mapToInt(v -> (int) v).sum();
        int averageTime = tmpTime / ticketsTime.size();


        //Percentile calculation
        double result;
        Collections.sort(ticketsTime);
        double percentile = 0.9;

        int N = ticketsTime.size();
        double n = (N - 1) * percentile + 1;
        if (n == 1d) result = (double) ticketsTime.get(0);
        else if (n == N) result = (double) ticketsTime.get(N - 1);
        else {

            int k = (int) n;
            double d = n - k;
            result = (int) ticketsTime.get(k - 1) + d * ((int) ticketsTime.get(k) - (int) ticketsTime.get(k - 1));
        }

        double scale = Math.pow(10, 2);
        double resultRounded = Math.round(result * scale) / scale;

        System.out.println("90 процентиль - " + resultRounded);
        System.out.println("Среднее время полета - " + averageTime);


    }
}
