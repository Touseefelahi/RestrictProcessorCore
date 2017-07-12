package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements javafx.fxml.Initializable {
 //   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));

    public long add(int input1, int input2) {
        return input1+input2;
    }
    private int affinityValue;
    @FXML private TableView<ObservableList<String>> tableView;
    @FXML private HBox layoutHBoxCores;
    @FXML private ProgressIndicator progressIndicator;

    private Image imageProcessorOff,imageProcessorOn;

    @FXML  private void buttonGetProcessedClick(ActionEvent event){
        tableView.getItems().clear();
        ObservableList<String[]> processes =  Utility.listRunningProcessesWithDetails();

        for (String[]process:processes) {
            tableView.getItems().add(FXCollections.observableArrayList( process ));
        }
    }

    @Override public void initialize(URL url, ResourceBundle rb) {
        initializeResources();
        initializeProcessorCoreGui();
        initializeTableView();
        buttonGetProcessedClick(null);
    }

    private void initializeTableView(){
        TableColumn<ObservableList<String>,String> columnProcessName=new TableColumn<>("Process Name");
        columnProcessName.setCellValueFactory(param ->new ReadOnlyObjectWrapper<>(param.getValue().get(0)));

        TableColumn<ObservableList<String>,String> columnProcessID=new TableColumn<>("Process ID");
        columnProcessID.setCellValueFactory(param ->new ReadOnlyObjectWrapper<>(param.getValue().get(1)));

        tableView.getColumns().addAll(columnProcessName,columnProcessID);
    }

    private void initializeProcessorCoreGui() {
        int cores = Runtime.getRuntime().availableProcessors();
        for (int index = cores-1; index >= 0; index--) {
            ImageView imageView = new ImageView("Resources/Images/ProcessorON.png");
            imageView.setFitHeight(36);
            imageView.setFitWidth(36);
            imageView.setId("coreIcon_"+index);
            imageView.setOnMouseClicked((MouseEvent event) -> {
                processorCoreClicked(event,imageView);
            });
            layoutHBoxCores.getChildren().add(imageView);
        }
    }

    private void initializeResources() {
        imageProcessorOff = new Image("Resources/Images/ProcessorOFF.png");
        imageProcessorOn = new Image("Resources/Images/ProcessorON.png");
        affinityValue = (int) Math.pow(2,Runtime.getRuntime().availableProcessors()) - 1;
    }

    private void processorCoreClicked(MouseEvent event, ImageView imageView) {
        String imageViewId = imageView.getId();
        String[] imageViewIndex=imageViewId.split("_");
        int coreNumber=Integer.parseInt(imageViewIndex[1]);
        affinityValue=Utility.toggleBit(affinityValue,coreNumber);
        if(Utility.isBitSet(affinityValue,coreNumber))
        imageView.setImage(imageProcessorOn);
        else imageView.setImage(imageProcessorOff);
    }

    @FXML private void buttonSetAffinityClicked(ActionEvent event) throws InterruptedException {
        progressIndicator.setVisible(true);
       /* Thread setAffinityThread = new Thread(() -> {
            for (String processName:Utility.listRunningProcesses()) {
                if(processName.contains("java"))continue; //It won't set affinity to Java processes
                Utility.setAffinityForWindows(processName,affinityValue);
            }
            //System.out.println(Integer.toBinaryString(affinityValue));
            progressIndicator.setVisible(false);
        });*/
        Thread setAffinityThread = new Thread(() -> {
            String[] processes = Utility.listRunningProcesses().toArray(new String[0]);
            int index=0;
            for (Node tableRowNode:tableView.lookupAll("TableRow")) {
                String processName = processes[index++];
                boolean status = Utility.setAffinityForWindows(processName,affinityValue);
                if(!status) {
                    TableRow tableRow=(TableRow)tableRowNode;
                    tableRow.setStyle("-fx-background-color: rgb(219, 46, 72); -fx-font-weight:bold;");
                }
            }
            progressIndicator.setVisible(false);
        });
        setAffinityThread.start();
    }

    @FXML private void buttonBackToNormalAffinityClicked(ActionEvent event){
        progressIndicator.setVisible(true);
        final int[] index = {0};
        affinityValue=(int)Math.pow(2,Runtime.getRuntime().availableProcessors()) - 1;
        Thread setAffinityThread = new Thread(() -> {
            String[] processes= Utility.listRunningProcesses().toArray(new String[0]);
            int index1=0;
            for (Node tableRowNode:tableView.lookupAll("TableRow")) {
                String processName = processes[index1++];
                boolean status = Utility.setAffinityForWindows(processName,affinityValue);
                if(!status) {
                    TableRow tableRow=(TableRow)tableRowNode;
                    tableRow.setStyle("-fx-background-color: rgb(200, 50, 23); -fx-font-weight:bold;");
                }
            }
            progressIndicator.setVisible(false);
            setProcessorGui(affinityValue);
        });
        setAffinityThread.start();
    }

    private void setProcessorGui(int affinityValue) {
        for (int index=0;index<Runtime.getRuntime().availableProcessors();index++){
            ImageView coreImage= (ImageView)this.layoutHBoxCores.lookup("#coreIcon_"+index);
            if(Utility.isBitSet(affinityValue,index))
            coreImage.setImage(imageProcessorOn);
            else coreImage.setImage(imageProcessorOff);
        }
    }
}
