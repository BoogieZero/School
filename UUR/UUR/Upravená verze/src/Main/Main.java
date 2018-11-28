package Main;

import java.util.ArrayList;
import java.util.List;

import Main.ControlList.ControlListType;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hlavní řídící třída programu
 * @author Martin Hamet
 *
 */
public class Main extends Application {
	//hlavni okno
	/**
	 * Minimální výška okna
	 */
	private final int MIN_HEIGHT = 200;
	
	/**
	 * Minimální šířka okna
	 */
	private final int MIN_WIDTH = 200;
	
	/**
	 * Preferovaná výška hlavního okna
	 */
	private final int PREF_HEIGHT = 500;
	
	/**
	 * Preferovaná šířka hlavního okna
	 */
	private final int PREF_WIDTH = 900;
	//Control list
	
	/**
	 * Preferovaná šířka výběrového stromu
	 */
	private final int CONTROLLIST_PREF_WIDTH = 100;
	//tabulka
	/**
	 * Safe představující virtuální sklad programu
	 */
	public static Safe safe = new Safe();
	
	/**
	 * Třída pro správu tabulky zobrazované v hlavním okně
	 */
	public static VarTable vTable;
	
	/**
	 * Stack pane hlavního okna
	 */
	public static StackPane SP;
	
	/**
	 * Refresh list pro obnovování
	 */
	public static List<Refresh> refreshList = new ArrayList<Refresh>();
	
	/**
	 * Výběrový seznam pro hlavni okno.
	 */
	private static TreeView<ControlList> tree;
	
	/**
	 * Obnoví aktuální tabulku v hlavním okně
	 */
	public static void refreshActualTable(){
		@SuppressWarnings("rawtypes")
		TableColumn col = (TableColumn)(vTable.getActualTable().getColumns().get(0));
		col.setVisible(false);
		col.setVisible(true);
	}
	
