package org.combinators.cls.scheduling.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class to start the application
 */
public class Main extends Application {
	
	/**
	 * Static void main
	 * Launches the application
	 *
	 * @param args Runtime args
	 */
	public static void main(String[] args) {
		launch();
	}
	
	/**
	 * Called upon application launch
	 *
	 * @param primaryStage Primary stage of the application
	 * @throws IOException IOException if .fxml files do not exist
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		Pane pane = loader.load();
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		MainWindowViewController controller = loader.getController();
		controller.setup(primaryStage);
	}
}
