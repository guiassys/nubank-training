public class Main {

    public static void main(String[] args) {

        System.out.println("\n\nTeste case 01");
        WalletService wallet = new WalletService();

        System.out.println("Criando A: "+ wallet.create("A"));
        System.out.println("Criando B: " +wallet.create("B"));

        System.out.println("Criando A novamente: "+ wallet.create("A"));
        System.out.println("Criando B novamente: " +wallet.create("B"));

        System.out.println("Deposita 100 na conta A: " +wallet.deposit("A", 100));
        wallet.printAccounts();

        System.out.println("Deposita 150 na conta B: " +wallet.deposit("B", 150));
        wallet.printAccounts();

        System.out.println("Transfere 50 da conta A para conta B: " +wallet.transfer("A", "B", 50));
        wallet.printAccounts();

        System.out.println("Transfere 100 da conta A para conta B: " +wallet.transfer("A", "B", 100));
        wallet.printAccounts();


        // Teste case 02
        System.out.println("\n\nTeste case 02");
        wallet = new WalletService();

        System.out.println("Criando A: "+ wallet.create("A"));
        System.out.println("Criando B: "+ wallet.create("B"));

        System.out.println("Deposita 100 na conta A: " +wallet.deposit("A", 100));
        wallet.printAccounts();

        System.out.println("Transfere 50 da conta A para conta B: " +wallet.transfer("A", "B", 50));
        wallet.printAccounts();

        System.out.println("Transfere 100 da conta A para conta B: " +wallet.transfer("A", "B", 100));
        wallet.printAccounts();

        System.out.println("Transfere -10 da conta A para conta B: " +wallet.transfer("A", "B", -10));
        wallet.printAccounts();

        System.out.println("Transfere 10 da conta A para conta B: " +wallet.transfer("A", "A", 10));
        wallet.printAccounts();

        System.out.println("Transfere 10 da conta X para conta B: " +wallet.transfer("X", "B", 10));
        wallet.printAccounts();

        System.out.println("Transfere 10 da conta A para conta X: " +wallet.transfer("A", "X", 10));
        wallet.printAccounts();

        // Teste case 03
        System.out.println("\n\nTeste case 03");
        wallet = new WalletService();

        System.out.println("Criando A: "+ wallet.create("A"));
        System.out.println("Criando B: "+ wallet.create("B"));

        System.out.println("Deposita 100 na conta A: " +wallet.deposit("A", 100));
        System.out.println("Saldo conta A: " + wallet.balance("A") );
        System.out.println("Saldo conta B: " + wallet.balance("B") );
        System.out.println("Saldo conta X: " + wallet.balance("X") );






    }


}