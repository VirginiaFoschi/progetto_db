package model;

import java.util.Objects;

public class Theater {
    
    private final int id;
    private final Boolean type3D;
    private final int surface;
    private final int capacity;
    
    public Theater(int id, Boolean type3D, int surface, int capacity) {
        this.id = id;
        this.type3D = type3D;
        this.surface = surface;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public Boolean getType() {
        return type3D;
    }

    public int getSurface() {
        return surface;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Theater [id=" + id + ", type3D=" + type3D + ", surface=" + surface + ", capacity=" + capacity + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,type3D,surface,capacity);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Theater)
                && ((Theater) other).getId() == this.getId()
                && ((Theater) other).getType().equals(this.getType())
                && ((Theater) other).getSurface() == this.getSurface()
                && ((Theater) other).getCapacity() == this.getCapacity();
    }

    
    
}