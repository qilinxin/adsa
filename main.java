import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

class AVLTree1 {
    class Node {
        int key;
        int height;
        Node left, right;

        Node(int d) {
            key = d;
            height = 1;
        }
    }

    Node root;

    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    Node minValueNode(Node node) {
        Node current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    Node deleteNode(Node root, int key) {
        if (root == null)
            return root;

        // 寻找要删除的节点
        if (key < root.key)
            root.left = deleteNode(root.left, key);
        else if (key > root.key)
            root.right = deleteNode(root.right, key);
        else {
            // 找到要删除的节点
            // Case 1: 节点只有一个子节点或没有子节点
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                if (root.left != null)
                    temp = root.left;
                else
                    temp = root.right;

                // 没有子节点的情况
                if (temp == null) {
                    temp = root;
                    root = null;
                } else // 只有一个子节点的情况
                    root = temp;
            } else {
                // Case 2: 节点有两个子节点，使用左子树的最大值替换
                Node temp = maxValueNode(root.left);

                // 使用左子树的最大值替换当前节点的值
                root.key = temp.key;

                // 删除左子树中的最大值节点
                root.left = deleteNode(root.left, temp.key);
            }
        }

        if (root == null)
            return root;

        // 更新当前节点的高度
        root.height = max(height(root.left), height(root.right)) + 1;

        // 获取平衡因子
        int balance = getBalance(root);

        // 如果节点不平衡，则进行旋转操作

        // 左左情况
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // 左右情况
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // 右右情况
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // 右左情况
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // 找到左子树的最大值节点
    Node maxValueNode(Node node) {
        Node current = node;

        // 左子树的最大值在最右侧
        while (current.right != null)
            current = current.right;

        return current;
    }


    Node checkBalance(Node root) {
        if (root == null)
            return root;

        root.height = max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }

    void postOrder(Node node) {
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            System.out.print(node.key + " ");
        }
    }
}

public class main {

    static List<String> readInputData() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputData = reader.readLine();
//        String inputData = //"A1 D1 POST";
////                "A1 A2 A3 A4 A5 A6 A7 A8 A9 POST";
////        "D13 D60 D76 D12 A17 D98 A94 D70 D3 A23 A42 D45 A100 D50 A99 A22 A87 A4 A90 D88 A71 A20 D39 D83 A97 A56 D28 A9 D43 A19 D5 A11 A54 A73 D54 A9 A24 A58 D6 D80 A72 A47 A82 A12 A75 D77 D84 D86 A60 D64 D70 D70 A73 A71 A40 D94 D27 A63 D47 A42 A44 A27 A100 A6 D84 A19 D65 A75 A55 A63 A39 D99 A50 D98 A98 D100 D93 A91 A81 D59 D56 D29 D11 D45 D47 D55 D85 D7 D70 A13 A55 A25 D35 D65 A48 D55 A45 D29 A35 A15 IN";
//        "A88 D77 D95 A78 A71 A2 D9 A2 A60 D80 A85 A65 D11 A30 D8 A68 D87 A28 A88 A96 D29 D26 D88 D47 D68 A65 A86 A100 A61 D7 D76 D21 D24 A40 D94 A84 A16 D28 A45 A60 D34 D14 A68 A64 A74 A62 D99 D2 D34 D32 D60 D52 D19 A95 A28 A91 D24 A34 D22 D77 D7 D78 A3 A100 D95 D53 D82 D64 A55 A46 A17 A70 D4 A25 A75 D71 A30 D50 D44 A11 D39 A47 D77 A71 D1 A98 D51 A63 D15 A15 D75 A4 D14 D77 A9 D84 D70 A5 D67 A22 POST";
        return Arrays.asList(inputData.split(" "));
    }

    // 处理输入数据并更新树
    static void processInputData(List<String> inputs, AVLTree1 tree) {
        for (int i = 0; i < inputs.size() - 1; i++) {
            String operation = inputs.get(i);
            int value = Integer.parseInt(operation.substring(1));

            if (operation.charAt(0) == 'A') {
                tree.root = tree.insert(tree.root, value);
            } else if (operation.charAt(0) == 'D') {
                tree.root = tree.deleteNode(tree.root, value);
            }
        }

        String traversal = inputs.getLast();
        if (tree.root == null) {
            System.out.println("EMPTY");
        } else {
            if (traversal.equals("PRE")) {
                tree.preOrder(tree.root);
            } else if (traversal.equals("IN")) {
                tree.inOrder(tree.root);
            } else if (traversal.equals("POST")) {
                tree.postOrder(tree.root);
            }
            System.out.println(); // To ensure the output ends with a newline.
        }
    }

    public static void main(String[] args) throws IOException {
        AVLTree1 tree = new AVLTree1();
        List<String> inputData = readInputData();
        processInputData(inputData, tree); // 处理输入数据并更新树
    }
}