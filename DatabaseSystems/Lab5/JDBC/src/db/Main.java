package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    // MySQL
    static String dbdriver = "com.mysql.jdbc.Driver";
    static String dburl = "jdbc:mysql://localhost";
    static String dbname = "data_analytics_2020";

    public static void main(String[] args) {
        String login = "root";
        String password = "NerdHerd11";

        System.out.println("Connecting as user '" + login + "' . . .");

        // Load the JDBC driver.
        // Library (.jar file) must be added to project build path.
        try {
            Class.forName(dbdriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Connect to the database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection((dburl + "/" + dbname),
                    login, password);
            connection.setClientInfo("autoReconnect", "true");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<String> symbols = Arrays.asList("GOOG", "CELG", "NVDA", "FB");
        List<List<Double>> output = optimizeAlloc(connection, symbols);
        System.out.println(Arrays.toString(output.toArray()));
    }

    private static List<List<Double>> optimizeAlloc(Connection connection, List<String> symbols) {
        // Check for errors in input
        if (symbols.isEmpty()) {
            System.out.println("ERROR: Array can't be empty");
            System.exit(0);
        }

        List<String> validSymbols = Arrays.asList("NVDA", "GOOG", "FB", "CELG");
        for (String symbol : symbols) {
            if (!validSymbols.contains(symbol)) {
                System.out.println("ERROR: Invalid symbol provided");
                System.exit(0);
            }
        }

        // Try all combinations of weights that total 1.0 to find the best Sharpe Ratio
        double bestSharpe = 0;
        List<Double> bestAlloc = new ArrayList<>();
        List<Double> bestOutput = new ArrayList<>();

        for (double i = 0; i <= 1; i += 0.1) {
            for (double j = 0; j <= 1; j += 0.1) {
                for (double k = 0; k <= 1; k += 0.1) {
                    for (double l = 0; l <= 1; l += 0.1) {
                        i = (double)Math.round(i * 10) / 10;
                        j = (double)Math.round(j * 10) / 10;
                        k = (double)Math.round(k * 10) / 10;
                        l = (double)Math.round(l * 10) / 10;
                        if (i + j + k + l == 1) {
                            List<Double> alloc = Arrays.asList(i, j, k, l);
                            List<Double> output = portfolioSim(connection, symbols, alloc);
                            double currSharpe = output.get(2);
                            if (currSharpe > bestSharpe) {
                                bestSharpe = currSharpe;
                                bestAlloc = alloc;
                                bestOutput = output;
                            }
                        }
                    }
                }
            }
        }

        portfolioSim(connection, symbols, bestAlloc);

        return Arrays.asList(bestAlloc, bestOutput);
    }

    private static List<Double> portfolioSim(Connection connection, List<String> symbols, List<Double> alloc) {
        // Check for errors in input
        if (symbols.size() != alloc.size()) {
            System.out.println("ERROR: Arrays are different lengths");
            System.exit(0);
        }
        if (symbols.size() == 0) {
            System.out.println("ERROR: Arrays can't be empty");
            System.exit(0);
        }

        List<String> validSymbols = Arrays.asList("NVDA", "GOOG", "FB", "CELG");
        for (String symbol : symbols) {
            if (!validSymbols.contains(symbol)) {
                System.out.println("ERROR: Invalid symbol provided");
                System.exit(0);
            }
        }

        double sum = 0;
        for (double val : alloc) {
            sum += val;
        }
        if (Math.round(sum) != 1) {
            System.out.println("ERROR: Sum of allocation is not 1.0");
            System.exit(0);
        }

        // Calculate the value fields for each stock
        for (int i = 0; i < symbols.size(); i++) {
            String symbol = symbols.get(i).toLowerCase();
            double weight = alloc.get(i);

            String query = "UPDATE pf SET " + symbol + "_value = " + weight + " * 1 * " + symbol + "_cum_return";
            executeUpdate(connection, query);
        }

        // Calculate the daily portfolio values
        StringBuilder query = new StringBuilder("UPDATE pf SET pf_value = ");
        for (String symbol : symbols) {
            query.append(symbol).append("_value + ");
        }
        String finalQuery = query.toString();
        finalQuery = finalQuery.substring(0, finalQuery.length() - 3); // Removes extra + at the end
        executeUpdate(connection, finalQuery);

        // Calculate the daily spy value
        String spyValQuery = "UPDATE pf SET spy_value = spy_cum_return";
        executeUpdate(connection, spyValQuery);

        // Calculate the daily cumulative portfolio return
        String portCumQuery = "UPDATE pf SET pf_cum_return = pf_value";
        executeUpdate(connection, portCumQuery);

        // Calculate Sharpe Ratio of the portfolio
        int n = 250;
        ResultSet results = executeQuery(connection, "SELECT pf_cum_return - spy_cum_return FROM pf");
        List<Double> diffs = new ArrayList<>();
        try {
            while (results.next()) {
                double diff = results.getDouble(1);
                diffs.add(diff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        sum = 0;
        for (double diff : diffs) {
            sum += diff;
        }
        double avgDiff = sum / (double)diffs.size();
        double stdDiff = calcStd(avgDiff, diffs);
        double sharpe = Math.sqrt(n) * avgDiff / stdDiff;

        // Calculate overall cumulative return
        results = executeQuery(connection, "SELECT pf_cum_return FROM pf");
        List<Double> cumReturns = new ArrayList<>();
        try {
            while (results.next()) {
                double cumReturn = results.getDouble(1);
                cumReturns.add(cumReturn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        sum = 0;
        for (double cumReturn : cumReturns) {
            sum += cumReturn;
        }
        double returnAvg = sum / (double)cumReturns.size();
        double returnStd = calcStd(returnAvg, cumReturns);
        double firstValue = cumReturns.get(cumReturns.size() - 1);
        double lastValue = cumReturns.get(0);
        double overallReturn = (lastValue - firstValue) / firstValue;

        return Arrays.asList(returnStd, returnAvg, sharpe, overallReturn);
    }

    private static ResultSet executeQuery(Connection connection, String query) {
        ResultSet results = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return results;
    }

    private static int executeUpdate(Connection connection, String query) {
        int results = -1;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            results = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return results;
    }

    private static double calcStd(double mean, List<Double> nums) {
        double temp = 0;

        for (double val : nums) {
            temp += Math.pow(val - mean, 2);
        }
        double meanOfDiffs = temp / (double)nums.size();

        return Math.sqrt(meanOfDiffs);
    }
}
