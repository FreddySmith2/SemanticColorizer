import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.text.ParseException;


public class FractalArt extends Application
{
    
    public FractalArt()
    {
        
    }
    
    public void start(Stage stage) throws ParseException, java.io.IOException, org.json.simple.parser.ParseException{
        Displayer displayer = new Displayer();
        //Group root = new Group();
        Scene scene = new Scene(displayer, 800, 800, Color.BLACK);
        
        //final Canvas canvas = new Canvas(250,250);
        GraphicsContext gc = displayer.canvas.getGraphicsContext2D();
        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(Color.PINK);
        displayer.switchFractal.setOnAction(event -> {
            int color = displayer.buttonPress();
            if(color==0){
                gc.setFill(Color.BLUE); 
            }else if(color==1){
                gc.setFill(Color.RED);
            }else if(color==2){
                gc.setFill(Color.GREEN);
            }else if(color==3){
                gc.setFill(Color.YELLOW);
            }else{
                gc.setFill(Color.PINK);
            }   
        });
        displayer.canvas.setOnMouseClicked(event ->{
            displayer.currentFractal(gc, (int)event.getX()-100, (int)event.getY()-100, 200);
        });
        
        
        stage.setTitle("Semantic Colorizer");
        stage.setScene(scene);
        stage.show();
    }
    
    
}
