package buttons;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Třída implementující funkce základních tlačítek podle pro příjemky
 * @author Martin Hamet
 *
 */
public class ButtonsForPrijemky extends ButtonsInTable {
	/**
	 * Vytvoří novou instanci tlačítek pro zboží
	 */
	public ButtonsForPrijemky(){
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
            			Main.safe.prijemky.formAdd());
            }
        });
		
		
		/*
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.safe.zbozi.removeZbozi();
			}
		});
		*/
		buttonRemove.setVisible(false);
		buttonRemove.setDisable(true);
		
		buttonDetail.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Object selected = Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
				if(selected==null){
					Main.safe.zbozi.wrongInput(Main.safe.zbozi.ER_01);
					return;
				}
				items.Prijemka selectedP = (items.Prijemka)selected;
				Main.setEditing(true);
				Main.addNodeSP(
						Main.safe.prijemky.formDetail(selectedP));
			}
		});
	}
}
