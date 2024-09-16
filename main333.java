import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AVL平衡二叉树
 *
 * @author ronglexie
 * @version 2018/9/1
 */
class AVLTree<K extends Comparable<K>,V> {

    Node root;
    int size;

    public AVLTree() {
        root = null;
        size = 0;
    }

    /**
     * 获取某个节点的高度
     *
     * @param node
     * @return int
     * @author ronglexie
     * @version 2018/9/1
     */
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * 获取某个节点的平衡因子
     *
     * @param node
     * @return int
     * @author ronglexie
     * @version 2018/9/1
     */
    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * 查看AVL平衡二叉树是否是二分搜索树
     *
     * @param
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private boolean isBinarySearchTree() {
        ArrayList<K> keys = new ArrayList<>();
        inOrder(root, keys);
        for (int i = 1; i < keys.size(); i++) {
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查看AVL平衡二叉树是否是平衡二叉树
     *
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * 递归查看以node为根节点的AVL平衡二叉树是否是平衡二叉树
     *
     * @param node
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int balanceFactor = getBalanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    // 对节点y进行向右旋转操作，返回旋转后新的根节点x
    //        y                              x
    //       / \                           /   \
    //      x   T4     向右旋转 (y)        z     y
    //     / \       - - - - - - - ->    / \   / \
    //    z   T3                       T1  T2 T3 T4
    //   / \
    // T1   T2

    /**
     * 右旋转操作
     *
     * @param y
     * @return AVLTree<K, V>.Node
     * @author ronglexie
     * @version 2018/9/1
     */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        //右旋转操作
        x.right = y;
        y.left = T3;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    // 对节点y进行向左旋转操作，返回旋转后新的根节点x
    //    y                             x
    //  /  \                          /   \
    // T1   x      向左旋转 (y)       y     z
    //     / \   - - - - - - - ->   / \   / \
    //   T2  z                     T1 T2 T3 T4
    //      / \
    //     T3 T4

    /**
     * 左旋转操作
     *
     * @param y
     * @return AVLTree<K, V>.Node
     * @author ronglexie
     * @version 2018/9/1
     */
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        //左旋转操作
        x.left = y;
        y.right = T2;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    /**
     * 中序遍历以node为根节点的AVL平衡二叉树
     * 深度优先遍历，递归实现
     *
     * @param node
     * @return void
     * @author ronglexie
     * @version 2018/8/18
     */
    private void inOrder(Node node, ArrayList<K> keys) {
        if (node == null) {
            return;
        }
        inOrder(node.left, keys);
        keys.add(node.key);
        inOrder(node.right, keys);
    }

    /**
     * 向AVL平衡二叉树中插入元素
     *
     * @param key
     * @param value
     * @return void
     * @author ronglexie
     * @version 2018/9/1
     */
    public void add(K key, V value) {
        root = add(root, key, value);
    }

    /**
     * 向node为根元素的AVL平衡二叉树中插入元素
     * 递归算法
     *
     * @param node
     * @param key
     * @param value
     * @return void
     * @author ronglexie
     * @version 2018/8/19
     */
    private Node add(Node node, K key, V value) {
        //递归终止条件，返回结果为null
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = add(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = add(node.right, key, value);
        } else {
            node.value = value;
        }

        /**========== 维护平衡 Start ==========*/
        //更新Height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(node);
        //LL左孩子节点的左侧产生不平衡
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            //右旋转操作
            return rightRotate(node);
        }
        //RR右孩子节点的右侧产生不平衡
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            //左旋转操作
            return leftRotate(node);
        }
        //LR左孩子节点的右侧产生不平衡
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            //右旋转操作
            return rightRotate(node);
        }
        //RL右孩子节点的左侧产生不平衡
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            //右旋转操作
            return leftRotate(node);
        }
        /**========== 维护平衡 End ==========*/
        return node;
    }

    /**
     * 查找AVL平衡二叉树的最小值
     *
     * @param
     * @return V
     * @author ronglexie
     * @version 2018/8/18
     */
    public V minimum() {
        if (isEmpty()) {
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        return minimum(root).value;
    }

    /**
     * 查找以node为根节点AVL平衡二叉树的最小节点
     * 深度优先遍历，递归实现
     *
     * @param node
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    private Node minimum(Node node) {
        if (isEmpty()) {
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    /**
     * 查找AVL平衡二叉树的最大值
     *
     * @param
     * @return V
     * @author ronglexie
     * @version 2018/8/18
     */
    public V maximize() {
        if (isEmpty()) {
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        return maximize(root).value;
    }

    /**
     * 查找以node为根节点AVL平衡二叉树的最大节点
     * 深度优先遍历，递归实现
     *
     * @param node
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    private Node maximize(Node node) {
        if (isEmpty()) {
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if (node.right == null) {
            return node;
        }
        return minimum(node.right);
    }

    /**
     * 删除AVL平衡二叉树的最大值
     *
     * @param
     * @return V
     * @author ronglexie
     * @version 2018/8/18
     */
    public V removeMax() {
        V maximize = maximize();
        removeMax(root);
        return maximize;
    }

    /**
     * 删除以node为根的AVL平衡二叉树的最大节点
     * 深度优先遍历，递归实现
     *
     * @param node
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    private Node removeMax(Node node) {
        if (node.right == null) {
            Node leftNode = node.left;
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMin(node.right);
        return node;
    }

    /**
     * 删除AVL平衡二叉树的最小值
     *
     * @param
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    public V removeMin() {
        V minimum = minimum();
        removeMin(root);
        return minimum;
    }

    /**
     * 删除以node为根的AVL平衡二叉树的最小节点
     * 深度优先遍历，递归实现
     *
     * @param node
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    public V remove(K key) {
        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    /**
     * 删除以node为根的AVL平衡二叉树中的指定元素
     * 深度优先遍历，递归实现
     *
     * @param node
     * @param key
     * @return BinarySearchTree<E>.Node
     * @author ronglexie
     * @version 2018/8/18
     */
    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }

        Node resultNode;

        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            resultNode = node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            resultNode = node;
        } else {
            // 找到待删除的节点

            // 处理只有右子树或没有子树的情况
            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                resultNode = leftNode;
            }
            // 处理只有左子树的情况
            else if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                resultNode = rightNode;
            }
            // 处理有左子树和右子树的情况
            else {
                // 使用左子树的最大节点替代当前节点
                Node predecessor = maximize(node.left);
                predecessor.left = remove(node.left, predecessor.key);  // 删除左子树中的最大节点
                predecessor.right = node.right;

                // 将待删除节点的左右子节点置为空
                node.left = node.right = null;

                resultNode = predecessor;
            }
        }

        // 维护平衡
        if (resultNode == null) {
            return null;
        }

        // 更新高度
        resultNode.height = 1 + Math.max(getHeight(resultNode.left), getHeight(resultNode.right));

        // 计算平衡因子
        int balanceFactor = getBalanceFactor(resultNode);

        // LL
        if (balanceFactor > 1 && getBalanceFactor(resultNode.left) >= 0) {
            return rightRotate(resultNode);
        }

        // RR
        if (balanceFactor < -1 && getBalanceFactor(resultNode.right) <= 0) {
            return leftRotate(resultNode);
        }

        // LR
        if (balanceFactor > 1 && getBalanceFactor(resultNode.left) < 0) {
            resultNode.left = leftRotate(resultNode.left);
            return rightRotate(resultNode);
        }

        // RL
        if (balanceFactor < -1 && getBalanceFactor(resultNode.right) > 0) {
            resultNode.right = rightRotate(resultNode.right);
            return leftRotate(resultNode);
        }

        return resultNode;
    }


    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {
        Node node = getNode(root, key);
        return node != null ? node.value : null;
    }

    public void set(K key, V value) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException("Set failed. key is not exists!");
        }
        node.value = value;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 根据key获取Node
     *
     * @param node
     * @param key
     * @return map.LinkedListMap<K, V>.Node
     * @author ronglexie
     * @version 2018/8/19
     */
    public Node getNode(Node node, K key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) == 0) {
            return node;
        } else if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else {
            return getNode(node.right, key);
        }
    }

    /**
     * 节点类
     *
     * @author ronglexie
     * @version 2018/8/18
     */
    private class Node {
        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }
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
public class main333 {

