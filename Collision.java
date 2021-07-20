/**
 Checking for a collision between a circle and a rectangle, as needed
 for the ball/paddle and ball/brick collisions in Breakout.
 @author Jim Teresco
 @version Spring 2020
 */

public class Collision {

    /**
     Return true if the given circle overlaps the given rectangle.
     Algorithm based on example at:
     https://www.gamedevelopment.blog/collision-detection-circles-rectangles-and-polygons/
     The idea is to see if the center of the circle lies within the
     rectangle if it expands on all sides by the radius of the
     circle.
     @param circleX x-coordinate of the center of the circle
     @param circleY y-coordinate of the center of the circle
     @param circleR radius of the circle
     @param rectX the x-coordinate of the upper-left corner of the rectangle
     @param rectY the y-coordinate of the upper-left corner of the rectangle
     @param rectW the width of the rectangle
     @param rectH the height of the rectangle
     @return true if the given circle overlaps the given rectangle
     */
    public static boolean circleOverlapsRectangle(int circleX, int circleY,
                                                  int circleR,
                                                  int rectX, int rectY,
                                                  int rectW, int rectH) {
        int rectCX = rectX + rectW/2;
        int rectCY = rectY + rectH/2;
        // x and y distances between the circle and rectangle
        int dx = Math.abs(circleX - rectCX);
        int dy = Math.abs(circleY - rectCY);

        // check if beyond the bounds of the expanded rectangle
        if (dx > (rectW/2 + circleR)) return false;
        if (dy > (rectH/2 + circleR)) return false;

        // is circle center within the expanded rectangle
        if (dx <= rectW/2) return true;
        if (dy <= rectH/2) return true;

        // check corners
        int cornerDistSq = ((dx - rectW/2) * (dx - rectW/2) +
                (dy - rectH/2) * (dy - rectH/2));
        if (cornerDistSq <= circleR*circleR) return true;

        // must not collide
        return false;
    }
}