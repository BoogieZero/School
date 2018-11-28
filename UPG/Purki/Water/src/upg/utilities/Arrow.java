/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;


import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author purka
 */
public class Arrow {
    public Arrow(){
        
    }
    public Line drawBody(AnchorPane ap, double startX, double startY, double endX, double endY){
        Line l = new Line();
        if(startX<endX && startY<endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX-50);
            l.setEndY(endY-50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX>endX && startY<endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX+50);
            l.setEndY(endY-50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX<endX && startY>endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX-50);
            l.setEndY(endY+50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX>endX && startY>endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX+50);
            l.setEndY(endY+50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX>endX && startY==endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX+50);
            l.setEndY(endY);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX==endX && startY<endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX);
            l.setEndY(endY-50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX<endX && startY==endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX-50);
            l.setEndY(endY);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        if(startX==endX && startY>endY)
        {
            l.setStartX(startX);
            l.setStartY(startY);
            l.setEndX(endX);
            l.setEndY(endY+50);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(3.0);
        }
        ap.getChildren().add(l);
        return l;
    }
    
    public void drawArrowHead(Line l, AnchorPane ap){
       if(l.getStartX()<l.getEndX() && l.getStartY()==l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, -25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, 25));
       }
       if(l.getStartX()>l.getEndX() && l.getStartY()==l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, -25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, 25));
       }
       if(l.getStartX()==l.getEndX() && l.getStartY()<l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, -25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, -25));
       }
       if(l.getStartX()==l.getEndX() && l.getStartY()>l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, 25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, 25));
       }
       if(l.getStartX()<l.getEndX() && l.getStartY()<l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 0, -25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, 0));
       }
       if(l.getStartX()<l.getEndX() && l.getStartY()>l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), -25, 0));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 0, 25));
       }
       if(l.getStartX()>l.getEndX() && l.getStartY()<l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 0, -25));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, 0));
       }
       if(l.getStartX()>l.getEndX() && l.getStartY()>l.getEndY())
       {
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 25, 0));
           ap.getChildren().add(drawHead(l.getEndX(), l.getEndY(), 0, 25));
       }
       
    }
    
    private Line drawHead(double endX, double endY, double translateX, double translateY){
        Line newLine = new Line();
        newLine.setStroke(Color.BLACK);
        newLine.setStrokeWidth(3.0);
        newLine.setStartX(endX);
        newLine.setStartY(endY);
        newLine.setEndX(endX+translateX);
        newLine.setEndY(endY+translateY);
        return newLine;
    }
}
