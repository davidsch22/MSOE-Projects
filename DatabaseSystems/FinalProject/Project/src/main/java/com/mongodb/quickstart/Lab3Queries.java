package com.mongodb.quickstart;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Aggregates.*;

public class Lab3Queries {

    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        String connectionString = System.getProperty("mongodb.uri");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase appDB = mongoClient.getDatabase("app");
            MongoCollection<Document> videoColl = appDB.getCollection("Video_DB");

            // Query 3: Number of videos for each category
            System.out.println("Query 3:");
            AggregateIterable<Document> catCounts = videoColl.aggregate(Collections.singletonList(
                    group("$category", Accumulators.sum("count", 1))));

            for (Document d : catCounts) {
                System.out.println(d.get("_id") + ": " + d.get("count"));
            }
            System.out.println();

            // Query 4: Number of videos for each category where inventory is non-zero
            System.out.println("Query 4:");
            AggregateIterable<Document> nonZeroCatCounts = videoColl.aggregate(Arrays.asList(
                    match(Filters.gt("stock_count", 0)),
                    group("$category", Accumulators.sum("count", 1))));

            for (Document d : nonZeroCatCounts) {
                System.out.println(d.get("_id") + ": " + d.get("count"));
            }
            System.out.println();

            // Query 5: Categories for each actor
            System.out.println("Query 5:");
            Map<String, List<String>> actorCategories = new HashMap<>(); // Create a map linking each actor to a list of the categories they've been in
            // For every recording entry in the collection...
            for (Document d : videoColl.find()) {
                String category = d.get("category").toString();
                String[] actors = d.get("actors").toString().split(",");

                // For every actor in the current recording entry...
                for (String actor : actors) {
                    List<String> cats;
                    if (!actorCategories.containsKey(actor)) { // If actor doesn't already exist in the map...
                        cats = new ArrayList<>();
                    } else {
                        cats = actorCategories.get(actor);
                    }
                    if (!cats.contains(category)) { // If the category is not already in the actor's list...
                        cats.add(category);
                    }
                    actorCategories.put(actor, cats);
                }
            }

            for (Map.Entry<String, List<String>> actor : actorCategories.entrySet()) {
                System.out.println(actor.getKey() + ": " + actor.getValue().toString());
            }
            System.out.println();

            // Query 6: Actors appeared in movies in different categories
            System.out.println("Query 6:");
            for (Map.Entry<String, List<String>> actor : actorCategories.entrySet()) {
                if (actor.getValue().size() > 1) {
                    System.out.println(actor.getKey() + ": " + actor.getValue().toString());
                }
            }
            System.out.println();

            // Query 7: Actors not appeared in a comedy
            System.out.println("Query 7:");
            for (Map.Entry<String, List<String>> actor : actorCategories.entrySet()) {
                if (!actor.getValue().contains("Comedy")) {
                    System.out.println(actor.getKey() + ": " + actor.getValue().toString());
                }
            }
            System.out.println();

            // Query 8: Actors appeared in comedy and action-adventure movies
            System.out.println("Query 8:");
            for (Map.Entry<String, List<String>> actor : actorCategories.entrySet()) {
                if (actor.getValue().contains("Comedy") && actor.getValue().contains("Action & Adventure")) {
                    System.out.println(actor.getKey() + ": " + actor.getValue().toString());
                }
            }
        }
    }
}
