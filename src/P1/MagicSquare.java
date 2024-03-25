package P1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MagicSquare {
    /**
     * 传入一个含有一个矩阵的文件的文件名，判断是否是MagicSquare
     *
      * @param fileName 文件名，含有一个n*n的矩阵
     * @return 文件内的矩阵是否是魔法矩阵，是则返回true，否则返回false
     * @throws Exception 两种exception，一是文件名对应的文件不存在，二是文件内的矩阵不合法或为空
     */
    public static boolean isLegalMagicSquare(String fileName) throws Exception{
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        List<Integer> list=new ArrayList<>();
        int index=0;
        int maxLine=1;
        int[] rowSum=new int[maxLine];//计算每行的和
        int[] colSum=new int[maxLine];
        while (scanner.hasNext()) {
             String tempInput = scanner.nextLine();
             String[] input = tempInput.split("\\t");
             int sum=0;
             for(int j=0;j<input.length;j++){
                 if(!input[j].matches("[0-9]+")) throw new IOException("Illegal input!(character/point)");
                 sum+=Integer.parseInt(input[j]);
                 if(j==colSum.length) colSum=Arrays.copyOf(colSum,j+1);
                 colSum[j]+=Integer.parseInt(input[j]);//列的和加上对应的数
             }
             if(index==0  && input.length!=maxLine) {
                 maxLine=input.length;//修改数组大小
                 rowSum=Arrays.copyOf(rowSum,maxLine);
             }
             if(input.length!=maxLine) throw new IOException("The input is NOT a n*n matrix!");
             rowSum[index++]=sum;//一行计算完毕
        }
        if (index == 0) throw new IOException("Empty File");
        if(maxLine!=index) throw new IOException("The input is NOT a n*n matrix!");
        return Arrays.equals(colSum, rowSum);
}
public static boolean generateMagicSquare(int n){
         /*
                余数为零，表示一个“斜线”已经填满，如果再进行“行-1，列+1”的操作，就会回到重复的位置
                此处row++表明每次斜线的起点向数字的前面移动的一位，如一开始以3开始，回到2以后，就会以2开始
                但此处忽略了row++==n+1的情况，后续没有进行相应处理
                在二维数组中，col的位置是col+1，如n=6，则col=3，从magic[0][3]开始，第一行第四列
                每个斜线结束的数字在起始数字的左下方，而经过row++，下一个斜线的开始就是起始数字的下方第三行，左侧一列
                如n=6，起始为(1,4)，则下次开始为(3,3)(x行y列)
                若row++前就在第n行（row=n-1），则下次起始的行刚好停在n+1行
                所以可以得出结论，如果某一次起点会跳到n+1行，就会越界
                由于矩阵的特性，一共会经历n-1次row++，增加
                对奇数来说，第一次起点到达边界是（n-1）/2次跳跃后，会到达第n行，不越界；后续继续进行（n-1）/2次跳跃，在n-1行结束计算，不会越界
                而对偶数来说，在（m-1）/2次跳跃后，会到达m-1行（想象奇数矩阵多一行就便于理解了），一圈赋值后到达m行，row++，越界
        */
         /*
                如果想要不越界，只要在row++后判断是否越界即可
                但是这样做，虽然可以输出一个矩阵了，却不是我们想要的魔法矩阵
                如果每次都想象成一个在对角线方向的1~n的循环，实际上在行和列上都应该是一个1~6间的某个排列的循环置换
                对奇数和偶数的矩阵，都是在纵向上形成12...n的循环置换
                而在行上，由于新循环会从上一个循环结尾数n的下方开始，故n的右侧是（n+2）%n
                对奇数n来说，2是1~n的集合在加法上的群的生成元，故一行可以覆盖1~n的所有数，如n=5，则一行为1 3 5 2 4（的置换）
                而对偶数n来说，2不是这样的一个生成元，会导致n/2个元素交替出现，如n=6，则一行为1 3 5 1 3 5或2 4 6 2 4 6的（的置换）
                故而采用这种方式来构造魔法矩阵是不可行的
        */
        if(n<=0 || n%2==0) return false;
        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;//square为数的个数
        for (i = 1; i <= square; i++) {
            magic[row][col] = i;//填充当前位置
            if (i % n == 0) {
                row++;
            }
            else {

                if (row == 0)//当前坐标到达上边界，回到下边界继续减
                    row = n - 1;
                else
                    row--;//赋值坐标向上移动
                if (col == (n - 1))//到达右边界，回到左边界
                    col = 0;
                else
                    col++;//赋值坐标向右移动
            }
        }
        try
        {
            File file=new File("./P1/txt/6.txt");
            file.createNewFile();//如果文件已经存在，不会发生什么
            PrintWriter myPrint = new PrintWriter(file);
            for (i = 0; i < n; i++) {
                {
                    for (j = 0; j < n - 1; j++) myPrint.print(magic[i][j]+"\t");
                    myPrint.println(magic[i][n-1]);
                }
            }
            myPrint.close();
            System.out.println("写入成功！");
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
    public static void main(String[] args) {
        //测试“生成魔法矩阵”函数
        System.out.println("Enter a number");
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        boolean flag=false;
        flag=generateMagicSquare(n);
        if (!flag)
        {
            System.out.println("Illegal input of n!");
        }

        for(int i=1;i<7;i++)
        {
            if(i==6&&!flag) break;
            String filename=String.format("./P1/txt/%d.txt",i);
            try{
                boolean legal=isLegalMagicSquare(filename);
                if(legal){
                    System.out.println("The square of \""+filename+"\" IS a Magic Square");
                }
                else{
                    System.out.println("The square of \""+filename+"\" is NOT a Magic Square");
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
