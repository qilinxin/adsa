import java.util.Arrays;

public class main {


    static class assignment3HashTable{
        private final int size = 26; // 26 slots, from 'a' to 'z'
        private final String[] table = new String[size];
        private final String[] status = new String[size]; // "never used", "tombstone", "occupied"

        public assignment3HashTable() {
            Arrays.fill(status, "never used");
        }

        private int hashFunction(String key) {
            return key.charAt(key.length() - 1) - 'a';
        }

        public int search(String key) {
            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < size; i++) {
                if ("never used".equals(status[index])) {
                    return -1; // Key not found
                } else if ("occupied".equals(status[index]) && key.equals(table[index])) {
                    return index; // Key found
                }
                index = (index + 1) % size;
            }
            return -1; // Key not found
        }

        public void insert(String key) {
            if (search(key) != -1) {
                return; // Key already exists
            }

            int hashValue = hashFunction(key);
            int index = hashValue;
            for (int i = 0; i < size; i++) {
                if (!"occupied".equals(status[index])) {
                    table[index] = key;
                    status[index] = "occupied";
                    return;
                }
                index = (index + 1) % size;
            }
        }

        public void delete(String key) {
            int index = search(key);
            if (index != -1) {
                status[index] = "tombstone";
            }
        }

        public String output() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < size; i++) {
                if ("occupied".equals(status[i])) {
                    result.append(table[i]).append(" ");
                }
            }
            return result.toString().trim();
        }


    }


    public static void main(String[] args) {
        // Sample inputs
        String sampleInput1 = "Aaaa Accc Abbb";
        String sampleInput2 = "Abba Aaaa Acca";
        String sampleInput3 = "Abba Aaaa Acca Daaa";

        // Process the sample inputs
        processCommands(sampleInput1); // Output: aaa bbb ccc
        processCommands(sampleInput2); // Output: bba aaa cca
        processCommands(sampleInput3); // Output: bba cca
    }

    public static void processCommands(String commands) {
        assignment3HashTable hashTable = new assignment3HashTable();
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