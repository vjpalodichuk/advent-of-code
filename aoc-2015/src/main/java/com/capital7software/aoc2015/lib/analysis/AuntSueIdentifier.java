package com.capital7software.aoc2015.lib.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Contains a list of all known Aunt Sues and their various properties. This record can be used to
 * identify which Aunt Sue to send a thank-you card to based on the output of the
 * My First Crime Scene Analysis Machine (MFCSAM) that one of the Aunt Sues gave you as a gift.
 *
 * @param auntSues The list of all known Aunt Sues.
 */
public record AuntSueIdentifier(List<AuntSue> auntSues) {
    /**
     * Parses the list of Aunt Sues and returns a new instance of AuntSueIdentifier.
     *
     * @param input The list of Aunt Sues in String form. See the Aunt Sue class for details of the format.
     * @return A new instance of AuntSueIdentifier with all the Aunt Sues loaded into it.
     */
    public static @NotNull AuntSueIdentifier parse(@NotNull List<String> input) {
        return new AuntSueIdentifier(input.stream().map(AuntSue::parse).filter(Objects::nonNull).toList());
    }

    /**
     * Identifies and returns which Aunt Sue(s) best match the Aunt Sue properties specified in target.
     * <br><br>
     * For Example: If target contained the following properties for Aunt Sue.<br><br>
     * children: 3<br>
     * cats: 7<br>
     * samoyeds: 2<br>
     * pomeranians: 3<br>
     * akitas: 0<br>
     * vizslas: 0<br>
     * goldfish: 5<br>
     * trees: 3<br>
     * cars: 2<br>
     * perfumes: 1<br>
     * <br>
     * Then Aunt Sue 40, from the input for Day 16, would be returned.
     * <br>
     *
     * @param target The Aunt Sue instance filled with the properties from the MFCSAM readout.
     * @return A list of which Aunt Sue(s) best match the Aunt Sure properties specified in target.
     */
    public @NotNull List<AuntSue> identifySoftSuspects(@NotNull AuntSue target) {
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

    /**
     * Identifies and returns which Aunt Sue(s) best match the Aunt Sue properties specified in target. The
     * major differences between this method and the Soft method is the following: <br>
     * <ul>
     *     <li>
     *         The cats and trees readings indicates that there are greater than that many.
     *     </li>
     *     <li>
     *         The pomeranians and goldfish readings indicate that there are fewer than that many.
     *     </li>
     * </ul>
     *
     * For Example: If target contained the following properties for Aunt Sue.<br><br>
     * children: 3<br>
     * cats: 7<br>
     * samoyeds: 2<br>
     * pomeranians: 3<br>
     * akitas: 0<br>
     * vizslas: 0<br>
     * goldfish: 5<br>
     * trees: 3<br>
     * cars: 2<br>
     * perfumes: 1<br>
     * <br>
     * Then Aunt Sue 243, from the input for Day 16, would be returned.
     * <br>
     *
     * @param target The Aunt Sue instance filled with the properties from the MFCSAM readout.
     * @return A list of which Aunt Sue(s) best match the Aunt Sure properties specified in target.
     */
    public @NotNull List<AuntSue> identifyHardSuspects(@NotNull AuntSue target) {
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
