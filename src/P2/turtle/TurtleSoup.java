/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.*;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        turtle.forward(sideLength);//先走第一条边
        for(int i=0;i<3;i++)//转弯90度三次，画出一个正方形
        {
            turtle.turn(90);
            turtle.forward(sideLength);
        }

        //throw new RuntimeException("implement me!");
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        double angle;
        if(sides<=0||!Integer.toString(sides).matches("[0-9]+")){
            throw new IllegalArgumentException("Illegal Input of Sides!");
        }
        else angle=(double)(sides-2)*180/sides;
        return angle;
        //throw new RuntimeException("implement me!");
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        double sides;
        if(angle<=0||angle>=180) throw new IllegalArgumentException("Illegal input of angle!");
        sides=(360/(180-angle));
        return (int)Math.round(sides);
        //throw new RuntimeException("implement me!");
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double angle=calculateRegularPolygonAngle(sides);
        for(int i=0;i<sides;i++){
            turtle.forward(sideLength);
            turtle.turn(180-angle);
        }
        //throw new RuntimeException("implement me!");
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        if(currentX==targetX&&currentY==targetY) return 0;
        int instanceX,instanceY;//距离
        instanceX=targetX-currentX;
        instanceY=targetY-currentY;
        double bearing=90-Math.toDegrees(Math.atan((double)instanceY/instanceX));//利用正切计算偏离y轴的角度
        if(instanceX<0) bearing+=180;//x与y异号，结合图形可知，需要再加180度
        bearing-=currentBearing;
        if(bearing<0) bearing+=360;//角度为负变成逆时针旋转，加360°调整为顺时针旋转
        return bearing;
        //throw new RuntimeException("implement me!");
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        if(xCoords.size()!=yCoords.size()||xCoords.size()<1){
            throw new IllegalArgumentException("Illegal input of lists!");
        }
        List<Double> bearings=new ArrayList<>();
        for(int i=0;i<xCoords.size()-1;i++){
            double preBearing=0;
            if(i>0) preBearing=bearings.get(i-1);
            bearings.add(calculateBearingToPoint(preBearing,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1)));
        }
        return bearings;
        //throw new RuntimeException("implement me!");
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        if(points.size()<=3) return points;
        Point LTPoint=null;//找到最左上角的点的位置|这个点必然在集合中
        Set<Point> Convex=new HashSet<>();
        for(Point point:points){//找到最左上的点
            if(LTPoint==null) LTPoint=point;
            else{
                if(point.x()<LTPoint.x()) LTPoint=point;
                else if(point.x()== LTPoint.x()){
                    if(point.y()>LTPoint.y()) LTPoint=point;
                }
            }
        }
        Point nowPoint=LTPoint;
        Convex.add(LTPoint);
        while(true){//回到起点或所有点都被加入就停止
            double minAngle=360;
            Point tempPoint=null;
            for(Point point:points){
                if(nowPoint==point||(Convex.contains(point)&&point!=LTPoint)) continue;//为了不破坏原有set，此处要进行两个判断，第一个是点是否是当前点，第二个是是否添加过，而且不是起始点
                double angle=calculateBearingToPoint(0,(int)nowPoint.x(),(int)nowPoint.y(),(int)point.x(),(int)point.y());
                //System.out.println(angle);
                if(angle<minAngle) {
                    tempPoint = point;
                    minAngle=angle;
                }
                else if(angle==minAngle){
                    int nowIns=(int)((point.x()-nowPoint.x())*(point.x()-nowPoint.x())+(point.y()-nowPoint.y())*(point.y()-nowPoint.y()));
                    int minIns=(int)((tempPoint.x()-nowPoint.x())*(tempPoint.x()-nowPoint.x())+(tempPoint.y()-nowPoint.y())*(tempPoint.y()-nowPoint.y()));
                    if(nowIns>minIns) tempPoint=point;
                }
            }
            nowPoint=tempPoint;//当前点成为最小角度点
            //System.out.println("x="+nowPoint.x()+"y="+nowPoint.y());
            if(nowPoint.x()== LTPoint.x()&&nowPoint.y()==LTPoint.y()) break;//回到起点结束
            else Convex.add(nowPoint);
        }
        return Convex;

        //throw new RuntimeException("implement me!");
    }
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        turtle.turn(270);
        for(int j=0;j<360;j++) {
            turtle.forward(1);
            turtle.turn(1);
        }
        turtle.turn(270);
        turtle.forward(20);
        turtle.turn(90);
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                turtle.forward(2);
                turtle.turn(1);
            }
            turtle.turn(90);
            turtle.forward(10);
            turtle.turn(270);
            for(int j=0;j<21;j++){
                turtle.forward(2);
                turtle.turn(1);
            }
            turtle.turn(270);
            turtle.forward(10);
            turtle.turn(90);
            for(int j=0;j<12;j++){
                turtle.forward(2);
                turtle.turn(1);
            }
        }
        for(int i=0;i<2;i++){
            turtle.forward(100);
            turtle.turn(270);
            turtle.forward(50);
            turtle.turn(270);
            turtle.forward(100);
        }
        //throw new RuntimeException("implement me!");
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //drawSquare(turtle, 40);
        drawRegularPolygon(turtle,7,40);
        //drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
