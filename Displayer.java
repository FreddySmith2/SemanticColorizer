import javafx.scene.layout.VBox;
import javafx.scene.*;
import javafx.scene.Scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.*;
import javafx.scene.shape.ArcType;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.*;
import javafx.scene.paint.Color.*;
import javafx.scene.paint.Color;
import java.text.*;
import javafx.scene.text.Font.*;
import javafx.scene.shape.Shape.*;

public class Displayer extends VBox
{
    public Button switchFractal;
    public Canvas canvas;
    int whichFractal;
    Label texta;
    TextField textin;
    TextFlow sentence;
    Pane root;
    public Displayer() throws ParseException, java.io.IOException, org.json.simple.parser.ParseException
    {
        DictionarySearch dict = new DictionarySearch();
        root = new Pane();
        StackPane holder = new StackPane();
        
        textin = new TextField();
        texta = new Label("");
        whichFractal = 4;
        switchFractal = new Button("Set Fractal");
        canvas = new Canvas(800, 800);
        //canvas.setFill();
        sentence = new TextFlow();
        
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {
                Text temp = new Text("");
                Label text = new Label("");
                temp.setText(textin.getText());
                String word = temp.getText();
                try{temp.setFill(Color.web(dict.getHSLFromKey(word)));}
                catch(Exception l){}
                //texta.setText(""+dict.getValueFromKey(word)+", = "+dict.getHSLFromKey(word));
                text.setText(" ");
                //sentence.getChildren().add(texta);
                sentence.getChildren().add(temp);
                sentence.getChildren().add(text);
            }
        };
        //holder.setStyle("-fx-background-color: grey");
        textin.setOnAction(event);
        
        
        getChildren().addAll(textin, sentence, root);
    }
    
    //
    //public void draw1(GraphicsContext g, int x, int y, int size)
    //   draw on all 8 directions. size/3 each time. coordinates increased by *5 or 2 depending
    // base case, size <=0 
    
    /**
     * draw1 draws a Sierpinski carpet from a starting point.
     * after drawing a rectangle on location, it draws 8 subsequent rectangles
     * 1/3 the size, in 8 directions proportionate to the size.
     * once the size reaches 0, we reach the Base Case
     * each recursion, size is divided by 3 (smaller caller)
     * each recursion, a rectangle is drawn (general case).
     */
    public void draw1(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            int unit = size/3;
            g.fillRect(x, y, size, size);
            draw1(g, x-(unit*2), y+(unit), unit);       // left
            draw1(g, x-(unit*2), y-(unit*2), unit);     // left+up
            draw1(g, x+(unit), y-(unit*2), unit);       // up
            draw1(g, x+(unit*4), y-(unit*2), unit);     // up+right
            draw1(g, x+(unit*4), y+(unit), unit);       // right
            draw1(g, x+(unit*4), y+(unit*4), unit);     // right+down
            draw1(g, x+(unit), y+(unit*4), unit);       // down
            draw1(g, x-(unit*2), y+(unit*4), unit);     // down+left
        }
    }
    /**
     * draw2 (with the help of a,b, and c) does NOT draw
     * a mandlebrot set, but it's pretending to be
     * makes circles, with each method pertaining to a primary
     * direction 
     * base case: size <= 0
     * smaller caller: size/2 (or /3)
     * general case: fillOval
     */    
    public void draw2(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            g.fillOval(x, y, size, size);
            draw2(g, x-(size/2), y+(size/4), size/2);//left
            draw2a(g, x+(size/3), y-(size/3), size/3);//up
            draw2b(g, x+(size/3), y+(size), size/3);//down
        }
    }
    
    public void draw2a(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            g.fillOval(x, y, size, size);
            draw2a(g, x+(size/4), y-(size/2), size/2);//up
            draw2(g, x-(size/3), y+(size/3), size/3);//left
            draw2c(g, x+(size), y+(size/3), size/3);//right
        }
    }
    
    public void draw2b(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            g.fillOval(x, y, size, size);
            draw2b(g, x+(size/4), y+(size), size/2);//down
            draw2(g, x-(size/3), y+(size/3), size/3);//left
            draw2c(g, x+(size), y+(size/3), size/3);//right
        }
    }
    
    public void draw2c(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            g.fillOval(x, y, size, size);
            draw2c(g, x+(size), y+(size/4), size/2);//right
            draw2a(g, x+(size/3), y-(size/3), size/3);//up
            draw2b(g, x+(size/3), y+(size), size/3);//down
        }
    }
    
    public void draw3(GraphicsContext g, int x, int y, int size){
        if(!(size<=0)){
            g.fillArc(x, y, size, size, 0.5, 10, ArcType.ROUND);
            draw3(g, x, y, size/2);
        }
    }
    
    public void currentFractal(GraphicsContext g, int x, int y, int size){
        if(whichFractal==4){
            draw2c(g,  x,  y,  size);
        }else if(whichFractal==3){
            draw2b(g, x, y, size);
        }else if(whichFractal==5){
            draw3(g, x, y, size);
        }else if(whichFractal==2){
            draw2a(g, x, y, size);
        }else if(whichFractal==1){
            draw2(g, x, y, size);
        }else{
            draw1(g, x, y, size);
        }
    }
    
    public int buttonPress(){
        if(whichFractal==5){
            whichFractal=0;
            return 0;
        }else{
            return ++whichFractal;
        }
    }
}
