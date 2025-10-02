package com.gridnine.testing;

import com.gridnine.testing.filters.*;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("=== ALL FLIGHTS ===");
        printFlightsWithAnalysis(flights);
        System.out.println();

        testFilter("FILTER 1: Exclude flights with departure before now",
                flights, new DepartureBeforeNowFilter());

        testFilter("FILTER 2: Exclude flights with arrival before departure",
                flights, new ArrivalBeforeDepartureFilter());

        testFilter("FILTER 3: Exclude flights with ground time > 2 hours",
                flights, new GroundTimeExceedsTwoHoursFilter());
    }

    private static void testFilter(String filterName, List<Flight> allFlights, FlightFilter filter) {
        System.out.println("=== " + filterName + " ===");
        List<Flight> filtered = filter.filter(allFlights);
        List<Flight> excluded = new ArrayList<>(allFlights);
        excluded.removeAll(filtered);

        System.out.println("Remaining flights (" + filtered.size() + "):");
        printFlights(filtered);

        if (!excluded.isEmpty()) {
            System.out.println("Excluded flights (" + excluded.size() + "):");
            for (int i = 0; i < excluded.size(); i++) {
                int originalIndex = allFlights.indexOf(excluded.get(i)) + 1;
                System.out.println("  Flight #" + originalIndex + ": " + excluded.get(i));
            }
        }
        System.out.println();
    }

    private static void printFlightsWithAnalysis(List<Flight> flights) {
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            System.out.print((i + 1) + ". " + flight);

            List<String> issues = analyzeFlight(flight);
            if (!issues.isEmpty()) {
                System.out.print(" ⚠ Issues: " + String.join(", ", issues));
            }
            System.out.println();
        }
        System.out.println("Total: " + flights.size() + " flights");
    }

    private static void printFlights(List<Flight> flights) {
        for (int i = 0; i < flights.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + flights.get(i));
        }
    }

    private static List<String> analyzeFlight(Flight flight) {
        List<String> issues = new ArrayList<>();
        List<Segment> segments = flight.getSegments();

        if (segments.stream().anyMatch(seg -> seg.getDepartureDate().isBefore(java.time.LocalDateTime.now()))) {
            issues.add("departure in past");
        }

        if (segments.stream().anyMatch(seg -> seg.getArrivalDate().isBefore(seg.getDepartureDate()))) {
            issues.add("arrival before departure");
        }

        if (segments.size() > 1) {
            long groundTime = calculateTotalGroundTime(flight);
            if (groundTime > 120) {
                issues.add(groundTime + " min ground time");
            }
        }

        return issues;
    }

    private static long calculateTotalGroundTime(Flight flight) {
        List<Segment> segments = flight.getSegments();
        if (segments.size() <= 1) return 0;

        long total = 0;
        for (int i = 0; i < segments.size() - 1; i++) {
            java.time.Duration groundTime = java.time.Duration.between(
                    segments.get(i).getArrivalDate(),
                    segments.get(i + 1).getDepartureDate()
            );
            total += groundTime.toMinutes();
        }
        return total;
    }
}