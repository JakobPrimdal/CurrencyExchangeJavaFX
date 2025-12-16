package dk.easv.currencyexchange.dal;

// Project imports
import dk.easv.currencyexchange.be.Currency;

// Maven dependencies
import org.json.JSONArray;
import org.json.JSONObject;

// Java imports
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAO {

    private final String API_BASE = "https://api.exchangerate.host/";
    private final int CONNECTION_TIMEOUT = 5000;
    private final int READ_TIMEOUT = 5000;

    public List<Currency> getAllCurrencyRates() throws Exception {
        ArrayList<Currency> allRates = new ArrayList<>();

        String apiRequest = API_BASE + "live?" + "access_key=" + getAPIAccessKey() + "&format=1";

        String jsonResponse = makeHttpRequest(apiRequest);

        allRates = (ArrayList<Currency>) mapJsonResponseToCurrencyObject(jsonResponse);

        return allRates;
    }

    private static String getAPIAccessKey() {
        Properties accessKey = new Properties();
        try {
            accessKey.load(new FileInputStream(new File("config/API.key")));
        } catch (IOException e) {
            // Display error to user // Throw exception upwards instead
            throw new RuntimeException(e);
        }
        return accessKey.getProperty("API_Key");
    }

    private String makeHttpRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        // Map JSON response to String
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private List<Currency> mapJsonResponseToCurrencyObject(String jsonResponse) {
        List<Currency> currencies = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Check if the request was successful
        if (!jsonObject.has("success") || !jsonObject.getBoolean("success")) {
            // Display error to user // Throw exception upwards
            throw new RuntimeException("API request failed");
        }

        JSONObject quotes = jsonObject.getJSONObject("quotes");

        long timestamp = jsonObject.getLong("timestamp");
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC);

        String source = jsonObject.getString("source");


        for (String key : quotes.keySet()) {
            double rate = quotes.getDouble(key);

            String baseCurrency = key.substring(0, 3);
            String targetCurrency = key.substring(3);

            // Create Currency objects
            Currency currency = new Currency(baseCurrency, targetCurrency, rate, dateTime);
            currencies.add(currency);
        }

        return currencies;
    }

    public static void main(String[] args) throws Exception {
        DAO dao = new DAO();
        System.out.println(dao.getAllCurrencyRates());

        System.out.println("\nTotal currencies fetched: " + dao.getAllCurrencyRates().size());

        for (int i = 0; i < Math.min(5, dao.getAllCurrencyRates().size()); i++) {
            Currency c = dao.getAllCurrencyRates().get(i);
            System.out.println(c);
        }
    }



}