	/**
	 * Zjistí zda je v hlavním okně pouze tabulka a prvek (Node) který tuto metodu volá
	 * @return
	 */
	public static boolean isAloneInSP(){
		if(SP.getChildren().size()!=2){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Nastaví zda probíhá editování a pokud ano tak se zamkne výběrový strom
	 * 
	 * @param isEditing	stav editování
	 */
	public static void setEditing(boolean isEditing){
		if(!isAloneInSP()){		//I'm not alone in SP -> still editing
			isEditing = true;
		}
		if(isEditing){
			tree.setDisable(true);
		}else{
			tree.setDisable(false);
		}
	}
	
	/**
	 * Hlavní metoda main
	 * 
	 * @param args	vstupní parametry
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Skladové účetnictví");
		stage.setScene(getScene());
		stage.setMinHeight(MIN_HEIGHT);
		stage.setMinWidth(MIN_WIDTH);
		//safe.createDefaultStuff();		//vytvoreni zakladniho materialu
		//vTable.refreshAll();
		//refreshSPtable();
		stage.show();
	}
	
	/**
	 * Vrátí novou scénu
	 * 
	 * @return	nová scéna
	 */
	private Scene getScene(){
		return new Scene(getRoot());
	}
	
	/**
	 * Vytvoří kořenový prvek scény a zajistí vytvoření tabulky
	 * 
	 * @return	kořenový prvek scény BorderPane
	 */
	private Parent getRoot() {
		BorderPane rootPane = new BorderPane();
		rootPane.setPrefHeight(PREF_HEIGHT);
		rootPane.setPrefWidth(PREF_WIDTH);
		rootPane.setLeft(getTree());
		SP = new StackPane();
		vTable = VarTable.getInstance();				//vytvoreni tabulky
		rootPane.setCenter(SP);
		SP.getChildren().setAll(vTable.getActualNode());
		return rootPane;
	}
	
	/**
	 * Prida node do hlavniho okna misto tabulky a skryje předchozí.
	 * 
	 * @param node	přidávaný node
	 */
	public static void addNodeSP(Node node){
		ObservableList<Node> list = SP.getChildren();
		if(list.isEmpty()){		//SP je prazdny
			list.add(node);
		}else{								//SP neni prazdny
			Node front = list.get(list.size()-1);
			front.setVisible(false);
			list.add(node);
		}
		//System.out.println("SP: "+SP.getChildren().size());
	}
	
	/**
	 * Odstraní poslední (viditelný) node v popředí a zobrazí předchozí.
	 * Obnovi tabulky pridane seznamu pro obnoveni.
	 */
	public static void removeNodeSP(){
		setEditing(false);
		ObservableList<Node> list = SP.getChildren();
		if(list.isEmpty()){
			//System.out.println("Nelzde odebrat node SP je prázdný!");
		}else{
			doRefreshList();
			list.remove(list.size()-1);
			list.get(list.size()-1).setVisible(true);
		}
		//System.out.println("SP: "+SP.getChildren().size());
	}
	
	/**
	 * Provede obnovení pro prvky v seznamu pro obnovení a vymaže list
	 */
	private static void doRefreshList(){
		for(Refresh ref:refreshList){
			if(ref.type == ControlListType.ZBOZI){
				@SuppressWarnings("unchecked")
				TableView<items.Zbozi> tab = (TableView<items.Zbozi>)ref.table;
				tab.getItems().setAll(Main.vTable.gettZbozi().getItems());
			}
		}
		refreshList.clear();
	}
	
	/**
	 * Znovu načte aktuální tabulku z vTable
	 */
	public static void refreshSPtable(){
		SP.getChildren().setAll(vTable.getActualNode());
	}
	
	/**
	 * Změní zobrazovanou tabulku podle parametru
	 * 
	 * @param type	typ žádané tabulky
	 */
	private void changeTable(ControlList.ControlListType type){
		vTable.changeNode(type);
		refreshSPtable();
	}
	
	
	/**
	 * Připraví stromovou strukturu voleb pro hlavni okno
	 * 
	 * @return	tree node modu pro hlavni okno
	 */
	private Node getTree(){
		tree = new TreeView<ControlList>();
		
		tree.setEditable(false);
		//tree.setMinWidth(CONTROLLIST_PREF_WIDTH);
		tree.setPrefWidth(CONTROLLIST_PREF_WIDTH);
		createDefaultControlList(tree);
		BorderPane.setMargin(tree, new Insets(5));
		
		return tree;
	}
	
	
	/**
	 * Inicializace
	 */
	public void init() throws Exception {
		//System.out.println("Initialization");
	}
	
	// -----------------------------------ControlList ITEMS------------
	/**
	 * Vytvoří strukturu výběrového stromu
	 * 
	 * @param tree	výběrový strom
	 */
	private void createDefaultControlList(TreeView<ControlList> tree){
		tree.setRoot(new TreeItem<ControlList>());
		tree.setShowRoot(false);
		addControlListCategories(tree.getRoot());
	}
	
	/**
	 * Přidá hlavní uzly výběrového stromu
	 * 
	 * @param parent	rodičovský uzel
	 */
	@SuppressWarnings("unchecked")
	private void addControlListCategories(TreeItem<ControlList> parent){
		TreeItem<ControlList> categorySklad = new TreeItem<ControlList>(
				new ControlList("SKLAD"));
		TreeItem<ControlList> categoryUcetnictvi = new TreeItem<ControlList>(
				new ControlList("ŮČETNICTVÍ"));
		
		addControlListItemsSklad(categorySklad);
		addControlListItemsUcetnictvi(categoryUcetnictvi);
		
		tree.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<TreeItem<ControlList>>() {

					@Override
					public void changed(ObservableValue<? extends TreeItem<ControlList>> observable,
							TreeItem<ControlList> oldValue, TreeItem<ControlList> newValue) {
						
						changeTable(newValue.getValue().getType());
					}
				});
		
		
		parent.getChildren().addAll(categorySklad, categoryUcetnictvi);
	}
	
	/**
	 * Vytvoří listy výběrového stromu pro sklad.
	 * 
	 * @param parent	nadřazený uzel
	 */
	@SuppressWarnings("unchecked")
	private void addControlListItemsSklad(TreeItem<ControlList> parent){
		TreeItem<ControlList> zboziItem = new TreeItem<ControlList>(new ControlList(
								"Zboží",ControlList.ControlListType.ZBOZI));
		TreeItem<ControlList> prijemkyItem = new TreeItem<ControlList>(new ControlList(
								"Příjemky",ControlList.ControlListType.PRIJEMKY));
		TreeItem<ControlList> vydejkyItem = new TreeItem<ControlList>(new ControlList(
								"Výdejky",ControlList.ControlListType.VYDEJKY));
		
		
		
		parent.getChildren().addAll(zboziItem, prijemkyItem, vydejkyItem);
	}
	
	/**
	 * Vytvoří listy výběrového stromu pro účetnictví
	 * 
	 * @param parent	nadřazený uzel
	 */
	@SuppressWarnings("unchecked")
	private void addControlListItemsUcetnictvi(TreeItem<ControlList> parent){
		TreeItem<ControlList> fakturyItem = new TreeItem<ControlList>(new ControlList(
				"Faktury",ControlList.ControlListType.FAKTURY));
		TreeItem<ControlList> zakazkyItem = new TreeItem<ControlList>(new ControlList(
				"Zakázky",ControlList.ControlListType.ZAKAZKY));
		
		
		parent.getChildren().addAll(zakazkyItem, fakturyItem);
	}
	
	/**
	 * Obalová třída pro objeky které budou žádat obnovení z refresh listu
	 * @author Martin Hamet
	 *
	 */
	public static class Refresh{
		
		/**
		 * Tabulka
		 */
		@SuppressWarnings("rawtypes")
		TableView table;
		
		/**
		 * typ tabulky
		 */
		ControlList.ControlListType type;
		
		/**
		 * Vytvoří novou instanci třídy podle zadaných parametrů.
		 * 
		 * @param table	tabulka
		 * @param type	typ tabulky
		 */
		@SuppressWarnings("rawtypes")
		public Refresh(TableView table, ControlList.ControlListType type){
			this.table = table;
			this.type = type;
		}
	}
}
