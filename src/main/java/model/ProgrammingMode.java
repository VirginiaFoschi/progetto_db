package model;

import java.util.Optional;

public class ProgrammingMode {

    private final String type;
    private final Optional<String> viewingExperience;
    private final String visualEffect;
    
    public ProgrammingMode(String type, Optional<String> viewingExperience, String visualEffect) {
        this.type = type;
        this.viewingExperience = viewingExperience;
        this.visualEffect = visualEffect;
    }

    public String getType() {
        return type;
    }

    public Optional<String> getViewingExperience() {
        return viewingExperience;
    }

    public String getVisualEffect() {
        return visualEffect;
    }


}