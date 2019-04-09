/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temclient;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import socketfx.Constants;
import socketfx.FxSocketClient;
import socketfx.SocketListener;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class ClientGUIController implements Initializable {

    @FXML
    private Button sendButton, start;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button connectButton;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private Label lblName1, lblName2, lblName3, lblName4, lblMessages, clientLabel;
    @FXML
    private ImageView imgS0, imgS1, imgS2, imgS3, imgS4, imgS5, imgS6, imgS7, imgS8, imgS9, 
            imgC0, imgC1, imgC2, imgC3, imgC4, imgC5, imgC6, imgC7, imgC8, imgC9,
            imgS01, imgS11, imgS21, imgS31, imgS41, imgS51, imgS61, imgS71, imgS81, imgS91,   receive, discard;
    @FXML
    private GridPane gPaneServer, gPaneClient, discards;

    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private boolean isConnected, turn, serverUNO = false, clientUNO = false;

    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketClient socket;

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                hostTextField.getText(),
                Integer.valueOf(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//                connectButton.setDisable(false);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                connectButton.setDisable(true);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case CONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//            case AUTOCONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        /*
         * Uncomment to have autoConnect enabled at startup
         */
//        autoConnectCheckBox.setSelected(true);
//        displayState(ConnectionDisplayState.WAITING);
//        connect();
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            System.out.println("message received client");
            //lblMessages.setText(line);

//            switch(line){
//                case "deal":
//                    handleStart();
//            }
if(line.equals("red")){
                    clientLabel.setText("GAME OVER");
                }else if(line.equals("green")){
                    clientLabel.setText("YOU WON");
                }
            System.out.println("the line is " + line);

            if (line.length() > 4) {

                if (line.substring(0, 5).equals("first")) {

                    System.out.println(line.substring(line.length() - 4, line.length()));
                    hand2D.add(line.substring(5));
                    getAllCardViews();

                } else if (line.substring(0, 4).equals("main")) {
                    receive.setImage(new Image(line.substring(4)));
                    //line.substring(4)
                } else if (line.equals("clear")) {
                    hand2D.clear();
                    for (int i = 0; i < 10; i++) {
                        hand2I.get(i).setImage(null);
                    }
                }else if(line.equals("clear2")){
                    for(int i = 0; i< hand2I.size();i++){
                        
                        
                        hand2I.get(i).setImage(null);
                    }
                }
                
                else if(line.equals("clearsecond")){
                    hand2Ipart1S.clear();
                    for(int i = 0; i< hand2Ipart1.size();i++){
                        hand2Ipart1.get(i).setImage(null);
                    }
                }else if(line.substring(0, 6).equals("second")){
                     
                    for(int i = 0; i< hand2Ipart1S.size();i++){
                        hand2Ipart1.get(i).setImage(null);
                    }
                    hand2Ipart1S.add(line.substring(6));
                    
                    for(int i = 0; i<hand2Ipart1S.size();i++){
                        hand2Ipart1.get(i).setImage(new Image(hand2Ipart1S.get(i)));
                    }
                    
                }else if(line.substring(0,5).equals("other")){
                    for(int i = 0; i< Integer.parseInt(line.substring(5));i++){
           hand2I.get(i). setImage(new Image("resources/BACK-7.jpg"));
        }

                    
                }
            } else if (line.length() > 7) {
                if (line.substring(0, 7).equals("message")) {
                    lblMessages.setText(line);
                }

            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {

        }
    }

    @FXML
    private void getAllCardViews() {

        for (int i = 0; i < hand2D.size(); i++) {

            hand2I.get(i).setImage(new Image(hand2D.get(i)));
            System.out.println("array : " + hand2I.size() + " and " + hand2D.size());
        }
        
        //imgS01.setImage(new Image("resources/BACK-4.jpg"));

    }

    @FXML
    private void startButton(ActionEvent event) {
        handleStart();
        socket.sendMessage("1ready");
        receive.setImage(new Image("resources/BACK-7.jpg"));
    }

    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        if (!sendTextField.getText().equals("")) {
            String x = sendTextField.getText();
            socket.sendMessage(x);
            System.out.println("sent message client");

        }

    }

    @FXML
    private void handleConnectButton(ActionEvent event) {
        connectButton.setDisable(true);

        displayState(ConnectionDisplayState.WAITING);
        connect();
    }

    @FXML
    private void sendPart1(MouseEvent event) {

        System.out.println("test this");
        int imgClicked;
        imgClicked = gPaneClient.getColumnIndex((ImageView) event.getSource());
        socket.sendMessage("z" + imgClicked);
        hand2Ipart1S.clear();

    }

   

    @FXML
    private void sendToServer(ActionEvent event) {

        //imgClicked = discards.getColumnIndex((ImageView) event.getSource());
        socket.sendMessage("o");

    }

    @FXML
    private void handleStart() {

        System.out.println("this is a test");

        start.setDisable(true);
        hand2I.add(imgS0);
        hand2I.add(imgS1);
        hand2I.add(imgS2);
        hand2I.add(imgS3);
        hand2I.add(imgS4);
        hand2I.add(imgS5);
        hand2I.add(imgS6);
        hand2I.add(imgS7);
        hand2I.add(imgS8);
        hand2I.add(imgS9);
        

        System.out.println("wit this work");

        hand2Ipart1.add(imgS01);
        hand2Ipart1.add(imgS11);
        hand2Ipart1.add(imgS21);
        hand2Ipart1.add(imgS31);
        hand2Ipart1.add(imgS41);
        hand2Ipart1.add(imgS51);
        hand2Ipart1.add(imgS61);
        hand2Ipart1.add(imgS71);
        hand2Ipart1.add(imgS81);
        hand2Ipart1.add(imgS91);
     

        for (int i = 0; i < 10; i++) {
            hand2I.get(i).setImage(new Image("resources/BACK-7.jpg"));
        }

        imgC0.setImage(new Image("resources/BACK-7.jpg"));
        imgC1.setImage(new Image("resources/BACK-7.jpg"));
        imgC2.setImage(new Image("resources/BACK-7.jpg"));
        imgC3.setImage(new Image("resources/BACK-7.jpg"));
        imgC4.setImage(new Image("resources/BACK-7.jpg"));
        imgC5.setImage(new Image("resources/BACK-7.jpg"));
        imgC6.setImage(new Image("resources/BACK-7.jpg"));
        imgC7.setImage(new Image("resources/BACK-7.jpg"));
        imgC8.setImage(new Image("resources/BACK-7.jpg"));
        imgC9.setImage(new Image("resources/BACK-7.jpg"));
      
//        
        System.out.println("this is he he");

    }
    @FXML
    int imgClicked;

    // List<Card> deck = new ArrayList<>();
    List<ImageView> hand2I = new ArrayList<>();
    List<ImageView> hand2Ipart1 = new ArrayList<>();

    List<String> hand2D = new ArrayList<>();
    List<String> hand2Ipart1S = new ArrayList<>();
}
