import java.util.Scanner;
import java.util.Vector;

class TreeNode {
    int value;
    int balanceFactor;
    TreeNode[] subTree = new TreeNode[2]; // subTree[0]为左子树，subTree[1]为右子树

    TreeNode(int value) {
        this.value = value;
        this.balanceFactor = 0;
        this.subTree[0] = null;
        this.subTree[1] = null;
    }
}

public class main {

    // 旋转树节点以平衡树
    static void rotateTree(TreeNode[] nodeRef, int rotatingDirection) {
        int leftSubtreeIndex = rotatingDirection == 1 ? 0 : 1;
        int rightSubtreeIndex = 1 - leftSubtreeIndex;
        int rotationFactor = rotatingDirection == 1 ? 1 : -1;

        TreeNode node = nodeRef[0];
        TreeNode left = node.subTree[leftSubtreeIndex];

        if (left == null) {
            System.exit(1);
        } else {
            int nodeBalanceFactorTimesRotationFactor = node.balanceFactor * rotationFactor;
            int leftBalanceFactorTimesRotationFactor = left.balanceFactor * rotationFactor;

            int newBalanceFactorNode = (leftBalanceFactorTimesRotationFactor >= 0)
                    ? (nodeBalanceFactorTimesRotationFactor - 1 - leftBalanceFactorTimesRotationFactor)
                    : (nodeBalanceFactorTimesRotationFactor - 1);

            int newBalanceFactorLeft = (leftBalanceFactorTimesRotationFactor >= 0)
                    ? ((nodeBalanceFactorTimesRotationFactor - 1 - leftBalanceFactorTimesRotationFactor >= 0)
                    ? (leftBalanceFactorTimesRotationFactor - 1)
                    : (nodeBalanceFactorTimesRotationFactor - 2))
                    : ((nodeBalanceFactorTimesRotationFactor - 1 >= 0)
                    ? (leftBalanceFactorTimesRotationFactor - 1)
                    : (nodeBalanceFactorTimesRotationFactor + leftBalanceFactorTimesRotationFactor - 2));

            node.balanceFactor = newBalanceFactorNode * rotationFactor;
            left.balanceFactor = newBalanceFactorLeft * rotationFactor;
            node.subTree[leftSubtreeIndex] = left.subTree[rightSubtreeIndex];
            left.subTree[rightSubtreeIndex] = node;
            nodeRef[0] = left;
        }
    }

