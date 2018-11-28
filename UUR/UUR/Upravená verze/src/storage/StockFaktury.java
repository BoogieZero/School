package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Main.ControlList.ControlListType;
import items.Faktura;
import items.Faktura.PaymentType;
import items.Zakazka;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Třída představující systém faktur a jejich manipulaci
 * 
 * @author Martin Hamet
 *
 */
public class StockFaktury extends Stock{
	/**
	 * Mapa faktur v systému
	 */
	private Map<Integer,items.Faktura> map;
	
	/**
	 * Vrátí observablelist faktur v systému
	 * @return	observablelist faktur v systému
	 */
	public ObservableList<items.Faktura> getFaktury(){
		List<items.Faktura> listFaktury = new ArrayList<items.Faktura>(map.values());
		ObservableList<items.Faktura> obFaktury = FXCollections.observableArrayList(listFaktury);
		return obFaktury;
	}
	
	/**
	 * Vytvoří formulář detailu faktury
	 * @param faktura	aktuálně vybraná faktura
	 * @return	node s formulářem pro detail faktury
	 */
	public Node formDetail(Faktura faktura){
		GridPane grid = getGrid();
		Button chZakazka = new Button("Vybrat zakázku");
		ChooseListZakazka chLz = new ChooseListZakazka();
		FormFaktura myGrid = new FormFaktura(grid, chZakazka, chLz);
		chLz.setChosenZakazka(faktura.getProject());
		myGrid.setTexts(faktura);
		myGrid.setEditable(false);
		
		
		
		
		//left side
		
		chZakazka.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Vytvoří formulář pro vbrání zakázky
			 */
			@Override
			public void handle(ActionEvent event) {
				Main.Main.vTable.refreshZakazky();
				chLz.chooseZakazkaForm(myGrid, ControlListType.FAKTURY);
				
			}
		});
		
		Button buttonClose = new Button("UZAVŘÍT FAKTURU");
		buttonClose.setAlignment(Pos.CENTER_RIGHT);
		Button buttonEdit = new Button("Upravit");
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zavřít");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonEdit,
											buttonSave,
											buttonStorno);
		
		if(faktura.isClosed()){
			buttonEdit.setDisable(true);
			buttonClose.setDisable(true);
		}
		
		HBox btns = new HBox();
		btns.setSpacing(150);
		btns.setAlignment(Pos.CENTER);
		btns.setPadding(new Insets(10));
		btns.getChildren().setAll(	hbReSaveStorno,
									buttonClose);
		
		buttonSave.setDisable(true);
	
		buttonClose.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Zobrazí potvrzovací dialog pr ouzavření faktury a při kladné odpovědi uzavře fakturu
			 */
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("UZAVŘENÍ FAKTURY");
				alert.setHeaderText("Uzavřením faktury potvrzujete že faktura byla zaplacená a"
						+ " u všech dokladů, s touto fakturou spojených, budou uzamčeny možnosti úprav."+"\n"+""
								+ "Chcete uzavřít fakturu?");
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
					faktura.setClosed(true);
					Main.Main.vTable.refreshZakazky();
					Main.Main.refreshActualTable();
					Main.Main.removeNodeSP();
				}else{
					return;
				}
				
			}
		});
		
		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Zpřístupní editaci faktury
			 */
			@Override
			public void handle(ActionEvent event) {
				if(faktura.isClosed()){
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_43);
					return;
				}
				buttonSave.setDisable(false);
				buttonEdit.setDisable(true);
				myGrid.setEditable(true);
				buttonClose.setDisable(true);
			}
		});
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží změny ve faktuře
			 */
			@Override
			public void handle(ActionEvent event) {
				
				if(faktura.getProject()==chLz.getChosenZakazka()){
					faktura.getProject().setBill(null);
				}
				if(myGrid.isInputOk()){
					buttonEdit.setDisable(false);
					buttonSave.setDisable(true);
					buttonClose.setDisable(false);
					myGrid.setEditable(false);
					faktura.getProject().setBill(null);
					faktura.setProject(chLz.getChosenZakazka());
					faktura.setPrice(Float.parseFloat(myGrid.tfPrice.getText()));
					faktura.setDueDate(myGrid.dp.getValue());
					faktura.setDescription(myGrid.tfDescription.getText());
					faktura.setPayment(myGrid.CHBpayment.getValue());
					faktura.setAccNumber(myGrid.tfAccNumber.getText());
					
					faktura.setcName(myGrid.tfcName.getText());
					faktura.setcSurname(myGrid.tfcSurname.getText());
					faktura.setcAddress(myGrid.tfcAddress.getText());
					faktura.setcPsc(myGrid.tfcPsc.getText());
					faktura.setcICO(myGrid.tfcICO.getText());
					faktura.setcDIC(myGrid.tfcDIC.getText());
					faktura.setInfo(myGrid.tfInfo.getText());
					
					faktura.getProject().setBill(faktura);
					Main.Main.vTable.refreshZakazky();
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
		gridBox.getChildren().addAll(grid);
		
		VBox mainBox = new VBox();
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));
		mainBox.getChildren().setAll(	gridBox,
										myGrid.lDescription,
										myGrid.tfDescription,
										btns);
		
		if(faktura.isClosed()){
			Label lClosed = new Label("FAKTURA JE UZAVŘENÁ");
			lClosed.setFont(new Font("Arial", 15));
			mainBox.getChildren().add(lClosed);
		}
		return mainBox;
	}
	
	/**
	 * Vytvoří formulář pro přidání nové faktury
	 * @return	node s formulářem pro novou fakturu
	 */
	public Node formAdd(){
		GridPane grid = getGrid();
		Button chZakazka = new Button("Vybrat zakázku");
		ChooseListZakazka chLz = new ChooseListZakazka();
		FormFaktura myGrid = new FormFaktura(grid, chZakazka, chLz);
		
		
		//left side
		
		chZakazka.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Vytvoří formulář pro výběr zakázky
			 */
			@Override
			public void handle(ActionEvent event) {
				chLz.chooseZakazkaForm(myGrid, ControlListType.FAKTURY);
				
			}
		});
		
		
		Button buttonSave = new Button("Uložit");
		Button buttonStorno = new Button("Zrušit");
		HBox hbReSaveStorno = new HBox();
		hbReSaveStorno.setSpacing(10);
		hbReSaveStorno.setAlignment(Pos.CENTER);
		hbReSaveStorno.getChildren().addAll(buttonSave,
											buttonStorno);
	
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			
			/**
			 * Uloží vytvořenou fakturu a uzavře formulář
			 */
			@Override
			public void handle(ActionEvent event) {
				if(myGrid.isInputOk()){
					items.Faktura newFaktura = new Faktura(	chLz.getChosenZakazka(),
															Float.parseFloat(myGrid.tfPrice.getText()));
					newFaktura.setDueDate(myGrid.dp.getValue());
					newFaktura.setDescription(myGrid.tfDescription.getText());
					newFaktura.setPayment(myGrid.CHBpayment.getValue());
					newFaktura.setAccNumber(myGrid.tfAccNumber.getText());
					
					newFaktura.setcName(myGrid.tfcName.getText());
					newFaktura.setcSurname(myGrid.tfcSurname.getText());
					newFaktura.setcAddress(myGrid.tfcAddress.getText());
					newFaktura.setcPsc(myGrid.tfcPsc.getText());
					newFaktura.setcICO(myGrid.tfcICO.getText());
					newFaktura.setcDIC(myGrid.tfcDIC.getText());
					newFaktura.setInfo(myGrid.tfInfo.getText());
					
					addFaktura(newFaktura);
					
					newFaktura.getProject().setBill(newFaktura);
					
					if(Main.Main.isAloneInSP()){
						@SuppressWarnings("unchecked")
						ObservableList<Faktura> ol = 
								Main.Main.vTable.getActualTable().getItems();
						ol.add(newFaktura);
					}else{
						Main.Main.vTable.refreshZakazky();
					}
					//Main.Main.setEditing(false);
					Main.Main.removeNodeSP();
					Main.Main.vTable.refreshZakazky();
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
		gridBox.getChildren().addAll(grid);
		
		VBox mainBox = new VBox();
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));
		mainBox.getChildren().setAll(	gridBox,
										myGrid.lDescription,
										myGrid.tfDescription,
										hbReSaveStorno);
		
		
		return mainBox;
	}
	
	/**
	 * Vytvoří instanci systému faktur
	 */
	public StockFaktury(){
		super();
		this.map = new HashMap<Integer,items.Faktura>();
	}
	
	/**
	 * Odstraní vybranou fakturu ze systému s potvrzovacím dialog
	 */
	public void removeFaktura(){
		Faktura fa = (Faktura)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
		if(fa==null){
			wrongInput(ER_01);
			return;
		}
		
		if(fa.isClosed()){
			Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_322);
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Odstranění faktury");
		alert.setHeaderText("Chcete odstranit fakturu: "+"\n"+fa.toString());
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
			Faktura sFaktura = (Faktura)Main.Main.vTable.getActualTable().getSelectionModel().getSelectedItem();
			int code = sFaktura.getCode();
			sFaktura.getProject().setBill(null);
			map.remove(code);
			Main.Main.vTable.getActualTable().getItems().remove(index);
			Main.Main.vTable.getActualTable().getSelectionModel().clearSelection();
			Main.Main.vTable.refreshZakazky();
		}else{
			return;
		}
	}
	
	/**
	 * Přidá fakturu do systému.
	 * 
	 * @param faktura	pridavana faktura
	 */
	public void addFaktura(items.Faktura faktura){
		int code = createCode();
		faktura.setCode(code);
		map.put(code, faktura);
	}
	
	/**
	 * Třída spravující formulář pro fakturu 
	 * attributy l%název% představují nápisy ve formuláři
	 * attributy tf%název% představují zobrazovaná data ve formuláři 
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class FormFaktura{
		//Code
		int code;
		Label lCode;
		Label tfCode;
		
		Label lProject;
		Button chZakazka;
		storage.ChooseListZakazka chLz;	
		
			//Code
			Label lpCode;
			Label tfpCode;
		
			//Name
			Label lpName;
			Label tfpName;
			
			//Customer
			Label lpCustomer;
			Label tfpCustomer;
		
		
		//Creation Date
		Label lCreationDate;
		Label tfCreationDate;
		
		//Deadline
		Label lDueDate;
		Label tfDueDate;
		DatePicker dp;
		
		//Description
		Label lDescription;
		TextArea tfDescription;
		
		//Price tag
		Label lPrice;
		Label tfPrice;
		
		//Payment
		Label lPayment;
		ChoiceBox<Faktura.PaymentType> CHBpayment;
			
		Label lAccNumber;
		TextField tfAccNumber;
		
		//info
		Label lInfo;
		TextField tfInfo;
		
		//Customer
		Label lCustomer;
			Label lcName;
			TextField tfcName;
			
			Label lcSurname;
			TextField tfcSurname;
			
			Label lcAddress;
			TextField tfcAddress;
			
			Label lcPsc;
			TextField tfcPsc;
			
			Label lcICO;
			TextField tfcICO;
			
			Label lcDIC;
			TextField tfcDIC;
		
		/**
		 * Zkontroluje zda jsou zadaná data vpořádku
		 * 
		 * @return	true - kontrola proběhla v pořádku
		 */
		public boolean isInputOk(){
			if(	tfpCustomer.getText().isEmpty()||
				dp.getValue()==null||
				tfcName.getText().isEmpty()||
				tfcSurname.getText().isEmpty()||
				tfcAddress.getText().isEmpty()||
				tfcPsc.getText().isEmpty()||
				chLz.getChosenZakazka()==null||
				dp.getValue()==null||
				CHBpayment.getValue()==null){
					
					//Some of the inputs are empty
					wrongInput(ER_0);
					return false;
			}
			if(CHBpayment.getValue()==Faktura.PaymentType.ACCOUNT&&
					tfAccNumber.getText().isEmpty()){
				wrongInput(ER_0);
				return false;
			}
			if(chLz.getChosenZakazka().getClosed()==storage.Closed.CLOSED){
				Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_321);
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
			
			
			tfAccNumber.setDisable(dis);
			tfAccNumber.setOpacity(op);
			
			tfCode.setDisable(dis);
			tfCode.setOpacity(op);
			
			chZakazka.setDisable(dis);
			
			tfDueDate.setVisible(!editable);
			dp.setVisible(editable);
			
			tfDescription.setDisable(dis);
			tfDescription.setOpacity(op);
			
			CHBpayment.setDisable(dis);
			CHBpayment.setOpacity(op);
			
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
			
				tfcName.setDisable(dis);
				tfcName.setOpacity(op);
				
				tfcSurname.setDisable(dis);
				tfcSurname.setOpacity(op);
				
				tfcAddress.setDisable(dis);
				tfcAddress.setOpacity(op);
				
				tfcPsc.setDisable(dis);
				tfcPsc.setOpacity(op);
				
				tfcICO.setDisable(dis);
				tfcICO.setOpacity(op);
				
				tfcDIC.setDisable(dis);
				tfcDIC.setOpacity(op);
		
			tfInfo.setDisable(dis);
			tfInfo.setOpacity(op);
		}
		
		/**
		 * Nastaví data formuláře podle zadané faktury
		 * @param f	zadaná faktura
		 */
		public void setTexts(items.Faktura f){
			setEditable(false);
			
			tfCode.setText(f.getCode()+"");
			tfCreationDate.setText(f.getCreationDate().toString());
			tfpCustomer.setText(f.getCustomer());
			tfPrice.setText(f.getPrice()+"");
			dp.setValue(f.getDueDate());
			tfDueDate.setText(dp.getValue().toString());
			tfAccNumber.setText(f.getAccNumber());
			tfInfo.setText(f.getInfo());
			CHBpayment.setValue(f.getPayment());
			
			tfcName.setText(f.getcName());
			tfcSurname.setText(f.getcSurname());
			tfcAddress.setText(f.getcAddress());
			tfcPsc.setText(f.getcPsc());
			tfcICO.setText(f.getcICO());
			tfcDIC.setText(f.getcDIC());
			tfDescription.setText(f.getDescription());
			
			setTextsForZakazka(f.getProject());
		}
		
		/**
		 * Nastaví data formuláře podle zadané zakázky
		 * @param za	zadaná zakázka
		 */
		public void setTextsForZakazka(Zakazka za){
			lpName.setText("Název zakázky:");
			tfpName.setText(za.getName());
			lpCode.setText("Kód zakázky:");
			tfpCode.setText(za.getCode()+"");
			tfpCustomer.setText(za.getCustomer());
			tfPrice.setText(za.getPriceTag()+"");
		}
		
		/**
		 * Vytvoří novou instanci třídy a nastaví zadaný GridPane
		 * @param grid	zadaný gridPane
		 * @param chZakazka	tlačítko pro vybrání zakázky
		 * @param chLz	třída pro vytvoření formuláře pro vybrání zakázky
		 */
		public FormFaktura(GridPane grid, Button chZakazka, storage.ChooseListZakazka chLz){
			this.chZakazka = chZakazka;
			this.chLz = chLz;
			lCode = new Label("Kód faktury:");
			code = Main.Main.safe.zakazky.lastCode+1;
			lProject = new Label("Zakázka:");
			lpCode = new Label();
			tfpCode = new Label();
			lpName = new Label();
			tfpName = new Label();
			lCreationDate = new Label("Datum vytvoření:");
			tfCreationDate = new Label("Automaticky generováno");
			tfCode = new Label(code+"");
			lpCustomer = new Label("Zákazník:");
			tfpCustomer = new Label();
			lDueDate = new Label("Splatnost:");
			tfDueDate = new Label();
			lPrice = new Label("Navrhovaná cena:");
			tfPrice = new Label();
			lInfo = new Label("Poznámka: ");
			tfInfo = new TextField();
			
			lCustomer = new Label("Odběratel:");
			lCustomer.setAlignment(Pos.CENTER);
			lcName = new Label("Jméno:");
			tfcName = new TextField();
			lcSurname = new Label("Příjmení:");
			tfcSurname = new TextField();
			lcAddress = new Label("Adresa:");
			tfcAddress = new TextField();
			lcPsc = new Label("PSČ:");
			tfcPsc = new TextField();
			lcICO = new Label("IČO");
			tfcICO = new TextField();
			lcDIC = new Label("DIČ");
			tfcDIC = new TextField();
			lDescription = new Label("Popis produktu:");
			tfDescription = new TextArea();
			
			dp = new DatePicker();
			StackPane sp = new StackPane();
			sp.setAlignment(Pos.CENTER_LEFT);
			sp.getChildren().setAll(tfDueDate, dp);
			
			lPayment = new Label("Způsob platby:");
			lAccNumber = new Label("Číslo účtu:");
			lAccNumber.setDisable(true);
			tfAccNumber = new TextField();
			tfAccNumber.setDisable(true);
			
			CHBpayment = new ChoiceBox<Faktura.PaymentType>(
					FXCollections.observableArrayList(
							PaymentType.ACCOUNT,
							PaymentType.CASH));
			CHBpayment.setConverter(new StringConverter<Faktura.PaymentType>() {
				
				@Override
				public String toString(PaymentType object) {
					if(object == Faktura.PaymentType.ACCOUNT){
						return "Na účet";
					}
					return "Hotově";
				}
				
				@Override
				public PaymentType fromString(String string) {
					if(string.equals("Na účet")){
						return Faktura.PaymentType.ACCOUNT;
					}
					return Faktura.PaymentType.CASH;
				}
			});
			
			CHBpayment.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Faktura.PaymentType>() {

				@Override
				public void changed(ObservableValue<? extends PaymentType> observable, PaymentType oldValue,
						PaymentType newValue) {
					if(newValue == Faktura.PaymentType.ACCOUNT){
						lAccNumber.setDisable(false);
						tfAccNumber.setDisable(false);
					}else{
						lAccNumber.setDisable(true);
						tfAccNumber.setDisable(true);
					}
					
				}
			});
			
			//grid
			//left side
			grid.add(lCode, 1, 1); 			grid.add(tfCode, 2, 1);
			grid.add(lProject, 1, 2);		grid.add(chZakazka, 2, 2);
			grid.add(lpName, 1, 3);			grid.add(tfpName, 2, 3);
			grid.add(lpCode, 1, 4); 		grid.add(tfpCode, 2, 4);
			grid.add(lCreationDate, 1, 5);	grid.add(tfCreationDate, 2, 5);
			grid.add(lDueDate, 1, 6);		grid.add(sp, 2, 6);
			grid.add(lPrice, 1, 7);			grid.add(tfPrice, 2, 7);
			grid.add(lPayment, 1, 8); 		grid.add(CHBpayment, 2, 8);
			grid.add(lAccNumber, 1, 9); 	grid.add(tfAccNumber, 2, 9);
			
			//right side
											grid.add(lCustomer, 5, 1);
			grid.add(lpCustomer, 4, 2);		grid.add(tfpCustomer, 5, 2);
			grid.add(lcName, 4, 3); 		grid.add(tfcName, 5, 3);
			grid.add(lcSurname, 4, 4); 		grid.add(tfcSurname, 5, 4);
			grid.add(lcAddress, 4, 5); 		grid.add(tfcAddress, 5, 5);
			grid.add(lcPsc, 4, 6);			grid.add(tfcPsc, 5, 6);
			grid.add(lcICO, 4, 7); 			grid.add(tfcICO, 5, 7);
			grid.add(lcDIC, 4, 8); 			grid.add(tfcDIC, 5, 8);
			grid.add(lInfo, 4, 9);			grid.add(tfInfo, 5, 9);
		}
		
	}
}
