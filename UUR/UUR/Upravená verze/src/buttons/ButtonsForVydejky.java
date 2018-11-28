package buttons;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Třída implementující funkce základních tlačítek podle pro výdejky
 * @author Martin Hamet
 *
 */
public class ButtonsForVydejky extends ButtonsInTable {
	
	/**
	 * Vytvoří novou instanci tlačítek pro výdejky
	 */
	public ButtonsForVydejky(){
		super();
		setButtons();
	}
	
	/**
	 * Nastaví funkce tlačítek
	 */
	private void setButtons(){
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
        	
            @Override
            public void handle(ActionEvent event) {
            	Main.setEditing(true);
            	Main.addNodeSP(
            			Main.safe.vydejky.formAdd());
            }
        });
		
		
		
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.safe.vydejky.removeVydejka();
			}
		});
		
		buttonDetail.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Object selected = Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
				if(selected==null){
					Main.safe.zbozi.wrongInput(Main.safe.zbozi.ER_01);
					return;
				}
				items.Vydejka selectedV = (items.Vydejka)selected;
				Main.setEditing(true);
				Main.addNodeSP(
						Main.safe.vydejky.formDetail(selectedV));
			}
		});
	}
}
