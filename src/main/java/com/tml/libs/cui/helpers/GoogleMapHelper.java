package com.tml.libs.cui.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by TML on 01/12/2017.
 */

public class GoogleMapHelper {
    public static final double EARTH_RADIUS = 6378100.0;

    public interface GoogleMapAPIWrapper {
        public Point projectLatLngToScreenLocation(double lat, double lng);
    }

    public static GoogleMapAPIWrapper googleMapAPIWrapper = null;

    private static int convertMetersToPixels(double radiusInMeters) {
        // anchor point
        double lat = 0;
        double lng = 0;

        // calc latlng of a point in circle
        double lat1 = radiusInMeters / EARTH_RADIUS;
        double lng1 = radiusInMeters
                / (EARTH_RADIUS * Math.cos((Math.PI * lat / 180)));

        double lat2 = lat + lat1 * 180 / Math.PI;
        double lng2 = lng + lng1 * 180 / Math.PI;

        Point p1 = googleMapAPIWrapper.projectLatLngToScreenLocation(lat, lng);
        Point p2 = googleMapAPIWrapper.projectLatLngToScreenLocation(lat2, lng2);

        // distance radius in pixels
        return Math.abs(p1.x - p2.x);
    }

    public static Bitmap getRadiusBitmap(int fillColor, int fillAlpha, int strokeWidth, int radiusInPixels) {
        Paint paintFill = new Paint();
        paintFill.setColor(fillColor);
        paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
        paintFill.setStrokeWidth(strokeWidth);
        paintFill.setAlpha(fillAlpha);

        // create empty bitmap
        Bitmap b = Bitmap.createBitmap(radiusInPixels * 2, radiusInPixels * 2,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawCircle(radiusInPixels, radiusInPixels, radiusInPixels, paintFill);

        return b;
    }

    public static float AngleToRadian(float a) {
        return (float) (a * Math.PI / 180.0f);
    }

    /**
     * Calculate distance between 2 place in EARTH MAP
     *
     * @param latA
     * @param longA
     * @param latB
     * @param longB
     * @return distance in meter
     */
    public static float CalcDistance(float latA, float longA,
                                     float latB, float longB) {
        float radA = AngleToRadian(latA);
        float radB = AngleToRadian(latB);
        float dLat = AngleToRadian(latB - latA);
        float dLong = AngleToRadian(longB - longA);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radA) * Math.cos(radB) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float distance = (float) (EARTH_RADIUS * c);
        return distance;
    }

    public static float[] limitDistance(float latA, float longA,
                                        float latB, float longB, float maxDistance) {
        float latB2 = latB;
        float lngB2 = longB;

        float d = CalcDistance(latA, longA, latB, longB);
        if (d > maxDistance) {

        }
        return new float[] {latB2, lngB2};
    }
}
