class Bank {
    // Menggunakan private untuk enkapsulasi data saldo
    private int balance;

    // Constructor untuk inisialisasi saldo awal
    public Bank(int balance) {
        this.balance = balance;
    }

    // Method untuk menambah saldo
    public void deposit(int amount) {
        this.balance += amount;
    }

    // Method untuk menarik saldo
    public void withdraw(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            System.out.println("Saldo tidak mencukupi.");
        }
    }

    // Method untuk mendapatkan informasi saldo saat ini
    public int getBalance() {
        return this.balance;
    }
}

public class BankDemo {
    public static void main(String[] args) {
       
        Bank myBank = new Bank(100000);

        System.out.println("Welcome to Bank BOB");
        System.out.println("Current balance: Rp " + myBank.getBalance());
        System.out.println();

        // Deposit
        int depositAmount = 250000;
        myBank.deposit(depositAmount);
        System.out.println("Deposit: Rp " + depositAmount);
        System.out.println("Current balance: Rp " + myBank.getBalance());
        System.out.println();

        // Withdraw
        int withdrawAmount = 300000;
        myBank.withdraw(withdrawAmount);
        System.out.println("Withdraw: Rp " + withdrawAmount);
        System.out.println("Current balance: Rp " + myBank.getBalance());
    }
}