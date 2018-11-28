package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import items.Zbozi;
import items.Zbozi.Units;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Třída představující systém zboží a jejich manipulaci
 * 
 * @author Martin Hamet
 *
 */
public class StockZbozi extends Stock {
	/**
	 * Mapa zbozi faktur
	 */
	private Map<Integer,items.Zbozi> map;
	
	/**
	 * Vrátí observablelist zboží v systému
	 * @return	observablelist zboží v systému
	 */
	public ObservableList<items.Zbozi> getZbozi(){
		List<items.Zbozi> listZbozi = new ArrayList<items.Zbozi>(map.values());
		ObservableList<items.Zbozi> obZbozi = FXCollections.observableArrayList(listZbozi);
		return obZbozi;
	}
	
	/**
	 * Vrátí mapu zboží v systému
	 * @return	mapa zboží v systému
	 */
	public Map<Integer, items.Zbozi> getZboziMap(){
		return map;
	}
	
	/**
	 * Vytvoří formulář detailu faktury
	 * @param zbozi	aktuálně vybrané zboží
	 * @return	formulář pro detail zboží
	 */
	public GridPane formDetail(Zbozi zbozi){
		GridPane grid = getGrid();
		FormZbozi myGrid = new FormZbozi(grid);
		myGrid.setTexts(zbozi);
		
		Button buttonEdit = new Button("Upravit");
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zavřít");
		buttonSave.setDisable(true);
		HBox hbEditStorno = new HBox();
		hbEditStorno.setSpacing(10);
		
		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Umožní editování zboží
			 */
			@Override
			public void handle(ActionEvent event) {
				myGrid.setEditable(true);
				buttonSave.setDisable(false);
				buttonEdit.setDisable(true);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží změny zboží
			 */
			@Override
			public void handle(ActionEvent event) {
				if(myGrid.isInputOk()){
					Zbozi newZbozi = new Zbozi(	myGrid.tfName.getText(), 
												myGrid.unitCHB.getValue(), 
												myGrid.checkedPrice, 
												myGrid.tfManufacturer.getText(),
												myGrid.tfInfo.getText());
					
					Zbozi upravaZbozi = (Zbozi)(Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem());
					upravaZbozi.setAll(newZbozi);
					
					Main.Main.refreshActualTable();
					
					myGrid.setEditable(false);
					buttonEdit.setDisable(false);
					buttonSave.setDisable(true);
				}
				
			}
		});
		
