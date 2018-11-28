package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Main.ControlList.ControlListType;
import items.Prijemka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Třída představující systém příjemek a jejich manipulaci
 * @author Martin Hamet
 *
 */
public class StockPrijemky extends Stock {
	/**
	 * Mapa příjemek v systému
	 */
	private Map<Integer,items.Prijemka> map;
	
	/**
	 * Vrátí observablelist příjemek v systému
	 * @return	observablelist příjemek v systému
	 */
	public ObservableList<items.Prijemka> getPrijemky(){
		List<items.Prijemka> listPrijemky = new ArrayList<items.Prijemka>(map.values());
		ObservableList<items.Prijemka> obPrijemky = FXCollections.observableArrayList(listPrijemky);
		return obPrijemky;
	}
	
	/**
	 * Vytvoří formulář detailu příjemky
	 * @param prijemka	vybraná příjemka
	 * @return	node s formulářem pro detail příjemky
	 */
	public Node formDetail(Prijemka prijemka){
		GridPane grid = getGrid();
		EditingListZbozi elz = new EditingListZbozi();
		TableView<items.Zbozi> fTable = elz.getfTable(ControlListType.PRIJEMKY);
		Button tabEdit = new Button("Upravit zboží");
		tabEdit.setVisible(false);
		FormPrijemka myGrid = new FormPrijemka(grid, fTable, tabEdit);
		
		myGrid.tfExpenses.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
				
			}
		});
		
		//setting texts
		fTable.getItems().setAll(prijemka.getItems());
		fTable.setEditable(false);
		fTable.setOpacity(0.75);
		myGrid.setTexts(prijemka);
		
		//left side
		Button buttonEdit = new Button("Upravit");
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zavřít");
		
		buttonSave.setDisable(true);
		
		HBox hbEdSaveStorno = new HBox();
		hbEdSaveStorno.setSpacing(10);
		hbEdSaveStorno.setAlignment(Pos.CENTER);
		hbEdSaveStorno.getChildren().addAll(buttonEdit,
											buttonSave,
											buttonStorno);
		
		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Zpřístupní editaci příjemky
			 */
			@Override
			public void handle(ActionEvent event) {
				buttonEdit.setDisable(true);
				buttonSave.setDisable(false);
				myGrid.setEditable(true);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží změny v příjemce
			 */
			@Override
			public void handle(ActionEvent event) {
				
				recountValue(fTable, myGrid);
				if(myGrid.isInputOk()){
					buttonSave.setDisable(true);
					buttonEdit.setDisable(false);
					items.Prijemka newPrijemka = new Prijemka(	myGrid.tfSupplier.getText(),
																fTable.getItems());
					newPrijemka.setValue(valuePrijemky(fTable.getItems()));
					newPrijemka.setInfo(myGrid.tfInfo.getText());
					newPrijemka.setExpencses(Float.parseFloat(myGrid.tfExpenses.getText()));
					
					prijemka.setAll(newPrijemka);
					
					System.out.println("V: "+prijemka.getValue());
					System.out.println("E: "+prijemka.getExpencses());
					Main.Main.vTable.refreshPrijemky();
					Main.Main.vTable.refreshZbozi();
					
					Main.Main.refreshActualTable();
				}
			}
		});
		
		/**
		 * Uzavře formulář detailu
		 */
		buttonStorno.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//Main.Main.setEditing(false);
				Main.Main.removeNodeSP();
				
			}
		});
		
		VBox gridBox = new VBox();
		gridBox.setSpacing(10);
		gridBox.getChildren().addAll(grid,hbEdSaveStorno);
		
		//right side
		HBox vbfTable = new HBox();
		vbfTable.getChildren().add(fTable);
		
		VBox zbTableBox = new VBox();
		zbTableBox.setSpacing(10);
		zbTableBox.setPadding(new Insets(10));
		
		
		Label lContain = new Label("Příjemka obsahuje:");
		zbTableBox.getChildren().addAll(lContain,
										vbfTable,
										tabEdit);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox,
									zbTableBox);
		
		return mainHb;
	}
	
	/**
	 * Vytvoří formulář pro přidání nové příjemky
	 * @return	node s formulářem pro novou příjemku
	 */
	public Node formAdd(){
		GridPane grid = getGrid();
		EditingListZbozi elz = new EditingListZbozi();
		TableView<items.Zbozi> fTable = elz.getfTable(ControlListType.PRIJEMKY);
		
		Button tabReCount = new Button("Přepočítat");
		Button tabEdit = new Button("Upravit zboží");
		HBox hbTabReEd = new HBox();
		hbTabReEd.setSpacing(10);
		hbTabReEd.getChildren().setAll(	tabEdit,
										tabReCount);
		
		FormPrijemka myGrid = new FormPrijemka(grid, fTable, tabEdit);
		
		myGrid.tfExpenses.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
				
			}
		});
		
		//left side
		
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zrušit");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonSave,
											buttonStorno);
		
		
		
		
		tabReCount.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Přepočítá hodnoty příjemky
			 */
			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží novou příjemku
			 */
			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
				if(myGrid.isInputOk()){
					items.Prijemka newPrijemka = new Prijemka(	myGrid.tfSupplier.getText(),
																fTable.getItems());
			
					newPrijemka.setValue(valuePrijemky(fTable.getItems()));
					newPrijemka.setExpencses(Float.parseFloat(myGrid.tfExpenses.getText()));
					newPrijemka.setInfo(myGrid.tfInfo.getText());
					addZboziByPrijemka(newPrijemka);
					addPrijemka(newPrijemka);
					
					if(Main.Main.isAloneInSP()){
						@SuppressWarnings("unchecked")
						ObservableList<Prijemka> ol = 
								Main.Main.vTable.getActualTable().getItems();
						ol.add(newPrijemka);
					}else{
						Main.Main.vTable.refreshPrijemky();
					}
					
					//Main.Main.setEditing(false);
					Main.Main.removeNodeSP();
				}
				
			}
		});
		
		buttonStorno.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uzavře formulář
			 */
			@Override
			public void handle(ActionEvent event) {
				//Main.Main.setEditing(false);
				Main.Main.removeNodeSP();
				
			}
		});
		
		
		VBox gridBox = new VBox();
		gridBox.setSpacing(10);
		gridBox.getChildren().addAll(grid,hbReSaveStorno);
		
		//right side
		HBox vbfTable = new HBox();
		vbfTable.getChildren().add(fTable);
		
		VBox zbTableBox = new VBox();
		zbTableBox.setSpacing(10);
		zbTableBox.setPadding(new Insets(10));
		
		
		tabEdit.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Vytvoří formulář pro úpravu zboží
			 */
			@Override
			public void handle(ActionEvent event) {
				elz.editForm(fTable, vbfTable);
			}
		});
		
		
		Label lContain = new Label("Příjemka obsahuje:");
		zbTableBox.getChildren().addAll(lContain,
										vbfTable,
										hbTabReEd);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox,
									zbTableBox);
		
		return mainHb;
	}
	
	/**
	 * Přidá zboží podle obsahu příjemky
	 * @param p	daná příjemka
	 */
	private void addZboziByPrijemka(Prijemka p){
		List<items.Zbozi> list = p.getItems();
		Map<Integer,items.Zbozi> zbMap = Main.Main.safe.zbozi.getZboziMap();
		items.Zbozi puvZb;
		for(items.Zbozi sZb: list){
			puvZb = zbMap.get(sZb.getCode());
			
			//changing Zbozi price to actual price
			puvZb.setPrice(sZb.getPrice());
			
			//puvZb.addPrijemkaUsed(p);				//connecting Prijemka
			puvZb.addQuantity(sZb.getQuantity());	//adding quantity
			
		}
		Main.Main.vTable.refreshZbozi();
	}
	
	/**
	 * Přepočítá hodnoty příjemky
	 * @param fTable	tabulka zboží v příjemce
	 * @param myGrid	odkaz na formulář příjemky
	 */
	private void recountValue(TableView<items.Zbozi> fTable, FormPrijemka myGrid){
		Float prijVal = valuePrijemky(fTable.getItems());
		try {
			prijVal += Float.parseFloat(myGrid.tfExpenses.getText());
		} catch (Exception e) {
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_21);
			return;
		}
		myGrid.tfValue.setText(prijVal+"");
	}
	
	/**
	 * Zjistí celkovou hodnotu příjemky
	 * @param list	list zboží příjemky
	 * @return	hodnotu příjemky
	 */
	private Float valuePrijemky(ObservableList<items.Zbozi> list){
		Float suma = 0.f;
		for(items.Zbozi sZbozi:list){
			suma += (sZbozi.getPrice()*sZbozi.getQuantity());
		}
		return suma;
	}
	
	/**
	 * Vytvoří novou instanci systému příjemek
	 */
	public StockPrijemky(){
		super();
		this.map = new HashMap<Integer,items.Prijemka>();
	}
	
	/**
	 * Prida prijemku do zaznamu.
	 * 
	 * @param prijemka	pridavana prijemka
	 */
	public void addPrijemka(items.Prijemka prijemka){
		int code = createCode();
		prijemka.setCode(code);
		map.put(code, prijemka);
	}
	
	/**
	 * Třída spravující formulář pro příjemku 
	 * attributy l%název% představují nápisy ve formuláři
	 * attributy tf%název% představují zobrazovaná data ve formuláři 
	 * 
	 * @author Martin Hamet
	 *
	 */
	private class FormPrijemka{
		//table
		//TableView<items.Zbozi> fTable;
		//Button tabEdit;	//Button under table
		
		//Code
		Label lCode;
		int code;
		Label tfCode;
		
		//Creation Date
		Label lCreationDate;
		Label tfCreationDate;
		
		//numberOfItems
		Label lItemsCount;
		Label tfItemsCount;
		
		//Expenses
		Label lExpenses;
		TextField tfExpenses;
		
		//Value
		//Float checkedPrice;
		Label lValue;
		Label tfValue;
		
		//Supplier
		Label lSupplier;
		TextField tfSupplier;
		
		//info
		Label lInfo;
		TextField tfInfo;
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku
		 * 
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOk(){
			if(	tfValue.getText().isEmpty()||
				tfSupplier.getText().isEmpty()||
				tfExpenses.getText().isEmpty()){
					
					//Some of the inputs are empty
					wrongInput(ER_0);
					return false;
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
			
			//fTable.setEditable(editable);
			//fTable.setOpacity(op);
			
			//tabEdit.setDisable(dis);
			
			tfItemsCount.setDisable(dis);
			tfItemsCount.setOpacity(op);
			
			tfExpenses.setDisable(dis);
			tfExpenses.setOpacity(op);
			
			tfSupplier.setDisable(dis);
			tfSupplier.setOpacity(op);
			
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
		}
		
		/**
		 * Nastaví data formuláře podle zadané faktury
		 * @param pr	zadaná příjemka
		 */
		public void setTexts(items.Prijemka pr){
			setEditable(false);
			tfCode.setText(pr.getCode()+"");
			tfCreationDate.setText(pr.getCreationDate().toString());
			tfItemsCount.setText(pr.getItemsCount()+"");
			tfExpenses.setText(pr.getExpencses()+"");
			tfValue.setText(pr.getValue()+pr.getExpencses()+"");
			tfSupplier.setText(pr.getSupplier());
			tfInfo.setText(pr.getInfo());
		}
		
		/**
		 * Vytvoří novou instanci třídy a nastaví zadaný GridPane
		 * @param grid	zadaný gridPane
		 * @param fTable	tabulka zboží v příjemce
		 * @param tabEdit	tlačítko pro editování zboží v příjemce
		 */
		public FormPrijemka(GridPane grid, TableView<items.Zbozi> fTable, Button tabEdit){
			//this.fTable = fTable;
			//this.tabEdit = tabEdit;
			lCode = new Label("Kód příjemky: ");
			code = Main.Main.safe.prijemky.lastCode+1;
			lCreationDate = new Label("Datum vytvoření:");
			tfCreationDate = new Label("Automaticky generováno");
			tfCode = new Label(code+"");
			lItemsCount = new Label("Počet položek:");
			tfItemsCount = new Label("Automaticky generováno");
			lExpenses = new Label("Další výdaje (Kč):");
			tfExpenses = new TextField("0");
			lValue = new Label("Hodnota (Kč):");
			tfValue = new Label();
			lSupplier = new Label("Dodavatel:");
			tfSupplier = new TextField();
			lInfo = new Label("Poznámka: ");
			tfInfo = new TextField();
			
			//grid
			grid.add(lCode, 1, 1);			grid.add(tfCode, 2, 1);
			grid.add(lCreationDate, 1, 2);	grid.add(tfCreationDate, 2, 2);
			grid.add(lSupplier, 1, 3);		grid.add(tfSupplier, 2, 3);
			grid.add(lItemsCount, 1, 4);	grid.add(tfItemsCount, 2, 4);
			grid.add(lExpenses, 1, 5);		grid.add(tfExpenses, 2, 5);
			grid.add(lValue, 1, 6);			grid.add(tfValue, 2, 6);
			grid.add(lInfo, 1, 7);			grid.add(tfInfo, 2, 7);
		}
		
	}
}
