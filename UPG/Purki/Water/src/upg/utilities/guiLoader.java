/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import upg.controllers.MainWindowController;

/**
 *
 * @author purkart
 */
public class guiLoader {
    
    
    public static Node loadFXML(String fileName){
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL(MainWindowController.PATH_IN_STRING+"/"+fileName));
            Node newNode = loader.load();
            return newNode;
        }
        catch(Exception ex)
        {
            System.out.println("Chyba v loadFXML() "+ex);
            return null;
        }
    }
}
