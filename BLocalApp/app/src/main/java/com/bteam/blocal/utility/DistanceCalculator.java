package com.bteam.blocal.utility;

import android.location.Location;

public class DistanceCalculator {
    public static double getDistance(Location from, Location to) {
        return from.distanceTo(to);
    }
}
