import java.util.ArrayList;
import java.util.Scanner;

public class main {
    static class AvlTree<E extends Comparable<E>> {

        class Node {
            private Node left; //左孩子
            private Node right; //右孩子
            private E e; //值
            private int height;//节点高度

            public Node(E e) {
                left = null;
                right = null;
                this.e = e;
                height = 1;//因为初始节点时，，高度默认为1
            }
        }

        private Node root;
        private int size;

        public int size() {
            return size;
        }

        //判断是否有元素e
        public boolean contains(E e) {
            return contains(root, e);
        }

        //判断以node为根节点的二分搜索树，是否有e
        private boolean contains(Node node, E e) {
            if (node == null) {
                return false;
            }
            if (e.compareTo(node.e) == 0) {
                return true;
            } else if (e.compareTo(node.e) > 0) {
                return contains(node.right, e);
            } else {
                return contains(node.left, e);
            }
        }

        //获得节点node的高度
        public int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return node.height;
        }

        //获得节点node的平衡因子
        private int getBalanceFactor(Node node) {
            if (node == null) {
                return 0;
            }
            return getHeight(node.left) - getHeight(node.right);
        }

        //判断当前二叉树是否是一颗二分搜索树
        //因为二叉树，按照中序排序，默认是从小到大
        public boolean isBST() {
            ArrayList<E> keys = new ArrayList<>();
            //将当前树，按中序遍历方式，放入数组
            inOrder(root, keys);
            for (int i = 1; i < keys.size(); i++) {
                if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
                    return false;
                }
            }
            return true;
        }

        //判断该二叉树是否时一颗平衡二叉树
        public boolean isBalanced() {
            return isBalanced(root);
        }

        // 判断以node为根的二叉树是否是一颗平衡二叉树，递归算法
        private boolean isBalanced(Node node) {
            if (node == null) {
                return true;
            }
            int balanceFactor = getBalanceFactor(node);
            if (Math.abs(balanceFactor) > 1) {//判断当前节点的二叉树平衡因子
                return false;
            }
            //递归判断左右子孩子是否平衡
            return isBalanced(node.left) && isBalanced(node.right);
        }

        private void inOrder(Node node, ArrayList<E> keys) {
            if (node == null) {
                return;
            }
            inOrder(node.left, keys);
            keys.add(node.e);
            inOrder(node.right, keys);
        }

        public void add(E e) {
            root = add(root, e);
        }

        //以node为根节点，插入e 的递归写法
        //返回插入新节点后二分搜索的根
        public Node add(Node node, E e) {
            if (node == null) {
                size++;
                return new Node(e);
            }
            if (e.compareTo(node.e) < 0) {
                node.left = add(node.left, e);
            }
            if (e.compareTo(node.e) > 0) {
                node.right = add(node.right, e);
            }
            //更新height= 当前节点左右子树最高的节点高度+1
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            //计算平衡因子
            int balanceFactor = getBalanceFactor(node);
            //LL
            if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {//当前节点的平衡因子大于1 并且左子树的平衡因子大于=0
                return rightRotate(node);
            }
            //RR
            if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
                return LeftRotate(node);
            }
            //LR
            if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
                node.left = LeftRotate(node.left);
                return rightRotate(node);
            }
            //RL
            if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
                node.right = rightRotate(node.right);
                return LeftRotate(node);
            }
            return node;
        }

        //获取二分搜索树的最大值
        public E getMaxValue() {
            if (size == 0) {
                throw new IllegalArgumentException("BST IS empty");
            }
            return getMaxNode(root).e;
        }

        //获取 以node为根节点，值最大值所在的节点
        private Node getMaxNode(Node node) {
            if (node.right == null) {
                return node;
            }
            return getMaxNode(node.right);
        }

        //获取二分搜索树的最小值
        public E getMinValue() {
            if (size == 0) {
                throw new IllegalArgumentException("BST IS empty");
            }
            return getMinNode(root).e;
        }

        //获取 以node为根节点，值最小值所在的节点
        private Node getMinNode(Node node) {
            if (node.left == null) {
                return node;
            }
            return getMinNode(node.left);
        }

        //对节点y进行向右旋转操作，返回旋转后的新的根节点x
        //         y                        x
        //        / \                     /   \
        //       x   T4   向右旋转（y）   z       y
        //      / \       ------->     /  \     / \
        //     z   T3                T1   T2  T3  T4
        //    / \
        //  T1   T2
        private Node rightRotate(Node y) {//LL 右旋转
            Node x = y.left;
            Node T3 = x.right;
            //向右旋转
            x.right = y;
            y.left = T3;
            //维护高度
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            return x;
        }

        //左旋转
        private Node LeftRotate(Node y) {
            Node x = y.right;
            Node T2 = x.left;
            //向左旋转
            x.left = y;
            y.right = T2;
            //维护高度
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            return x;
        }

        //删除二分搜索树的最小值 返回最小值
        public E removeMinValue() {
            E e = getMinValue();//获取最小值
            root = removeMinNode(root);
            return e;
        }

        // 删除掉以node为根的二分搜索树中的最小节点
        // 返回删除节点后新的二分搜索树的根
        private Node removeMinNode(Node node) {
            if (node.left == null) {//递归的最终状态，如果node的左孩子为空，则说明它是最小的
                Node temp = node.right;//维护节点的右孩子
                node.right = null;
                size--;
                return temp;//删除后，根节点为右孩子，返回右孩子即可
            }
            //左孩子不为空，说明还不是最小，递归以左孩子为根节点找
            node.left = removeMinNode(node.left);
            return node;
        }

        //删除二分搜索树的最大值，返回最大值
        public E removeMaxValue() {
            E e = getMaxValue();
            root = removeMaxNode(root);
            return e;
        }

        // 删除掉以node为根的二分搜索树中的最大节点
        // 返回删除节点后新的二分搜索树的根
        public Node removeMaxNode(Node node) {
            if (node.right == null) {
                Node temp = node.left;
                node.left = null;
                size--;
                return temp;
            }
            node.right = removeMaxNode(node.right);
            return node;
        }

        //从二分搜索树中删除元素为e的节点
        public void remove(E e) {
            root = removeNode(root, e);
        }

        // 删除掉以node为根的二分搜索树中值为e的节点, 递归算法
        // 返回删除节点后新的二分搜索树的根
        private Node removeNode(Node node, E e) {
            if (node == null) {
                return null;
            }
            Node retNode;//因为我们最后要统一维护树的平衡性，因此这里不可以直接返回node 最后再返回
            if (e.compareTo(node.e) > 0) {//如果e> node.e 则去node的右孩子找
                node.right = removeNode(node.right, e);
                retNode = node;
            } else if (e.compareTo(node.e) < 0) {//如果e< node.e 则去node的左孩子找
                node.left = removeNode(node.left, e);
                retNode = node;
            } else {//相等 进行删除逻辑
                // 注意：由BST的return改为直接赋值，下面的三种情况本身是互斥的，因此需要加else
                if (node.left == null) {//如果node.left 为空，则逻辑相当于删除 最小节点
                    Node temp = node.right;
                    node.right = null;
                    size--;
                    retNode = temp;
                } else if (node.right == null) {//如果node.right 为空，则逻辑相当于删除 最大节点
                    Node temp = node.left;
                    node.left = null;
                    size--;
                    retNode = temp;
                } else {//如果左右孩子都不为空，则默认把node的后继放到自己的位置
                    //即，找node右孩子的最小节点
                    Node rightMin = getMinNode(node.right);
                    //且node的后继的右孩子为 removeMinNode(node.right) 这步相当于，把后继节点直接放到node的位置，原来的后继位置删除了
                    //rightMin.right = removeMinNode(node.right);//注意removeMinNode里面已经有size--方法了
                    //removeMinNode(node.right); 为删除node.right的最小值，而rightMin本身就是最小值，因此改用如下方法
                    rightMin.right = removeNode(node.right, rightMin.e);//这样写 removeMinNode就不用维护平衡了
                    rightMin.left = node.left;
                    node.left = node.right = null;
                    retNode = rightMin;
                }

            }
            //最终维护retnode的平衡 方法和新增相同
            if (retNode == null) {//如果删除后的节点，返回的新的树节点为null，那么久不需要维护平衡了，直接返回null
                return null;
            }
            //更新height= 当前节点左右子树最高的节点高度+1
            retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));
            //计算平衡因子
            int balanceFactor = getBalanceFactor(retNode);
            //LL
            if (balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0) {//当前节点的平衡因子大于1 并且左子树的平衡因子大于=0
                return rightRotate(retNode);
            }
            //RR
            if (balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0) {
                return LeftRotate(retNode);
            }
            //LR
            if (balanceFactor > 1 && getBalanceFactor(retNode.left) < 0) {
                retNode.left = LeftRotate(retNode.left);
                return rightRotate(retNode);
            }
            //RL
            if (balanceFactor < -1 && getBalanceFactor(retNode.right) > 0) {
                retNode.right = rightRotate(retNode.right);
                return LeftRotate(retNode);
            }
            return retNode;
        }

        //遍历
        //前序遍历
        public void preOrder() {
            preOrder(root);
        }

        public void preOrder(Node node) {
            if (node != null) {
                System.out.print(node.e + " ");
                preOrder(node.left);
                preOrder(node.right);
            }
        }


        //后续遍历
        public void postOrder() {
            postOrder(root);
        }

        public void postOrder(Node node) {
            if (node != null) {
                postOrder(node.left);
                postOrder(node.right);
                System.out.print(node.e + " ");
            }
        }

        public void inOrder() {
            inOrder(root);
        }

        private void inOrder(Node node) {
            if (node != null) {
                inOrder(node.left);
                System.out.print(node.e + " ");
                inOrder(node.right);
            }
        }


