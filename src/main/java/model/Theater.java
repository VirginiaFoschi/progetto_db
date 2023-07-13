package model;

import java.util.Objects;
import java.util.Optional;

public class Theater {
    
    private final int id;
    private final Optional<Boolean> type3D;
    private final int capacity;
    
    public Theater(int id, Optional<Boolean> type3D, int capacity) {
        this.id = id;
        this.type3D = type3D;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public Optional<Boolean> getType() {
        return type3D;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Theater [id=" + id + ", type3D=" + type3D + ", capacity=" + capacity + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,type3D,capacity);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Theater)
                && ((Theater) other).getId() == this.getId()
                && ((Theater) other).getType().equals(this.getType())
                && ((Theater) other).getCapacity() == this.getCapacity();
    }

    
    
}