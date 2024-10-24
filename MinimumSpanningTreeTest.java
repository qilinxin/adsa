// Test class to validate different inputs

class MinimumSpanningTreeTest {
    public static void main(String[] args) {
        // Define multiple test cases
        String[] inputs = {
                "000,000,000 ABD,BAC,DCA ABD,BAC,DCA",
                "011,101,110 ABD,BAC,DCA ABD,BAC,DCA",
                "011000,101000,110000,000011,000101,000110 ABDFFF,BACFFF,DCAFFF,FFFABD,FFFBAC,FFFDCA ABDFFF,BACFFF,DCAFFF,FFFABD,FFFBAC,FFFDCA",
                "0001,0001,0001,1110 AfOj,fAcC,OcAP,jCPA AWFH,WAxU,FxAV,HUVA"
        };

        // Loop through each test case
        for (String input : inputs) {
            System.out.println("Input: " + input);
            String[] parts = input.split(" ");
            String matrixInput = parts[0];
            String buildInput = parts[1];
            String destroyInput = parts[2];

            // Redirect inputs to Main class
            main.createTree(matrixInput, buildInput, destroyInput);
            System.out.println();
        }
    }
}