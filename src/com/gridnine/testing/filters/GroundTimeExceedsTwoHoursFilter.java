package com.gridnine.testing.filters;

import com.gridnine.testing.Flight;
import com.gridnine.testing.Segment;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class GroundTimeExceedsTwoHoursFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> calculateTotalGroundTime(flight) <= 2 * 60)
                .collect(Collectors.toList());
    }

    private long calculateTotalGroundTime(Flight flight) {
        List<Segment> segments = flight.getSegments();

        if (segments.size() <= 1) {
            return 0;
        }

        long totalGroundTime = 0;

        for (int i = 0; i < segments.size() - 1; i++) {
            Segment currentSegment = segments.get(i);
            Segment nextSegment = segments.get(i + 1);

            Duration groundTime = Duration.between(
                    currentSegment.getArrivalDate(),
                    nextSegment.getDepartureDate()
            );

            totalGroundTime += groundTime.toMinutes();
        }

        return totalGroundTime;
    }
}
