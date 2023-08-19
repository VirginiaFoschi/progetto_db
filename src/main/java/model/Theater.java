package model;

import java.util.Optional;

public class Theater {
    
    private final int id;
    private final Boolean type3D;
    private final Optional<Integer> surface;
    private final int capacity;
    
    public Theater(int id, Boolean type3D, Optional<Integer> surface, int capacity) {
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

    public Optional<Integer> getSurface() {
        return surface;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((type3D == null) ? 0 : type3D.hashCode());
        result = prime * result + ((surface == null) ? 0 : surface.hashCode());
        result = prime * result + capacity;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Theater other = (Theater) obj;
        if (id != other.id)
            return false;
        if (type3D == null) {
            if (other.type3D != null)
                return false;
        } else if (!type3D.equals(other.type3D))
            return false;
        if (surface == null) {
            if (other.surface != null)
                return false;
        } else if (!surface.equals(other.surface))
            return false;
        if (capacity != other.capacity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Theater [id=" + id + ", type3D=" + type3D + ", surface=" + surface + ", capacity=" + capacity + "]";
    }
    
}