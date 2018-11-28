package storage;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class Stock {
	/**
	 * Chybová hláška
	 * "Nejprve vyberte položku!"
	 */
	public final String ER_01 = "Nejprve vyberte položku!";
	
	/**
	 * Chybová hláška
	 * "Nejsou vyplněny všechny povinné údaje!"
	 */
	public final String ER_0 = "Nejsou vyplněny všechny povinné údaje!";
	
	/**
	 * Chybová hláška
	 * "Chybně zadané množství"
	 */
	public final String ER_1 = "Chybně zadané množství";
	
	/**
	 * Chybová hláška
	 * "Položku nelze odebrat dokud je naskladněno nenulové množství!"
	 */
	public final String ER_11 = "Položku nelze odebrat dokud je naskladněno nenulové množství!";
	
	/**
	 * Chybová hláška
	 * "Na skladě není dostatek zvoleného zboží!"
	 */
	public final String ER_12 = "Na skladě není dostatek zvoleného zboží!";
	
	/**
	 * Chybová hláška
	 * "Chybně zadaná cena!"
	 */
	public final String ER_2 = "Chybně zadaná cena!";
	
	/**
	 * Chybová hláška
	 * "Chybně zadané dodatečné náklady!"
	 */
	public final String ER_21 = "Chybně zadané dodatečné náklady!";
	
	/**
	 * Chybová hláška
	 * "Zvolená zakázka je již uzavřená!"
	 */
	public final String ER_3 = "Zvolená zakázka je již uzavřená!";
	
	/**
	 * Chybová hláška
	 * "Není vybraná zakázka!"
	 */
	public final String ER_31 = "Není vybraná zakázka!";
	
	/**
	 * Chybová hláška
	 * "Zvolená výdejka je již uzavřená"
	 */
	public final String ER_32 = "Zvolená výdejka je již uzavřená";
	
	/**
	 * Chybová hláška
	 * "Zvolená zakázka je již uzavřená"
	 */
	public final String ER_321 = "Zvolená zakázka je již uzavřená";
	
	/**
	 * Chybová hláška
	 * "Zvolená faktura je již uzavřená"
	 */
	public final String ER_322 = "Zvolená faktura je již uzavřená";
	
	/**
	 * Chybová hláška
	 * "Některé zboží již bylo záměrně odstraněno ze záznamů. Proto nelze doklad odstranit!"
	 */
	public final String ER_33 = "Některé zboží již bylo záměrně odstraněno ze záznamů. Proto nelze doklad odstranit!";
	
	/**
	 * Chybová hláška
	 * "Zakázka je již uzavřena (tj. spojena s fakturou) a nelze ji dále upravovat"
	 */
	public final String ER_41 = "Zakázka je již uzavřena (tj. spojena s fakturou) a nelze ji dále upravovat";
	
	/**
	 * Chybová hláška
	 * "Výdejka je již uzavřená (tj. spojena s fakturou) a nelze ji dále upravovat"
	 */
	public final String ER_42 = "Výdejka je již uzavřená (tj. spojena s fakturou) a nelze ji dále upravovat";
	
	/**
	 * Chybová hláška
	 * "Faktura je již uzavřená (zaplacená) a nelze ji dále upravovat"
	 */
	public final String ER_43 = "Faktura je již uzavřená (zaplacená) a nelze ji dále upravovat";
	
	/**
	 * Poslední použitýidentifikační kód předmětu daného dokladu
	 */
	public int lastCode;
	
	/**
	 * Pripraví novou instanci třídy
	 */
	public Stock(){
		this.lastCode = 0;
	}
	
	/**
	 * Vytvoří nový kód pro další položku dokladu
	 * @return
	 */
	public int createCode(){
		return ++lastCode;
	}
	
	/**
	 * Vytvoří základní GridPane pro formuláře
	 * @return
	 */
	public GridPane getGrid(){
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(15);
		grid.setVgap(10);
		
		ColumnConstraints col1 = new ColumnConstraints(100);
		
		col1.halignmentProperty().set(HPos.LEFT);
		grid.getColumnConstraints().addAll(new ColumnConstraints(),col1);
		return grid;
	}
	
	/**
	 * Metoda vytvoří chybovou hlášku podle zadaného parametru
	 * @param error	chybová hláška
	 */
	public void wrongInput(String error){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Chyba při zadávání");
		alert.setHeaderText(error);
		alert.showAndWait();
	}
}
