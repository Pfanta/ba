package org.combinators.cls.scheduling.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.combinators.cls.scheduling.control.GenerationRunner;
import org.combinators.cls.scheduling.model.*;
import org.combinators.cls.scheduling.utils.*;
import org.combinators.cls.scheduling.view.customDialogs.ProgressDialog;
import org.combinators.cls.scheduling.view.customDialogs.ResultDialog;
import org.combinators.cls.scheduling.view.customcontrol.CustomJFXPlusButton;
import org.combinators.cls.scheduling.view.customcontrol.CustomJFXTextField;
import org.combinators.cls.scheduling.view.customcontrol.CustomLabel;

import java.io.File;
import java.io.IOException;

/**
 * ViewController for MainWindow GUI
 */
public class MainWindowViewController implements MainWindowAUI {
    /**
     * Thresholds for Visualizing Tasks in GUI, too large values make long scrollbars and affect performance
     */
    private static final int MAX_JOBS_SHOWN = 20;
    private static final int MAX_MACHINES_SHOWN = 15;

    /**
     * Attributes for accessing view components declared in .fxml file
     */
    @FXML
    private Pane jobsPane;
    @FXML
    private JFXComboBox<String> comboBox;
    @FXML
    private JFXButton runBtn;
    @FXML
    private JFXButton benchmarkBtn;
    @FXML
    private JFXButton taillardBenchmarkBtn;
    @FXML
    private JFXProgressBar benchmarkProgressBar;
    @FXML
    private JFXProgressBar taillardenchmarkProgressBar;
    
    /**
     Windows man stage
     */
    private javafx.stage.Stage stage;
    
    /**
     * ProgressDialog that is shown while running task
     *
     * @see org.combinators.cls.scheduling.view.customDialogs.ProgressDialog
     */
    private ProgressDialog progressDialog;

    /**
     * Currently loaded or generated Task to be scheduled upon run
     *
     * @see org.combinators.cls.scheduling.model.Task
     */
    private Task currentTask;

    /**
     * Instance of the currently used generationRunner to execute scheduling tasks
     *
     * @see org.combinators.cls.scheduling.control.GenerationRunner
     */
    private GenerationRunner generationRunner;

    /**
     * Instantiates currentTask and generationRunner
     *
     * @param stage javaFX's main stage
     */
    void setup(javafx.stage.Stage stage) {
        this.stage = stage;
        this.currentTask = new Task();
        this.generationRunner = new GenerationRunner(this);
        comboBox.getSelectionModel().select(0);

        refreshJobsPane();
    }

    /**
     * Refreshes jobs pane.
     * If current job's dimensions are less than threshold, the job gets visualized
     */
    private void refreshJobsPane() {
        ObservableList<Node> nodes = jobsPane.getChildren();
        nodes.clear();

        //Catch too large tasks
        JobMachineTuple tuple = ClassificationUtils.getTaskDimensions(currentTask);
        if (tuple.getJobCount() > MAX_JOBS_SHOWN || tuple.getMachineCount() > MAX_MACHINES_SHOWN) {
            nodes.add(new CustomLabel("Dimensions of Task too large to visualize.", 50, 50));
            return;
        }

        //Show all Jobs in own row
        for (int i = 0; i < currentTask.getJobs().size(); i++) {
            nodes.add(new CustomLabel(currentTask.getJobs().get(i).getName(), 10, 10 + i * 40, 50, 30));

            int y = 0;
            final Job currentJob = currentTask.getJobs().get(i);
            for (Stage stage : currentJob.getRoutes().get(0).getStages()) {
//				TextField textFieldMachineCount = new CustomJFXTextField(stage.getMachineCount(), 60 + y * 130, 10 + i * 40, 20, 30);
//				textFieldMachineCount.textProperty().addListener((observable, oldValue, newValue) -> {
//					if(!newValue.matches("\\d+") || Integer.parseInt(newValue) < 0)
//						textFieldMachineCount.setText(oldValue);
//					else
//						stage.setMachineCount(Integer.parseInt(newValue));
//				});

                TextField textFieldMachine = new CustomJFXTextField(stage.getMachines().get(0).toString(), 60 + y * 130 + 30, 10 + i * 40, 50, 30);
                textFieldMachine.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue.isEmpty())
                        textFieldMachine.setText(oldValue);
                    else
                        stage.getMachines().get(0).setName(newValue);
                });

