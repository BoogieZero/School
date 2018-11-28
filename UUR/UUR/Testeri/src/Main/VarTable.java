package Main;

import java.time.LocalDate;

import items.Faktura;
import items.Prijemka;
import items.Vydejka;
import items.Zakazka;
import items.Zbozi;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import storage.Closed;

public class VarTable {
	/**
	 * Jediná instance této třídy
	 */
	private static VarTable instance;
	
	/**
	 * Vytvořené nody pro každý typ tabulky
	 */
	private Node zbozi,prijemky,vydejky,zakazky,faktury;
	
	/**
	 * Vytvořené tabulky pro každý typ tabulky
	 */
	@SuppressWarnings("rawtypes")
	private TableView tZbozi,tPrijemky,tVydejky,tZakazky,tFaktury;
	
	/**
	 * Nápis nad tabulkou zboží
	 */
	private final Label labelZbozi = new Label("Zboží");
	
	/**
	 * Nápis nad tabulkou příjemky
	 */
	private final Label labelPrijemky = new Label("Příjemky");
	
	/**
	 * Nápis nad tabulkou výdejky
	 */
	private final Label labelVydejky = new Label("Výdejky");
	
	/**
	 * Nápis nad tabulkou zakázky
	 */
	private final Label labelZakazky = new Label("Zakázky");
	
	/**
	 * Nápis nad tabulkou faktury
	 */
	private final Label labelFaktury = new Label("Faktury");
	
	/**
	 * Font nápisů nad tabulkou 
	 */
	private Font nadpisTabulky = new Font("Arial", 25);
	
	/**
	 * Aktuální vybraný node.
	 */
	private Node actualNode;
	
	/**
	 * Aktuální vybraná tabulka
	 */
	@SuppressWarnings("rawtypes")
	private TableView actualTable;
	
	
	/**
	 * Vrátí tabulku zboží
	 * @return	tabulka zboží
	 */
	public TableView<Zbozi> gettZbozi() {
		return zboziTable();
	}

	/**
	 * Vrátí tabulku příjemek
	 * @return	tabulka příjemek
	 */
	public TableView<Prijemka> gettPrijemky() {
		return prijemkyTable();
	}

	/**
	 * Vrátí tabulku výdejek
	 * @return	tabulka výdejek
	 */
	public TableView<Vydejka> gettVydejky() {
		return vydejkyTable();
	}

	/**
	 * Vrátí tabulku zakázek
	 * @return	tabulka zakázek
	 */
	public TableView<Zakazka> gettZakazky() {
		return zakazkyTable();
	}

	/**
	 * Vrátí tabulku faktur
	 * @return	tabulka faktur
	 */
	public TableView<Faktura> gettFaktury() {
		return fakturyTable();
	}

	/**
	 * Změní aktuální node a tabulku podle zadaného parametru
	 * 
	 * @param type	typ tabulku která se nastaví jako aktuální
	 */
	public void changeNode(ControlList.ControlListType type){
		switch(type){
			case ZBOZI:		actualNode = zbozi; 	
							actualTable = tZbozi;	
							break;
			case PRIJEMKY:	actualNode = prijemky;	
							actualTable = tPrijemky;
							break;
			case VYDEJKY:	actualNode = vydejky;	
							actualTable = tVydejky;	
							break;
			case ZAKAZKY:	actualNode = zakazky;	
							actualTable = tZakazky;	
							break;
			case FAKTURY:	actualNode = faktury;	
							actualTable = tFaktury;	
							break;
							
			case NONE:		break;
				default:	System.out.println("Chyba při výběru typu ze stromu");	
							break;
		}
	}
	
	/**
	 * Vrátí aktuální node
	 * @return	aktuální node
	 */
	public Node getActualNode(){
		return actualNode;
	}
	
	/**
	 * Vrátí aktuální tabulku
	 * @return	aktuální tabulka
	 */
	@SuppressWarnings("rawtypes")
	public TableView getActualTable(){
		return actualTable;
	}
	
