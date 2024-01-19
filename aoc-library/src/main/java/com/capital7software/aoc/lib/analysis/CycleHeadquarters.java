package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Modules communicate using pulses. Each pulse is either a high pulse or a low pulse. When a module
 * sends a pulse, it sends that type of pulse to each module in its list of destination modules.
 * <p><br>
 * There are several different types of modules:
 * <p><br>
 * Flip-flop modules (prefix %) are either on or off; they are initially off. If a flip-flop module
 * receives a high pulse, it is ignored and nothing happens. However, if a flip-flop module receives
 * a low pulse, it flips between on and off. If it was off, it turns on and sends a high pulse.
 * If it was on, it turns off and sends a low pulse.
 * <p><br>
 * Conjunction modules (prefix &#38;) remember the type of the most recent pulse received from each of
 * their connected input modules; they initially default to remembering a low pulse for each input.
 * When a pulse is received, the conjunction module first updates its memory for that input. Then,
 * if it remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.
 * <p><br>
 * There is a single broadcast module (named broadcaster). When it receives a pulse, it sends the
 * same pulse to all of its destination modules.
 * <p><br>
 * Here at Desert Machine Headquarters, there is a module with a single button on it called, aptly,
 * the button module. When you push the button, a single low pulse is sent directly to the broadcaster module.
 * <p><br>
 * After pushing the button, you must wait until all pulses have been delivered and fully handled
 * before pushing it again. Never push the button if modules are still processing pulses.
 * <p><br>
 * Pulses are always processed in the order they are sent. So, if a pulse is sent to modules a, b,
 * and c, and then module a processes its pulse and sends more pulses, the pulses sent to modules
 * b and c would have to be handled first.
 * <p><br>
 * The module configuration (your puzzle input) lists each module. The name of the module is preceded
 * by a symbol identifying its type, if any. The name is then followed by an arrow and a list of
 * its destination modules. For example:
 * <p><br>
 * <code>
 * broadcaster -> a, b, c
 * %a -> b
 * %b -> c
 * %c -> inv
 * &#38;inv -> a
 * </code>
 * <p><br>
 * In this module configuration, the broadcaster has three destination modules named a, b, and c.
 * Each of these modules is a flip-flop module (as indicated by the % prefix). a outputs to b which
 * outputs to c which outputs to another module named inv. inv is a conjunction module
 * (as indicated by the &#38; prefix) which, because it has only one input, acts like an inverter
 * (it sends the opposite of the pulse type it receives); it outputs to a.
 * <p><br>
 * By pushing the button once, the following pulses are sent:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * broadcaster -low-> b
 * broadcaster -low-> c
 * a -high-> b
 * b -high-> c
 * c -high-> inv
 * inv -low-> a
 * a -low-> b
 * b -low-> c
 * c -low-> inv
 * inv -high-> a
 * </code>
 * <p><br>
 * After this sequence, the flip-flop modules all end up off, so pushing the button again repeats
 * the same sequence.
 * <p><br>
 * Here's a more interesting example:
 * <p><br>
 * <code>
 * broadcaster -> a
 * %a -> inv, con
 * &#38;inv -> b
 * %b -> con
 * &#38;con -> output
 * </code>
 * <p><br>
 * This module configuration includes the broadcaster, two flip-flops (named a and b), a single-input
 * conjunction module (inv), a multi-input conjunction module (con), and an untyped module named
 * output (for testing purposes). The multi-input conjunction module con watches the two flip-flop
 * modules and, if they're both on, sends a low pulse to the output module.
 * <p><br>
 * Here's what happens if you push the button once:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -high-> inv
 * a -high-> con
 * inv -low-> b
 * con -high-> output
 * b -high-> con
 * con -low-> output
 * </code>
 * <p><br>
 * Both flip-flops turn on and a low pulse is sent to output! However, now that both flip-flops
 * are on and con remembers a high pulse from each of its two inputs, pushing the button a second
 * time does something different:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -low-> inv
 * a -low-> con
 * inv -high-> b
 * con -high-> output
 * </code>
 * <p><br>
 * Flip-flop a turns off! Now, con remembers a low pulse from module a, and so it sends only a
 * high pulse to output.
 * <p><br>
 * Push the button a third time:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -high-> inv
 * a -high-> con
 * inv -low-> b
 * con -low-> output
 * b -low-> con
 * con -high-> output
 * </code>
 * <p><br>
 * This time, flip-flop a turns on, then flip-flop b turns off. However, before b can turn off, the
 * pulse sent to con is handled first, so it briefly remembers all high pulses for its inputs and
 * sends a low pulse to output. After that, flip-flop b turns off, which causes con to update its
 * state and send a high pulse to output.
 * <p><br>
 * Finally, with a on and b off, push the button a fourth time:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -low-> inv
 * a -low-> con
 * inv -high-> b
 * con -high-> output
 * </code>
 * <p><br>
 * This completes the cycle: a turns off, causing con to remember only low pulses and restoring all
 * modules to their original states.
 * <p><br>
 * To get the cables warmed up, the Elves have pushed the button 1000 times. How many pulses got sent as
 * a result (including the pulses sent by the button itself)?
 * <p><br>
 * In the first example, the same thing happens every time the button is pushed: 8 low pulses and 4 high
 * pulses are sent. So, after pushing the button 1000 times, 8000 low pulses and 4000 high pulses are sent.
 * Multiplying these together gives 32000000.
 * <p><br>
 * In the second example, after pushing the button 1000 times, 4250 low pulses and 2750 high pulses are
 * sent. Multiplying these together gives 11687500.
 * <p><br>
 * Consult your module configuration; determine the number of low pulses and high pulses that would be
 * sent after pushing the button 1000 times, waiting for all pulses to be fully handled after each push
 * of the button. What do you get if you multiply the total number of low pulses sent by the total
 * number of high pulses sent?
 * <p><br>
 * The final machine responsible for moving the sand down to Island Island has a module attached named rx.
 * The machine turns on when a single low pulse is sent to rx.
 * <p><br>
 * Reset all modules to their default states. Waiting for all pulses to be fully handled after each button
 * press, what is the fewest number of button presses required to deliver a single low pulse to the module
 * named rx?
 */
public class CycleHeadquarters {
    private enum PulseType {
        HIGH,
        LOW
    }

    private record Pulse(PulseType pulseType, Instant sendTime) implements Comparable<Pulse> {
        @Override
        public int compareTo(Pulse o) {
            return sendTime.compareTo(o.sendTime);
        }
    }

    private enum ModuleState {
        ON,
        OFF
    }

    private interface Module {

        default void receive(Module sender, Pulse pulse) {
        }

        void addDestination(Module module);

        void addSource(Module module);

        Map.Entry<Module, Pulse> processNextPulse();

        boolean hasPulsesToProcess();

        String getId();

        Instant getTimeOfEarliestPulseToProcess();

        List<Module> getSourceModules();

        default void reset() {
        }

        long getSentPulseTypeCount(PulseType pulseType);

    }

    private abstract static class BaseModule implements Module, Comparable<Module> {
        protected List<Module> destinations;
        protected List<Module> sources;
        protected Map<String, Pulse> lastPulse;
        protected ModuleState state;
        protected final String id;
        protected Queue<Map.Entry<Module, Pulse>> pulsesToProcess;
        protected Map<PulseType, Long> sent;

        protected BaseModule(String id) {
            this.id = id;
            this.destinations = new LinkedList<>();
            this.sources = new LinkedList<>();
            this.lastPulse = new LinkedHashMap<>();
            this.state = ModuleState.OFF;
            this.sent = new HashMap<>();
            this.pulsesToProcess = new PriorityQueue<>(Map.Entry.comparingByValue());
        }

        @Override
        public String getId() {
            return id;
        }

        public void setState(ModuleState state) {
            this.state = state;
        }

        @Override
        public void addDestination(Module module) {
            if (module == null || destinations.contains(module)) {
                return;
            }

            module.addSource(this);

            destinations.add(module);
        }

        @Override
        public void addSource(Module module) {
            if (module == null || sources.contains(module)) {
                return;
            }

            sources.add(module);
            lastPulse.put(module.getId(), new Pulse(PulseType.LOW, Instant.now()));
        }

        protected void send(Pulse pulse) {
            if (pulse == null) {
                throw new RuntimeException("Cannot send a null pulse!");
            }

            if (destinations.size() == 1) {
                destinations.forEach(module -> module.receive(this, pulse));
            } else {
                destinations.forEach(module -> {
                    // Ensure that pulses are sent and processed in order!!
                    var newPulse = new Pulse(pulse.pulseType(), Instant.now());
                    module.receive(this, newPulse);
                });
            }
            var count = sent.computeIfAbsent(pulse.pulseType(), it -> 0L);
            sent.put(pulse.pulseType(), count + destinations.size());
        }

        @Override
        public void receive(Module sender, Pulse pulse) {
            pulsesToProcess.offer(new AbstractMap.SimpleEntry<>(sender, pulse));
        }

        @Override
        public Map.Entry<Module, Pulse> processNextPulse() {
            var entry = pulsesToProcess.poll();

            if (entry == null) {
                throw new RuntimeException("processNextPulse called with no pulses waiting to be processed!");
            }
            lastPulse.put(entry.getKey().getId(), entry.getValue());

            return entry;
        }

        @Override
        public boolean hasPulsesToProcess() {
            return !pulsesToProcess.isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BaseModule that)) {
                return false;
            }
            return getId().equals(that.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        @Override
        public Instant getTimeOfEarliestPulseToProcess() {
            var pending = pulsesToProcess.peek();

            if (pending == null) {
                return Instant.MAX;
            } else {
                return pending.getValue().sendTime();
            }
        }

        @Override
        public int compareTo(Module o) {
            return getTimeOfEarliestPulseToProcess().compareTo(o.getTimeOfEarliestPulseToProcess());
        }

        @Override
        public String toString() {
            return "BaseModule{" +
                    ", id='" + id + '\'' +
                    ", pulsesToProcess=" + pulsesToProcess +
                    ", sent=" + sent +
                    '}';
        }

        @Override
        public List<Module> getSourceModules() {
            return new LinkedList<>(sources);
        }

        @Override
        public void reset() {
            state = ModuleState.OFF;
            lastPulse.entrySet().forEach(entry -> entry.setValue(new Pulse(PulseType.LOW, Instant.MIN)));
        }

        @Override
        public long getSentPulseTypeCount(PulseType pulseType) {
            return sent.computeIfAbsent(pulseType, it -> 0L);
        }

        protected boolean isOn() {
            return state == ModuleState.ON;
        }

        protected boolean isOff() {
            return state == ModuleState.OFF;
        }

    }

    private static class UntypedModule extends BaseModule {
        public UntypedModule(String id) {
            super(id);
        }
    }

    private static class FlipFlopModule extends BaseModule {
        protected FlipFlopModule(String id) {
            super(id);
        }

        @Override
        public Map.Entry<Module, Pulse> processNextPulse() {
            var entry = super.processNextPulse();
            var pulse = entry.getValue();

            if (pulse.pulseType() == PulseType.LOW) {
                if (isOff()) {
                    setState(ModuleState.ON);
                    send(new Pulse(PulseType.HIGH, Instant.now()));
                } else {
                    setState(ModuleState.OFF);
                    send(new Pulse(PulseType.LOW, Instant.now()));
                }
            }
            return entry;
        }
    }

    private static class ConjunctionModule extends BaseModule {
        public ConjunctionModule(String id) {
            super(id);
        }

        @Override
        public Map.Entry<Module, Pulse> processNextPulse() {
            var entry = super.processNextPulse();

            if (lastPulse.values().stream().allMatch(it -> it.pulseType() == PulseType.HIGH)) {
                if (isOff()) {
                    setState(ModuleState.ON);
                }
                send(new Pulse(PulseType.LOW, Instant.now()));
            } else {
                if (isOn()) {
                    setState(ModuleState.OFF);
                }
                send(new Pulse(PulseType.HIGH, Instant.now()));
            }

            return entry;
        }
    }

    private static class Broadcaster extends BaseModule {
        public Broadcaster(String id) {
            super(id);
            setState(ModuleState.ON);
        }

        @Override
        public Map.Entry<Module, Pulse> processNextPulse() {
            if (isOff()) {
                return new AbstractMap.SimpleEntry<>(null, null);
            }

            var entry = super.processNextPulse();
            var pulse = entry.getValue();

            send(pulse);
            return entry;
        }

        @Override
        public void reset() {
            super.reset();
            setState(ModuleState.ON);
        }
    }

    private static class Button extends BaseModule {
        public Button(String id) {
            super(id);
            setState(ModuleState.ON);
        }

        public void push() {
            if (isOff()) {
                return;
            }

            send(new Pulse(PulseType.LOW, Instant.now()));
        }

        @Override
        public void reset() {
            super.reset();
            setState(ModuleState.ON);
        }
    }

    private record ModuleConfiguration(Broadcaster broadcaster, @NotNull List<Module> modules) {
        public static final String BROADCASTER_ID = "broadcaster";

        public Module getModuleById(String id) {
            return modules.stream().filter(it -> it.getId().equals(id)).findAny().orElse(null);
        }

        public static ModuleConfiguration build(List<String> input) {
            // Keep track of the modules and their destination ids so that we can
            // continue to process them after loading the input List.
            var modules = new HashMap<Module, List<String>>();

            input.forEach(line -> processLine(line, modules));

            var modulesById = modules.keySet().stream().collect(Collectors.toMap(Module::getId, it -> it));

            configureSourcesAndDestinations(modules, modulesById);

            var broadcaster = (Broadcaster) modulesById.get(BROADCASTER_ID);

            var remainingModules = modules.keySet()
                    .stream()
                    .filter(it -> !it.getId().equals(BROADCASTER_ID))
                    .toList();

            return new ModuleConfiguration(broadcaster, remainingModules);
        }

        private static void configureSourcesAndDestinations(
                HashMap<Module, List<String>> modules,
                Map<String, Module> modulesById
        ) {
            // New Untyped modules
            var newModules = new LinkedList<Module>();

            modules.forEach((sourceModule, value) -> {

                for (var destination : value) {
                    var module = modulesById.get(destination);

                    if (module == null) {
                        module = new UntypedModule(destination);
                        newModules.add(module);
                        modulesById.put(destination, module);
                    }
                    sourceModule.addDestination(module);
                    module.addSource(sourceModule);
                }
            });

            for (var module : newModules) {
                modules.put(module, new LinkedList<>());
            }
        }

        private static void processLine(String line, HashMap<Module, List<String>> modules) {
            if (line == null || line.isBlank()) {
                return;
            }

            var split = line.split("->");
            var moduleType = split[0].trim();
            var moduleDestinations = split[1].trim().split(",");

            var destinations = Arrays.stream(moduleDestinations).map(String::trim).toList();

            // Process the Module type first
            if (BROADCASTER_ID.equals(moduleType)) {
                modules.put(new Broadcaster(BROADCASTER_ID), destinations);
            } else {
                var classType = moduleType.charAt(0);
                var moduleId = moduleType.substring(1);

                switch (classType) {
                    case '%' -> modules.put(new FlipFlopModule(moduleId), destinations);
                    case '&' -> modules.put(new ConjunctionModule(moduleId), destinations);
                    default -> throw new RuntimeException("Unknown module class type!");
                }
            }
        }

        public void processPulses() {
            // First process any Broadcast messages
            broadcaster.processNextPulse();

            var queue = new PriorityQueue<Module>();

            // Initially add any modules that have pulses to process
            modules.stream().filter(Module::hasPulsesToProcess).forEach(queue::add);

            while (!queue.isEmpty()) {
                var nextModule = queue.poll();

                if (nextModule.hasPulsesToProcess()) {
                    nextModule.processNextPulse();
                    // Add back any modules with pending pulses!
                    modules.stream().filter(Module::hasPulsesToProcess).forEach(queue::add);
                }
            }

            // Validate we are done!
            var modulesWithPulsesPending = modules.stream().filter(Module::hasPulsesToProcess).toList();

            if (!modulesWithPulsesPending.isEmpty()) {
                throw new RuntimeException("Processing failed as there are still modules with pending messages!");
            }
        }

        public void reset() {
            modules.forEach(Module::reset);
        }
    }

    private static final String BUTTON_ID = "button";
    private final Button button;
    private final ModuleConfiguration configuration;

    private CycleHeadquarters(Button button, ModuleConfiguration configuration) {
        this.button = button;
        this.configuration = configuration;
    }

    /**
     * Builds and returns a new CycleHeadquarters instance loaded with the specified module configuration.
     *
     * @param input The List of Strings to parse the module configuration from.
     * @return A new CycleHeadquarters instance loaded with the specified module configuration.
     */
    public static CycleHeadquarters buildCycleHeadquarters(List<String> input) {
        var configuration = ModuleConfiguration.build(input);

        var button = new Button(BUTTON_ID);
        button.addDestination(configuration.broadcaster());

        return new CycleHeadquarters(button, configuration);
    }

    /**
     * Pushes the button the specified number of times.
     *
     * @param count The number of times to push the button.
     */
    public void pushButton(long count) {
        LongStream.range(0, count)
                .forEach(iteration -> pushButton());
    }

    /**
     * Pushes the button a single time. Every time the button is pushed, it sends a low pulse to the
     * broadcast module.
     */
    public void pushButton() {
        button.push();
        configuration.processPulses();
    }

    /**
     * Returns the number of low pulses that have been sent.
     *
     * @return The number of low pulses that have been sent.
     */
    public long getLowPulseCount() {
        return getPulseTypeCount(PulseType.LOW);
    }

    /**
     * Returns the number of high pulses that have been sent.
     *
     * @return The number of high pulses that have been sent.
     */
    public long getHighPulseCount() {
        return getPulseTypeCount(PulseType.HIGH);
    }

    private long getPulseTypeCount(PulseType pulseType) {
        var count = button.getSentPulseTypeCount(pulseType);
        count += configuration.broadcaster().getSentPulseTypeCount(pulseType);

        count += configuration.modules()
                .stream()
                .mapToLong(it -> it.getSentPulseTypeCount(pulseType))
                .sum();

        return count;
    }

    /**
     * Returns the minimum number of button presses needed in order to send a low pulse to the specified
     * module. If maxButtonPresses is reached before a low pulse is sent to the specified module
     * then -1 is returned.
     *
     * @param id                  The ID of the module we want to send a low pulse to.
     * @param requiredSourceCount If the specified ID has more than one source, then how many of them have to
     *                            send a low pulse to the destination?
     * @param maxButtonPresses    The maximum number of button presses to perform.
     * @return The minimum number of button presses needed to send a low pulse to the specified module.
     * Or -1 if the maximum number of button presses was reached before a low pulse would be sent to the
     * specified module.
     */
    public long buttonPressesNeededToSendLowPulseToModule(String id, int requiredSourceCount, long maxButtonPresses) {
        if (id.equals(BUTTON_ID)) {
            return 0L; // No presses are needed since it is the button itself!
        }
        if (id.equals(ModuleConfiguration.BROADCASTER_ID)) {
            return 1L; // If it is the broadcaster then only a single button push is needed.
        }

        var targetModule = configuration.getModuleById(id);
        if (targetModule == null) {
            throw new RuntimeException("Unable to find the target module with id: " + id);
        }
        var broadcaster = configuration.broadcaster();
        if (broadcaster == null) {
            throw new RuntimeException("Unable to find broadcaster module with id: " + ModuleConfiguration.BROADCASTER_ID);
        }

        configuration.reset(); // Start from a fresh state!
        // Get the set of modules to monitor
        var watchList = getWatchList(targetModule);

        if (watchList.size() < requiredSourceCount) {
            throw new RuntimeException("The destination doesn't have the required number of sources! " +
                    "Required source count: " + requiredSourceCount + " destination source count: " + watchList.size());
        }

        // Sources that already met the done condition
        var doneSources = new HashSet<Module>(watchList.size() + 2);
        // Track the number of counts for each module in each source set
        var moduleCounts = new HashMap<Module, Map<Module, Long>>();
        // Track the number of times the button has been pressed
        var pushCount = 0L;
        var done = false;

        while (!done && pushCount < maxButtonPresses) {
            // Start by pushing the flipping button!
            pushButton();
            pushCount++;

            // Now we need to check the state of our modules in our watch list
            // If any of the watched modules have sent a HIGH pulse, then we take the
            // current button count and add that to the count map if it isn't already there.
            if (processWatchListAndUpdateCounts(watchList, moduleCounts, doneSources, pushCount)) {
                processCompletedWatchListAndUpdateDoneSources(watchList, moduleCounts, doneSources);
            }

            if (doneSources.size() >= requiredSourceCount) {
                done = true;
            }
        }

        if (!done) {
            return -1; // Reached the maximum number of button presses
        }

        var result = -2L;
        var lcms = moduleCounts.entrySet()
                .stream()
                .filter(it -> doneSources.contains(it.getKey()))
                .map(Map.Entry::getValue)
                .map(it -> it.values().stream().reduce(MathOperations::lcm).orElse(0L))
                .filter(it -> it > 0L)
                .sorted()
                .limit(requiredSourceCount)
                .toList();

        if (lcms.size() > 1) {
            result = lcms.stream().reduce(MathOperations::lcm).orElse(0L);
        } else if (lcms.size() == 1) {
            result = lcms.getFirst();
        }

        return result;
    }

    private void processCompletedWatchListAndUpdateDoneSources(
            Map<Module, Set<Module>> watchList,
            HashMap<Module, Map<Module, Long>> moduleCounts,
            HashSet<Module> doneSources
    ) {
        watchList.entrySet()
                .stream()
                .filter(it -> !doneSources.contains(it.getKey()))
                .forEach(moduleSetEntry -> {
                    if (moduleSetEntry.getValue().size() ==
                            moduleCounts.getOrDefault(moduleSetEntry.getKey(), Collections.emptyMap()).size()) {
                        doneSources.add(moduleSetEntry.getKey());
                    }
                });
    }

    private boolean processWatchListAndUpdateCounts(
            Map<Module, Set<Module>> watchList,
            HashMap<Module, Map<Module, Long>> moduleCounts,
            Set<Module> doneSources,
            long pushCount
    ) {
        var newCounts = new AtomicBoolean(false);

        watchList.entrySet()
                .stream()
                .filter(it -> !doneSources.contains(it.getKey()))
                .filter(this::containsHighPulse)
                .forEach(moduleSetEntry -> {
                    var countMap = moduleCounts.computeIfAbsent(
                            moduleSetEntry.getKey(),
                            it -> new HashMap<>(moduleSetEntry.getValue().size()));
                    moduleSetEntry
                            .getValue()
                            .stream()
                            .filter(it -> !countMap.containsKey(it))
                            .filter(it -> it.getSentPulseTypeCount(PulseType.HIGH) > 0)
                            .forEach(it -> {
                                countMap.put(it, pushCount);
                                newCounts.set(true);
                            });
                });
        return newCounts.get();
    }

    private boolean containsHighPulse(Map.Entry<Module, Set<Module>> moduleSetEntry) {
        return !moduleSetEntry.getValue().stream().filter(it -> it.getSentPulseTypeCount(PulseType.HIGH) > 0).toList().isEmpty();
    }

    private Map<Module, Set<Module>> getWatchList(Module module) {
        var targetSets = new HashMap<Module, Set<Module>>();

        module.getSourceModules()
                .forEach(source -> targetSets.computeIfAbsent(source, it -> new HashSet<>(it.getSourceModules())));

        return targetSets;
    }

    /**
     * Calculates and returns the product of the low and high pulses.
     *
     * @return The product of the low and high pulses.
     */
    public long getPulseProduct() {
        return getLowPulseCount() * getHighPulseCount();
    }
}
