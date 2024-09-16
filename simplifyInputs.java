import java.util.*;

public class simplifyInputs {
    public static void main(String[] args) {
        // 输入操作列表
        String[] operations = {"A54", "A99", "D60", "A80", "A28", "D4", "D60", "D62", "A76", "A100",
                "A34", "A31", "A29", "A31", "D57", "D59", "D95", "D90", "A46", "A91",
                "D44", "D81", "A56", "A2", "A84", "A50", "D59", "A47", "D22", "A49",
                "A74", "A59", "A15", "D17", "A44", "D77", "D77", "D90", "A94", "A70",
                "D83", "D84", "A21", "D82", "D5", "D1", "D27", "D33", "A60", "A55",
                "A38", "A36", "A57", "D14", "A50", "A78", "D84", "D30", "A92", "A48",
                "D94", "A3", "D3", "A7", "D34", "A96", "D37", "D68", "A58", "A18",
                "A26", "A60", "D69", "D33", "A66", "D17", "D64", "D16", "D10", "A14",
                "A78", "A8", "A70", "A31", "D40", "D82", "D71", "A14", "A49", "D64",
                "A34", "D99", "A35", "A67", "A14", "D43", "D55", "A10", "D66", "A94",
                "POST"};

        // 记录集合，模拟插入和删除操作
        Set<Integer> set = new HashSet<>();
        List<String> optimizedOperations = new ArrayList<>();

        for (String operation : operations) {
            if (operation.equals("POST")) {
                optimizedOperations.add(operation);
                break;
            }
            char action = operation.charAt(0);
            int value = Integer.parseInt(operation.substring(1));

            if (action == 'A') {
                if (!set.contains(value) && value > 80) {
                    set.add(value);
                    optimizedOperations.add(operation);
                }
            } else if (action == 'D') {
                if (set.contains(value)) {
                    set.remove(value);
                    optimizedOperations.add(operation);
                }
            }
        }

        // 输出优化后的操作序列
        for (String op : optimizedOperations) {
            System.out.print(op + " ");
        }
    }
}
