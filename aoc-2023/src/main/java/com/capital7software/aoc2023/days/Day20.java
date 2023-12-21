package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day20 {
    public enum PulseType {
        HIGH,
        LOW
    }

    public record Pulse(PulseType pulseType, Instant sendTime) implements Comparable<Pulse> {
        @Override
        public int compareTo(Pulse o) {
            return sendTime.compareTo(o.sendTime);
        }
    }

    public enum ModuleState {
        ON,
        OFF
    }

    public interface Module {

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

    abstract public static class BaseModule implements Module, Comparable<Module> {
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
            if (this == o) return true;
            if (!(o instanceof BaseModule that)) return false;
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

    public static class UntypedModule extends BaseModule {
        public UntypedModule(String id) {
            super(id);
        }
    }

    public static class FlipFlopModule extends BaseModule {
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

    public static class ConjunctionModule extends BaseModule {
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

    public static class Broadcaster extends BaseModule {
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

    public static class Button extends BaseModule {
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

    public static class ModuleConfiguration {
        public static final String BROADCASTER_ID = "broadcaster";

        private final Broadcaster broadcaster;
        private final List<Module> modules;

        private ModuleConfiguration(Broadcaster broadcaster, List<Module> modules) {
            this.broadcaster = broadcaster;
            this.modules = modules;
        }

        public Broadcaster getBroadcaster() {
            return broadcaster;
        }

        public List<Module> getModules() {
            return modules;
        }

        public Module getModuleById(String id) {
            return modules.stream().filter(it -> it.getId().equals(id)).findAny().orElse(null);
        }

        public static ModuleConfiguration build(Stream<String> stream) {
            // Keep track of the modules and their destination ids so that we can
            // continue to process them after loading the input stream.
            var modules = new HashMap<Module, List<String>>();

            stream.forEach(line -> processLine(line, modules));

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

    public static class Headquarters {
        public static final String BUTTON_ID = "button";
        private final Button button;
        private final ModuleConfiguration configuration;

        private Headquarters(Button button, ModuleConfiguration configuration) {
            this.button = button;
            this.configuration = configuration;
        }

        public static Headquarters parse(Stream<String> stream) {
            var configuration = ModuleConfiguration.build(stream);

            var button = new Button(BUTTON_ID);
            button.addDestination(configuration.getBroadcaster());

            return new Headquarters(button, configuration);
        }

        public void pushButton(long count) {
            LongStream.range(0, count)
                    .forEach(iteration -> pushButton());
        }

        public void pushButton() {
            button.push();
            configuration.processPulses();
        }

        public long getLowPulseCount() {
            return getPulseTypeCount(PulseType.LOW);
        }

        public long getHighPulseCount() {
            return getPulseTypeCount(PulseType.HIGH);
        }

        public long getPulseTypeCount(PulseType pulseType) {
            var count = button.getSentPulseTypeCount(pulseType);
            count += configuration.getBroadcaster().getSentPulseTypeCount(pulseType);

            count += configuration.getModules()
                    .stream()
                    .mapToLong(it -> it.getSentPulseTypeCount(pulseType))
                    .sum();

            return count;
        }

        /**
         *
         * @param id The ID of the module we want to send a low pulse to.
         * @param requiredSouceCount If the specified ID has more than one source, then how many of them have to
         *                           send a low pulse to the destination?
         * @param maxButtonPresses The maximum number of button presses to perform.
         * @return The minimum number of button presses needed to send a low pulse to the specified module.
         * Or -1 if the maximum number of button presses was reached before a low pulse would be sent to the
         * specified module.
         */
        public long buttonPressesNeededToSendLowPulseToModule(String id, int requiredSouceCount, long maxButtonPresses) {
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
            var broadcaster = configuration.getBroadcaster();
            if (broadcaster == null) {
                throw new RuntimeException("Unable to find broadcaster module with id: " + ModuleConfiguration.BROADCASTER_ID);
            }

            configuration.reset(); // Start from a fresh state!
            // Get the set of modules to monitor
            var watchList = getWatchList(targetModule);

            if (watchList.size() < requiredSouceCount) {
                throw new RuntimeException("The destination doesn't have the required number of sources! " +
                        "Required source count: " + requiredSouceCount + " destination source count: " + watchList.size());
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

                if (doneSources.size() >= requiredSouceCount) {
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
                    .map(it -> it.values().stream().reduce(Day08::leastCommonMultiple).orElse(0L))
                    .filter(it -> it > 0L)
                    .sorted()
                    .limit(requiredSouceCount)
                    .toList();

            if (lcms.size() > 1) {
                result = lcms.stream().reduce(Day08::leastCommonMultiple).orElse(0L);
            } else if (lcms.size() == 1){
                result = lcms.get(0);
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
                        if (moduleSetEntry.getValue().size() == moduleCounts.get(moduleSetEntry.getKey()).size()) {
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
    }

    private static final String inputFilename = "inputs/input_day_20-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var headquarters = Headquarters.parse(stream);
            var start = Instant.now();
            headquarters.pushButton(1_000);
            var pulseProduct = headquarters.getLowPulseCount() * headquarters.getHighPulseCount();
            var end = Instant.now();
            System.out.println("Product of pulse types sent: " + pulseProduct + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var headquarters = Headquarters.parse(stream);
            var start = Instant.now();
            var requiredPresses = headquarters.buttonPressesNeededToSendLowPulseToModule("rx", 1, 10_000);
            var end = Instant.now();
            System.out.println("Number of button presses needed to activate rx module: " + requiredPresses + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
