import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static boolean isIncluded(int index, List<String> alreadyProcessed) {
        for (String str : alreadyProcessed) {
            if (index == Integer.parseInt(str.split(",")[0])) {
                return true;
            }
        }
        return false;
    }

    public static String findOptimalRoute(int currentCity, int totalCities, int[][] adjMatrix, int[][] connCost, int[][] disConnCost, List<String> alreadyProcessed) {
        List<Integer> connected = new ArrayList<>();
        List<Integer> disconnected = new ArrayList<>();
        String route;
        int subsequentCity;

        for (int nextCity = 0; nextCity < totalCities; nextCity++) {
            if (adjMatrix[currentCity][nextCity] == 1 && !isIncluded(nextCity, alreadyProcessed)) {
                connected.add(nextCity);
            } else if (adjMatrix[currentCity][nextCity] == 0 && !isIncluded(nextCity, alreadyProcessed)) {
                disconnected.add(nextCity);
            }
        }

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String matrixInput = scanner.next();
        String buildInput = scanner.next();
        String destroyInput = scanner.next();
        scanner.close();

        int cityCount = matrixInput.split(",").length;

        int[][] adjacencyMatrix = new int[cityCount][cityCount];
        int[][] buildMatrix = new int[cityCount][cityCount];
        int[][] destroyMatrix = new int[cityCount][cityCount];
        int[][] newAdjMatrix = new int[cityCount][cityCount];

        List<String> processedCities = new ArrayList<>();
        List<String> nextCities = new ArrayList<>();

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

        processedCities.add("0,0.0");
        String nextCity;
        for (int i = 0; i < cityCount - 1; i++) {
            for (String city : processedCities) {
                String optimal = findOptimalRoute(Integer.parseInt(city.split(",")[0]), cityCount, adjacencyMatrix, buildMatrix, destroyMatrix, processedCities);
                nextCities.add(optimal);
            }
            nextCity = prioritizeRoutes(nextCities, destroyMatrix, cityCount);
            int origin = Integer.parseInt(nextCity.split(",")[1].split("\\.")[0]);
            int destination = Integer.parseInt(nextCity.split(",")[0]);
            newAdjMatrix[origin][destination] = 1;
            newAdjMatrix[destination][origin] = 1;
            processedCities.add(nextCity);
            nextCities.clear();
        }

        int minimumCost = 0;
        for (String city : processedCities) {
            minimumCost += Integer.parseInt(city.split("\\.")[1]);
        }

        int destroyCost = 0;
        for (int row = 0; row < cityCount; row++) {
            for (int col = 0; col < cityCount; col++) {
                if (adjacencyMatrix[row][col] == 1 && newAdjMatrix[row][col] == 0) {
                    destroyCost += destroyMatrix[row][col];
                }
            }
        }
        minimumCost += destroyCost / 2;

        System.out.println(minimumCost);
    }
}
