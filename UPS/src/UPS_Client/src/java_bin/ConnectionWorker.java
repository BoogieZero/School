package java_bin;

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionWorker extends Thread {

    //Constants
    /**
     * maximum message size
     */
    public final int MSG_SIZE_MAX = 20;
    /**
     * message header length
     */
    public final int HEADER_LENGTH = 2;
    /**
     * time for receiving consecutive data in one message
     */
    private final int WAIT_MSG_RCV = 1000;
    /**
     * Maximum time between messages from server for it to be considered as alive.
     */
    private final int SERVER_ALIVE_CHECK = 3000;
    /**
     * how often to send alive token
     */
    private final int CLIENT_ALIVE_UPDATE = 1000;
    /**
     * value added to accept (drawn cards) meaning flag is set
     */
    public static final int ACC_DONE = 100;
    /**
     * maximum winning score
     */
    public static final int SCORE_MAX = 21;
    /**
     * maximum name size
     */
    public static final int CLIENT_NAME_SIZE = 3;

    //Client
    /**
     * commands for client
     */
    public final int CMD_CL_ROOM_ID = 255;      //room id
    public final int CMD_CL_NAME_UPDATE = 254;  //present clients update
    public final int CMD_CL_START = 253;        //game started
    public final int CMD_CL_CARD = 252;         //new card
    public final int CMD_CL_DRAWN = 251;        //status update / number of cards drawn
    public final int CMD_CL_SCORE = 250;        //final score

    public final int CMD_CL_TERM = 21;          //client was disconnected

    //Expected msg body length
    public final int CMD_CL_LEN_ROOM_ID = 1;
    public final int CMD_CL_LEN_NAME_UPDATE = 15;
    public final int CMD_CL_LEN_START = 15;
    public final int CMD_CL_LEN_CARD = 1;
    public final int CMD_CL_LEN_DRAWN = 5;
    public final int CMD_CL_LEN_SCORE = 5;

    //Server
    /**
     * commands for server
     */
    public final int CMD_SV_ROOM_CREATE = 0;    //create room
    public final int CMD_SV_ROOM_JOIN = 1;      //join room
    public final int CMD_SV_ROOM_START = 2;     //vote start
    public final int CMD_SV_PLAY_HIT = 3;       //new card request
    public final int CMD_SV_PLAY_DONE = 4;      //done drawing
    public final int CMD_SV_ALIVE = 5;          //alive token

    /**
     * State of this client in relation to the game.
     *      0 - connecting, waiting for room id
     *      1 - start phase of the game, reading results from before
     *      2 - game - drawing cards, done
     *      3 - game - waiting for end (done)
     *      4 - watching final score + new start phase
     */
    private int gameState = 0;

    //Client settings
    /**
     * client settings
     */
    private final String name;                  //client name
    private final String host;                  //host ip
    private final int port;                     //host port
    private final Integer roomToJoin;           //room id to join / null for create new

    //GUI Reference
    /**
     * reference to GUI
     */
    private Controller controller;

    //Thread run cycle
    /**
     * set to true for running thread
     */
    public boolean run;
    /**
     * static access to run (set's run to false when set as true)
     */
    private static boolean CLOSE = false;

    //Timer
    /**
     * timer value for subsequent data receives in one message
     */
    private long end;
    /**
     * Timer value for validating server alive connection
     */
    private long srvrAlive;
    /**
     * Actual time.
     */
    private long curTime;
    /**
     * Timer for sending alive token to server
     */
    private long myAlive = 0;

    //Que
    /**
     * queue for requests from GUI
     */
    private Queue<RequestItem> que = new LinkedList<>();
    /**
     * lock for queue
     */
    private static final Lock mutex = new ReentrantLock(true);       //Que lock

    //Streams
    /**
     * socket's output stream
     */
    private OutputStream oos;
    /**
     * socket's input stream
     */
    private InputStream rd;
    /**
     * socket
     */
    private Socket socket;

    //Game
    /**
     * actual score
     */
    private int score = 0;

    /**
     * Main thread loop.
     * Connects socket once.
     * Checks input stream for incoming messages.
     * Sends requests from queue.
     * Runs if run is set to true.
     * After finish socket and it's streams are disposed of.
     */
    @Override
    public void run(){
        System.out.println("<Connection worker ["+name+"]> Started");
        try {
            connect();
        } catch (IOException e) {
            System.out.println("<Connection worker ["+name+"]> Could not establish connection to the server.");
            //Tell to GUI
            Platform.runLater(()->{
                controller.gotDisconnected();
                controller.lbConnect.setText("Could not establish connection to the server.");
                controller.lbMsg.setText("Host unreachable");
            });
            return;
            //run = false;
        }

        RequestItem rq;
        int err = 0;

        curTime = System.currentTimeMillis();
        srvrAlive = curTime;
        while(run){
            //Server alive check
            if((curTime - srvrAlive) > SERVER_ALIVE_CHECK){
                //Served not alive
                System.out.println("<Connection worker ["+name+"]> Server is not sending updates.");
                run = false;
            }
            curTime = System.currentTimeMillis();

            //Send alive token?
            if(gameState > 0 && myAlive < curTime){
                sendAlive();
                myAlive = curTime + CLIENT_ALIVE_UPDATE;
            }

            //GUI Closed?
            if(ConnectionWorker.CLOSE == true){
                ConnectionWorker.CLOSE = false;
                run = false;
            }

            //Receive
            try {
                err = receiveMsg();
            } catch (IOException e) {
                System.out.println("InputStream is not ready. IO Failed");
                err = -1;
                if(!run){
                    break;
                }
                //e.printStackTrace();
            }

            if(err < 0){
                //Disconnect by read
                run = false;
                break;
            }

            //Send
            mutex.lock();
            if(!que.isEmpty() && run){
                //work on que
                rq = que.remove();
                err = sendRequest(rq);
            }
            mutex.unlock();

            if(err < 0){
                //Disconnect by send
                run = false;
                break;
            }

            //Cpu HOG
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        //Main loop ended

        disconnect();

        Platform.runLater(()->{
            controller.gotDisconnected();
            controller.lbMsg.setText("Disconnected ");
        });

        System.out.println("<Connection worker ["+name+"]> Closed");
    }

    /**
     * Connects socket and get's it's streams (output, input).
     * @param host host's ip adress
     * @param port host's port
     * @throws IOException
     */
    private void establishSocket(String host, int port) throws IOException {
        System.out.println("<Connection worker ["+name+"]> Connecting to "+host+":"+port);
        socket = new Socket(host, port);
        InetAddress adress = socket.getInetAddress();

        oos = socket.getOutputStream();
        rd = socket.getInputStream();

        System.out.println("<Connection worker ["+name+"]> Connected");
    }

    /**
     * Sends create or join room command based on constructor call.
     * Joins this client to room on server.
     * @throws IOException stream or socket error
     */
    private void connect() throws IOException {
        establishSocket(host, port);
        char []nameB= Arrays.copyOf(name.toCharArray(), CLIENT_NAME_SIZE);

        for(int i=name.length();i<CLIENT_NAME_SIZE;i++){
            nameB[i] = ' ';   //fill spaces to max size
        }

        if(roomToJoin != null){
            //join existing room
            byte[]arr = new byte[6];
            arr[0] = 6;                     //length
            arr[1] = CMD_SV_ROOM_JOIN;      //cmd join
            //name
            arr[2] = (byte)nameB[0];
            arr[3] = (byte)nameB[1];
            arr[4] = (byte)nameB[2];
            //room to join
            arr[5] = roomToJoin.byteValue();

            oos.write(arr);
            oos.flush();
        }else{
            //new room
            byte[]arr = new byte[5];
            arr[0] = 5;                     //length
            arr[1] = CMD_SV_ROOM_CREATE;    //cmd create
            //name
            arr[2] = (byte)nameB[0];
            arr[3] = (byte)nameB[1];
            arr[4] = (byte)nameB[2];

            oos.write(arr);
            oos.flush();
        }

        System.out.println("<Connection worker ["+name+"]> \t Room request sent...");
    }

    /**
     * Sends request from queue to server and returns error value.
     *  error value:
     *      0   - successful send
     *      -1  - error while sending
     *
     *  if command DONE was sent game state is set appropriately
     *
     * @param rq    request for sending
     * @return      error value
     */
    private int sendRequest(RequestItem rq){
        char []bts = rq.arr;

        //Length
        int len = rq.msgLength;

        //Command
        int cmd = rq.command;

        try{
            oos.write(len);         //length
            oos.write(cmd);         //command

            if(rq.arr != null){     //array
                for(int i = 0; i < bts.length; i++){
                    oos.write(bts[i]);
                }
            }
            oos.flush();
        }catch (Exception e){
            System.out.println("<Connection Worker ["+name+"]> \t Write stream failed (send request).");
            //e.printStackTrace();
            return -1;
        }

        //Succcess

        if(cmd == 4 && gameState == 2){
            gameState = 3;          //move into state 3 -> waiting for results
        }

        System.out.println("<Connection Worker ["+name+"]> \t Request sent (cmd: "+cmd+")");
        return 0;

    }

    private void sendAlive() {
        try{
            oos.write(2);
            oos.write(CMD_SV_ALIVE);
        }catch(Exception e){
            System.out.println("<Connection Worker ["+name+"]> \tWrite stream failed (send alive).");
            //e.printStackTrace();
            run = false;
        }

    }

    /**
     * Adds new request to queue.
     * @param msgLength     length of the given message (including command and length)
     * @param command       command for the server
     * @param arr           body of the message
     */
    public void addRequest(int msgLength, int command, char []arr){
        mutex.lock();
        RequestItem rq = new RequestItem(msgLength, command, arr);
        boolean tr = que.add(rq);
        mutex.unlock();
    }

    /**
     * Checks if input stream contains enough bytes for header of message and if so
     * whole message is received. The message is then executed by execute().
     * @return              0 for successful receive otherwise error value
     * @throws IOException  error while reading from input buffer
     */
    private int receiveMsg() throws IOException {
        int size;
        int cmd;
        end = 0;

        if(rd.available() < HEADER_LENGTH){
            //not enough bytes ready
            return 0;
        }

        size = rd.read();
        cmd = rd.read();
        int []arr = null;

        if(size > MSG_SIZE_MAX){
            //Too long message (could not be good)
            return -1;
        }

        end = 0;
        if(size > HEADER_LENGTH){
            arr = new int[size - HEADER_LENGTH];    //body
            int read = HEADER_LENGTH;
            int i = 0;

            while(read < size){
                if(rd.available() > 0){
                    arr[i] = rd.read();
                    i++;
                    read++;
                    end = 0;
                }else{
                    //Timeout?
                    if(timeOut_msg()) return -1;
                }
            }
            //Array end
        }
        //Success

        srvrAlive = curTime;    //server sent something meaningful -> isAlive
        return execute(cmd, arr);

    }

    /**
     * Evaluates given command cmd and message body. Appropriate methods are called afterwards.
     * Checks if given command is valid against actual game state.
     * @param cmd   command
     * @param arr   message body
     * @return      0 for successful execution of a valid command otherwise -1
     */
    private int execute(int cmd, int[] arr){
        boolean stateFailed;
        if(!correctCmdLength(cmd, arr)){
            //Incorrect length
            stateFailed = true;
        }else{
            //Correct length
            stateFailed = false;
            switch(cmd){
                case CMD_CL_ROOM_ID:        //room id           255
                    if(gameState == 0){
                        setRoomId(arr[0]);  //gameState -> 1
                    }else stateFailed = true;
                    break;

                case CMD_CL_NAME_UPDATE:    //update names      254
                    setNames(arr);
                    if(gameState == 0){
                        //waiting to join game
                        setWaiting();
                    }
                    break;

                case CMD_CL_START:          //start game        253
                    if(gameState == 1){     //first start
                        setStart(arr);      //gameState -> 2
                    }else if(gameState == 4){
                        setStart(arr);      //gameState -> 2
                        //reset
                        score = 0;
                        gameState = 2;
                    }else stateFailed = true;
                    break;

                case CMD_CL_CARD:           //new card          252
                    if(gameState == 2){
                        setCard(arr[0]);    //gameState -> 3 (for score over 21)
                    }else stateFailed = true;
                    break;

                case CMD_CL_DRAWN:          //drawn             251
                    if(gameState == 1 || gameState == 4){           //accepts to start + watching score
                        Platform.runLater(()->{
                            controller.fillStatusStart(arr);        //status
                        });
                    }else if(gameState == 2 || gameState == 3 || gameState == 0){     //drawn cards update + done status
                        Platform.runLater(()->{
                            controller.fillCards(arr);              //drawn cards
                            controller.fillStatusGame(arr);         //status
                        });
                    }else stateFailed = true;
                    break;

                case CMD_CL_SCORE:          //game done         250
                    if(gameState == 3){         //finish ok
                        setScore(arr, false);   //gameState -> 4
                        break;
                    }else if(gameState == 2){   //inactivity end
                        setScore(arr, true);    //gameState -> 4
                    }else stateFailed = true;
                    break;

                case CMD_CL_TERM:           //client disconnected
                    System.out.println("TERM");
                    run = false;
                    break;

                default:
                    //Invalid command
                    System.out.println("<Connection worker ["+name+"]> \t invalid command (cmd: "+cmd+")");
                    return -1;
            }
            System.out.println("<Connection worker ["+name+"]> \t Executed cmd: "+cmd+" actual state: "+gameState);
        }

        if(stateFailed){
            System.out.println("<Connection worker ["+name+"]> \t Invalid (cmd: "+cmd+") in (Game state"+gameState+")");
            return -1;
        }
        //Command done
        return 0;
    }

    /**
     * Check if given command have corresponding body length.
     * @param cmd   command
     * @param arr   examined body length
     * @return      true for correct length or non-listed command
     */
    private boolean correctCmdLength(int cmd, int[] arr) {
        switch(cmd){
            case CMD_CL_ROOM_ID:
                if(arr.length != CMD_CL_LEN_ROOM_ID) return false;
                break;

            case CMD_CL_NAME_UPDATE:
                if(arr.length != CMD_CL_LEN_NAME_UPDATE) return false;
                break;

            case CMD_CL_START:
                if(arr.length != CMD_CL_LEN_START) return false;
                break;

            case CMD_CL_CARD:
                if(arr.length != CMD_CL_LEN_CARD) return false;
                break;

            case CMD_CL_DRAWN:
                if(arr.length != CMD_CL_LEN_DRAWN) return false;
                break;

            case CMD_CL_SCORE:
                if(arr.length != CMD_CL_LEN_SCORE) return false;
                break;

            default:
                return true;
        }
        return true;
    }

    /**
     * If timer was not set it will do so. Otherwise checks if the timer run out.
     * Returns true if timer run out otherwise false.
     * @return  true if timer run out
     */
    private boolean timeOut_msg(){
        if(end == 0){
            end = WAIT_MSG_RCV + System.currentTimeMillis();
        }else if(System.currentTimeMillis() > end){
            //Timeout
            System.out.println("<Connection worker ["+name+"]> \t timed out");
            return true;
        }
        return false;
    }

    /**
     * Disconnects client from the host server including socket and it's sctreams.
     */
    private void disconnect(){
        run = false;
        try{
            oos.flush();
            oos.close();
        }catch(Exception e){
            //already closed
            System.out.println("OutputStream is already closed");
        }
        try{
            rd.close();
        }catch(Exception e){
            //already closed
            System.out.println("InputStream is already closed");
        }
        try{
            socket.close();
        }catch(Exception e){
            //already closed
            System.out.println("Socket is already closed");
        }
        System.out.println("<Connection worker ["+name+"]> Forced Disconnection");
    }

    //CONSTRUCTORS

    /**
     * Creates new instance of this class.
     * @param host          host ip
     * @param port          host port
     * @param controller    reference to calling GUI
     * @param name          client's name
     * @param roomId        room id for joining, for new room set s null
     */
    public ConnectionWorker(String host, int port, Controller controller, String name, Integer roomId){
        this.name = name;
        this.host = host;
        this.port = port;
        this.controller = controller;
        this.roomToJoin = roomId;
        this.run = true;
    }

    /**
     * Class representing one request(message) from client for server.
     */
    private class RequestItem{
        /**
         * message length
         */
        public int msgLength;
        /**
         * command for server
         */
        public int command;
        /**
         * message body
         */
        public char []arr;

        /**
         * Creates new instance of this class.
         * @param msgLength message length
         * @param command   command for server
         * @param arr       message body
         */
        public RequestItem(int msgLength, int command, char []arr){
            super();
            this.msgLength = msgLength;
            this.command = command;
            this.arr = arr;
        }
    }

    //Commands UTIL

    /**
     * Returns array of names from given array.
     * Byte values of ASCII are expected in the given array for 3 names.
     * @param arr   names in numeric representation
     * @return      names as string array
     */
    private String[] getNamesFromInt(int []arr){
        String[] names = new String[5];
        int arrIdx = 0;

        Arrays.fill(names,"");

        for(int i=0;i<5;i++) {
            for(int j=0;j<3;j++){
                if(arr[arrIdx] != 0)
                    names[i]+=""+(char)arr[arrIdx];
                arrIdx++;
            }
        }

        return names;
    }

    /**
     * Static access to stopping this thread's main loop.
     * Used as clear-up method after closing GUI.
     */
    public static void killWorkers(){
        CLOSE = true;
    }

    //COMMANDS

    /**
     * Sets game state to start phase and informs GUI with acquired room id.
     * @param id    room id
     */
    private void setRoomId(int id){
        gameState = 1;
        Platform.runLater(()->{
            controller.gotRoomId(id);
        });
    }

    /**
     * Informs GUI about present clients in the room.
     * @param arr
     */
    private void setNames(int []arr){
        String []names = getNamesFromInt(arr);

        Platform.runLater(()->{
            controller.fillPresentClients(names);
        });
    }

    private void setWaiting(){
        Platform.runLater(()->{
            controller.gotWaiting();
        });
    }

    /**
     * Sets game state to playing and updates presents clients in the room for GUI.
     * @param arr
     */
    private void setStart(int[] arr) {
        gameState = 2;

        String[] names = getNamesFromInt(arr);

        Platform.runLater(()->{
            controller.fillPresentClients(names);
            controller.gotStart();
        });

    }

    /**
     * Sets new score and informs GUI.
     * @param value new card value added to score
     */
    private void setCard(int value) {
        score += value;

        if(score > SCORE_MAX) gameState = 3;

        Platform.runLater(()->{
            controller.gotCard(score, value);
        });
    }

    /**
     * Sets game state to watching score. GUI is informed about results.
     * @param arr       results of the game
     * @param timedOut  true if this client did not confirmed end of the game
     */
    private void setScore(int[] arr, boolean timedOut){
        gameState = 4;
        Platform.runLater(()->{
            controller.fillScore(arr);
            if(timedOut)
                controller.gotInactivityFlag();
        });
    }

}
