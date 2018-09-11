import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class ProcessingUnit {
    //获取开始符号
    static Character getStart(String input) {
        if(input.length()!=4) {
            System.out.println("文法长度必须为4");
            System.exit(1);
        }
        return input.charAt(2);
    }

    //处理、获取非终结符号
    static Character[] getNonterminal(String input) {
        StringTokenizer st = new StringTokenizer(input, ",", false);
        ArrayList<Character> cs = new ArrayList<>();

        while (st.hasMoreTokens()) {
            cs.add(st.nextToken().trim().charAt(0));
        }

        return cs.toArray(new Character[0]);
    }

    //处理产生式规则，并找出终结符号集合
    static Character[] processProductions(String[] productions, Character[] nonterminals) {
        //allCharacters用来存放所有的符号
        ArrayList<Character> allCharacters = new ArrayList<>();

        //对productions中的String进行遍历，找出所有的终结符号和非终结符号，然后通过已知的非终结符号找出终结符号
        for(String s : productions) {
            String[] leftAndRight = s.split("::=");

            //对左部进行处理，将左部的每一个符号添加到allCharacters
            for(int i=0; i<leftAndRight[0].trim().length(); i++) {
                if(!allCharacters.contains(leftAndRight[0].charAt(i)))
                    allCharacters.add(leftAndRight[0].charAt(i));
            }

            //对右部进行处理，将右部拆分，然后对每个字符进行处理，同时要剔除#-epsilon
            String[] rightElements = leftAndRight[1].split("\\|");
            for(String element : rightElements) {
                element = element.trim();
                //判断每个部分是否在ALLCHARACTERS中
                for(int i=0; i<element.length(); i++) {
                    if(!allCharacters.contains(element.charAt(i)) && element.charAt(i)!='#')
                        allCharacters.add(element.charAt(i));
                }
            }
        }

        //如果移除所有的nontermials不成功
        if(!allCharacters.removeAll(new ArrayList<Character>(Arrays.asList(nonterminals))))
            System.exit(1);

        return allCharacters.toArray(new Character[0]);
    }

    //判断文法类型

    /**
     *
     * @param productions is the set of all productions
     * @return int grammarType
     *
     * 0 -> chomsky 0 type
     *
     * 1 -> chomsky 1 type
     *
     * 2 -> chomsky 2 type
     *
     * 3 -> chomsky 3 type left-regular
     *
     * 4 -> chomsky 3 type right-regular
     *
     * 5 -> chomsky 1 type with extension
     *
     * 6 -> chomsky 2 type with extension
     */
    static int determineGrammarType(String[] productions, Character[] nonterminals, Character[] terminals) {
        int chomskyType = -1;
        boolean isExtended = false;
        boolean isLeft = true;
        boolean is2Or3 = true;

        ArrayList<Character> vns = new ArrayList<>(Arrays.asList(nonterminals));
        ArrayList<Character> vts = new ArrayList<>(Arrays.asList(terminals));

        //首先判断是0，1文法还是2，3文法
        for(String production : productions) {
            String left = production.split("::=")[0].trim();

            if(left.length()!=1 || !vns.contains(left.charAt(0))) {
                is2Or3 = false;
                break;
            }
        }

        if(is2Or3) {
            //用于存放所有的右部
            ArrayList<String> rightElements = new ArrayList<>();

            for(String production : productions) {
                String right = production.split("::=")[1];
                String[] strings = right.split("\\|");

                for(String string : strings) {
                    rightElements.add(string.trim());
                }
            }

            //遍历右部的各个产生式，看是否是只有一个终结符号或者是一个终结符号与一个非终结符号，且两个符号顺序要相同
            boolean isFirstTimeToChangeIsLeft = true; //用来保证isLeft只可以修改一次
            for(String string : rightElements) {
                //如果长度不是1或者2，或者含有epsilon
                if((string.length()!=1 && string.length()!=2) || string.contains("#")) {
                    chomskyType = 2;
                    break;
                }

                //如果长度是1，但是不在终结符号中
                if(string.length()==1 && !vts.contains(string.charAt(0))) {
                    chomskyType = 2;
                    break;
                }

                //如果长度是2
                if(string.length()==2) {
                    //如果两个符号同时在vt或者vn中
                    if( (vns.contains(string.charAt(0)) && vns.contains(string.charAt(1))) || (vts.contains(string.charAt(0))&&vts.contains(string.charAt(1))) ) {
                        chomskyType = 2;
                        break;
                    }

                    //否则判断和之前的顺序是否相同
                    if(vns.contains(string.charAt(0)) && vts.contains(string.charAt(1))) { //Ab
                        if(isFirstTimeToChangeIsLeft) {
                            isLeft = true;
                            isFirstTimeToChangeIsLeft = false;
                            continue;
                        } else if(!isLeft) {
                            chomskyType = 2;
                            break;
                        }
                    } else { //aB
                        if(isFirstTimeToChangeIsLeft) {
                            isLeft = false;
                            isFirstTimeToChangeIsLeft = false;
                            continue;
                        } else if(isLeft){
                            chomskyType = 2;
                            break;
                        }
                    }
                }
                chomskyType = 3;
            }
        } else {
            for(String production : productions) {
                String[] leftAndRight = production.split("::=");

                String[] rightElements = leftAndRight[1].split("\\|");
                for(String element : rightElements) {
                    if(leftAndRight[0].trim().length() > element.trim().length()) {
                        chomskyType=0;
                        break;
                    }
                }

                if(chomskyType==0)
                    break;

                chomskyType=1;
            }
        }

        for(String production : productions) {
            if(production.contains("#")) {
                isExtended = true;
                break;
            }
        }

        switch (chomskyType) {
            case 0:
                return 0;

            case 1:
                if(isExtended)
                    return 5;
                return 1;

            case 2:
                if(isExtended)
                    return 6;
                return 2;

            case 3:
                if(isLeft)
                    return 3;
                return 4;
        }

        return -1;
    }
}
