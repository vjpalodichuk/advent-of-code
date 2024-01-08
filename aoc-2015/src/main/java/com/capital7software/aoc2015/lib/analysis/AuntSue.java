package com.capital7software.aoc2015.lib.analysis;

public record AuntSue(
        Integer id,
        Integer children,
        Integer cats,
        Integer samoyeds,
        Integer pomeranians,
        Integer akitas,
        Integer vizslas,
        Integer goldfish,
        Integer trees,
        Integer cars,
        Integer perfumes
) {
    public static AuntSue parse(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        var split1 = input.split(",");
        var split2 = split1[0].split(": ");

        var id = Integer.parseInt(split2[0].split(" ")[1].trim());

        Integer children = null;
        Integer cats = null;
        Integer samoyeds = null;
        Integer pomeranians = null;
        Integer akitas = null;
        Integer vizslas = null;
        Integer goldfish = null;
        Integer trees = null;
        Integer cars = null;
        Integer perfumes = null;

        for (int i = 1; i < split2.length; i += 2) {
            switch (split2[i].trim()) {
                case "children" -> children = Integer.parseInt(split2[i + 1].trim());
                case "cats" -> cats = Integer.parseInt(split2[i + 1].trim());
                case "samoyeds" -> samoyeds = Integer.parseInt(split2[i + 1].trim());
                case "pomeranians" -> pomeranians = Integer.parseInt(split2[i + 1].trim());
                case "akitas" -> akitas = Integer.parseInt(split2[i + 1].trim());
                case "vizslas" -> vizslas = Integer.parseInt(split2[i + 1].trim());
                case "goldfish" -> goldfish = Integer.parseInt(split2[i + 1].trim());
                case "trees" -> trees = Integer.parseInt(split2[i + 1].trim());
                case "cars" -> cars = Integer.parseInt(split2[i + 1].trim());
                case "perfumes" -> perfumes = Integer.parseInt(split2[i + 1].trim());
                default -> throw new IllegalArgumentException("Unknown property parsing Aunt Sue: " + split2[i].trim());
            }
        }

        for (int i = 1; i < split1.length; i++) {
            var split3 = split1[i].trim().split(": ");

            switch (split3[0].trim()) {
                case "children" -> children = Integer.parseInt(split3[1].trim());
                case "cats" -> cats = Integer.parseInt(split3[1].trim());
                case "samoyeds" -> samoyeds = Integer.parseInt(split3[1].trim());
                case "pomeranians" -> pomeranians = Integer.parseInt(split3[1].trim());
                case "akitas" -> akitas = Integer.parseInt(split3[1].trim());
                case "vizslas" -> vizslas = Integer.parseInt(split3[1].trim());
                case "goldfish" -> goldfish = Integer.parseInt(split3[1].trim());
                case "trees" -> trees = Integer.parseInt(split3[1].trim());
                case "cars" -> cars = Integer.parseInt(split3[1].trim());
                case "perfumes" -> perfumes = Integer.parseInt(split3[1].trim());
                default -> throw new IllegalArgumentException("Unknown property parsing Aunt Sue: " + split3[0].trim());
            }
        }

        return new AuntSue(id, children, cats, samoyeds, pomeranians, akitas, vizslas, goldfish, trees, cars, perfumes);
    }
}
