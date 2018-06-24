package recipePuppy;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Connects to recipePuppy API and accesses information about cocktail recipes.
 *
 * @author Pierce Kelaita
 * @since 6-23-2018
 */
public class RecipeDeserializer {

    /**
     * Searches for recipes matching given parameters
     *
     * @param query Search parameter
     * @param page Page number from which to show results
     * @return Results of the search
     * @throws IOException Typically due to malformed JSON
     */
    private RecipeObject[] getResults(String query, int page) throws IOException {

        Gson g = new Gson();

        // connect to recipePuppy API
        URL url = new URL(
                "http://www.recipepuppy.com/api?" +
                        "q=" + query.toLowerCase().replace(" ", "_") + "&" +
                        "p=" + page
        );
        URLConnection request = url.openConnection();
        request.connect();

        // parse results
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(
                new InputStreamReader((InputStream) request.getContent()));
        JsonArray results = root.getAsJsonObject().getAsJsonArray("results");

        // standardize JSON objects
        RecipeObject[] result = new RecipeObject[results.size()];
        for (int i = 0; i < results.size(); i++) {
            RecipeObject.RecipeBuilder rb = g.fromJson(
                    results.get(i).toString(),
                    RecipeObject.RecipeBuilder.class
            );
            result[i] = rb.getRecipeObject();
        }
        return result;
    }

    /**
     * Testing method
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        String q = "";
        try {
            for (int p = 1; p <= 1; p++) {
                RecipeObject[] arr = new RecipeDeserializer().getResults(q, p);
                for (RecipeObject r : arr)
                    System.out.println(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
