package storage;
import Main.ControlList.ControlListType;
import items.Zakazka;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Třída formuláře pro výběr zakázky z tabulky zakázek
 * @author Martin Hamet
 *
 */
public class ChooseListZakazka {
	/**
	 * Vybraná zakázka
	 */
	private Zakazka chosenZakazka;
	
	/**
	 * Tlačítko pro výběr zakázky
	 */
	public Button btChoose = new Button("Vybrat");
	
	/**
	 * tabulka zakázek
	 */
	public TableView<Zakazka> sTable;
	
	/**
	 * vytvoří novou instanci třídy
	 */
	public ChooseListZakazka(){
		this.chosenZakazka = null;
	}
	
	/**
	 * Nastaví vybranou zakázku
	 * @param z	vybraná zakázka
	 */
	public void setChosenZakazka(Zakazka z){
		this.chosenZakazka = z;
	}
	
	/**
	 * Vrátí vybranou zakázku pokud nebyla žádná vybrána ani nastavena vrátí null
	 * @return	vybraná zakázka
	 */
	public Zakazka getChosenZakazka(){
		return chosenZakazka;
	}
	
	/**
	 * Vytvoří formulář pro výběr zakázky a umístího do do hlavního okna
	 * 
	 * @param myGrid	odkaz na předchozí formulář pro nastavování aktuálních hodnot
	 * @param type		typ formuláře pro který se zakázka vybírá
	 */
	public void chooseZakazkaForm(Object myGrid, ControlListType type){
		sTable = Main.Main.vTable.gettZakazky();
		
		Button btStorno = new Button("Storno");
		HBox chooseStorno = new HBox();
		chooseStorno.setSpacing(10);
		chooseStorno.getChildren().setAll(	btChoose,
											btStorno);
		
		btChoose.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				chosenZakazka =  sTable.getSelectionModel().getSelectedItem();
				if(chosenZakazka == null){
					Main.Main.safe.zbozi.wrongInput(Main.Main.safe.zbozi.ER_01);
					return;
				}
				if(type==ControlListType.VYDEJKY){
					StockVydejky.FormVydejka vmyGrid;
					vmyGrid = (StockVydejky.FormVydejka)(myGrid);
					vmyGrid.setTextsForZakazka(chosenZakazka);
				}
				if(type==ControlListType.FAKTURY){
					StockFaktury.FormFaktura fmyGrid;
					fmyGrid = (StockFaktury.FormFaktura)myGrid;
					fmyGrid.setTextsForZakazka(chosenZakazka);
				}
				
				
				Main.Main.removeNodeSP();
				
			}
		});
		
		btStorno.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.Main.removeNodeSP();
			}
		});
		
		Label lChZ = new Label("Výběr zakázky pro výdejku:");
		VBox mainVb = new VBox();
		mainVb.setSpacing(10);
		mainVb.setPadding(new Insets(10));
		mainVb.getChildren().setAll(lChZ,
									sTable,
									chooseStorno);
		
		Main.Main.addNodeSP(mainVb);
		
	}
	
}
