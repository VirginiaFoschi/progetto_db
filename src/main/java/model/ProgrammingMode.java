package model;

public class ProgrammingMode {

    private final String type;
    private final String viewingExperience;
    private final String visualEffect;
    
    public ProgrammingMode(String type, String viewingExperience, String visualEffect) {
        this.type = type;
        this.viewingExperience = viewingExperience;
        this.visualEffect = visualEffect;
    }

    public String getType() {
        return type;
    }

    public String getViewingExperience() {
        return viewingExperience;
    }

    public String getVisualEffect() {
        return visualEffect;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((viewingExperience == null) ? 0 : viewingExperience.hashCode());
        result = prime * result + ((visualEffect == null) ? 0 : visualEffect.hashCode());
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
        ProgrammingMode other = (ProgrammingMode) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (viewingExperience == null) {
            if (other.viewingExperience != null)
                return false;
        } else if (!viewingExperience.equals(other.viewingExperience))
            return false;
        if (visualEffect == null) {
            if (other.visualEffect != null)
                return false;
        } else if (!visualEffect.equals(other.visualEffect))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProgrammingMode [type=" + type + ", viewingExperience=" + viewingExperience + ", visualEffect="
                + visualEffect + "]";
    }

}