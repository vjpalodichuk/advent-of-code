package com.capital7software.aoc2015.lib.analysis;

import java.util.List;
import java.util.Objects;

public record AuntSueIdentifier(List<AuntSue> auntSues) {
    public static AuntSueIdentifier parse(List<String> input) {
        return new AuntSueIdentifier(input.stream().map(AuntSue::parse).filter(Objects::nonNull).toList());
    }

    public List<AuntSue> identifySoftSuspects(AuntSue target) {
        return auntSues.stream()
                .filter(it -> it.children() == null || target.children().equals(it.children()))
                .filter(it -> it.cats() == null || target.cats().equals(it.cats()))
                .filter(it -> it.samoyeds() == null || target.samoyeds().equals(it.samoyeds()))
                .filter(it -> it.pomeranians() == null || target.pomeranians().equals(it.pomeranians()))
                .filter(it -> it.akitas() == null || target.akitas().equals(it.akitas()))
                .filter(it -> it.vizslas() == null || target.vizslas().equals(it.vizslas()))
                .filter(it -> it.goldfish() == null || target.goldfish().equals(it.goldfish()))
                .filter(it -> it.trees() == null || target.trees().equals(it.trees()))
                .filter(it -> it.cars() == null || target.cars().equals(it.cars()))
                .filter(it -> it.perfumes() == null || target.perfumes().equals(it.perfumes()))
                .toList();
    }

    public List<AuntSue> identifyHardSuspects(AuntSue target) {
        return auntSues.stream()
                .filter(it -> it.children() == null || target.children().equals(it.children()))
                .filter(it -> it.cats() == null || target.cats().compareTo(it.cats()) < 0)
                .filter(it -> it.samoyeds() == null || target.samoyeds().equals(it.samoyeds()))
                .filter(it -> it.pomeranians() == null || target.pomeranians().compareTo(it.pomeranians()) > 0)
                .filter(it -> it.akitas() == null || target.akitas().equals(it.akitas()))
                .filter(it -> it.vizslas() == null || target.vizslas().equals(it.vizslas()))
                .filter(it -> it.goldfish() == null || target.goldfish().compareTo(it.goldfish()) > 0)
                .filter(it -> it.trees() == null || target.trees().compareTo(it.trees()) < 0)
                .filter(it -> it.cars() == null || target.cars().equals(it.cars()))
                .filter(it -> it.perfumes() == null || target.perfumes().equals(it.perfumes()))
                .toList();
    }
}
