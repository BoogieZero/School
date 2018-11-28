package animation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;


public class GroupBox extends StackPane {
	public GroupBox(String title, Node content) {
		
		
		Label lbTitle = new Label(" " + title + " ");
		lbTitle.setStyle(	"-fx-translate-y: -10;"+
							"-fx-background-color: white;");
		//lbTitle.translateYProperty().set(-16);
		setAlignment(lbTitle, Pos.TOP_CENTER);
		
		StackPane contentPane = new StackPane();
		content.setStyle("-fx-padding: 26 10 10 10;");
		
		contentPane.getChildren().add(content);
		
		setStyle(	"-fx-content-display: top;"+
					"-fx-border-insets: 20 15 15 15;"+
					"-fx-border-color: black;"+
					"-fx-border-width: 1;"+
					"-fx-background-color: white;");
		
		getChildren().addAll(lbTitle, contentPane);
	}
}
