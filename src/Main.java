import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("选择输入模式：（1）手动输入，（2）文本输入 (使用#代表epsilon）:");

        int mode = Integer.parseInt(scanner.nextLine());
        GrammarContent gc = new GrammarContent();

        switch (mode) {
            case 1:
                //获取开始符号
                System.out.println("输入文法：");
                Character c1 = ProcessingUnit.getStart(scanner.nextLine());
                gc.setStart(c1);

                //获取非终结符号
                System.out.println("输入非终结符号，以逗号分隔：");
                Character[] cs1 = ProcessingUnit.getNonterminal(scanner.nextLine());
                gc.setVns(cs1);

                //获取产生式规则
                System.out.println("输入产生式规则，以一行$结束");

                /*
                 * todo: implement this -> when the user enters the productions you need to process them to the processProductions function
                 */


                break;

            case 2:
                System.out.println("输入文件路径：");
                String path = scanner.nextLine();

                LineNumberReader lineNumberReader = lineNumberReader(path);

                //获取开始符号
                Character c2 = ProcessingUnit.getStart(lineNumberReader.readLine());
                gc.setStart(c2);

                //获取非终结符号
                Character[] cs2 = ProcessingUnit.getNonterminal(lineNumberReader.readLine());
                gc.setVns(cs2);

                //获取产生式规则
                String str;
                ArrayList<String> strings = new ArrayList<>();
                while((str = lineNumberReader.readLine())!=null) {
                    strings.add(str);
                }
                gc.setProductions(strings.toArray(new String[0]));

                //获取终结符号
                Character[] vts = ProcessingUnit.processProductions(strings.toArray(new String[0]), cs2);
                gc.setVts(vts);

                //判断文法类型
                int chomskyType = ProcessingUnit.determineGrammarType(strings.toArray(new  String[0]), cs2, vts);
                gc.setChomskyType(chomskyType);

                gc.print();

                break;
        }
    }

    public static LineNumberReader lineNumberReader(String path) throws IOException {
        File file = new File(path);

        if (file.exists())
        {
            if(!file.isDirectory())
            {
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(path));
                return lineNumberReader;
            }
            else
            {
                System.out.println("输入的是一个文件夹而不是文件");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("不存在这个文件夹");
            System.exit(1);
        }

        return null;
    }
}
