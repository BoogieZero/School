package main;

import com.sun.deploy.util.ArrayUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    ConnectionWorker cnwr;

    //Main
    /**
     * Main components references
     */
    @FXML public Label lbConnect;
    @FXML public TextField txIp;
    @FXML public TextField txPort;
    @FXML public TextField txName;
    @FXML public TextField txRoomId;
    @FXML public Label lbMsg;
    @FXML public Label lbRoomId;
    @FXML public Label lbScore;

    //Buttons
    /**
     * Active button references
     */
    @FXML public Button btNewRoom;
    @FXML public Button btJoin;
    @FXML public Button btStart;
    @FXML public Button btDraw;
    @FXML public Button btDone;

    //Body
    /**
     * Main room body reference
     */
    @FXML public GridPane gridPane;

    /**
     * [y][x]
     * y - rows
     * x - Name, Cards, Status, Score
     */
    Label [][] gridNodes = new Label[5][4];

    //Buttons

    /**
     * Implementation of Click_Action events from GUI.
     */
    @FXML
    private void bt_newRoom_Click_Action(){
        setDisableJoin(true);
        clearGrid();
        int port = 0;
        String host = "";
        String name = "";

        boolean failedAttrib = false;

        try{
            //port = Integer.parseInt(txPort.getText().trim());
        }catch (Exception e){
            failedAttrib = true;
        }
        host = txIp.getText();
        //if(host.length() > 15 || host.length() < 7) failedAttrib = true;
        name = txName.getText();
        if(name.length() > 3 || name.length() == 0) failedAttrib = true;

        //Testing
        host = "192.168.44.3";
        port = 10000;
        //
        if(failedAttrib){
            lbMsg.setText("Invalid settings");
            setDisableJoin(false);
            return;
        }

        lbMsg.setText("Waiting for room");
        cnwr = new ConnectionWorker(host, port, this, name, null);
        cnwr.start();

    }

    @FXML
    private void bt_joinRoom_Click_Action(){
        setDisableJoin(true);
        clearGrid();
        int port = 0;
        String host = "";
        String name = "";
        int roomId = -1;

        boolean failedAttrib = false;

        try{
            //port = Integer.parseInt(txPort.getText().trim());
            roomId = Integer.parseInt((txRoomId.getText()));
        }catch (Exception e){
            failedAttrib = true;
        }
        host = txIp.getText();
        //if(host.length() > 15 || host.length() < 7) failedAttrib = true;
        name = txName.getText();
        if(name.length() > 3 || name.length() == 0) failedAttrib = true;


        //Testing
        host = "192.168.44.3";
        port = 10000;
        failedAttrib = false;
        //
        if(failedAttrib){
            lbMsg.setText("Invalid settings");
            setDisableJoin(false);
            return;
        }

        lbMsg.setText("Joining room");
        cnwr = new ConnectionWorker(host, port, this, name, roomId);
        cnwr.start();
    }

    @FXML
    private void bt_start_Click_Action(){
        btStart.setDisable(true);
        int len = cnwr.HEADER_LENGTH;
        int cmd = cnwr.CMD_SV_ROOM_START;
        cnwr.addRequest(len,cmd,null);
    }

    @FXML
    private void bt_draw_Click_Action(){
        btDraw.setDisable(true);
        btDone.setDisable(true);

        int len = cnwr.HEADER_LENGTH;
        int cmd = cnwr.CMD_SV_PLAY_HIT;
        cnwr.addRequest(len,cmd,null);
    }

    @FXML
    private void bt_done_Click_Action(){
        btDone.setDisable(true);
        btDraw.setDisable(true);

        int len = cnwr.HEADER_LENGTH;
        int cmd = cnwr.CMD_SV_PLAY_DONE;
        cnwr.addRequest(len,cmd,null);
    }

    @FXML
    private void bt_leave_Click_Action(){
        if(cnwr == null) Platform.exit();
        if(cnwr.run){
            //Close connection
            cnwr.run = false;
            try{
                cnwr.join();
            }catch(Exception e){
                System.out.println("ConnectionWorker did not join");
            }
            gotDisconnected();
        }else{
            //Close application
            Platform.exit();
        }
    }

    //Sets

    /**
     * Enables / disables join options.
     * @param disable   true for disabling
     */
    private void setDisableJoin(boolean disable){
        txIp.setDisable(disable);
        txPort.setDisable(disable);
        txName.setDisable(disable);
        txRoomId.setDisable(disable);
        btNewRoom.setDisable(disable);
        btJoin.setDisable(disable);
    }

    /**
     * Sets GUI into default state.
     */
    private void defaultState(){

        setDisableJoin(false);

        btStart.setDisable(true);
        btDraw.setDisable(true);
        btDone.setDisable(true);
        txRoomId.setText("");
        txName.setText("");
        txPort.setText("");
        txIp.setText("");

        lbMsg.setText("Not Connected");
        lbConnect.setText("Not Connected");

        clearGrid();
    }

    /**
     * Sets GUI to state after disconnection from the host.
     */
    public void gotDisconnected(){
        setDisableJoin(false);

        btStart.setDisable(true);
        btDraw.setDisable(true);
        btDone.setDisable(true);
        lbScore.setText("");
        lbScore.setTextFill(Color.BLACK);

        lbMsg.setText("Disconnected");
        lbConnect.setText("Not Connected");
    }

    /**
     * Updates present clients in the room. The non-present ones are
     * set as invisible.
     * @param names
     */
    public void fillPresentClients(String[]names){
        char nl = (char)0;
        for(int i=0;i<names.length;i++){
            if(names[i].equals("") || names[i].equals(nl)){
                names[i] = "Not Connected";
                setPlayerActive(i, false);
            }else{
                setPlayerActive(i,true);
            }
            gridNodes[i][0].setText(names[i]);
        }
    }

    /**
     * Sets one client with given index as active / inactive.
     * @param index     client index
     * @param active    true for set active
     */
    private void setPlayerActive(int index, boolean active){
        if(!gridNodes[index][1].isVisible() == active){
            //Values are not the same
            for(int i=1;i<4;i++){
                gridNodes[index][i].setVisible(active);
            }
        }
    }

    /**
     * Updates drawn card values for each client. Arbitrary flag is ignored.
     * @param cards array with corresponding values of drawn cards
     */
    public void fillCards(int []cards){
        //Remove arbitrary vote value
        for(int i=0;i<cards.length;i++){
            if(cards[i]>ConnectionWorker.ACC_DONE){
                gridNodes[i][1].setText(cards[i] - ConnectionWorker.ACC_DONE+"");
            }else{
                gridNodes[i][1].setText(cards[i]+"");
            }
        }
    }

    /**
     * Updates status for the start phase.
     * @param acc   array with corresponding status values
     */
    public void fillStatusStart(int []acc){
        fillStatus("Ready", "Waiting", ConnectionWorker.ACC_DONE, acc);
    }

    /**
     * Updates status for the play phase.
     * @param acc   array with corresponding status values
     */
    public void fillStatusGame(int []acc){
        fillStatus("Done", "Drawing", ConnectionWorker.ACC_DONE, acc);
    }

    /**
     * Updates status of clients with over value if flag for client was set. Value under is
     * used otherwise.
     * @param over      status value for flag set
     * @param under     status value for flag not set
     * @param value     threshold for flag to be considered as set
     * @param acc       array of corresponding status values
     */
    private void fillStatus(String over, String under, int value, int []acc){
        for(int i=0;i<acc.length;i++){
            if(acc[i]>=value){
                gridNodes[i][2].setText(over);
            }else{
                gridNodes[i][2].setText(under);
            }
        }
    }

    private void clearGrid(){
        lbScore.setText("");
        lbScore.setTextFill(Color.BLACK);
        String []dummy = new String[] {"","","","",""};
        fillPresentClients(dummy);
        int []dummyI = new int[] {0,0,0,0,0};
        fillCards(dummyI);
        fillStatusStart(dummyI);
        clearScore();
    }

    /**
     * Clears displayed score values.
     */
    private void clearScore(){
        for(int i=0;i<5;i++){
            gridNodes[i][3].setText("");
            gridNodes[i][3].setTextFill(Color.BLACK);
        }
    }

    /**
     * Fills final score result. Score above maximum is marked as red. All winning
     * score values are marked by green.
     * @param score corresponding score values
     */
    public void fillScore(int []score){
        btDone.setDisable(true);
        btDone.setDisable(true);

        btStart.setDisable(false);
        Integer []arr = new Integer [score.length];

        for(int i=0;i<score.length;i++){
            arr[i] = score[i];
            arr[i] <<= 3;
            arr[i] += i;
            if(score[i]>ConnectionWorker.SCORE_MAX){
                gridNodes[i][3].setText(score[i]+"");
                gridNodes[i][3].setTextFill(Color.RED);
            }else{
                gridNodes[i][3].setText(score[i]+"");
                gridNodes[i][3].setTextFill(Color.BLACK);
            }
        }

        //Highlight the best one/ones
        Arrays.sort(arr, Collections.reverseOrder());


        int scrW = -1;
        int i;
        for(i=0;i<5;i++){               //find best score
            scrW = arr[i] >> 3;
            if(scrW <= ConnectionWorker.SCORE_MAX)
                break;
        }
        int idW = arr[i] & 0b111;
        gridNodes[idW][3].setTextFill(Color.GREEN);

        int scr, id;
        i++;
        for(;i<5;i++){                  //mark others with the same score
            scr = arr[i] >> 3;
            if(scr == scrW){
                id = arr[i] & 0b111;
                gridNodes[id][3].setTextFill(Color.GREEN);
            }
        }

        lbMsg.setText("Game ended");
    }

    /**
     * Displays message informing client about his inactivity.
     */
    public void gotInactivityFlag(){
        lbMsg.setText("Due to your inactivity game ended");
    }

    public void gotWaiting(){
        lbMsg.setText("Waiting for round to end.");
    }

    /**
     * Sets given room id. Allows start confirmation.
     * @param id    acquired room id
     */
    public void gotRoomId(int id){
        lbRoomId.setText(id+"");
        lbMsg.setText("Waiting for all clients to confirm start");
        btStart.setDisable(false);
    }

    /**
     * Informs about game started and allows client to play.
     */
    public void gotStart(){

        btDraw.setDisable(false);
        btDone.setDisable(false);
        lbMsg.setText("Game stared");

        lbScore.setText("0");
        lbScore.setTextFill(Color.BLACK);
        clearScore();
    }

    /**
     * Updates score value and displays acquired card value from the host.
     * @param score actual score
     * @param value acquired card value
     */
    public void gotCard(int score, int value){
        lbScore.setText(score+"");
        lbMsg.setText("Received card: "+value);
        if(score > ConnectionWorker.SCORE_MAX){
            //Over -> done
            lbScore.setTextFill(Color.RED);
            bt_done_Click_Action();
        }else{
            btDraw.setDisable(false);
            btDone.setDisable(false);
        }
    }

    //Init



    /**
     * Creates label references array for easy future updates. Default state for GUI is set.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i=0;i<5;i++){
            for(int j=0;j<4;j++){
                Label lb = new Label("Label");
                gridNodes[i][j] = lb;
                gridPane.add(lb, j, i+1);
            }
        }
        defaultState();
    }
}
