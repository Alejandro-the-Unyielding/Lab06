package it.unibo.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Example class using {@link List} and {@link Map}.
 *
 */
public final class UseListsAndMaps {

    private static final int ELEMENTS = 100_000;
    private static final int READS = 10_000;
    private static final int START = 1000;
    private static final int END = 2000;
    private static final long AFRICA_POPULATION = 1_110_635_000L;
    private static final long AMERICAS_POPULATION = 972_005_000L;
    private static final long ANTARCTICA_POPULATION = 0L;
    private static final long ASIA_POPULATION = 4_298_723_000L;
    private static final long EUROPE_POPULATION = 742_452_000L;
    private static final long OCEANIA_POPULATION = 38_304_000L;

    private UseListsAndMaps() {
    }

    /**
     * @param s
     *            unused
     */
    public static void main(final String... s) {
        /*
         * 1) Create a new ArrayList<Integer>, and populate it with the numbers
         * from 1000 (included) to 2000 (excluded).
         */
        final List<Integer> arrayList = new ArrayList<>();
        for (int i = START; i < END; i++) {
            arrayList.add(i);
        }
        /*
         * 2) Create a new LinkedList<Integer> and, in a single line of code
         * without using any looping construct (for, while), populate it with
         * the same contents of the list of point 1.
         */
        final List<Integer> linkedList = new LinkedList<>(arrayList);
        log(linkedList);
        /*
         * 3) Using "set" and "get" and "size" methods, swap the first and last
         * element of the first list. You can not use any "magic number".
         * (Suggestion: use a temporary variable)
         */
        final int el = arrayList.getLast();
        arrayList.set(arrayList.size() - 1, arrayList.getFirst());
        arrayList.set(0, el);
        /*
         * 4) Using a single for-each, print the contents of the arraylist.
         */
        final var builder = new StringBuilder();
        for (final int i : arrayList) {
            builder.append(i).append(", ");
        }
        if (!builder.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length());
        }
        log(builder);
        /*
         * 5) Measure the performance of inserting new elements in the head of
         * the collection: measure the time required to add 100.000 elements as
         * first element of the collection for both ArrayList and LinkedList,
         * using the previous lists. In order to measure times, use as example
         * TestPerformance.java.
         */
        long time = System.nanoTime();
        for (int i = 0; i < ELEMENTS; i++) {
            arrayList.addFirst(i);
        }
        time = System.nanoTime() - time;
        log("Inserting " + ELEMENTS + " elements as first in an ArrayList took " + timeAsString(time));
        time = System.nanoTime();
        for (int i = 0; i < ELEMENTS; i++) {
            linkedList.addFirst(i);
        }
        time = System.nanoTime() - time;
        log("Inserting " + ELEMENTS + " elements as first in a LinkedList took " + timeAsString(time));
        /*
         * 6) Measure the performance of reading 1000 times an element whose
         * position is in the middle of the collection for both ArrayList and
         * LinkedList, using the collections of point 5. In order to measure
         * times, use as example TestPerformance.java.
         */
        time = System.nanoTime();
        for (int i = 0; i < READS; i++) {
            arrayList.get(arrayList.size() / 2); // Warning OK: we are just benchmarking
        }
        time = System.nanoTime() - time;
        log(
            "Reading " + READS + " elements in the middle of an ArrayList took " + timeAsString(time)
        );
        time = System.nanoTime();
        for (int i = 0; i < READS; i++) {
            linkedList.get(linkedList.size() / 2);
        }
        time = System.nanoTime() - time;
        log(
            "Reading " + READS + " elements in the middle of a LinkedList took " + timeAsString(time)
        );
        /*
         * 7) Build a new Map that associates to each continent's name its
         * population:
         *
         * Africa -> 1,110,635,000
         *
         * Americas -> 972,005,000
         *
         * Antarctica -> 0
         *
         * Asia -> 4,298,723,000
         *
         * Europe -> 742,452,000
         *
         * Oceania -> 38,304,000
         */
        final Map<String, Long> world = new HashMap<>();
        world.put("Africa", AFRICA_POPULATION);
        world.put("Americas", AMERICAS_POPULATION);
        world.put("Antarctica", ANTARCTICA_POPULATION);
        world.put("Asia", ASIA_POPULATION);
        world.put("Europe", EUROPE_POPULATION);
        world.put("Oceania", OCEANIA_POPULATION);
        /*
         * 8) Compute the population of the world
         */
        long worldPopulation = 0;
        for (final long population : world.values()) {
            worldPopulation += population;
        }
        log(
            "We are ~" + worldPopulation + " humans beings on this pale blue dot (just enough to overflow integers :D)."
        );
    }

    private static String timeAsString(final long nanoseconds) {
        return nanoseconds + "ns (" + NANOSECONDS.toMillis(nanoseconds) + "ms)";
    }

    private static void log(final Object message) {
        System.out.println(message); // NOPMD
    }
}