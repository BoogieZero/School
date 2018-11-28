package logoscislo;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Nastaveni logu
 * @author Martin Hamet
 *
 */
public class InterniKonfigurace {
	
	/**
	 * Konstruktor nastavi loggery a jejich format pro ostatni tridy.
	 * 
	 * @throws SecurityException	pristupova prava souboru
	 * @throws IOException	chyba pri zapisu do souboru
	 */
	public InterniKonfigurace() throws SecurityException, IOException {
		Formatovac form = new Formatovac();
		
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		ch.setFormatter(form);
		

		FileHandler fh = new FileHandler(Konstanty.LOGOVACI_SOUBOR, true);
		fh.setEncoding(Konstanty.KODOVANI);
		fh.setFormatter(form);
		
		//Hlavni
		Hlavni.hlavniLogger = Logger.getLogger(Hlavni.class.getName());
		Hlavni.hlavniLogger.setLevel(Level.ALL);
		//Hlavni.hlavniLogger.addHandler(ch);
		Hlavni.hlavniLogger.addHandler(fh);
		
		//Generator
		Generator.generatorLogger = Logger.getLogger(Generator.class.getName());
		Generator.generatorLogger.setLevel(Level.ALL);
		//Generator.generatorLogger.addHandler(ch);
		Generator.generatorLogger.addHandler(fh);
		
		//OsobniCislo
		OsobniCislo.OsobniCisloLogger = Logger.getLogger(OsobniCislo.class.getName());
		OsobniCislo.OsobniCisloLogger.setLevel(Level.ALL);
		//OsobniCislo.OsobniCisloLogger.addHandler(ch);
		OsobniCislo.OsobniCisloLogger.addHandler(fh);
	}
	
	/**
	 * Trida urcujici format logu.
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class Formatovac extends Formatter {

		@Override
		public String format(LogRecord rec) {
			int dotIndex = rec.getLoggerName().lastIndexOf(".");
			String s = 
				String.format("%d. [%s - %s.%s()] - %s%s", 
						rec.getSequenceNumber(),
						rec.getLevel().getName(),
						rec.getLoggerName().substring(dotIndex +  1),
						rec.getSourceMethodName(),
						rec.getMessage(),
						System.getProperty("line.separator")
						);
			return s;
		}

	}
}