    static List<String> readInputData() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String inputData = reader.readLine();
        String inputData =
                "A17 A94 A23 A42 A100 A99 A22 A87 A4 A90 A71 A20 A97 A56 A9 A19 A11 A54 A73 D54 A24 A58 A72 A47 A82 A12 A75 A60 A40 D94 A63 D47 A44 A27 A6 A55 A39 D99 A50 A98 D100 A91 A81 D56 D11 D55 A13 A55 A25 A48 D55 A45 A35 A15 IN";
        return Arrays.asList(inputData.split(" "));
    }

    // 处理输入数据并更新树
    static void processInputData(List<String> inputs, AVLTree tree) {
        for (int i = 0; i < inputs.size() - 1; i++) {
            String operation = inputs.get(i);
            int value = Integer.parseInt(operation.substring(1));

            if (operation.charAt(0) == 'A') {
                tree.add(value, value);
            } else if (operation.charAt(0) == 'D') {
                tree.remove(value);
            }
//            tree.postOrder(tree.root);
//            System.out.println();
        }

        String traversal = inputs.get(inputs.size() - 1);
        if (tree.isEmpty()) {
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
        AVLTree tree = new AVLTree();
        List<String> inputData = readInputData();
//        Arrays.asList("A54", "A99", "D60", "A80", "A28", "D4", "D60", "D62", "A76", "A100",
//                "A34", "A31", "A29", "A31", "D57", "D59", "D95", "D90", "A46", "A91",
//                "D44", "D81", "A56", "A2", "A84", "A50", "D59", "A47", "D22", "A49",
//                "A74", "A59", "A15", "D17", "A44", "D77", "D77", "D90", "A94", "A70",
//                "D83", "D84", "A21", "D82", "D5", "D1", "D27", "D33", "A60", "A55",
//                "A38", "A36", "A57", "D14", "A50", "A78", "D84", "D30", "A92", "A48",
//                "D94", "A3", "D3", "A7", "D34", "A96", "D37", "D68", "A58", "A18",
//                "A26", "A60", "D69", "D33", "A66", "D17", "D64", "D16", "D10", "A14",
//                "A78", "A8", "A70", "A31", "D40", "D82", "D71", "A14", "A49", "D64",
//                "A34", "D99", "A35", "A67", "A14", "D43", "D55", "A10", "D66", "A94",
//                "POST");
        processInputData(inputData, tree); // 处理输入数据并更新树
    }
}