package storage;

import Main.ControlList.ControlListType;
import Main.Main;
import Main.Main.Refresh;
import items.Zbozi;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Třída formuláře pro úpravu seznamu zboží dokladu
 * @author Martin Hamet
 *
 */
public class EditingListZbozi {
	/**
	 * Tabulka skladového zboží
	 */
	private static TableView<Zbozi> sTable;
	
	/**
	 * Typ pro který byl formulář volán
	 */
	private ControlListType type;
	
	/**
	 * Vrátí tabulku skladového zboží
	 * @return	tabulka skladového zboží
	 */
	public TableView<Zbozi> getsTable() {
		return sTable;
	}

	/**
	 * Nastaví tabulku skladového zboží
	 * @param sTable	tabulka skladového zboží
	 */
	public void setsTable(TableView<Zbozi> sTable) {
		EditingListZbozi.sTable = sTable;
	}

	/**
	 * Vrátí vytvořenou tabulku vybraného zboží
	 * @param type	typ pro který byl formulář volán
	 * @return	vytvořená tabulka
	 */
	public TableView<Zbozi> getfTable(ControlListType type) {
		this.type = type;
		return setUpTable();
	}

	/**
	 * Vytvoří formulář pro výběr zboží ze skladové tabulky do tabulky vybraného zboží
	 * 
	 * @param nfTable	tabulka vybraného zboží
	 * @param vbfTable	HBox obsahující tabulku vybraného zboží
	 */
	public void editForm(TableView<Zbozi> nfTable, HBox vbfTable){
		sTable = Main.vTable.gettZbozi();
		sTable.setPrefWidth(320);
		HBox hbsTable = new HBox();
		hbsTable.getChildren().add(sTable);
		
		HBox mainHb = new HBox();
		mainHb.setSpacing(10);
		mainHb.setAlignment(Pos.CENTER);
		
		VBox vbAddRemove = new VBox();
		vbAddRemove.setSpacing(10);
		vbAddRemove.setAlignment(Pos.CENTER);
		Button btAdd = new Button("Přidat ->");
		Button btRemove = new Button("Odebrat <-");
		
		btAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Tlačítko přidá do tabulky právě vybraný prvek ze skladového zboží
			 */
			@Override
			public void handle(ActionEvent event) {
				Zbozi sZbozi = (Zbozi)sTable.getSelectionModel().getSelectedItem();
				if(sZbozi==null){
					Main.safe.prijemky.wrongInput(Main.safe.prijemky.ER_01);
					return;
				}
				Zbozi aZbozi = new Zbozi(sZbozi);
				if(type == ControlListType.VYDEJKY){
					aZbozi.setPrice(sZbozi.getPrice());
				}
				nfTable.getItems().add(aZbozi);
			}
		});
		
		
		btRemove.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Tlačítko odebere z tabulky vybraného zboží právě vybraný prvek
			 */
			@Override
			public void handle(ActionEvent event) {
				Zbozi sZbozi = nfTable.getSelectionModel().getSelectedItem();
				if(sZbozi==null){
					Main.safe.prijemky.wrongInput(Main.safe.prijemky.ER_01);
					return;
				}
				int index = nfTable.getSelectionModel().getSelectedIndex();
				nfTable.getItems().remove(index);
			}
		});
		
		vbAddRemove.getChildren().addAll(	btAdd,
											btRemove);
		
		
		VBox vbSklad = new VBox();
		vbSklad.setSpacing(10);
		Label lSklad = new Label("Zboží na skladě:");
		
		Button btCreateZbozi = new Button("Vytvořit nové zboží");
		
		if(type == ControlListType.VYDEJKY){
			btCreateZbozi.setVisible(false);
		}
		

		btCreateZbozi.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Tlačítko otevře formulář pro vytvoření nového zboží.
			 */
			@Override
			public void handle(ActionEvent event) {
				Main.addNodeSP(Main.safe.zbozi.formAdd());
				Main.vTable.refreshZbozi();
				
				//hbsTable.getChildren().setAll(Main.vTable.gettZbozi());
				Main.refreshList.add(new Refresh(sTable, ControlListType.ZBOZI));
				@SuppressWarnings("rawtypes")
				TableColumn col = (TableColumn)sTable.getColumns().get(0);
				col.setVisible(false);
				//col.setVisible(true);
				//sTable.setVisible(false);
			}
		});
		
		vbSklad.getChildren().addAll(	lSklad,
										hbsTable,
										btCreateZbozi);
		
		VBox vbPrij = new VBox();
		vbPrij.setSpacing(10);
		Label lPrijemka = new Label("Zboží v příjemce:");
		vbPrij.getChildren().addAll(lPrijemka,
									nfTable);
		
		mainHb.getChildren().addAll(vbSklad,
									vbAddRemove,
									vbPrij);
		
		Button btFinish = new Button("Hotovo");
		
		btFinish.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Tlačítko zavře aktuální formulář
			 */
			@Override
			public void handle(ActionEvent event) {
				Main.removeNodeSP();
				vbfTable.getChildren().setAll(nfTable);
			}
		});
		
		HBox vbBtFinish = new HBox();
		vbBtFinish.setAlignment(Pos.CENTER);
		vbBtFinish.setPadding(new Insets(10));
		vbBtFinish.getChildren().add(btFinish);
		VBox vbC = new VBox();
		vbC.setSpacing(20);
		vbC.getChildren().addAll(	mainHb,
									vbBtFinish);
		
		Main.addNodeSP(vbC);
		
	}
	
	/**
	 * Připraví tabulku pro formulář
	 */
	public EditingListZbozi(){
		setUpTable();
	}
	
	/**
	 * Připraví tabulku pro formulář
	 */
	@SuppressWarnings("unchecked")
	private TableView<Zbozi> setUpTable(){
		TableView<Zbozi> table = new TableView<Zbozi>();
		table.setEditable(true);
		
		TableColumn<items.Zbozi, Integer> productCode = new TableColumn<items.Zbozi,Integer>("Kód");
		TableColumn<items.Zbozi, String> productName = new TableColumn<items.Zbozi,String>("Název");
		TableColumn<items.Zbozi, Integer> productQuantity = new TableColumn<items.Zbozi, Integer>("Množství");
		TableColumn<items.Zbozi, Float> productPrice = new TableColumn<items.Zbozi, Float>("Cena");
		TableColumn<items.Zbozi, String> productManufacturer = new TableColumn<items.Zbozi, String>("Výrobce");
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Zbozi,Integer>("code"));
		productName.setCellValueFactory(new PropertyValueFactory<items.Zbozi,String>("name"));
		
		productQuantity.setCellValueFactory(new PropertyValueFactory<items.Zbozi, Integer>("quantity"));
		Callback<TableColumn<Zbozi,Integer>, TableCell<Zbozi,Integer>> quantFactory = 
				new Callback<TableColumn<Zbozi,Integer>, TableCell<Zbozi,Integer>>() {

					@Override
					public TableCell<Zbozi, Integer> call(TableColumn<Zbozi, Integer> param) {
						
						TextFieldTableCell<Zbozi, Integer> tfc = new TextFieldTableCell<Zbozi,Integer>(new StringConverter<Integer>() {

							@Override
							public Integer fromString(String string) {
								int a;
								try {
									a = Integer.parseInt(string);
								} catch (Exception e) {
									Main.safe.zbozi.wrongInput(Main.safe.zbozi.ER_1);
									return 0;
								}
								return a;
							}

							@Override
							public String toString(Integer object) {
								// TODO Auto-generated method stub
								return object.toString();
							}
						}){
							@Override
							public void updateItem(Integer item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									if(getTableRow()==null||getTableRow().getItem()==null){
										setText(null);
										return;
									}
									Zbozi sZbozi = (Zbozi)getTableRow().getItem();
									
									setText(getItem().toString()+" "+
												sZbozi.getUnit().toString());
								}else{
									setText(null);
								}
							}
						};
						tfc.setAlignment(Pos.CENTER_RIGHT);
						return tfc;
					}
				};
		productQuantity.setCellFactory(quantFactory);
		
		productPrice.setCellValueFactory(new PropertyValueFactory<items.Zbozi, Float>("price"));
		
		Callback<TableColumn<Zbozi,Float>, TableCell<Zbozi,Float>> priceFactory = 
				new Callback<TableColumn<Zbozi,Float>, TableCell<Zbozi,Float>>(){
					
					@Override
					public TableCell<Zbozi, Float> call(TableColumn<Zbozi, Float> param) {
						TextFieldTableCell<Zbozi, Float> tfc = new TextFieldTableCell<Zbozi,Float>(new StringConverter<Float>() {

							@Override
							public Float fromString(String string) {
								Float a;
								try {
									a = Float.parseFloat(string);
								} catch (Exception e) {
									Main.safe.zbozi.wrongInput(Main.safe.zbozi.ER_1);
									return 0.f;
								}
								return a;
							}

							@Override
							public String toString(Float object) {
								return object.toString();
							}
						}){
							@Override
							public void updateItem(Float item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									setText(getItem().toString()+" Kč");
								}else{
									setText(null);
								}
							}
						};
						tfc.setAlignment(Pos.CENTER_RIGHT);
						return tfc;
					}
			
		};
		productPrice.setCellFactory(priceFactory);
		productManufacturer.setCellValueFactory(new PropertyValueFactory<Zbozi, String>("manufacturer"));
		
		productQuantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Zbozi,Integer>>() {

			@Override
			public void handle(CellEditEvent<Zbozi, Integer> event) {
				int index = event.getTablePosition().getRow();
				int quantity = event.getNewValue();
				event.getTableView().getItems().get(index).setQuantity(quantity);
			}
		});
		productPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Zbozi,Float>>() {

			@Override
			public void handle(CellEditEvent<Zbozi, Float> event) {
				int index = event.getTablePosition().getRow();
				Float price = event.getNewValue();
				event.getTableView().getItems().get(index).setPrice(price);
			}
		});
		
		if(type == ControlListType.VYDEJKY){
			productPrice.setEditable(false);
		}
		
		table.getColumns().addAll(	productCode,
									productName,
									productQuantity,
									productPrice);
		
		return table;
	}
}
