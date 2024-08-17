package main.java;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        scanner.close();

        // 解析输入
        List<String> parts = Arrays.asList(input.split(" "));
        String first = parts.get(0);
        String second = parts.get(1);
        int base = Integer.parseInt(parts.get(2));

        // 转换为大整数
        BigInteger bigInteger1 = new BigInteger(first, base);
        BigInteger bigInteger2 = new BigInteger(second, base);

        // 使用学校方法进行加法
        BigInteger sum = bigInteger1.add(bigInteger2);

        // 使用 Karatsuba 算法进行乘法
        BigInteger product = Karatsuba(bigInteger1, bigInteger2);

        // yanjiushengxuyao chufa
        BigInteger quotient = bigInteger1.divide(bigInteger2);


        // 以指定进制格式化输出
        System.out.println(sum.toString(base) + " " + product.toString(base) + " " + quotient.toString(base));
    }

    /**
     *  Karatsuba 算法实现
     * @param x
     * @param y
     * @return
     */
    private static BigInteger Karatsuba(BigInteger x, BigInteger y) {
        int maxLen = Math.max(x.bitLength(), y.bitLength());

        if (maxLen <= 10) { // 阈值可以调整，这里为了简单起见用较小的值
            return x.multiply(y);
        }

        maxLen = (maxLen / 2) + (maxLen % 2); // 计算半长度
        System.out.println("x==="+x+", y===="+y+", N===="+maxLen);
        // x = a + 2^N * b, y = c + 2^N * d
        BigInteger b = x.shiftRight(maxLen);
        BigInteger a = x.subtract(b.shiftLeft(maxLen));
        BigInteger d = y.shiftRight(maxLen);
        BigInteger c = y.subtract(d.shiftLeft(maxLen));

        // 计算 ac, bd 和 (a+b)(c+d)
        BigInteger ac = Karatsuba(a, c);
        BigInteger bd = Karatsuba(b, d);
        BigInteger abcd = Karatsuba(a.add(b), c.add(d));

        // 组合结果
        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(maxLen)).add(bd.shiftLeft(2 * maxLen));
    }
}
