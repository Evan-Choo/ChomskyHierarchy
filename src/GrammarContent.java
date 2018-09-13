public class GrammarContent {
    private Character[] vns;       //非终结符号集合
    private Character[] vts;       //终结符号集合
    private String[] productions;  //产生式集合
    private Character start;       //开始符号
    private int chomskyType;       //chomsky类型
    private boolean isExtended;    //是否是拓展的文法
    private boolean isLeft = true; //是否是左线性文法

    //constructor

    GrammarContent() {

    }

    public GrammarContent(Character[] vns, Character[] vts, String[] productions, Character start, int chomskyType, boolean isExtended) {
        this.vns = vns;
        this.vts = vts;
        this.productions = productions;
        this.start = start;
        this.chomskyType = chomskyType;
        this.isExtended = isExtended;
    }

    //getters

    public Character[] getVns() {
        return vns;
    }

    public Character[] getVts() {
        return vts;
    }

    public String[] getProductions() {
        return productions;
    }

    public Character getStart() {
        return start;
    }

    public int getChomskyType() {
        return chomskyType;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public boolean isLeft() {
        return isLeft;
    }

    //setters

    void setVns(Character[] vns) {
        this.vns = vns;
    }

    void setVts(Character[] vts) {
        this.vts = vts;
    }

    void setProductions(String[] productions) {
        this.productions = productions;
    }

    void setStart(Character start) {
        this.start = start;
    }

    void setChomskyType(int chomskyType) {
        this.chomskyType = chomskyType;
    }

    void setExtended(boolean extended) {
        isExtended = extended;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    //todo: implement this
    void print() {
        System.out.println("文法G["+start+"]=("+printNonterminals()+", "+printTerminals()+", P, "+start+")");
        printProductions();
        System.out.println("该文法是Chomsky"+printType()+"型文法");

    }

    private String printNonterminals() {
        if(vns.length == 1)
            return "{"+vns[0]+"}";

        String result = "{";
        for(int i=0; i<vns.length; i++) {
            if(i==vns.length-1)
                result+=(vns[i]+"}");
            else
                result+=vns[i]+", ";
        }
        return result;
    }

    private String printTerminals() {
        if(vts.length == 1)
            return "{"+vts[0]+"}";

        String result = "{";
        for(int i=0; i<vts.length; i++) {
            if(i==vts.length-1)
                result+=(vts[i]+"}");
            else
                result+=vts[i]+", ";
        }
        return result;
    }

    private void printProductions() {
        System.out.println("P:");
        for(String production : productions)
            System.out.println("    "+production);
    }

    private String printType() {
        switch (chomskyType) {
            case 0:
                return "0";

            case 1:
                return "1";

            case 2:
                return "2";

            case 3:
                return "3 左线性";

            case 4:
                return "3 右线性";

            case 5:
                return "3";

            case 6:
                return "2 拓展";

            case 7:
                return "3 左线性拓展";

            case 8:
                return "3 右线性拓展";
        }

        return null;
    }
}