		buttonStorno.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uzavře formulář
			 */
			@Override
			public void handle(ActionEvent event) {
				//Main.Main.setEditing(false);	//ukonceni editace
				Main.Main.removeNodeSP();	//odstraneni formulare
			}
		});
		
		hbEditStorno.getChildren().addAll(	buttonEdit,
											buttonSave,
											buttonStorno);
		grid.add(hbEditStorno, 2, 8);
		return grid;
	}
	
	/**
	 * Vytvoří formulář pro přidání nového zboží
	 * @return	node s formulářem pro přidání nového zboží
	 */
	public Node formAdd(){
		GridPane grid = getGrid();
		FormZbozi myGrid = new FormZbozi(grid);
		
		//Butons
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zrušit");
		HBox hbSaveStorno = new HBox();
		hbSaveStorno.setSpacing(10);
		hbSaveStorno.setAlignment(Pos.CENTER);
		
		myGrid.tfQuantity.setVisible(false);
		
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží nové zboží do systému
			 */
			@Override
			public void handle(ActionEvent event) {
				if(!myGrid.isInputOk()){
					//Error in input data
					return;
				}
				Zbozi newZbozi = new Zbozi(	myGrid.tfName.getText(), 
											myGrid.unitCHB.getValue(), 
											myGrid.checkedPrice, 
											myGrid.tfManufacturer.getText(),
											myGrid.tfInfo.getText());
				addZbozi(newZbozi);			//pridani do skladu
				
				if(Main.Main.isAloneInSP()){
					@SuppressWarnings("unchecked")
					ObservableList<Zbozi> table = Main.Main.vTable.getActualTable().getItems();
					table.add(newZbozi);		//pridani do tabulky
				}else{
					Main.Main.vTable.refreshZbozi();
				}
				
				
				//Main.Main.setEditing(false);	//ukonceni editace
				Main.Main.removeNodeSP();	//odstraneni formulare
			}
		});
		
		buttonStorno.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Zavře formulář
			 */
			@Override
			public void handle(ActionEvent event) {
				//Main.Main.setEditing(false);	//ukonceni editace
				Main.Main.removeNodeSP();	//odstraneni formulare
			}
		});
		
		hbSaveStorno.getChildren().addAll(	buttonSave, 
											buttonStorno);
		
		VBox gridBox = new VBox();
		gridBox.setSpacing(10);
		gridBox.getChildren().addAll(grid,hbSaveStorno);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox);
		
		//grid.add(hbSaveStorno, 2, 8);
		//grid.setGridLinesVisible(true); 	/////
		return mainHb;
	}
	
	/**
	 * Odstraní zboží ze systému s potvrzovacím dialogem
	 */
	public void removeZbozi(){
		Zbozi zb = (Zbozi)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
		if(zb==null){
			wrongInput(ER_01);
			return;
		}
		
		if(zb.getQuantity()!=0){
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_11);
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);//, "Delete " + selection + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setTitle("Odstranění zboží");
		alert.setHeaderText("Chcete odstranit zboží: "+"\n"+zb.toString());
		ButtonType btYes = new ButtonType("Ano", ButtonData.YES);
		ButtonType btNo = new ButtonType("Ne", ButtonData.NO);
		
		alert.setResultConverter(new Callback<ButtonType, ButtonType>() {

			@Override
			public ButtonType call(ButtonType p) {
				if(p==btYes){
					return ButtonType.YES;
				}else{
					return ButtonType.NO;
				}
			}
		});
		
		alert.getDialogPane().getButtonTypes().setAll(	btYes,
														btNo);
		
		alert.showAndWait();
		if(alert.getResult()==ButtonType.YES){
			int index = Main.Main.vTable.getActualTable().getSelectionModel().getSelectedIndex();
			Zbozi sZbozi = (Zbozi)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
			int code = sZbozi.getCode();
			Main.Main.vTable.getActualTable().getItems().remove(index);
			map.remove(code);
		}else{
			return;
		}
		
	}

	/**
	 * Vytvoří novou instanci systému zboží
	 */
	public StockZbozi(){
		super();
		this.map = new HashMap<Integer,items.Zbozi>();
	}
	
	
	/**
	 * Prida zbozi do skladu.
	 * 
	 * @param zbozi	pridavane zbozi
	 */
	public void addZbozi(items.Zbozi zbozi){
		int code = createCode();
		zbozi.setCode(code);
		map.put(code, zbozi);
	}
	
	/**
	 * Třída spravující formulář pro fakturu 
	 * attributy l%název% představují nápisy ve formuláři
	 * attributy tf%název% představují zobrazovaná data ve formuláři 
	 * 
	 * @author Martin Hamet
	 *
	 */
	private class FormZbozi{
		//Code
		Label lCode;
		int code;
		Label tfCode;
		
		//Name
		Label lName;
		TextField tfName;
		
		//Quantity
		Label lQuantity;
		HBox hb = new HBox();
		
		ChoiceBox<items.Zbozi.Units> unitCHB;
		TextField tfQuantity;
		
		//Price
		Float checkedPrice;
		Label lPrice;
		TextField tfPrice;
	
		//Manufacturer
		Label lManufacturer;
		TextField tfManufacturer;
		
		//info
		Label lInfo;
		TextField tfInfo;
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku
		 * 
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOk(){
			if(	tfName.getText().isEmpty()||
				tfPrice.getText().isEmpty()||
				tfManufacturer.getText().isEmpty()||
				unitCHB.getValue()==null){
					
					//Some of inputs are empty
					wrongInput(ER_0);
					return false;
			}else{
				try {
					checkedPrice = Float.parseFloat(tfPrice.getText());
				} catch (Exception e) {
					wrongInput(ER_2);
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Nastaví editovatelnost prvků ve formuláři
		 * @param editable	editovatelnost formuláře
		 */
		public void setEditable(boolean editable){
			boolean dis = !editable;
			
			Double op;			//opacity
			if(editable){
				op = 1.0;
			}else{
				op = 0.75;
			}
			
			tfName.setDisable(dis);
			tfName.setOpacity(op);
			
			tfPrice.setDisable(dis);
			tfPrice.setOpacity(op);
			
			tfManufacturer.setDisable(dis);
			tfManufacturer.setOpacity(op);
			
			
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
			
			unitCHB.setDisable(dis);
			unitCHB.setOpacity(op);
			
		}
		
		/**
		 * Nastaví data formuláře podle zadaného zboží
		 * @param zbozi	zadané zboží
		 */
		public void setTexts(items.Zbozi zbozi){
			setEditable(false);
			tfCode.setText(zbozi.getCode()+"");
			tfName.setText(zbozi.getName());
			tfQuantity.setText(zbozi.getQuantity()+"");
			unitCHB.setValue(zbozi.getUnit());
			tfPrice.setText(zbozi.getPrice()+"");
			tfManufacturer.setText(zbozi.getManufacturer());
			tfInfo.setText(zbozi.getInfo());
		}
		
		/**
		 * Vytvoří novou instanci třídy a nastaví zadaný GridPane
		 * @param grid	zadaný gridPane
		 */
		public FormZbozi(GridPane grid){
			lCode = new Label("Kód zboží: ");
			code = Main.Main.safe.zbozi.lastCode+1;
			tfCode = new Label(code+"");
			lName = new Label("Název: ");
			tfName = new TextField();
			lQuantity = new Label("Množství:");
			lPrice = new Label("Cena");
			unitCHB = new ChoiceBox<items.Zbozi.Units>(
					FXCollections.observableArrayList(
							items.Zbozi.Units.ks, 
							items.Zbozi.Units.bal,
							items.Zbozi.Units.m));
			unitCHB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<items.Zbozi.Units>() {

				@Override
				public void changed(ObservableValue<? extends Units> observable, Units oldValue, Units newValue) {
					lPrice.setText("Cena/"+newValue+": ");	//price
					}
				});
			
			tfQuantity = new TextField();
			tfQuantity.setOpacity(0.75);
			tfQuantity.setEditable(false);
			hb.getChildren().addAll(tfQuantity, unitCHB);
			tfPrice = new TextField();
			lManufacturer = new Label("Výrobce:");
			tfManufacturer = new TextField();
			lInfo = new Label("Typ/Poznámka: ");
			tfInfo = new TextField();
			
			//grid
			grid.add(lCode, 1, 1);			grid.add(tfCode, 2, 1);
			grid.add(lName, 1, 2);			grid.add(tfName, 2, 2);
			grid.add(lQuantity, 1, 3);		grid.add(hb, 2, 3);
			grid.add(lPrice, 1, 4);			grid.add(tfPrice, 2, 4);
			grid.add(lManufacturer, 1, 5);	grid.add(tfManufacturer, 2, 5);
			grid.add(lInfo, 1, 6);			grid.add(tfInfo, 2, 6);
		}
		
	}
	
}
