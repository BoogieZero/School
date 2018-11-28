package animation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Elevator extends StackPane{
	private Rectangle rectangle;
	private int count;
	private Text text;
	private IntegerProperty actualFloor;
	
	public void setTextValue(int value){
		this.count = value;
		text.setText(""+count);
	}
	
	public void addPasangers(int amount){
		this.count += amount;
		text.setText(""+count);
	}
	
	public void removePasangers(int amount){
		this.count -= amount;
		text.setText(""+count);
	}
	
	public void up(){
		int i = actualFloor.getValue();
		i++;
		actualFloor.set(i);
	}
	
	public void down(){
		int i = actualFloor.getValue();
		i--;
		actualFloor.set(i);
	}
	
	public int getFloor(){
		return actualFloor.getValue();
	}
	
	public void bindToFloor(StringProperty text){
		text.bind(actualFloor.asString());
	}
	
	public Elevator(int width, int height, Color color) {
		rectangle = new Rectangle(width, height, color);
		text = new Text();
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		count = 0;
		actualFloor = new SimpleIntegerProperty(0);
		setTextValue(0);
		getChildren().addAll(rectangle, text);
	}
	
}
