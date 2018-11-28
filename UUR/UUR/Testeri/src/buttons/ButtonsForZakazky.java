package buttons;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Třída implementující funkce základních tlačítek podle pro zakázky
 * @author Martin Hamet
 *
 */
public class ButtonsForZakazky extends ButtonsInTable {
	
	/**
	 * Vytvoří novou instanci tlačítek pro zakázky
	 */
	public ButtonsForZakazky(){
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
            			Main.safe.zakazky.formAdd());
            }
        });
		
		
		
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.safe.zakazky.removeZakazka();
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
				items.Zakazka selectedZ = (items.Zakazka)selected;
				Main.setEditing(true);
				Main.addNodeSP(
						Main.safe.zakazky.formDetail(selectedZ));
			}
		});
	}
}
