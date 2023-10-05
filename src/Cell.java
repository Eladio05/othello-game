public class Cell {
    private Disk disk;

    public Cell() {
        this.disk = null; // initialement, la cellule est vide
    }

    public void setDisk(Disk d) {
        this.disk = d;
    }

    public Disk getDisk() {
        return disk;
    }

    public boolean isEmpty() {
        return disk == null;
    }

    public Cell(Cell other) {
        if (other.disk != null) {
            this.disk = new Disk(other.disk.getColor());
        } else {
            this.disk = null;
        }
    }


    @Override
    public String toString() {
        if (isEmpty()) {
            return "-";
        } else {
            if(disk.getColor() == DiskColor.BLACK){
                return "X";
            }else {
                return "0";
            }
        }
    }

    public static void main(String[] args) {
        // Test
        Cell cell = new Cell();
        System.out.println(cell); // -

        Disk disk = new Disk(DiskColor.BLACK);
        cell.setDisk(disk);
        System.out.println(cell); // BLACK
    }
}
