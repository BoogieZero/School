package buttons;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Třída implementující funkce základních tlačítek podle pro faktury
 * 
 * @author Martin Hamet
 *
 */
public class ButtonsForFaktury extends ButtonsInTable {
	
	/**
	 * Vytvoří novou instanci tlačítek pro faktury
	 */
	public ButtonsForFaktury(){
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
            			Main.safe.faktury.formAdd());
            }
        });
		
		
		
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.safe.faktury.removeFaktura();
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
				items.Faktura selectedF = (items.Faktura)selected;
				Main.setEditing(true);
				Main.addNodeSP(
						Main.safe.faktury.formDetail(selectedF));
			}
		});
	}
}