    static int updateTreeNode(TreeNode[] nodeRef, int value, int operationType) {
        TreeNode node = nodeRef[0];

        if (node == null) {
            if (operationType == 0) {
                return 0; // 删除操作中找不到节点，直接返回
            } else {
                nodeRef[0] = new TreeNode(value); // 插入新节点
                return 1;
            }
        } else {
            int leftSubtreeIndex, balanceChange = 0, tmpDeltaFactor;
            TreeNode nextTreeNode;

            if (value == node.value) {
                if (operationType == 1) {
                    return 0; // 插入操作时发现值已经存在
                } else {
                    // 处理删除操作
                    if (node.subTree[1] == null) {
                        nodeRef[0] = node.subTree[0];
                        return -1;
                    } else if (node.subTree[0] == null) {
                        nodeRef[0] = node.subTree[1];
                        return -1;
                    } else {
                        nextTreeNode = node.subTree[0];
                        while (nextTreeNode.subTree[1] != null) {
                            nextTreeNode = nextTreeNode.subTree[1];
                        }
                        node.value = nextTreeNode.value;
                        leftSubtreeIndex = 0;
                        tmpDeltaFactor = 1;
                        balanceChange = updateTreeNode(new TreeNode[]{node.subTree[leftSubtreeIndex]}, nextTreeNode.value, operationType);
                    }
                }
            } else {
                leftSubtreeIndex = (value < node.value) ? 0 : 1;
                tmpDeltaFactor = (value < node.value) ? 1 : -1;

                // 这里我们确保递归调用前，子节点已经被初始化
                if (node.subTree[leftSubtreeIndex] == null && operationType == 1) {
                    node.subTree[leftSubtreeIndex] = new TreeNode(value);
                    return 1;
                }

                // 确保 node.subTree[leftSubtreeIndex] 不为 null
                if (node.subTree[leftSubtreeIndex] != null) {
                    balanceChange = updateTreeNode(new TreeNode[]{node.subTree[leftSubtreeIndex]}, value, operationType);
                }
            }

            if (balanceChange != 0) {
                int tmpBalanceFactor = node.balanceFactor;
                int shouldPerformRotation = 0;
                int leftSubtreeToRotate = 0;
                int rotationFactor = 0;
                int rightSubtreeToRotate = 0;

                node.balanceFactor = node.balanceFactor + balanceChange * tmpDeltaFactor;

                if (node.balanceFactor > 1) {
                    shouldPerformRotation = 1;
                    leftSubtreeToRotate = 0;
                    rotationFactor = 1;
                    rightSubtreeToRotate = 1;
                } else if (node.balanceFactor < -1) {
                    shouldPerformRotation = 1;
                    leftSubtreeToRotate = 1;
                    rotationFactor = -1;
                    rightSubtreeToRotate = 0;
                }

                if (shouldPerformRotation != 0) {
                    int balanceFactorInChild = node.subTree[leftSubtreeToRotate].balanceFactor;

                    // 再次确保子节点在旋转前已经存在
                    if (node.subTree[leftSubtreeToRotate] != null) {
                        if (node.subTree[leftSubtreeToRotate].balanceFactor * rotationFactor < 0) {
                            rotateTree(new TreeNode[]{node.subTree[leftSubtreeToRotate]}, 1 - rightSubtreeToRotate);
                        }
                        rotateTree(nodeRef, rightSubtreeToRotate);
                    } else {
                        throw new RuntimeException("Attempted to rotate on a null child during balance adjustment.");
                    }

                    if (balanceChange < 0 && balanceFactorInChild != 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

                if (balanceChange > 0 && tmpBalanceFactor == 0) {
                    return 1;
                } else if (balanceChange < 0 && tmpBalanceFactor != 0) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }


    // 前序遍历树
    static void preOrder(TreeNode tree, StringBuilder output) {
        if (tree == null) {
            return;
        }
        output.append(tree.value).append(" ");
        preOrder(tree.subTree[0], output);
        preOrder(tree.subTree[1], output);
    }

    // 后序遍历树
    static void postOrder(TreeNode tree, StringBuilder output) {
        if (tree == null) {
            return;
        }
        postOrder(tree.subTree[0], output);
        postOrder(tree.subTree[1], output);
        output.append(tree.value).append(" ");
    }

    // 中序遍历树
    static void inOrder(TreeNode tree, StringBuilder output) {
        if (tree == null) {
            return;
        }
        inOrder(tree.subTree[0], output);
        output.append(tree.value).append(" ");
        inOrder(tree.subTree[1], output);
    }

    // 打印树数据
    static String printTreeData(String order, TreeNode tree) {
        if (tree == null) {
            return "EMPTY";
        }
        StringBuilder output = new StringBuilder();
        if (order.equals("IN")) {
            inOrder(tree, output);
        } else if (order.equals("POST")) {
            postOrder(tree, output);
        } else if (order.equals("PRE")) {
            preOrder(tree, output);
        }
        return output.toString().trim();
    }

    // 读取输入数据
    static Vector<String> readInputData() {
        Scanner scanner = new Scanner(System.in);
        Vector<String> data = new Vector<>();
        while (scanner.hasNext()) {
            data.add(scanner.next());
        }
        return data;
    }

    // 处理输入数据并更新树
    static void processInputData(Vector<String> data, TreeNode[] root) {
        for (int i = 0; i < data.size(); i++) {
            String order = data.get(i);
            if (i == data.size() - 1) {
                String output = printTreeData(order, root[0]);
                System.out.println(output);
            } else {
                int operationType = (order.charAt(0) == 'A') ? 1 : 0;
                int value = Integer.parseInt(order.substring(1));
                updateTreeNode(root, value, operationType);
            }
        }
    }

    public static void main(String[] args) {
        TreeNode[] root = new TreeNode[]{null};
        Vector<String> inputData = readInputData();
        processInputData(inputData, root);
    }
}
