package com.gridnine.testing;

import com.gridnine.testing.filters.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FlightFilterTest {

    public static void main(String[] args) {
        System.out.println("=== RUNNING FLIGHT FILTER TESTS ===\n");

        testDepartureBeforeNowFilter();
        testArrivalBeforeDepartureFilter();
        testGroundTimeExceedsTwoHoursFilter();
        testSingleSegmentFlightGroundTime();

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }

    public static void testDepartureBeforeNowFilter() {
        System.out.println("Test 1: DepartureBeforeNowFilter");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(1);
        LocalDateTime future = now.plusDays(1);

        Segment pastSegment = new Segment(past, future);
        Segment futureSegment = new Segment(future, future.plusHours(2));

        Flight pastFlight = new Flight(Arrays.asList(pastSegment));
        Flight futureFlight = new Flight(Arrays.asList(futureSegment));

        List<Flight> flights = Arrays.asList(pastFlight, futureFlight);

        FlightFilter filter = new DepartureBeforeNowFilter();
        List<Flight> result = filter.filter(flights);

        boolean testPassed = result.size() == 1 &&
                result.contains(futureFlight) &&
                !result.contains(pastFlight);

        System.out.println("  Input flights: " + flights.size());
        System.out.println("  Output flights: " + result.size());
        System.out.println("  Test " + (testPassed ? "PASSED" : "FAILED"));
        System.out.println();
    }

    public static void testArrivalBeforeDepartureFilter() {
        System.out.println("Test 2: ArrivalBeforeDepartureFilter");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusHours(1);

        Segment validSegment = new Segment(now, future);
        Segment invalidSegment = new Segment(future, now);

        Flight validFlight = new Flight(Arrays.asList(validSegment));
        Flight invalidFlight = new Flight(Arrays.asList(invalidSegment));

        List<Flight> flights = Arrays.asList(validFlight, invalidFlight);

        FlightFilter filter = new ArrivalBeforeDepartureFilter();
        List<Flight> result = filter.filter(flights);

        boolean testPassed = result.size() == 1 &&
                result.contains(validFlight) &&
                !result.contains(invalidFlight);

        System.out.println("  Input flights: " + flights.size());
        System.out.println("  Output flights: " + result.size());
        System.out.println("  Test " + (testPassed ? "PASSED" : "FAILED"));
        System.out.println();
    }

    public static void testGroundTimeExceedsTwoHoursFilter() {
        System.out.println("Test 3: GroundTimeExceedsTwoHoursFilter");

        LocalDateTime baseTime = LocalDateTime.now();

        Segment seg1 = new Segment(baseTime, baseTime.plusHours(1));
        Segment seg2 = new Segment(baseTime.plusHours(2), baseTime.plusHours(3));
        Flight shortGroundTimeFlight = new Flight(Arrays.asList(seg1, seg2));

        Segment seg3 = new Segment(baseTime, baseTime.plusHours(1));
        Segment seg4 = new Segment(baseTime.plusHours(4), baseTime.plusHours(5));
        Flight longGroundTimeFlight = new Flight(Arrays.asList(seg3, seg4));

        List<Flight> flights = Arrays.asList(shortGroundTimeFlight, longGroundTimeFlight);

        FlightFilter filter = new GroundTimeExceedsTwoHoursFilter();
        List<Flight> result = filter.filter(flights);

        boolean testPassed = result.size() == 1 &&
                result.contains(shortGroundTimeFlight) &&
                !result.contains(longGroundTimeFlight);

        System.out.println("  Input flights: " + flights.size());
        System.out.println("  Output flights: " + result.size());
        System.out.println("  Test " + (testPassed ? "PASSED" : "FAILED"));
        System.out.println();
    }

    public static void testSingleSegmentFlightGroundTime() {
        System.out.println("Test 4: Single Segment Flight Ground Time");

        LocalDateTime baseTime = LocalDateTime.now();
        Segment segment = new Segment(baseTime, baseTime.plusHours(2));
        Flight singleSegmentFlight = new Flight(Arrays.asList(segment));

        FlightFilter filter = new GroundTimeExceedsTwoHoursFilter();
        List<Flight> result = filter.filter(Arrays.asList(singleSegmentFlight));

        boolean testPassed = result.size() == 1 && result.contains(singleSegmentFlight);

        System.out.println("  Input flights: 1");
        System.out.println("  Output flights: " + result.size());
        System.out.println("  Test " + (testPassed ? "PASSED" : "FAILED"));
        System.out.println();
    }
}
