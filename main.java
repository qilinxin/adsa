import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    // Inner class to implement Minimum Spanning Tree functionalities
    static class MinimumSpanningTree {
        // Method to check if a city has already been processed
        static class RouteHelper {
            public static boolean isIncluded(int index, List<String> alreadyProcessed) {
                for (String str : alreadyProcessed) {
                    if (index == Integer.parseInt(str.split(",")[0])) {
                        return true;
                    }
                }
                return false;
            }

            // Method to find the optimal route from the current city
            public static String findOptimalRoute(int currentCity, int totalCities, int[][] adjMatrix, int[][] connCost, int[][] disConnCost, List<String> alreadyProcessed) {
                List<Integer> connected = new ArrayList<>();
                List<Integer> disconnected = new ArrayList<>();
                String route;
                int subsequentCity;

                // Identify connected and disconnected cities
                for (int nextCity = 0; nextCity < totalCities; nextCity++) {
                    if (adjMatrix[currentCity][nextCity] == 1 && !isIncluded(nextCity, alreadyProcessed)) {
                        connected.add(nextCity);
                    } else if (adjMatrix[currentCity][nextCity] == 0 && !isIncluded(nextCity, alreadyProcessed)) {
                        disconnected.add(nextCity);
                    }
                }

                // Choose the best subsequent city
                if (!connected.isEmpty()) {
                    subsequentCity = connected.get(0);
                    for (int idx = 1; idx < connected.size(); idx++) {
                        if (disConnCost[currentCity][connected.get(idx)] > disConnCost[currentCity][subsequentCity]) {
                            subsequentCity = connected.get(idx);
                        }
                    }
                    route = subsequentCity + "," + currentCity + ".0";
                } else {
                    subsequentCity = disconnected.get(0);
                    for (int idx = 1; idx < disconnected.size(); idx++) {
                        if (connCost[currentCity][disconnected.get(idx)] < connCost[currentCity][subsequentCity]) {
                            subsequentCity = disconnected.get(idx);
                        }
                    }
                    route = subsequentCity + "," + currentCity + "." + connCost[currentCity][subsequentCity];
                }
                return route;
            }

            // Method to prioritize possible routes based on minimum cost
            public static String prioritizeRoutes(List<String> possibleRoutes, int[][] disConnCost, int totalCities) {
                int optimalIdx = 0;
                for (int i = 1; i < possibleRoutes.size(); i++) {
                    int currentMinCost = Integer.parseInt(possibleRoutes.get(optimalIdx).split("\\.")[1]);
                    int currentCityID = Integer.parseInt(possibleRoutes.get(optimalIdx).split(",")[0]);
                    int originCity = Integer.parseInt(possibleRoutes.get(optimalIdx).split(",")[1].split("\\.")[0]);

                    int newRouteCost = Integer.parseInt(possibleRoutes.get(i).split("\\.")[1]);
                    int newCityID = Integer.parseInt(possibleRoutes.get(i).split(",")[0]);
                    int newOriginCity = Integer.parseInt(possibleRoutes.get(i).split(",")[1].split("\\.")[0]);

                    if (currentMinCost > newRouteCost || (currentMinCost == newRouteCost && disConnCost[originCity][currentCityID] < disConnCost[newOriginCity][newCityID])) {
                        optimalIdx = i;
                    }
                }
                return possibleRoutes.get(optimalIdx);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String matrixInput = scanner.next();
        String buildInput = scanner.next();
        String destroyInput = scanner.next();
        scanner.close();
        createTree(matrixInput, buildInput, destroyInput);
    }

    public static void createTree(String matrixInput, String buildInput, String destroyInput) {

        int cityCount = matrixInput.split(",").length;

        int[][] adjacencyMatrix = new int[cityCount][cityCount];
        int[][] buildMatrix = new int[cityCount][cityCount];
        int[][] destroyMatrix = new int[cityCount][cityCount];
        int[][] newAdjMatrix = new int[cityCount][cityCount];

        List<String> processedCities = new ArrayList<>();
        List<String> nextCities = new ArrayList<>();

        // Parse input strings to populate matrices
        for (int row = 0; row < cityCount; row++) {
            for (int col = 0; col < cityCount; col++) {
                adjacencyMatrix[row][col] = Character.getNumericValue(matrixInput.charAt(row * (cityCount + 1) + col));
                char buildChar = buildInput.charAt(row * (cityCount + 1) + col);
                buildMatrix[row][col] = Character.isUpperCase(buildChar) ? buildChar - 'A' : buildChar - 'a' + 26;
                char destroyChar = destroyInput.charAt(row * (cityCount + 1) + col);
                destroyMatrix[row][col] = Character.isUpperCase(destroyChar) ? destroyChar - 'A' : destroyChar - 'a' + 26;
                newAdjMatrix[row][col] = 0;
            }
        }

        // Start with the first city
        processedCities.add("0,0.0");
        String nextCity;
        for (int i = 0; i < cityCount - 1; i++) {
            // Find optimal route for each processed city
            for (String city : processedCities) {
                String optimal = MinimumSpanningTree.RouteHelper.findOptimalRoute(Integer.parseInt(city.split(",")[0]), cityCount, adjacencyMatrix, buildMatrix, destroyMatrix, processedCities);
                nextCities.add(optimal);
            }
            // Prioritize routes to find the best next city to connect
            nextCity = MinimumSpanningTree.RouteHelper.prioritizeRoutes(nextCities, destroyMatrix, cityCount);
            int origin = Integer.parseInt(nextCity.split(",")[1].split("\\.")[0]);
            int destination = Integer.parseInt(nextCity.split(",")[0]);
            newAdjMatrix[origin][destination] = 1;
            newAdjMatrix[destination][origin] = 1;
            processedCities.add(nextCity);
            nextCities.clear();
        }

        // Calculate the minimum cost of building the new network
        int minimumCost = 0;
        for (String city : processedCities) {
            minimumCost += Integer.parseInt(city.split("\\.")[1]);
        }

        // Calculate the cost of destroying unused connections
        int destroyCost = 0;
        for (int row = 0; row < cityCount; row++) {
            for (int col = 0; col < cityCount; col++) {
                if (adjacencyMatrix[row][col] == 1 && newAdjMatrix[row][col] == 0) {
                    destroyCost += destroyMatrix[row][col];
                }
            }
        }
        minimumCost += destroyCost / 2;

        // Output the final minimum cost
        System.out.println(minimumCost);
    }
}