                TextField textFieldMachineTime = new CustomJFXTextField(stage.getScheduledMachine().getDuration(), 60 + y * 130 + 85, 10 + i * 40, 20, 30);
                textFieldMachine.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!newValue.matches("\\d+") || Integer.parseInt(newValue) < 0)
                        textFieldMachineTime.setText(oldValue);
                    else
                        stage.getScheduledMachine().setDuration(Integer.parseInt(newValue));
                });

                nodes.addAll(//textFieldMachineCount,
                        new CustomLabel("x", 60 + y * 130 + 20, 10 + i * 40, 10, 30),
                        textFieldMachine,
                        new CustomLabel(":", 60 + y * 130 + 80, 10 + i * 40, 5, 30),
                        textFieldMachineTime,
                        new CustomLabel("\u2192", 60 + y * 130 + 105, 10 + i * 40, 25, 30, 18));
                y++;
            }
            nodes.remove(nodes.size() - 1);
            nodes.add(new CustomJFXPlusButton(70 + y * 130, 10 + i * 40, 30, 30, event -> {
                currentJob.getRoutes().get(0).addStage(new Stage(new Machine("M0")));
                refreshJobsPane();
            }));
        }
        nodes.add(new CustomJFXPlusButton(10, 10 + currentTask.getJobs().size() * 40, 30, 30, event -> {
            currentTask.add(new Job("J" + currentTask.getJobs().size(), -1, new Route(new Stage(new Machine("M0")))));
            refreshJobsPane();
        }));
    }

    //region action Handler

    /**
     * Event handler for drag&drop .task files into the program
     *
     * @param event dragEvent
     */
    public void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    }

    /**
     * Event handler for drag&drop .task files into the program
     *
     * @param event dragEvent
     */
    public void onFileDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles() && dragboard.getFiles().size() == 1 && dragboard.getFiles().get(0).getName().endsWith(".task"))
            tryLoadFile(dragboard.getFiles().get(0));

        event.setDropCompleted(true);
        event.consume();
    }

    /**
     * Event handler for drag&drop .task files into the program
     * called if the file is dropped directly over the run button.
     * Loads the file and immediately starts execution
     *
     * @param event dragEvent
     */
    public void onFileDroppedOnButton(DragEvent event) {
        onFileDropped(event);
        onRunButtonClicked(null);
    }

    /**
     * Called when the user clicked "Generate".
     * Opens the generation dialog.
     *
     * @param event clickEvent
     * @see org.combinators.cls.scheduling.utils.GenerationUtils
     */
    public void onGenerateButtonClicked(ActionEvent event) {
        GenerationUtils.showGenerateDialog().ifPresent(result -> {
            this.currentTask = GenerationUtils.generateRandomTask(result);
            refreshJobsPane();
        });
    }

    /**
     * Called when the user clicked "Load".
     * Opens a fileChooser to select .task file.
     *
     * @param event clickEvent
     */
    public void onLoadButtonClicked(ActionEvent event) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("Open Task File");
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Task Files", "*.task"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileDialog.showOpenDialog(stage);
        if (file != null)
            tryLoadFile(file);
    }

    /**
     * Called when the user clicked "Save".
     * Opens a fileChooser to select destination folder and saves current task.
     *
     * @param event clickEvent
     */
    public void onSaveButtonClicked(ActionEvent event) {
        if (!ClassificationUtils.validate(currentTask)) {
            ApplicationUtils.showWarning("Invalid Task", "Current Task is not valid.");
            return;
        }

        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("Save Task File");
        File file = fileDialog.showSaveDialog(stage);
        if (file != null) {
            try {
                IOUtils.saveTask(file, currentTask);
            } catch (IOException ex) {
                ApplicationUtils.showException("Error", "Error occurred while saving", ex);
            }
        }
    }

    /**
     * Called when the user clicked "Run".
     * Starts the execution of current task.
     * Opens the progress dialog.
     *
     * @param event clickEvent
     * @see org.combinators.cls.scheduling.view.customDialogs.ProgressDialog
     */
    public void onRunButtonClicked(ActionEvent event) {
        runBtn.setOpacity(.5);
        generationRunner.run(currentTask);

        progressDialog = new ProgressDialog(comboBox.getSelectionModel().getSelectedItem()); // TODO: Use value for generation
        progressDialog.showAndWait().ifPresent(b -> {
            if (b == ButtonType.CANCEL)
                generationRunner.cancel();

            else if (b == ButtonType.NO)
                new ResultDialog(generationRunner.getResult()).show();

            else if (b == ButtonType.YES)
                for (Tuple<String, Task> result : generationRunner.getResults()) {
                    new ResultDialog(result).show();
                }
        });
    }

    /**
     * Called when the user clicked "Benchmark".
     * Opens the benchmark dialog and starts the execution of a benchmark.
     *
     * @param event clickEvent
     * @see org.combinators.cls.scheduling.view.customDialogs.ProgressDialog
     */
    public void onBenchmarkButtonClicked(ActionEvent event) {
        BenchmarkUtils.showBenchmarkDialog().ifPresent(result -> {
            benchmarkBtn.setOpacity(.5);
            generationRunner.runBenchmark(result.getNumJobs(), result.getNumMachines(), result.getNumInstances());
        });
    }
    
    public void onTaillardBenchmarkButtonClicked(ActionEvent event) {
        taillardBenchmarkBtn.setOpacity(.5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    generationRunner.runTaillardBenchmark(IOUtils.loadTaillard(new File(getClass().getResource("/tasks/20x5.taillard").getFile())));
                    generationRunner.runTaillardBenchmark(IOUtils.loadTaillard(new File(getClass().getResource("/tasks/20x10.taillard").getFile())));
                    generationRunner.runTaillardBenchmark(IOUtils.loadTaillard(new File(getClass().getResource("/tasks/20x20.taillard").getFile())));
                } catch(IOException e) {
                    ApplicationUtils.showException("Error", "Error occurred while loading", e);
                } catch(InterruptedException e) {
                    ApplicationUtils.showException("Error", "Error occurred while running benchmark", e);
                }
            }
        }).start();
    }
    
    private void tryLoadFile(File file) {
        try {
            currentTask = IOUtils.loadTask(file);
            refreshJobsPane();
        } catch(IOException ex) {
            ApplicationUtils.showException("Error", "Error occurred while loading", ex);
        } catch(IllegalArgumentException ex) {
            ApplicationUtils.showException("Error", "Error while parsing File", ex);
        }
    }
    //endregion

    //region AUI refreshes
    @Override
    public void onClassificationFinished(ClassificationUtils.Classification classification) {
        Platform.runLater(() -> progressDialog.setClassificationResult(classification));
    }

    @Override
    public void onGenerationFinished(int result) {
        Platform.runLater(() -> progressDialog.setGenerationResult(result));
    }

    @Override
    public void onBenchmarkProgress(float progress) {
        Platform.runLater(() -> {
            benchmarkProgressBar.setProgress(progress);
            taillardenchmarkProgressBar.setProgress(progress);
        });
    }

    @Override
    public void onRunnerProgress(float progress) {
        if (progressDialog != null)
            Platform.runLater(() -> progressDialog.setRunProgress(progress));
    }

    @Override
    public void onRunnerFinished() {
        Platform.runLater(() -> progressDialog.setRunFinished());
    }

    @Override
    public void onEvaluationResult(int result) {
        Platform.runLater(() -> progressDialog.setEvaluationResult(result));
    }

    @Override
    public void onFinishedOrCanceled() {
        Platform.runLater(() -> {
            runBtn.setOpacity(1);
            benchmarkBtn.setOpacity(1);
            taillardBenchmarkBtn.setOpacity(1);
            benchmarkProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            taillardenchmarkProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        });
    }
    //endregion
}
