public class BangunRuang {
    private double side;   // untuk cube
    private double length; // untuk box
    private double width;  // untuk box
    private double height; // untuk box
    private double radius; // untuk sphere

    // Default constructor
    public BangunRuang() {
        this.side = 0;
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.radius = 0;
    }

    // Constructor dengan side (cube)
    public BangunRuang(double side) {
        this.side = side;
    }

    // Constructor dengan radius (sphere)
    // Catatan: pakai flag boolean supaya tidak bentrok tanda tangan dengan constructor side
    public BangunRuang(double radius, boolean isSphere) {
        this.radius = radius;
    }

    // Constructor dengan length, width & height (box)
    public BangunRuang(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
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

    public double getHeight() {
        return height;
    }

    public double getRadius() {
        return radius;
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

    public void setHeight(double height) {
        this.height = height;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    // Surface area methods
    public double calculateCubeSurfaceArea() {
        return 6 * side * side;
    }

    public double calculateBoxSurfaceArea() {
        return 2 * (length * width + length * height + width * height);
    }

    public double calculateSphereSurfaceArea() {
        return 4 * Math.PI * radius * radius;
    }

    // Volume methods
    public double calculateCubeVolume() {
        return side * side * side;
    }

    public double calculateBoxVolume() {
        return length * width * height;
    }

    public double calculateSphereVolume() {
        return (4.0 / 3.0) * Math.PI * radius * radius * radius;
    }
}
