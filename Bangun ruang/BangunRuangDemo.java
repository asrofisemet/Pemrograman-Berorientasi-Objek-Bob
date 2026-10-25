public class BangunRuangDemo {
    public static void main(String[] args) {
        // cube -> menggunakan constructor side
        BangunRuang cube = new BangunRuang(4);

        // box1 -> menggunakan default constructor, lalu setter
        BangunRuang box1 = new BangunRuang();
        box1.setLength(10);
        box1.setWidth(5);
        box1.setHeight(7);

        // box2 -> menggunakan constructor length, width & height
        BangunRuang box2 = new BangunRuang(8, 3, 6);

        // sphere -> menggunakan constructor radius (pakai flag true untuk membedakan dari constructor cube)
        BangunRuang sphere = new BangunRuang(3, true);

        // Output deskriptif
        System.out.printf("Volume of box (L=%.0f, W=%.0f, H=%.0f) = %.1f%n",
                box1.getLength(), box1.getWidth(), box1.getHeight(), box1.calculateBoxVolume());
        System.out.printf("Surface area of box (L=%.0f, W=%.0f, H=%.0f) = %.1f%n",
                box1.getLength(), box1.getWidth(), box1.getHeight(), box1.calculateBoxSurfaceArea());

        System.out.printf("Volume of box (L=%.0f, W=%.0f, H=%.0f) = %.1f%n",
                box2.getLength(), box2.getWidth(), box2.getHeight(), box2.calculateBoxVolume());
        System.out.printf("Surface area of box (L=%.0f, W=%.0f, H=%.0f) = %.1f%n",
                box2.getLength(), box2.getWidth(), box2.getHeight(), box2.calculateBoxSurfaceArea());

        System.out.printf("Surface area of cube (side=%.0f) = %.1f%n",
                cube.getSide(), cube.calculateCubeSurfaceArea());
        System.out.printf("Volume of cube (side=%.0f) = %.1f%n",
                cube.getSide(), cube.calculateCubeVolume());

        System.out.printf("Volume of sphere (radius=%.0f) = %.1f%n",
                sphere.getRadius(), sphere.calculateSphereVolume());
        System.out.printf("Surface area of sphere (radius=%.0f) = %.1f%n",
                sphere.getRadius(), sphere.calculateSphereSurfaceArea());
    }
}
