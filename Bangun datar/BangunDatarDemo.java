public class BangunDatarDemo {
    public static void main(String[] args) {
        // square -> menggunakan constructor side
        BangunDatar square = new BangunDatar(7);

        // rectangle1 -> menggunakan default constructor, lalu setter
        BangunDatar rectangle1 = new BangunDatar();
        rectangle1.setLength(10);
        rectangle1.setWidth(5);

        // rectangle2 -> menggunakan constructor length & width
        BangunDatar rectangle2 = new BangunDatar(12, 4);

        // Output deskriptif
        System.out.printf("Area of square with side %.0f = %.0f%n",
                square.getSide(), square.calculateSquareArea());
        System.out.printf("Perimeter of square with side %.0f = %.0f%n",
                square.getSide(), square.calculateSquarePerimeter());

        System.out.printf("Area of rectangle with length %.0f and width %.0f = %.0f%n",
                rectangle1.getLength(), rectangle1.getWidth(), rectangle1.calculateRectangleArea());
        System.out.printf("Perimeter of rectangle with length %.0f and width %.0f = %.0f%n",
                rectangle1.getLength(), rectangle1.getWidth(), rectangle1.calculateRectanglePerimeter());

        System.out.printf("Area of rectangle with length %.0f and width %.0f = %.0f%n",
                rectangle2.getLength(), rectangle2.getWidth(), rectangle2.calculateRectangleArea());
        System.out.printf("Perimeter of rectangle with length %.0f and width %.0f = %.0f%n",
                rectangle2.getLength(), rectangle2.getWidth(), rectangle2.calculateRectanglePerimeter());
    }
}
