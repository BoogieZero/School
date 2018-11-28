package buttons;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Třída obsahující základní tlačítka pro danou tabulku
 * 
 * @author Martin Hamet
 *
 */
public class ButtonsInTable extends HBox {
	/**
	 * Tlačítko pro přidání dokladu
	 */
	public Button buttonAdd;
	
	/**
	 * Tlačítko pro odebrání dokladu
	 */
	public Button buttonRemove;
	
	/**
	 * Tlačítko pro zobrazení detailu dokladu
	 */
	public Button buttonDetail;
	
	/**
	 * Vytvoří novou instaci tlačítek
	 */
	public ButtonsInTable(){
		super();
		buttonAdd = new Button("Pridat");
		buttonRemove = new Button("Odebrat");
		buttonDetail = new Button("Detail");
		
		this.setSpacing(10);
		this.setPadding(new Insets(5));
		this.getChildren().addAll(	buttonAdd,
									buttonRemove,
									buttonDetail);
		
	}
}
