public class BangunDatar {
    private double side;   // untuk square
    private double length; // untuk rectangle
    private double width;  // untuk rectangle
    private double height; // untuk prism
    private double radius; // untuk circle
    
    // Default constructor
    public BangunDatar() {
        this.side = 0;
        this.length = 0;
        this.width = 0;
    }

    // Constructor dengan side (square)
    public BangunDatar(double side) {
        this.side = side;
    }

    // Constructor dengan length & width (rectangle)
    public BangunDatar(double length, double width, double height, double radius) {
        this.length = length;
        this.width = width; 
        this.height = height;
        this.radius = radius;
    }

    // Accessor
    public double getSide() {
        return side;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    // Mutator
    public void setSide(double side) {
        this.side = side;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    // Methods untuk square
    public double calculateSquareArea() {
        return side * side;
    }

    public double calculateSquarePerimeter() {
        return 4 * side;
    }

    // Methods untuk rectangle
    public double calculateRectangleArea() {
        return length * width;
    }

    public double calculateRectanglePerimeter() {
        return 2 * (length + width);
    }
}
