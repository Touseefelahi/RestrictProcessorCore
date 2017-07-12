package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Restrict Processor");
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        int cores=Runtime.getRuntime().availableProcessors();
        ImageView imageView=new ImageView("Resources/Images/ProcessorON.png");
        imageView.setFitHeight(36);
        imageView.setFitWidth(36);
        for (int index=0;index<cores;index++){
            //roo.gelayoutHBoxCores.getChildren().add(imageView);
        }
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
