package com.capital7software.aoc.lib.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Contains a list of all known Aunt Sues and their various properties. This record can be used to
 * identify which Aunt Sue to send a thank-you card to based on the output of the
 * My First Crime Scene Analysis Machine (MFCSAM) that one of the Aunt Sues gave you as a gift.
 *
 * @param auntSues The list of all known Aunt Sues.
 */
public record AuntSueIdentifier(@NotNull List<AuntSue> auntSues) {

    public AuntSueIdentifier(@NotNull List<AuntSue> auntSues) {
        this.auntSues = new ArrayList<>(auntSues);
    }

    @Override
    public List<AuntSue> auntSues() {
        return Collections.unmodifiableList(auntSues);
    }

    /**
     * Properties that various Aunt Sues have.
     *
     * @param id The ID in that uniquely identifies this Aunt Sue.
     * @param children The number of children she may have; may be null if unknown.
     * @param cats The number of cats she may have; may be null if unknown.
     * @param samoyeds The number of samayeds she may have; may be null if unknown.
     * @param pomeranians The number of pomeranians she may have; may be null if unknown.
     * @param akitas The number of akitas she may have; may be null if unknown.
     * @param vizslas The number of vixslas she may have; may be null if unknown.
     * @param goldfish The number of goldfish she may have; may be null if unknown.
     * @param trees The number of trees she may have; may be null if unknown.
     * @param cars The number of cars she may own; may be null if unknown.
     * @param perfumes The number of perfumes she may be wearing; may be null if unknown.
     */
    public record AuntSue(
            @NotNull Integer id,
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
        /**
         * Parses the input and creates an Aunt Sue instance.
         * <p>
         * The input must be in this format:
         * <p>
         * <ul>
         *     <li>
         *         Sue 1: goldfish: 9, cars: 0, samoyeds: 9
         *     </li>
         *     <li>
         *         Sue 9: cats: 4, pomeranians: 0, trees: 0
         *     </li>
         * </ul>
         * <p>
         * Only known properties should be specified. If a property not supported by this record is
         * listed an exception is thrown.
         *
         * @param input The input string to parse. Must be in the above format.
         * @return A new Aunt Sue instance with the values from the specified input.
         */
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
