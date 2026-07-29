package de0.backend.gartenmanagement.entities;



public enum ProductCategory {
    PLANT("Plant"),
    TOOL("Tool"),
    FERTILIZER("Fertilizer"),
    SOIL("Soil"),
    ACCESSORY("Accessory");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}