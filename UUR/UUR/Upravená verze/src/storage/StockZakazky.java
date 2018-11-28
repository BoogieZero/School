package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import items.Zakazka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Třída představující systém zakázek a jejich manipulaci
 * 
 * @author Martin Hamet
 *
 */
public class StockZakazky extends Stock {
	/**
	 * Mapa zakázek v systému
	 */
	private Map<Integer,items.Zakazka> map;
	
	/**
	 * Vrátí observablelist zakázek v systému
	 * @return	observablelist zakázek v systému
	 */
	public ObservableList<items.Zakazka> getZakazky(){
		List<items.Zakazka> listZakazky = new ArrayList<items.Zakazka>(map.values());
		ObservableList<items.Zakazka> obZakazky = FXCollections.observableArrayList(listZakazky);
		return obZakazky;
	}
	
	/**
	 * Vytvoří formulář detailu zakázky
	 * @param faktura	aktuálně vybraná zakázky
	 * @return	node s formulářem pro detail zakázky
	 */
	public Node formDetail(Zakazka zakazka){
		GridPane grid = getGrid();
		TableView<items.Vydejka> fTable = Main.Main.vTable.gettVydejky();
		fTable.getItems().clear();
		fTable.getItems().setAll(zakazka.getMaterialUsedList());
		fTable.setOpacity(0.75);
		FormZakazka myGrid = new FormZakazka(grid);
		
		
		myGrid.setTexts(zakazka);
		
		//left side
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
			 * Umožní edita zakázky
			 */
			@Override
			public void handle(ActionEvent event) {
				if(zakazka.getClosed()==storage.Closed.CLOSED){
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_41);
					return;
				}
				buttonSave.setDisable(false);
				buttonEdit.setDisable(true);
				myGrid.setEditable(true);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Uloží změny v zakázce
			 */
			@Override
			public void handle(ActionEvent event) {
				
				//myGrid.setEditable(false);
				if(myGrid.isInputOk()){
					buttonEdit.setDisable(false);
					buttonSave.setDisable(true);
					myGrid.tfDeadline.setText(myGrid.dp.getValue().toString());
					items.Zakazka newZakazka = new Zakazka(	myGrid.tfName.getText(), 
															myGrid.tfCustomer.getText(), 
															myGrid.dp.getValue(), 
															Float.parseFloat(myGrid.tfPriceTag.getText()), 
															Float.parseFloat(myGrid.tfWorkPrice.getText()));
					newZakazka.setInfo(myGrid.tfInfo.getText());
					zakazka.setAll(newZakazka);
					myGrid.setTexts(zakazka);
					
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
		VBox vbfTable = new VBox();
		vbfTable.getChildren().addAll(fTable);
		VBox zbTableBox = new VBox();
		zbTableBox.setSpacing(10);
		zbTableBox.setPadding(new Insets(10));
		Label lContain = new Label("Zakázka je spojena s výdejkami:");
		zbTableBox.getChildren().addAll(lContain,
										vbfTable);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox,
									zbTableBox);
		
		
		return mainHb;
	}
	
	/**
	 * Vytvoří formulář pro přidání nové zakázky
	 * @return	node s formulářem pro novou zakázky
	 */
	public Node formAdd(){
		GridPane grid = getGrid();
		TableView<items.Vydejka> fTable = Main.Main.vTable.gettVydejky();
		fTable.getItems().clear();
		fTable.setOpacity(0.75);
		FormZakazka myGrid = new FormZakazka(grid);
		
		//left side
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zrušit");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonSave,
											buttonStorno);
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží novou zakázku
			 */
			@Override
			public void handle(ActionEvent event) {
				if(myGrid.isInputOk()){
					items.Zakazka newZakazka = new Zakazka(	myGrid.tfName.getText(), 
															myGrid.tfCustomer.getText(), 
															myGrid.dp.getValue(), 
															Float.parseFloat(myGrid.tfPriceTag.getText()), 
															Float.parseFloat(myGrid.tfWorkPrice.getText()));
					newZakazka.setInfo(myGrid.tfInfo.getText());
					addZakazka(newZakazka);
					
					if(Main.Main.isAloneInSP()){
						@SuppressWarnings("unchecked")
						ObservableList<Zakazka> ol = 
								Main.Main.vTable.getActualTable().getItems();
						ol.add(newZakazka);
					}else{
						Main.Main.vTable.refreshZakazky();
					}
					//Main.Main.setEditing(false);
					Main.Main.removeNodeSP();
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
		VBox vbfTable = new VBox();
		vbfTable.getChildren().addAll(fTable);
		VBox zbTableBox = new VBox();
		zbTableBox.setSpacing(10);
		zbTableBox.setPadding(new Insets(10));
		Label lContain = new Label("Zakázka je spojena s výdejkami:");
		zbTableBox.getChildren().addAll(lContain,
										vbfTable);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.getChildren().addAll(gridBox,
									zbTableBox);
		
		
		return mainHb;
	}
	
	/*
	private void reCount(Zakazka z){
		List<items.Vydejka> list = z.getMaterialUsedList();
		Float suma = 0.f;
		for(items.Vydejka v:list){
			suma += v.getValue();
		}
		
	}
	*/
	
	/**
	 * Vytvoří novou instanci systému zakázek
	 */
	public StockZakazky(){
		super();
		this.map = new HashMap<Integer,items.Zakazka>();
	}
	
	/**
	 * Odstraní zakázku ze systému s potvrzovacím dialogem
	 */
	public void removeZakazka(){
		Zakazka za = (Zakazka)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
		if(za==null){
			wrongInput(ER_01);
			return;
		}
		
		if(za.getClosed()==storage.Closed.CLOSED){
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_32);
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Odstranění zakázky");
		alert.setHeaderText("Chcete odstranit zakázku: "+"\n"+za.toString());
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
			Zakazka sZakazka = (Zakazka)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
			int code = sZakazka.getCode();
			map.remove(code);
			Main.Main.vTable.getActualTable().getItems().remove(index);
			Main.Main.vTable.getActualTable().getSelectionModel().clearSelection();
		}else{
			return;
		}
	}
	
	/**
	 * Prida zakazku do systému.
	 * 
	 * @param zakazka	pridavana zakazka
	 */
	public void addZakazka(items.Zakazka zakazka){
		int code = createCode();
		zakazka.setCode(code);
		map.put(code, zakazka);
	}
	
	/**
	 * Třída spravující formulář pro zakázku 
	 * attributy l%název% představují nápisy ve formuláři
	 * attributy tf%název% představují zobrazovaná data ve formuláři 
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class FormZakazka{
		//table
		TableView<items.Vydejka> fTable;
		
		//Code
		int code;
		Label lCode;
		Label tfCode;
		
		//Name
		Label lName;
		TextField tfName;
		
		//Creation Date
		Label lCreationDate;
		Label tfCreationDate;
		
		//Customer
		Label lCustomer;
		TextField tfCustomer;
		
		//Deadline
		Label lDeadline;
		Label tfDeadline;
		DatePicker dp;
		
		//Price tag
		Label lPriceTag;
		TextField tfPriceTag;
		
		//Material
		Label lMaterialPrice;
		Label tfMaterialPrice;
		
		//Work
		Label lWorkPrice;
		TextField tfWorkPrice;
		
		//Value
		Label lValue;
		Label tfValue;
		
		//Gaint price
		Label lGainPrice;
		Label tfGainPrice;
		
		//Faktura
		Label lFakturaCode;
		Label tfFakturaCode;
		
		//info
		Label lInfo;
		TextField tfInfo;
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku
		 * 
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOk(){
			if(	tfCustomer.getText().isEmpty()||
				tfPriceTag.getText().isEmpty()||
				tfWorkPrice.getText().isEmpty()||
				tfName.getText().isEmpty()||
				dp.getValue()==null){
					
					//Some of the inputs are empty
					wrongInput(ER_0);
					return false;
			}else{
				try {
					Float.parseFloat(tfPriceTag.getText());
					Float.parseFloat(tfWorkPrice.getText());
				} catch (Exception e) {
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_2);
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
			
			//fTable.setEditable(editable);
			//fTable.setOpacity(op);
			
			tfName.setDisable(dis);
			tfName.setOpacity(op);
			
			dp.setVisible(editable);
			tfDeadline.setVisible(dis);
			
			tfCustomer.setDisable(dis);
			tfCustomer.setOpacity(op);
			
			tfPriceTag.setDisable(dis);
			tfPriceTag.setOpacity(op);
			
			tfWorkPrice.setDisable(dis);
			tfWorkPrice.setOpacity(op);
	
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
		}
		
		/**
		 * Nastaví data formuláře podle zadané zakázky
		 * @param z	zadaná zakázka
		 */
		public void setTexts(items.Zakazka z){
			setEditable(false);
			tfCode.setText(z.getCode()+"");
			tfName.setText(z.getName());
			tfCreationDate.setText(z.getCreationDate().toString());
			tfCustomer.setText(z.getCustomer());
			tfValue.setText(z.getValue()+"");
			dp.setValue(z.getDeadline());
			tfMaterialPrice.setText(z.getMaterialPrice()+"");
			tfPriceTag.setText(z.getPriceTag()+"");
			tfWorkPrice.setText(z.getWorkPrice()+"");
			tfGainPrice.setText(z.getGainPrice()+"");
			tfInfo.setText(z.getInfo());
			if(z.getBill()!=null){
				tfFakturaCode.setText(z.getBill().getCode()+"");
			}
			if(dp.getValue()!=null){
				tfDeadline.setText(dp.getValue().toString());
			}
			
		}
		
		/**
		 * Vytvoří novou instanci třídy a nastaví zadaný GridPane
		 * @param grid	zadaný gridPane
		 */
		public FormZakazka(GridPane grid){
			lCode = new Label("Kód zakázky:");
			code = Main.Main.safe.zakazky.lastCode+1;
			lName = new Label("Název zakázky:");
			tfName = new TextField();
			lCreationDate = new Label("Datum vytvoření:");
			tfCreationDate = new Label("Automaticky generováno");
			tfCode = new Label(code+"");
			lCustomer = new Label("Zákazník:");
			tfCustomer = new TextField();
			lDeadline = new Label("Mezní termín:");
			tfDeadline = new Label();
			lPriceTag = new Label("Navrhovaná cena:");
			tfPriceTag = new TextField();
			lMaterialPrice = new Label("Cena materiálu:");
			tfMaterialPrice = new Label();
			lWorkPrice = new Label("Cena za práci:");
			tfWorkPrice = new TextField();
			lValue = new Label("Celková hodnota:");
			tfValue = new Label();
			lGainPrice = new Label("Aktuální stav zisku:");
			tfGainPrice = new Label();
			lFakturaCode = new Label("Kód faktury:");
			tfFakturaCode = new Label();
			lInfo = new Label("Poznámka: ");
			tfInfo = new TextField();
			
			dp = new DatePicker();
			StackPane sp = new StackPane();
			sp.setAlignment(Pos.CENTER_LEFT);
			sp.getChildren().setAll(tfDeadline, dp);
			
			//grid
			grid.add(lName, 1, 1); 			grid.add(tfName, 2, 1);
			grid.add(lCode, 1, 2);			grid.add(tfCode, 2, 2);
			grid.add(lCreationDate, 1, 3);	grid.add(tfCreationDate, 2, 3);
			grid.add(lCustomer, 1, 4);		grid.add(tfCustomer, 2, 4);
			grid.add(lDeadline, 1, 5);		grid.add(sp, 2, 5);
			grid.add(lPriceTag, 1, 6);		grid.add(tfPriceTag, 2, 6);
			grid.add(lMaterialPrice, 1, 7);	grid.add(tfMaterialPrice, 2, 7);
			grid.add(lWorkPrice, 1, 8); 	grid.add(tfWorkPrice, 2, 8);
			grid.add(lValue, 1, 9); 		grid.add(tfValue, 2, 9);
			grid.add(lGainPrice, 1, 10); 	grid.add(tfGainPrice, 2, 10);
			grid.add(lFakturaCode, 1, 11);	grid.add(tfFakturaCode, 2, 11);
			grid.add(lInfo, 1, 12); 		grid.add(tfInfo, 2, 12);
		}
		
	}
	
}
