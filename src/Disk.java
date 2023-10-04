public class Disk {
    private DiskColor color;

    public Disk(DiskColor color) {
        this.color = color;
    }

    public DiskColor getColor() {
        return color;
    }

    public void flip() {
        if (color == DiskColor.BLACK) {
            color = DiskColor.WHITE;
        } else {
            color = DiskColor.BLACK;
        }
    }

    @Override
    public String toString() {
        return color.name();
    }

    public static void main(String[] args) {
        // Test
        Disk disk = new Disk(DiskColor.BLACK);
        System.out.println(disk); // BLACK
        disk.flip();
        System.out.println(disk); // WHITE
    }
}