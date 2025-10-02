package com.gridnine.testing.filters;

import com.gridnine.testing.Flight;

import java.util.List;

public interface FlightFilter {
    List<Flight> filter(List<Flight> flights);
}
