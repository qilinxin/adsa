import java.util.*;

public class simplifyInputs {
    public static void main(String[] args) {
        // 输入操作列表
        List<String> operations = Arrays.asList("D13 D60 D76 D12 A17 D98 A94 D70 D3 A23 A42 D45 A100 D50 A99 A22 A87 A4 A90 D88 A71 A20 D39 D83 A97 A56 D28 A9 D43 A19 D5 A11 A54 A73 D54 A9 A24 A58 D6 D80 A72 A47 A82 A12 A75 D77 D84 D86 A60 D64 D70 D70 A73 A71 A40 D94 D27 A63 D47 A42 A44 A27 A100 A6 D84 A19 D65 A75 A55 A63 A39 D99 A50 D98 A98 D100 D93 A91 A81 D59 D56 D29 D11 D45 D47 D55 D85 D7 D70 A13 A55 A25 D35 D65 A48 D55 A45 D29 A35 A15 IN".split(" "));

        // 记录集合，模拟插入和删除操作
        Set<Integer> set = new HashSet<>();
        List<String> optimizedOperations = new ArrayList<>();

        for (String operation : operations) {
            System.out.println(operation);
            char action = operation.charAt(0);
            if (action == 'P' || action == 'I') {
                optimizedOperations.add(operation);
                break;
            }
            int value = Integer.parseInt(operation.substring(1));

            if (action == 'A') {
                if (!set.contains(value)) {
                    set.add(value);
                    optimizedOperations.add(operation);
                }
            } else if (action == 'D') {
                if (set.contains(value)) {
                    set.remove(value);
                    optimizedOperations.add(operation);
                }
            }
            System.out.println(optimizedOperations);
        }

        // 输出优化后的操作序列
        for (String op : optimizedOperations) {
            System.out.print(op + " ");
        }
    }
}
