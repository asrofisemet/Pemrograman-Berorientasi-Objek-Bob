// Class House sebagai blueprint rumah
class House {
    int type;
    double price;

    // Constructor untuk menginisialisasi atribut
    public House(int type, double price) {
        this.type = type;
        this.price = price;
    }

    // Method untuk menghitung PBB sesuai logika if-else di soal
    public double calculatePBB() {
        if (this.type == 36) {
            return 0.04 * this.price;
        } else if (this.type == 45) {
            return 0.06 * this.price;
        } else if (this.type > 45) {
            return 0.09 * this.price;
        } else {
            return 0.0;
        }
    }
}

// Class Father sebagai blueprint ayah
class Father {
    String name;
    House house; // Ini yang dimaksud di soal: "Father (attribute: a House)"

    // Constructor untuk Father
    public Father(String name, House house) {
        this.name = name;
        this.house = house;
    }
}

// Class Utama (Harus sama dengan nama file: exerciseA.java)
public class exerciseA {
    public static void main(String[] args) {
        // Karena harga (price) tidak dituliskan di soal, kita buat harga fiktif
        double hargaTipe36 = 150000000;
        double hargaTipe45 = 250000000;
        double hargaTipe90 = 600000000;

        // 1. Membuat objek House (Blueprint House -> Object)
        House akbarsHouse = new House(36, hargaTipe36);
        House bambangsHouse = new House(45, hargaTipe45);
        House charliesHouse = new House(90, hargaTipe90);

        // 2. Membuat objek Father dan menghubungkannya dengan objek House
        Father akbar = new Father("Akbar", akbarsHouse);
        Father bambang = new Father("Bambang", bambangsHouse);
        Father charlie = new Father("Charlie", charliesHouse);

        // 3. Menghitung dan mencetak PBB untuk masing-masing ayah
        System.out.println("=== Perhitungan Pajak Rumah (PBB) ===");
        
        System.out.printf("PBB milik %s (Rumah Tipe %d): Rp %.0f\n", 
                          akbar.name, akbar.house.type, akbar.house.calculatePBB());
                          
        System.out.printf("PBB milik %s (Rumah Tipe %d): Rp %.0f\n", 
                          bambang.name, bambang.house.type, bambang.house.calculatePBB());
                          
        System.out.printf("PBB milik %s (Rumah Tipe %d): Rp %.0f\n", 
                          charlie.name, charlie.house.type, charlie.house.calculatePBB());
    }
}