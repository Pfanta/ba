package org.combinators.cls.scheduling.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		AnchorPane pane = loader.load();
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		MainWindowViewController controller = loader.getController();
		controller.setup(primaryStage);
	}
}
