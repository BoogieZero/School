package Main;

import java.time.LocalDate;

public class Safe {
	/**
	 * Seznam zboži na skladé
	 */
	public storage.StockZbozi zbozi;
	
	/**
	 * Seznam příjemek v systému
	 */
	public storage.StockPrijemky prijemky;
	
	/**
	 * Seznam výdejek v systému
	 */
	public storage.StockVydejky vydejky;
	
	/**
	 * Seznam zakázek v systému
	 */
	public storage.StockZakazky zakazky;
	
	/**
	 * Seznam faktur v systému
	 */
	public storage.StockFaktury faktury;
	
	/**
	 * Vytvoří novou instanci třídy.
	 */
	public Safe(){
		this.zbozi = new storage.StockZbozi();
		this.prijemky = new storage.StockPrijemky();
		this.vydejky = new storage.StockVydejky();
		this.zakazky = new storage.StockZakazky();
		this.faktury = new storage.StockFaktury();
		
		createDefaultStuff();
	}
	
	/**
	 * Vytvoří základný obsah systému
	 */
	public void createDefaultStuff(){
		//Zboží----------------------------------
		items.Zbozi zb1 = new items.Zbozi("Jistič 25/3B", items.Zbozi.Units.ks, 150f, "OEZ", "info");
		items.Zbozi zb2 = new items.Zbozi("Jistič 25/1B", items.Zbozi.Units.ks, 50f, "OEZ", "info");
		items.Zbozi zb3 = new items.Zbozi("Jistič 20/3B", items.Zbozi.Units.ks, 135f, "OEZ", "info");
		items.Zbozi zb4 = new items.Zbozi("Jistič 16/3B", items.Zbozi.Units.ks, 120f, "OEZ", "info");
		items.Zbozi zb5 = new items.Zbozi("Jistič 16/1B", items.Zbozi.Units.ks, 40f, "OEZ", "info");
		items.Zbozi zb6 = new items.Zbozi("Svorky SAK 10", items.Zbozi.Units.bal, 100f, "ELKAS", "info");
		items.Zbozi zb7 = new items.Zbozi("Svorky SAK 6", items.Zbozi.Units.bal, 90f, "ELKAS", "info");
		items.Zbozi zb8 = new items.Zbozi("Propojka Q2 SAK6", items.Zbozi.Units.bal, 75f, "ELKAS", "info");
		items.Zbozi zb9 = new items.Zbozi("CYKY 10 černá", items.Zbozi.Units.m, 17f, "ELKAS", "info");
		
		zbozi.addZbozi(zb1);
		zbozi.addZbozi(zb2);
		zbozi.addZbozi(zb3);
		zbozi.addZbozi(zb4);
		zbozi.addZbozi(zb5);
		zbozi.addZbozi(zb6);
		zbozi.addZbozi(zb7);
		zbozi.addZbozi(zb8);
		zbozi.addZbozi(zb9);
		
		//System.out.println("Zboží vytvořeno a přidáno");
		//System.out.println(zbozi.getZbozi().toString());
		
		//Příjemky----------------------------------
		items.Prijemka pr1 = new items.Prijemka("OEZ",500, zbozi.getZbozi());
		items.Prijemka pr2 = new items.Prijemka("HYUNDAI",800, zbozi.getZbozi());
		items.Prijemka pr3 = new items.Prijemka("EATON",200, zbozi.getZbozi());
		items.Prijemka pr4 = new items.Prijemka("OEZ",150, zbozi.getZbozi());
		items.Prijemka pr5 = new items.Prijemka("NEPTUN",520, zbozi.getZbozi());
		
		prijemky.addPrijemka(pr1);
		prijemky.addPrijemka(pr2);
		prijemky.addPrijemka(pr3);
		prijemky.addPrijemka(pr4);
		prijemky.addPrijemka(pr5);
		//System.out.println("Příjemky vytvořeny a přidány");
		//System.out.println(prijemky.getPrijemky().toString());
		
		//Zakázky----------------------------------
				items.Zakazka za1 = new items.Zakazka("ELKAS", "Elkas", null, 20000, 10000);
				items.Zakazka za2 = new items.Zakazka("ELDOR", "Eldor", null, 50000, 26000);
				items.Zakazka za3 = new items.Zakazka("OEZ", "Oez", null, 75000, 55000);
				items.Zakazka za4 = new items.Zakazka("ENERGON", "Energon", null, 20000, 8000);
				items.Zakazka za5 = new items.Zakazka("ELKAS", "Elkas", null, 26000, 11000);
				
				LocalDate date1 = LocalDate.now().plusWeeks(3);
				LocalDate date2 = LocalDate.now().plusWeeks(8);
				LocalDate date3 = LocalDate.now().plusWeeks(20);
				LocalDate date4 = LocalDate.now().plusWeeks(1);
				LocalDate date5 = LocalDate.now().plusWeeks(9);
				
				za1.setDeadline(date1);
				za2.setDeadline(date2);
				za3.setDeadline(date3);
				za4.setDeadline(date4);
				za5.setDeadline(date5);
				
				zakazky.addZakazka(za1);
				zakazky.addZakazka(za2);
				zakazky.addZakazka(za3);
				zakazky.addZakazka(za4);
				zakazky.addZakazka(za5);
				//System.out.println("Zakázky vytvořeny a přidány");
				//System.out.println(zakazky.getZakazky().toString());
		
		//Výdejky----------------------------------
		items.Vydejka vy1 = new items.Vydejka(za1, zbozi.getZbozi());
		items.Vydejka vy2 = new items.Vydejka(za2, zbozi.getZbozi());
		items.Vydejka vy3 = new items.Vydejka(za3, zbozi.getZbozi());
		items.Vydejka vy4 = new items.Vydejka(za4, zbozi.getZbozi());
		items.Vydejka vy5 = new items.Vydejka(za5, zbozi.getZbozi());
		
		vy1.setExpencses(100);
		vy2.setExpencses(200);
		vy3.setExpencses(800);
		vy4.setExpencses(500);
		vy5.setExpencses(2500);
		
		vy1.setProject(za1);
		vy2.setProject(za2);
		vy3.setProject(za3);
		vy4.setProject(za4);
		vy5.setProject(za5);
		
		
		vydejky.addVydejka(vy1);
		vydejky.addVydejka(vy2);
		vydejky.addVydejka(vy3);
		vydejky.addVydejka(vy4);
		vydejky.addVydejka(vy5);
		//System.out.println("Výdejky vytvořeny a přidány");
		//System.out.println(vydejky.getVydejky().toString());
		
		
		
		//Faktury----------------------------------
		items.Faktura fa1 = new items.Faktura(za1, 20000);
		items.Faktura fa2 = new items.Faktura(za2, 50000);
		items.Faktura fa3 = new items.Faktura(za3, 75000);
		items.Faktura fa4 = new items.Faktura(za4, 20000);
		items.Faktura fa5 = new items.Faktura(za5, 25000);
		
		fa1.setProject(za1);
		fa2.setProject(za2);
		fa3.setProject(za3);
		fa4.setProject(za4);
		fa5.setProject(za5);
		
		fa1.setDueDate(date1);
		fa2.setDueDate(date5);
		fa3.setDueDate(date4);
		fa4.setDueDate(date2);
		fa5.setDueDate(date3);
		
		fa1.setClosed(true);
		fa2.setClosed(true);
		fa3.setClosed(true);
		fa4.setClosed(true);
		fa5.setClosed(true);
		
		faktury.addFaktura(fa1);
		faktury.addFaktura(fa2);
		faktury.addFaktura(fa3);
		faktury.addFaktura(fa4);
		faktury.addFaktura(fa5);
		
		za1.setBill(fa1);
		za2.setBill(fa2);
		za3.setBill(fa3);
		za4.setBill(fa4);
		za5.setBill(fa5);
		//System.out.println("Faktury vytvořeny a přidány");
		//System.out.println(faktury.getFaktury().toString());
		
	}
}
