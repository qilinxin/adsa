import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    public static class Assignment3HashMap {
        private static final int SIZE = 26;
        // Constants representing the size of the hash table and slot statuses
        private static final String NEVER_USED = "Never Used";
        private static final String TOMBSTONE = "Tombstone";
        private static final String OCCUPIED = "Occupied";

        private final String[] table = new String[SIZE];
        private final String[] status = new String[SIZE];

        // Constructor to initialize the hash table with all slots set to "Never Used"
        public Assignment3HashMap() {
            Arrays.fill(status, NEVER_USED);
        }

        // Hash function that returns the index based on the last character of the key
        private int hashFunction(String key) {
            return key.charAt(key.length() - 1) - 'a';
        }

        // Search for a key in the hash table
        public int search(String key) {
            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < SIZE; i++) {
                if (status[index].equals(NEVER_USED)) {
                    // Key not found, since the slot has never been used
                    return -1;
                } else if (status[index].equals(OCCUPIED) && key.equals(table[index])) {
                    // Key found
                    return index;
                }
                // Move to the next slot (wrapping around if necessary)
                index = (index + 1) % SIZE;
            }
            // Key not found after checking all slots
            return -1;
        }

        // Insert a key into the hash table
        public void insert(String key) {
            if (search(key) != -1) {
                // Key already exists, do nothing
                return;
            }

            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < SIZE; i++) {
                if (!status[index].equals(OCCUPIED)) {
                    // Insert the key
                    table[index] = key;
                    // Mark the slot as occupied
                    status[index] = OCCUPIED;
                    return;
                }
                // Move to the next slot (wrapping around if necessary)
                index = (index + 1) % SIZE;
            }
        }

        // Delete a key from the hash table
        public void delete(String key) {
            int index = search(key);
            if (index != -1) {
                // If the key is found, mark its slot as "Tombstone"
                status[index] = TOMBSTONE;
            }
        }

        // Output all keys in the hash table, separated by space
        public String output() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < SIZE; i++) {
                if (status[i].equals(OCCUPIED)) {
                    result.append(table[i]).append(" ");
                }
            }
            return result.toString().trim();
        }
    }

//    public main () {}
//
//    public main (String commands) {
//        Assignment3HashMap hashTable = new Assignment3HashMap();
//        String[] commandList = commands.split(" ");
//        for (String command : commandList) {
//            if (command.startsWith("A")) {
//                String key = command.substring(1);
//                hashTable.insert(key);
//            } else if (command.startsWith("D")) {
//                String key = command.substring(1);
//                hashTable.delete(key);
//            }
//        }
//    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter commands: ");
        String input = scanner.nextLine();
        processCommands(input);
    }

    public static void processCommands(String commands) {
        Assignment3HashMap hashTable = new Assignment3HashMap();
        String[] commandList = commands.split(" ");
        for (String command : commandList) {
            if (command.startsWith("A")) {
                String key = command.substring(1);
                hashTable.insert(key);
            } else if (command.startsWith("D")) {
                String key = command.substring(1);
                hashTable.delete(key);
            }
        }
        System.out.println(hashTable.output());
    }

}
