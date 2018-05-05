/*Mohammad Uppal & Mohamed Seliman*/

package application;
	
import controller.ListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class SongLib extends Application {
	
	public void start(Stage primaryStage)  throws Exception {                 
		FXMLLoader loader = new FXMLLoader();    
		loader.setLocation(getClass().getResource("/view/List.fxml")); 
	    AnchorPane root = (AnchorPane)loader.load(); 
	    ListController listController =  loader.getController();
	    listController.start();
	    Scene scene = new Scene(root, 630, 451); 
	    primaryStage.setScene(scene);
	    listController.start(primaryStage);
	    primaryStage.show();
	    
	} 
	
	public static void main(String[] args) {
		launch(args);
	}
}
