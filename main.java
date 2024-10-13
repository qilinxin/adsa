import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    public static class Assignment3HashMap {
        private static final int SIZE = 26; // 26 slots, from 'a' to 'z'
        private static final String NEVER_USED = "Never Used";
        private static final String TOMBSTONE = "Tombstone";
        private static final String OCCUPIED = "Occupied";
        private final String[] table = new String[SIZE];
        private final String[] status = new String[SIZE];

        public Assignment3HashMap() {
            Arrays.fill(status, NEVER_USED);
        }

        private int hashFunction(String key) {
            return key.charAt(key.length() - 1) - 'a';
        }

        public int search(String key) {
            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < SIZE; i++) {
                if (status[index].equals(NEVER_USED)) {
                    return -1;
                } else if (status[index].equals(OCCUPIED) && key.equals(table[index])) {
                    return index; // Key found
                }
                index = (index + 1) % SIZE;
            }
            return -1; // Key not found
        }

        public void insert(String key) {
            if (search(key) != -1) {
                return; // Key already exists
            }

            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < SIZE; i++) {
                if (!status[index].equals(OCCUPIED)) {
                    table[index] = key;
                    status[index] = OCCUPIED;
                    return;
                }
                index = (index + 1) % SIZE;
            }
        }

        public void delete(String key) {
            int index = search(key);
            if (index != -1) {
                status[index] = TOMBSTONE;
            }
        }

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
//        System.out.println("==============="+hashTable.output());
    }

}
