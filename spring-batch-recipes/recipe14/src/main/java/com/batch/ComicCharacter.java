package com.batch;

public class ComicCharacter {
    private String name;
    private String universe;
    private boolean isVillain;

    public ComicCharacter(String name, String universe, boolean isVillain) {
        this.name = name;
        this.universe = universe;
        this.isVillain = isVillain;
    }

    public static ComicCharacter villain(String name, String universe) {
        return new ComicCharacter(name, universe, true);
    }

    public static ComicCharacter hero(String name, String universe){
        return new ComicCharacter(name, universe, false);
    }

    public String getName() {
        return name;
    }

    public String getUniverse() {
        return universe;
    }

    public boolean isVillain() {
        return isVillain;
    }

    @Override
    public String toString() {
        return "SuperHero{" +
                "name='" + name + '\'' +
                ", universe='" + universe + '\'' +
                ", isVillain=" + isVillain +
                '}';
    }
}
