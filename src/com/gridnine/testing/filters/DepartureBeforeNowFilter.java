package com.gridnine.testing.filters;

import com.gridnine.testing.Flight;
import com.gridnine.testing.Segment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DepartureBeforeNowFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        LocalDateTime now = LocalDateTime.now();

        return flights.stream()
                .filter(flight -> {
                    // Check if any segment departs before now
                    return flight.getSegments().stream()
                            .allMatch(segment -> segment.getDepartureDate().isAfter(now));
                })
                .collect(Collectors.toList());
    }
}