	/**
	 * Vytvoří VBox pro tlačítka
	 * @return	vbox pro tlačítka
	 */
	private VBox box(){
		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(5, 5, 5, 5));
		return vbox;
	}
	
	/**
	 * Vrátí node zboží
	 * 
	 * @return	node zboží
	 */
	private Node getNodeZbozi(){
		labelZbozi.setFont(nadpisTabulky);
		VBox vbox = box();
		tZbozi = zboziTable();
		vbox.getChildren().addAll(labelZbozi, tZbozi, new buttons.ButtonsForZbozi());
		return vbox;
	}
	
	/**
	 * Vrátí tabulku zboží
	 * @return	tabulka zboží
	 */
	@SuppressWarnings("unchecked")
	private TableView<items.Zbozi> zboziTable(){
		TableView<items.Zbozi> table = new TableView<>();
		
		TableColumn<items.Zbozi, Integer> productCode = new TableColumn<items.Zbozi,Integer>("Kód");
		TableColumn<items.Zbozi, String> productName = new TableColumn<items.Zbozi,String>("Název");
		TableColumn<items.Zbozi, Integer> productQuantity = new TableColumn<items.Zbozi, Integer>("Množství");
		TableColumn<items.Zbozi, Float> productPrice = new TableColumn<items.Zbozi, Float>("Cena/j.");
		TableColumn<items.Zbozi, String> productManufacturer = new TableColumn<items.Zbozi, String>("Výrobce");
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Zbozi,Integer>("code"));
		productName.setCellValueFactory(new PropertyValueFactory<items.Zbozi,String>("name"));
		
		productQuantity.setCellValueFactory(new PropertyValueFactory<items.Zbozi, Integer>("quantity"));
				
		productQuantity.setCellFactory(
				new Callback<TableColumn<Zbozi,Integer>, TableCell<Zbozi,Integer>>() {

					@Override
					public TableCell<Zbozi, Integer> call(TableColumn<Zbozi, Integer> param) {
						TableCell<Zbozi, Integer> tc = new TableCell<Zbozi, Integer>(){
							@Override
							protected void updateItem(Integer item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									if(getTableRow()==null||getTableRow().getItem()==null){
										setText(null);
										return;
									}
									Zbozi sZbozi = (Zbozi)getTableRow().getItem();
									setText(getItem().toString()+" "+sZbozi.getUnit().toString());
								}else{
									setText(null);
								}
							}
						};
						//dataHandler.quantityCell tc = new quantityCell();
						tc.setAlignment(Pos.CENTER_RIGHT);
						return tc;
					}
				});
		
		productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		productPrice.setCellFactory(
				new Callback<TableColumn<Zbozi,Float>, TableCell<Zbozi,Float>>() {

					@Override
					public TableCell<items.Zbozi, Float> call(TableColumn<items.Zbozi, Float> p) {
						TableCell<Zbozi, Float> tc = new TableCell<Zbozi, Float>(){
							@Override
							protected void updateItem(Float item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									setText(getItem().toString()+" Kč");
								}else{
									setText(null);
								}
							}
						};
						tc.setAlignment(Pos.CENTER_RIGHT);
						return tc;
					}
				});
		
		productManufacturer.setCellValueFactory(new PropertyValueFactory<Zbozi, String>("manufacturer"));
		
		table.getColumns().addAll(	productCode,
									productName,
									productQuantity,
									productPrice,
									productManufacturer);
		
		table.setItems(Main.safe.zbozi.getZbozi());
		return table;
	}
	
	/**
	 * Vrátí node příjemek
	 * @return	node příjemek
	 */
	private Node getNodePrijemky(){
		labelPrijemky.setFont(nadpisTabulky);
		VBox vbox = box();
		tPrijemky = prijemkyTable();
		vbox.getChildren().addAll(labelPrijemky, tPrijemky, new buttons.ButtonsForPrijemky());
		return vbox;
	}
	
	/**
	 * Vrátí tabulku příjemek
	 * @return	tabulka příjemek
	 */
	@SuppressWarnings("unchecked")
	private TableView<items.Prijemka> prijemkyTable(){
		TableView<items.Prijemka> table = new TableView<>();
		
		TableColumn<items.Prijemka, Integer> productCode = new TableColumn<items.Prijemka,Integer>("Kód");
		TableColumn<items.Prijemka, String> productSuplier = new TableColumn<items.Prijemka,String>("Dodavatel");
		TableColumn<items.Prijemka, Integer> productItemsCount = new TableColumn<items.Prijemka, Integer>("Počet položek");
		TableColumn<items.Prijemka, Float> productValue = new TableColumn<items.Prijemka, Float>("Celková hodnota");
		TableColumn<items.Prijemka, LocalDate> productCreationDate = new TableColumn<Prijemka, LocalDate>("Vytvořeno");
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Prijemka,Integer>("code"));
		productSuplier.setCellValueFactory(new PropertyValueFactory<items.Prijemka,String>("supplier"));
		
		productValue.setCellValueFactory(new PropertyValueFactory<items.Prijemka, Float>("value"));
		
		productValue.setCellFactory(
				new Callback<TableColumn<Prijemka,Float>, TableCell<Prijemka,Float>>() {

					@Override
					public TableCell<Prijemka, Float> call(TableColumn<Prijemka, Float> param) {
						TableCell<items.Prijemka, Float> tc = new TableCell<items.Prijemka, Float>(){
							@Override
							protected void updateItem(Float item, boolean empty) {
								super.updateItem(item, empty);
								if(item!=null){
									if(getTableRow()==null||getTableRow().getItem()==null){
										setText(null);
										return;
									}
									Prijemka pr = (Prijemka)getTableRow().getItem();
									Float price = getItem() + pr.getExpencses();
									setText(price+" Kč");
								}else{
									setText(null);
								}
							}
						};
						tc.setAlignment(Pos.CENTER_RIGHT);
						return tc;
					}
				});
		
		productItemsCount.setCellValueFactory(new PropertyValueFactory<items.Prijemka, Integer>("itemsCount"));
		productItemsCount.setCellFactory(
				new Callback<TableColumn<Prijemka,Integer>, TableCell<Prijemka,Integer>>() {

					@Override
					public TableCell<Prijemka, Integer> call(TableColumn<Prijemka, Integer> param) {
						TableCell<Prijemka, Integer> tc = new TableCell<Prijemka, Integer>(){
							@Override
							protected void updateItem(Integer item, boolean empty) {
								super.updateItem(item, empty);
								if(item!=null){
									setAlignment(Pos.CENTER_RIGHT);
									setText(getItem().toString());
								}else{
									setText(null);
								}
							}
						};
						return tc;
					}
				});
		
		productCreationDate.setCellValueFactory(new PropertyValueFactory<items.Prijemka, LocalDate>("creationDate"));
		
		table.getColumns().addAll(	productCode,
									productSuplier,
									productValue,
									productItemsCount,
									productCreationDate);
		
		table.setItems(Main.safe.prijemky.getPrijemky());
		return table;
	}
	
	/**
	 * Vrátí node výdejek
	 * @return node výdejek
	 */
	private Node getNodeVydejky(){
		labelVydejky.setFont(nadpisTabulky);
		VBox vbox = box();
		tVydejky = vydejkyTable();
		vbox.getChildren().addAll(labelVydejky, tVydejky, new buttons.ButtonsForVydejky());
		return vbox;
	}
	
	/**
	 * Vrátí tabulku výdejek
	 * @return	tabulka výdejek
	 */
	@SuppressWarnings("unchecked")
	private TableView<items.Vydejka> vydejkyTable(){
		TableView<items.Vydejka> table = new TableView<>();
		
		TableColumn<items.Vydejka, Integer> productCode = new TableColumn<items.Vydejka,Integer>("Kód");
		TableColumn<items.Vydejka, Integer> productProject = new TableColumn<items.Vydejka,Integer>("Na zakázku");
		TableColumn<items.Vydejka, Float> productValue = new TableColumn<items.Vydejka,Float>("Hodnota");
		TableColumn<items.Vydejka, LocalDate> productCreationDate = new TableColumn<Vydejka, LocalDate>("Vytvořeno");
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Vydejka,Integer>("code"));
		productProject.setCellValueFactory(new PropertyValueFactory<items.Vydejka,Integer>("projectName"));
		
		productValue.setCellValueFactory(new PropertyValueFactory<items.Vydejka, Float>("value"));
		productValue.setCellFactory(
				new Callback<TableColumn<Vydejka,Float>, TableCell<Vydejka,Float>>() {

					@Override
					public TableCell<Vydejka, Float> call(TableColumn<Vydejka, Float> param) {
						TableCell<items.Vydejka, Float> tc = new TableCell<items.Vydejka, Float>(){
							@Override
							protected void updateItem(Float item, boolean empty) {
								super.updateItem(item, empty);
								if(item!=null){
									if(getTableRow()==null||getTableRow().getItem()==null){
										setText(null);
										return;
									}
									Vydejka vy = (Vydejka)getTableRow().getItem();
									Float price = getItem() + vy.getExpencses();
									setText(price+" Kč");
								}else{
									setText(null);
								}
							}
						};
						tc.setAlignment(Pos.CENTER_RIGHT);
						return tc;
					}
				});
		
		productCreationDate.setCellValueFactory(new PropertyValueFactory<items.Vydejka, LocalDate>("creationDate"));
		
		table.getColumns().addAll(	productCode,
									productProject,
									productValue,
									productCreationDate);
		table.setItems(Main.safe.vydejky.getVydejky());
		
		return table;
	}
	
	/**
	 * Vrátí node zakázek
	 * @return	node zakázek
	 */
	private Node getNodeZakazky(){
		labelZakazky.setFont(nadpisTabulky);
		VBox vbox = box();
		tZakazky = zakazkyTable();
		vbox.getChildren().setAll(labelZakazky, tZakazky, new buttons.ButtonsForZakazky());
		return vbox;
	}
	
	/**
	 * Vrátí tabulku zakázek
	 * @return	tabulka zakázek
	 */
	@SuppressWarnings("unchecked")
	private TableView<items.Zakazka> zakazkyTable(){
		TableView<items.Zakazka> table = new TableView<>();
		
		TableColumn<items.Zakazka, Integer> productCode = new TableColumn<items.Zakazka,Integer>("Kód");
		TableColumn<items.Zakazka, String> productName = new TableColumn<items.Zakazka,String>("Název");
		TableColumn<items.Zakazka, String> productCustomer = new TableColumn<items.Zakazka,String>("Zákazník");
		TableColumn<items.Zakazka, LocalDate> productDeadline = new TableColumn<Zakazka, LocalDate>("Mezní termín");
		TableColumn<items.Zakazka, Float> productPriceTag = new TableColumn<items.Zakazka,Float>("Nastavená cena");
		TableColumn<items.Zakazka, Float> productWorkPrice = new TableColumn<items.Zakazka,Float>("Cena za práci");
		TableColumn<items.Zakazka, LocalDate> productCreationDate = new TableColumn<Zakazka, LocalDate>("Vytvořeno");
		TableColumn<items.Zakazka, storage.Closed> productClosed = new TableColumn<Zakazka, storage.Closed>("Stav");
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Zakazka, Integer>("code"));
		productName.setCellValueFactory(new PropertyValueFactory<items.Zakazka, String>("name"));
		productCustomer.setCellValueFactory(new PropertyValueFactory<items.Zakazka, String>("customer"));
		productDeadline.setCellValueFactory(new PropertyValueFactory<items.Zakazka,LocalDate>("deadline"));
		
		productPriceTag.setCellValueFactory(new PropertyValueFactory<items.Zakazka,Float>("priceTag"));
		productPriceTag.setCellFactory(
				new Callback<TableColumn<Zakazka,Float>, TableCell<Zakazka,Float>>() {

					@Override
					public TableCell<Zakazka, Float> call(TableColumn<Zakazka, Float> param) {
						TableCell<Zakazka, Float> tc = new TableCell<Zakazka, Float>(){
							@Override
							protected void updateItem(Float item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									setText(getItem().toString()+" Kč");
								}else{
									setText(null);
								}
							}
						};
						tc.setAlignment(Pos.CENTER_RIGHT);
						return tc;
					}
				});
		
		
		productWorkPrice.setCellValueFactory(new PropertyValueFactory<items.Zakazka,Float>("workPrice"));
		productWorkPrice.setCellFactory(new Callback<TableColumn<Zakazka,Float>, TableCell<Zakazka,Float>>() {

			@Override
			public TableCell<Zakazka, Float> call(TableColumn<Zakazka, Float> param) {
				TableCell<Zakazka, Float> tc = new TableCell<Zakazka,Float>(){
					@Override
					protected void updateItem(Float item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						if(item!=null){
							setText(getItem()+" Kč");
						}else{
							setText(null);
						}
					}
				};
				tc.setAlignment(Pos.CENTER_RIGHT);
				return tc;
			}
		});
		
		productCreationDate.setCellValueFactory(new PropertyValueFactory<items.Zakazka, LocalDate>("creationDate"));
		
		productClosed.setCellValueFactory(new PropertyValueFactory<items.Zakazka, storage.Closed>("closed"));
		productClosed.setCellFactory(
				new Callback<TableColumn<Zakazka,Closed>, TableCell<Zakazka,Closed>>() {

					@Override
					public TableCell<Zakazka, Closed> call(TableColumn<Zakazka, Closed> param) {
						TableCell<Zakazka, Closed> tc = new TableCell<Zakazka, Closed>(){
							@Override
							protected void updateItem(Closed item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								if(item!=null){
									/*
									if(getTableRow()==null||getTableRow().getItem()==null){
										setText(null);
										return;
									*/
									//Zakazka sZa = (Zakazka)getTableRow().getItem();
									

									if(getItem()==Closed.CLOSED){
										if(getTableRow()==null||getTableRow().getItem()==null){
											setText("            ");
											return;
										}
										Zakazka sZ = (Zakazka)getTableRow().getItem();
										if(sZ.getBill().isClosed()){
											setText("Uzavřená");
										}else{
											setText("Čeká na zaplacení");
										}
										
									}else{
										setText("Otevřená");
									}
									
									return;
								}else{
									setText(null);
								}
								setText(null);
							}
						};
						
						return tc;
					}
				});
		
		table.getColumns().addAll(	productCode,
									productName,
									productCustomer,
									productDeadline,
									productPriceTag,
									productWorkPrice,
									productCreationDate,
									productClosed);
		table.setItems(Main.safe.zakazky.getZakazky());
		
		return table;
	}
	
	/**
	 * Vrátí node faktur
	 * @return node faktur
	 */
	private Node getNodeFaktury(){
		labelFaktury.setFont(nadpisTabulky);
		VBox vbox = box();
		tFaktury =  fakturyTable();
		vbox.getChildren().addAll(labelFaktury, tFaktury, new buttons.ButtonsForFaktury());
		return vbox;
	}
	
	/**
	 * Vrátí tabulku faktur
	 * @return	tabulka faktur
	 */
	@SuppressWarnings("unchecked")
	private TableView<items.Faktura> fakturyTable(){
		TableView<items.Faktura> table = new TableView<>();
		
		TableColumn<items.Faktura, Integer> productCode = new TableColumn<items.Faktura,Integer>("Kód");
		TableColumn<items.Faktura, String> productProject = new TableColumn<items.Faktura,String>("Zakázka");
		TableColumn<items.Faktura, String> productPrice = new TableColumn<items.Faktura,String>("Hodnota");
		TableColumn<items.Faktura, LocalDate> productDueDate = new TableColumn<items.Faktura, LocalDate>("Splatnost");
		TableColumn<items.Faktura, String> productCustomer = new TableColumn<items.Faktura, String>("Zákazník");
		TableColumn<items.Faktura, String> productClosed = new TableColumn<items.Faktura, String>("Zaplaceno");
		
		
		productCode.setCellValueFactory(new PropertyValueFactory<items.Faktura, Integer>("code"));
		productProject.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Faktura,String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Faktura, String> p) {
						if(p.getValue()!=null){
							StringProperty sp = new SimpleStringProperty(p.getValue().getProject().getName());
							return sp;
						}
						return new SimpleStringProperty("Nezadáno");
					}
				});
		productPrice.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Faktura,String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Faktura, String> p) {
						if(p.getValue()!=null){
							StringProperty sp = new SimpleStringProperty(
									p.getValue().getPrice()+" Kč");
							return sp;
						}
						return new SimpleStringProperty("Nezdaáno");
					}
				});
		
		productDueDate.setCellValueFactory(new PropertyValueFactory<items.Faktura,LocalDate>("dueDate"));
		productCustomer.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Faktura,String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Faktura, String> p) {
						if(p.getValue()!=null){
							return new SimpleStringProperty(p.getValue().getProject().getCustomer());
						}else{
							return new SimpleStringProperty("Nezadáno");
						}
					}
				});
		
		productClosed.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Faktura,String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(CellDataFeatures<Faktura, String> p) {
						if(p.getValue()!=null){
							if(p.getValue().isClosed()){
								return new SimpleStringProperty("Zaplaceno");
							}else{
								return new SimpleStringProperty("Nezaplaceno");
							}
						}else{
							return new SimpleStringProperty("Nezadáno");
						}
					}
				});
		
		table.getColumns().addAll(	productCode,
									productProject,
									productPrice,
									productDueDate,
									productCustomer,
									productClosed);
		table.setItems(Main.safe.faktury.getFaktury());
		
		return table;
	}
	
	/**
	 * Vytvoří nový node zboží
	 */
	public void refreshZbozi(){
		zbozi = getNodeZbozi();
	}
	
	/**
	 * Vytvoří nový node příjemek
	 */
	public void refreshPrijemky(){
		prijemky = getNodePrijemky();
	}
	
	/**
	 * Vytvoří nový node výdejek
	 */
	public void refreshVydejky(){
		vydejky = getNodeVydejky();
	}
	
	/**
	 * Vytvoří nový node zakázek
	 */
	public void refreshZakazky(){
		zakazky = getNodeZakazky();
	}
	
	/**
	 * Vytvoří nový node faktur
	 */
	public void refreshFaktury(){
		faktury = getNodeFaktury();
	}
	
	/**
	 * Obnový všechny nody
	 */
	public void refreshAll(){
		zbozi = getNodeZbozi();
		prijemky = getNodePrijemky();
		vydejky = getNodeVydejky();
		zakazky = getNodeZakazky();
		faktury = getNodeFaktury();
	}
	
	/**
	 * Připraví všechny tabulky a nody
	 */
	private VarTable(){
		zbozi = getNodeZbozi();
		prijemky = getNodePrijemky();
		vydejky = getNodeVydejky();
		zakazky = getNodeZakazky();
		faktury = getNodeFaktury();
		actualNode = zbozi;
		actualTable = tZbozi;
		
	}
	
	/**
	 * jediná instance varTable
	 */
	static{
		instance = new VarTable();
	}
	
	/**
	 * Vrátí jedinou instanci varTable
	 * @return	jediná instance varTable
	 */
	public static VarTable getInstance(){
		return instance;
	}
}
