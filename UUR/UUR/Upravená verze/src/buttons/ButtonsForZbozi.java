package buttons;

import items.Zbozi;
import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Třída implementující funkce základních tlačítek podle pro zboží
 * @author Martin Hamet
 *
 */
public class ButtonsForZbozi extends ButtonsInTable {
	
	/**
	 * Vytvoří novou instanci tlačítek pro zboží
	 */
	public ButtonsForZbozi(){
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
            			Main.safe.zbozi.formAdd());
            }
        });
		
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.safe.zbozi.removeZbozi();
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
				items.Zbozi selectedZ = (Zbozi)selected;
				Main.setEditing(true);
				Main.addNodeSP(
						Main.safe.zbozi.formDetail(selectedZ));
			}
		});
		
		
	}
}
