package com.ibntaymiyya.gym;




import com.ibntaymiyya.gym.Util.CreateDataBase;
import com.ibntaymiyya.gym.Util.DBConnection;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import io.github.palexdev.materialfx.theming.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx. scene. image. Image;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private static Main instance;

    public static Stage stage;
    public static Stage mainStage;
    public static Stage currentStage;




    @Override
    public void start(Stage primaryStage) throws IOException {


        CreateDataBase.initializeDatabase();

        instance = this;

        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        stage = primaryStage;
        Main.currentStage = primaryStage;

        clearLoginData();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/ibntaymiyya/gym/fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle(SaveFileManager.getText("appName"));
        primaryStage.setMinWidth(502);
        primaryStage.setMinHeight(620);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/ibntaymiyya/gym/Images/gym-image.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(scene);
        primaryStage.show();


        mainStage = new Stage();



    }


    public static Main getInstance(){
        return instance;
    }

    public void mainStage() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/MainPage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/ibntaymiyya/gym/Images/gym-image.png")));
        mainStage.getIcons().add(icon);

        mainStage.setMaximized(true);
        mainStage.setMinWidth(950);
        mainStage.setMinHeight(620);
        mainStage.setTitle(SaveFileManager.getText("appName"));
        mainStage.setScene(scene);
        mainStage.show();

        stage.close();
        currentStage = mainStage;
    }

    public void openPrimaryStage() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (scene != null) {
            Main.stage.setTitle(SaveFileManager.getText("appName"));
            Main.stage.setMinWidth(502);
            Main.stage.setMinHeight(620);

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/ibntaymiyya/gym/Images/gym-image.png")));
            Main.stage.getIcons().add(icon);

            Main.stage.setScene(scene);
            Main.stage.show();

            Main.currentStage = Main.stage;
        } else {
            System.out.println("Error: Scene could not be loaded.");
        }
    }



    /*     Change Scene     */
    public void changeScene(String fxml) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent pane = loader.load();
            if (currentStage !=null && currentStage.getScene() != null){
                currentStage.getScene().setRoot(pane);
                currentStage.show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void clearLoginData(){
        SaveFileManager.setText("currentuser","");
        SaveFileManager.setText("currentuserpassword","");
        for (int id = 0 ; id < 5 ; id++){
            SaveFileManager.setText("perm"+(id+1),"");
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}