//        @Override
//        public String toString() {
//            StringBuilder res = new StringBuilder();
//            generateBSTString(root, 0, res);
//            return res.toString();
//        }
//
//        // 生成以node为根节点，深度为depth的描述二叉树的字符串
//        private void generateBSTString(Node node, int depth, StringBuilder res) {
//
//            if (node == null) {
//                res.append(generateDepthString(depth) + "null\n");
//                return;
//            }
//
//            res.append(generateDepthString(depth) + node.e + "\n");
//            generateBSTString(node.left, depth + 1, res);
//            generateBSTString(node.right, depth + 1, res);
//        }
//
//        private String generateDepthString(int depth) {
//            StringBuilder res = new StringBuilder();
//            for (int i = 0; i < depth; i++)
//                res.append("--");
//            return res.toString();
//        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        operator(scanner.nextLine());

        scanner.close();
//
//        String input = "A88 D77 D95 A78 A71 A2 D9 A2 A60 D80 A85 A65 D11 A30 D8 A68 D87 A28 A88 A96 D29 D26 D88 D47 D68 A65 A86 A100 A61 D7 D76 D21 D24 A40 D94 A84 A16 D28 A45 A60 D34 D14 A68 A64 A74 A62 D99 D2 D34 D32 D60 D52 D19 A95 A28 A91 D24 A34 D22 D77 D7 D78 A3 A100 D95 D53 D82 D64 A55 A46 A17 A70 D4 A25 A75 D71 A30 D50 D44 A11 D39 A47 D77 A71 D1 A98 D51 A63 D15 A15 D75 A4 D14 D77 A9 D84 D70 A5 D67 A22 POST";
//        operator(input);
////                        3 5 4 15 11 9 22 17 28 25 16 34 45 40 47 55 46 30 62 65 63 74 71 68 91 86 98 100 96 85 61
////        Expected Output: 3 5 4 15 11 9 22 17 28 25 16 34 40 46 55 47 63 62 61 45 30 71 68 85 74 91 98 100 96 86 65

    }

    public static void operator(String input) {
        String[] inputs = input.split(" ");

        AvlTree tree = new AvlTree();

        for (int i = 0; i < inputs.length - 1; i++) {
            String operation = inputs[i];
            int value = Integer.parseInt(operation.substring(1));

            if (operation.charAt(0) == 'A') {
                tree.root = tree.add(tree.root, value);
            } else if (operation.charAt(0) == 'D') {
                tree.root = tree.removeNode(tree.root, value);
            }
//            System.out.println("Current operation: " + operation);
//            System.out.println("Tree status after operation: ");
//            System.out.println(tree);
        }

        String traversal = inputs[inputs.length - 1];
        if (tree.root == null) {
            System.out.println("EMPTY");
        } else {
            if (traversal.equals("PRE")) {
                tree.preOrder();
            } else if (traversal.equals("IN")) {
                tree.inOrder();
            } else if (traversal.equals("POST")) {
                tree.postOrder();
            }
            System.out.println(); // To ensure the output ends with a newline.
        }
    }
}