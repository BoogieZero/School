package gui;
	
import function.TimeHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;


/**
 * Main class
 * This class loading fxml file and creating window application of our project.
 * @author Jiří Lukáš
 *
 */
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("PT-SP");
			primaryStage.setScene(createScene());
			primaryStage.show();
		
		} catch(Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Stage error");
			alert.setContentText("Nebyla vytvořena Scena");
			alert.showAndWait();
		}
	}
	/**
	 * Main
	 * creating time handler and launch our application.
	 * @param args	imput parameters
	 */
	public static void main(String[] args) {
		new TimeHandler();
		launch(args);
	}
	/**
	 * Method createScene
	 * This method loading fxml file and from this file create scene.
	 * @return Scene of our application
	 */
	private Scene createScene(){
		TabPane root = null;
		Scene scene = null;
		try{
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("SpaceProgram.fxml"));
			root = (TabPane) loader.load();
			scene = new Scene(root,1000,1000);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Nelze načíst soubor");
			alert.setContentText("Nelze najíst SpaceProgram.fxml");
			alert.showAndWait();
			e.printStackTrace();
			Platform.exit();
		}
		return scene;
	}
}
