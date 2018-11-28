package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Main.ControlList.ControlListType;
import items.Vydejka;
import items.Zakazka;
import items.Zbozi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Třída představující systém výdejek a jejich manipulaci
 * @author Martin Hamet
 *
 */
public class StockVydejky extends Stock {
	/**
	 * Mapa výdejek v systému
	 */
	private Map<Integer,items.Vydejka> map;

	/**
	 * Vrátí observablelist faktur v systému
	 * @return	observablelist faktur v systému
	 */
	public ObservableList<items.Vydejka> getVydejky(){
		List<items.Vydejka> listVydejky = new ArrayList<items.Vydejka>(map.values());
		ObservableList<items.Vydejka> obVydejky = FXCollections.observableArrayList(listVydejky);
		return obVydejky;
	}
	
	/**
	 * Vytvoří formulář detailu výdejky
	 * @param vydejka	aktuálně vybraná výdejka
	 * @return	node s formulářem pro detail výdejky
	 */
	public Node formDetail(Vydejka vydejka){
		GridPane grid = getGrid();
		EditingListZbozi elz = new EditingListZbozi();
		TableView<items.Zbozi> fTable = elz.getfTable(ControlListType.VYDEJKY);
		fTable.setEditable(false);
		fTable.setOpacity(0.75);
		fTable.getItems().setAll(vydejka.getItems());
		ChooseListZakazka chLz = new ChooseListZakazka();
		chLz.setChosenZakazka(vydejka.getProject());
		
		//Zakazka zak = vydejka.getProject();
		
		//Button tabReCount = new Button("Přepočítat");
		Button tabEdit = new Button("Upravit zboží");
		tabEdit.setVisible(false);
										
		
		Button chZakazka = new Button("Vybrat zakázku");
		
		FormVydejka myGrid = new FormVydejka(grid, fTable, tabEdit, chZakazka, chLz);
		
		chZakazka.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Vytvoří formulář pro vybrání zakázky
			 */
			@Override
			public void handle(ActionEvent event) {
				chLz.chooseZakazkaForm(myGrid, ControlListType.VYDEJKY);
				
			}
		});
		
		myGrid.setTexts(vydejka);
		
		
		//left side
		myGrid.tfExpenses.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
				
			}
		});
		
		
		Button buttonEdit = new Button("Upravit");
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zavřít");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonEdit,
											buttonSave,
											buttonStorno);
		buttonSave.setDisable(true);
		
		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Umožní editaci výdejky
			 */
			@Override
			public void handle(ActionEvent event) {
				if(vydejka.getClosed()==storage.Closed.CLOSED){
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_42);
					return;
				}
				buttonSave.setDisable(false);
				buttonEdit.setDisable(true);
				myGrid.setEditable(true);
			}
		});
		
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží úpravy ve výdejce
			 */
			@Override
			public void handle(ActionEvent event) {
				
				recountValue(fTable, myGrid);
				if(myGrid.isInputOk()){
					buttonEdit.setDisable(false);
					buttonSave.setDisable(true);
					myGrid.setEditable(false);
					vydejka.setExpencses(Float.parseFloat(myGrid.tfExpenses.getText()));
					vydejka.setInfo(myGrid.tfInfo.getText());
					vydejka.setProject(chLz.getChosenZakazka());
					Main.Main.refreshActualTable();
				}
			}
		});
		
		buttonStorno.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Zavře formulář
			 */
			@Override
			public void handle(ActionEvent event) {
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
			 * Vytvoří formulář pro úpravu položek výdejky
			 */
			@Override
			public void handle(ActionEvent event) {
				elz.editForm(fTable, vbfTable);
			}
		});
		
		
		Label lContain = new Label("Příjemka obsahuje:");
		zbTableBox.getChildren().addAll(lContain,
										vbfTable);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox,
									zbTableBox);
		
		return mainHb;
		
	}
	
	/**
	 * Vytvoří formulář pro přidání nové výdejky
	 * @return	node s formulářem pro novou výdejku
	 */
	public Node formAdd(){
		GridPane grid = getGrid();
		EditingListZbozi elz = new EditingListZbozi();
		TableView<items.Zbozi> fTable = elz.getfTable(ControlListType.VYDEJKY);
		ChooseListZakazka chLz = new ChooseListZakazka();
		//Zakazka zak = new Zakazka();
		//Zakazka zak = new Zakazka();
		
		Button tabReCount = new Button("Přepočítat");
		Button tabEdit = new Button("Upravit zboží");
		HBox hbTabReEd = new HBox();
		hbTabReEd.setSpacing(10);
		hbTabReEd.getChildren().setAll(	tabEdit,
										tabReCount);
										
		
		Button chZakazka = new Button("Vybrat zakázku");
		FormVydejka myGrid = new FormVydejka(grid, fTable, tabEdit, chZakazka, chLz);
		
		//left side
		myGrid.tfExpenses.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
				
			}
		});
		chZakazka.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Vytvoří formulář pro výběr zakázky
			 */
			@Override
			public void handle(ActionEvent event) {
				chLz.chooseZakazkaForm(myGrid, ControlListType.VYDEJKY);
				
			}
		});
		
		
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zrušit");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonSave,
											buttonStorno);
		
		tabReCount.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Přepočítá hodnoty výdejky
			 */
			@Override
			public void handle(ActionEvent event) {
				recountValue(fTable, myGrid);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží výdejku do systému
			 */
			@Override
			public void handle(ActionEvent event) {	
				recountValue(fTable, myGrid);
				if(myGrid.isInputOkForCreating((fTable.getItems()))){
					items.Vydejka newVydejka = new Vydejka(fTable.getItems());
					newVydejka.setValue(valueVydejky(fTable.getItems()));
					newVydejka.setExpencses(Float.parseFloat(myGrid.tfExpenses.getText()));
					newVydejka.setInfo(myGrid.tfInfo.getText());
					decZboziByVydejka(newVydejka);
					
					addVydejka(newVydejka);
					newVydejka.setProject(chLz.getChosenZakazka());
					
					if(Main.Main.isAloneInSP()){
						@SuppressWarnings("unchecked")
						ObservableList<Vydejka> ol = 
								Main.Main.vTable.getActualTable().getItems();
						ol.add(newVydejka);
					}else{
						Main.Main.vTable.refreshVydejky();
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
			 * Vytvoří formulář pro úpravu položek výdejky
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
	 * Odečte zboží ze skladu podle výdejky
	 * @param v
	 */
	private void decZboziByVydejka(Vydejka v){
		List<items.Zbozi> list = v.getItems();
		Map<Integer,items.Zbozi> zbMap = Main.Main.safe.zbozi.getZboziMap();
		items.Zbozi puvZb;
		for(items.Zbozi sZb: list){
			puvZb = zbMap.get(sZb.getCode());
			
			//changing Zbozi price to actual price
			//puvZb.setPrice(sZb.getPrice());
			
			//puvZb.addPrijemkaUsed(p);				//connecting Prijemka
			puvZb.addQuantity(-sZb.getQuantity());	//decrementing quantity
			
		}
		Main.Main.vTable.refreshZbozi();
	}
	
	/**
	 * Přepočítá hodnoty výdejky
	 * @param fTable	tabulka položek výdejky
	 * @param myGrid	odkaz na formulář výdejky
	 */
	private void recountValue(TableView<items.Zbozi> fTable, FormVydejka myGrid){
		Float prijVal = valueVydejky(fTable.getItems());
		try {
			prijVal += Float.parseFloat(myGrid.tfExpenses.getText());
		} catch (Exception e) {
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_21);
			return;
		}
		myGrid.tfValue.setText(prijVal+"");
	}
	
	/**
	 * Zjstí hodnotu výdejky
	 * @param list	list položek výdejky
	 * @return	hodnota výdejky
	 */
	private Float valueVydejky(ObservableList<items.Zbozi> list){
		Float suma = 0.f;
		for(items.Zbozi sZbozi:list){
			suma += (sZbozi.getPrice()*sZbozi.getQuantity());
		}
		return suma;
	}
	
	/**
	 * Vytvoří novou instanci systému výdejek
	 */
	public StockVydejky(){
		super();
		this.map = new HashMap<Integer,items.Vydejka>();
	}
	
	/**
	 * Odstraní výdejku ze systému s potvrzovacím dialogem
	 */
	public void removeVydejka(){
		Vydejka vy = (Vydejka)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
		if(vy==null){
			wrongInput(ER_01);
			return;
		}
		
		if(vy.getClosed()==storage.Closed.CLOSED){
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_32);
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Odstranění výdejky");
		alert.setHeaderText("Chcete odstranit výdejku: "+"\n"+vy.toString());
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
			Vydejka sVydejka = (Vydejka)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
			int code = sVydejka.getCode();
			Main.Main.vTable.getActualTable().getItems().remove(index);
			returnZboziByVydejka(sVydejka);
			map.remove(code);
		}else{
			return;
		}
		
	}
	
	/**
	 * Vrátí zboží na sklad podle zadané výdejky
	 * @param v	zadaný výdejka
	 */
	private void returnZboziByVydejka(Vydejka v){
		List<Zbozi> list = v.getItems();
		Map<Integer,items.Zbozi> zbMap = Main.Main.safe.zbozi.getZboziMap();
		items.Zbozi puvZb;
		for(items.Zbozi sZb: list){
			puvZb = zbMap.get(sZb.getCode());
			puvZb.addQuantity(sZb.getQuantity());	//adding quantity
		}
		Main.Main.vTable.refreshZbozi();
	}
	
	/**
	 * Prida vydejku do zaznamu.
	 * 
	 * @param vydejka	pridavana vydejka
	 */
	public void addVydejka(items.Vydejka vydejka){
		int code = createCode();
		vydejka.setCode(code);
		map.put(code, vydejka);
	}
	
	/**
	 * Třída spravující formulář pro fakturu 
	 * attributy l%název% představují nápisy ve formuláři
	 * attributy tf%název% představují zobrazovaná data ve formuláři 
	 * @author Martin Hamet
	 *
	 */
	public class FormVydejka{
		//table
		TableView<items.Zbozi> fTable;
		Button tabEdit;		//Button under table
		Button chZakazka;	//Button for selecting Zakazka
		ChooseListZakazka chLz;
		
		//Code
		Label lCode;
		int code;
		Label tfCode;
		
		//Creation Date
		Label lCreationDate;
		Label tfCreationDate;
		
		//Expenses
		Label lExpenses;
		TextField tfExpenses;
		
		//Value
		Label lValue;
		Label tfValue;
		
		//info
		Label lInfo;
		TextField tfInfo;
		
		//Project
		Label lProject;
			Label lProjectName;
			Label tfProjectName;
			Label lProjectCode;
			Label tfProjectCode;
			Label lDeadline;
			Label tfDeadline;
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku
		 * 
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOk(){
			if(	tfValue.getText().isEmpty()||
				//tfProjectCode.getText().isEmpty()||
				tfExpenses.getText().isEmpty()){
					
					//Some of the inputs are empty
					wrongInput(ER_0);
					return false;
			}
			return true;
		}
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku pro vytvoření nové výdejky
		 * @param list	list položek výdejky
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOkForCreating(List<items.Zbozi> list){
			if(!isInputOk()){
				return false;
			}
			if(!isChosenZakazkaOk()){
				return false;
			}
			Map<Integer ,items.Zbozi> sMap = Main.Main.safe.zbozi.getZboziMap();
			for(items.Zbozi sZb:list){
				if(sZb.getQuantity()>sMap.get(sZb.getCode()).getQuantity()){
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_12);
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Zkntroluje zda je vybraná zakázka v pořádku ( je vybraná a není uzavřená)
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isChosenZakazkaOk(){
			if(chLz.getChosenZakazka() == null){
				Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_31);
				return false;
			}
			if(chLz.getChosenZakazka().getClosed()==Closed.OPEN){
				return true;
			}else{
				Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_3);
				return false;
			}
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
			
			chZakazka.setDisable(dis);
			
			tfExpenses.setDisable(dis);
			tfExpenses.setOpacity(op);
			
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
		}
		
		/**
		 * Nastaví data formuláře podle zadané výdejky
		 * @param vy	zadaná výdejka
		 */
		public void setTexts(items.Vydejka vy){
			setEditable(false);
			tfCode.setText(vy.getCode()+"");
			tfCreationDate.setText(vy.getCreationDate().toString());
			tfExpenses.setText(vy.getExpencses()+"");
			tfValue.setText(vy.getValue()+vy.getExpencses()+"");
			tfInfo.setText(vy.getInfo());
				items.Zakazka za = vy.getProject();
				setTextsForZakazka(za);
		}
		
		/**
		 * Nastaví data formuláře podle zadané zakázky
		 * @param za	zadaná zakázka
		 */
		public void setTextsForZakazka(Zakazka za){
			lProject.setText("Na zakázku:");
			lProjectName.setText("Název:");
			tfProjectName.setText(za.getName());
			lProjectCode.setText("Kód zakázky:");
			tfProjectCode.setText(za.getCode()+"");
			lDeadline.setText("Mezní termín:");
			tfDeadline.setText(za.getDeadline().toString());
		}
		
		/**
		 * Vytvoří novou instanci třídy a nastaví zadaný GridPane podle zadaných parametrů
		 * @param grid	zadaný gridPane
		 * @param fTable	tabulka položek výdejky
		 * @param tabEdit	tlačítko pro editaci položek výdejky
		 * @param chZakazka	tlačítko pro výběr zakázky
		 * @param chLz	odkaz na formulář výběru zakázky
		 */
		public FormVydejka(GridPane grid, TableView<items.Zbozi> fTable, Button tabEdit, Button chZakazka, ChooseListZakazka chLz){
			this.fTable = fTable;
			this.tabEdit = tabEdit;
			this.chZakazka = chZakazka;
			this.chLz = chLz;
			lCode = new Label("Kód výdejky: ");
			code = Main.Main.safe.vydejky.lastCode+1;
			lCreationDate = new Label("Datum vytvoření:");
			tfCreationDate = new Label("Automaticky generováno");
			tfCode = new Label(code+"");
			lExpenses = new Label("Další výdaje (Kč):");
			tfExpenses = new TextField("0");
			lValue = new Label("Hodnota (Kč):");
			tfValue = new Label();
			lInfo = new Label("Poznámka: ");
			tfInfo = new TextField();
			lProject = new Label();
			lProjectName = new Label();
			tfProjectName = new Label();
			lProjectCode = new Label();
			tfProjectCode = new Label();
			lDeadline = new Label();
			tfDeadline = new Label();
			
			//grid
			grid.add(lCode, 1, 1);			grid.add(tfCode, 2, 1);
			grid.add(lCreationDate, 1, 2);	grid.add(tfCreationDate, 2, 2);
			grid.add(lExpenses, 1, 3);		grid.add(tfExpenses, 2, 3);
			grid.add(lValue, 1, 4);			grid.add(tfValue, 2, 4);
			grid.add(lInfo, 1, 5);			grid.add(tfInfo, 2, 5);
			grid.add(lProject, 1, 7);		grid.add(chZakazka, 2, 7);
			grid.add(lProjectName, 1, 8); 	grid.add(tfProjectName, 2, 8);
			grid.add(lProjectCode, 1, 9); 	grid.add(tfProjectCode, 2, 9);
			grid.add(lDeadline, 1, 10);		grid.add(tfDeadline, 2, 10);
		}
		
	}
}
