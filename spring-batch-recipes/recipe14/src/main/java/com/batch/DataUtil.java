package com.batch;

import java.util.List;

public class DataUtil {
    public static List<ComicCharacter> villainCharacters = List.of(
            ComicCharacter.villain("Whiplash", "Iron Man"),
            ComicCharacter.villain("Thanos", "Avenger"),
            ComicCharacter.villain("Alexander Pierce", "Captain America"),
            ComicCharacter.villain("Darren Cross", "AntMan"),
            ComicCharacter.villain("Dormamu", "Dr. Strange"),
            ComicCharacter.villain("Ultron", "Avenger"),
            ComicCharacter.villain("Zemo", "Captain America"),
            ComicCharacter.villain("Grandmaster", "Thor"),
            ComicCharacter.villain("Ego", "Guardians"),
            ComicCharacter.villain("Obadiah", "Iron Man"),
            ComicCharacter.villain("Hela", "Thor"),
            ComicCharacter.villain("Red Skull", "Captain America"),
            ComicCharacter.villain("KillMonger", "Black Panther")
    );
    public static List<ComicCharacter> heroCharacters = List.of(
            ComicCharacter.hero("Captain Marvel", "Avenger"),
            ComicCharacter.hero("Iron Man", "Avenger"),
            ComicCharacter.hero("Thor", "Avenger"),
            ComicCharacter.hero("SuperMan", "Justice League"),
            ComicCharacter.hero("Wonder Woman", "Justice League"),
            ComicCharacter.hero("Batman", "Justice League"),
            ComicCharacter.hero("Flash", "Justice League"),
            ComicCharacter.hero("Hulk", "Avenger"),
            ComicCharacter.hero("Black widow", "Avenger"),
            ComicCharacter.hero("Hawkeye", "Avenger"),
            ComicCharacter.hero("Loki", "Thor"),
            ComicCharacter.hero("Rocket", "Guardians"),
            ComicCharacter.hero("Groot", "Guardians"),
            ComicCharacter.hero("Gamora", "Guardians"),
            ComicCharacter.hero("Nebula", "Guardians"),
            ComicCharacter.hero("Starlord", "Guardians")
            );
}
