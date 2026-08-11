import service.WalletService;

public class Main {

    private static final String SIMBOL = "*";
    private static final String SPACE = " ";
    private static final int TOTAL_LINES = 10;
    private static final int TOTAL_SIMBOLS_BY_LINE = 20;

    public static void main(String[] args) {

        // Show one squad
        System.out.print("\n -- Show one squad --\n");
        int index = 1;
        while(index <= TOTAL_LINES){
            System.out.print("\n"+SIMBOL.repeat(TOTAL_SIMBOLS_BY_LINE));
            index++;
        }

        // Show one triangle
        System.out.print("\n -- Show one triangle --\n");
        index = 1;
        while(index <= TOTAL_LINES){
            boolean isImparNumber = index % 2 != 0;

            if(isImparNumber){
                int spaces = (TOTAL_SIMBOLS_BY_LINE - index) /2;
                System.out.print("\n"+SPACE.repeat(spaces)+ SIMBOL.repeat(index) + SPACE.repeat(spaces));
            }

            index++;
        }

    }


}