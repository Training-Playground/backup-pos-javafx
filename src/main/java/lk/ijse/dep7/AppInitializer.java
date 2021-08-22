package lk.ijse.dep7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep7.dbutils.SingleConnectionDataSource;

import java.io.IOException;
import java.sql.SQLException;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/main-form.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Java FX Backup POS");
        primaryStage.centerOnScreen();

        SingleConnectionDataSource.init("jdbc:mysql://localhost:3306/dep7_backup_pos","root","Hamza@mysql56597");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!SingleConnectionDataSource.getInstance().getConnection().isClosed()){
                    SingleConnectionDataSource.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        primaryStage.show();
    }
}
