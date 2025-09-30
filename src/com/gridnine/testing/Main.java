package com.gridnine.testing;

import com.gridnine.testing.filters.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("=== ALL FLIGHTS ===");
        printFlights(flights);
        System.out.println();

        FlightFilter departureFilter = new DepartureBeforeNowFilter();
        List<Flight> filteredByDeparture = departureFilter.filter(flights);

        System.out.println("=== FILTER 1: Exclude flights with departure before now ===");
        printFlights(filteredByDeparture);
        System.out.println();

        FlightFilter arrivalFilter = new ArrivalBeforeDepartureFilter();
        List<Flight> filteredByArrival = arrivalFilter.filter(flights);

        System.out.println("=== FILTER 2: Exclude flights with arrival before departure ===");
        printFlights(filteredByArrival);
        System.out.println();

        FlightFilter groundTimeFilter = new GroundTimeExceedsTwoHoursFilter();
        List<Flight> filteredByGroundTime = groundTimeFilter.filter(flights);

        System.out.println("=== FILTER 3: Exclude flights with ground time > 2 hours ===");
        printFlights(filteredByGroundTime);
    }

    private static void printFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            System.out.println("No flights found.");
            return;
        }

        for (int i = 0; i < flights.size(); i++) {
            System.out.println((i + 1) + ". " + flights.get(i));
        }
        System.out.println("Total: " + flights.size() + " flights");
    }
}