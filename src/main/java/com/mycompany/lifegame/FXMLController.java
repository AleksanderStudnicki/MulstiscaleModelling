package com.mycompany.lifegame;

import com.mycompany.lifegame.CellularAutomataRules.HexagonalRandom;
import com.mycompany.lifegame.LifeStructure.Glider;
import com.mycompany.lifegame.LifeStructure.Oscilator2;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class FXMLController implements Initializable {

    public LifeGame lifeGame;
    public CellularAutomata2D cellularAutomata;
    GraphicsContext gc, gcCA;
    @FXML
    TextField timeIntervalField;
    @FXML
    TextField cellSizeField;
    @FXML
    TextField columnsField;
    @FXML
    TextField linesField;
    @FXML
    Canvas canvasCA;
    @FXML
    TextField blueField;
    @FXML
    TextField greenField;
    @FXML
    TextField redField;
    @FXML
    Label cAInfo;
    @FXML
    CheckBox isPeriodicalBox;
    @FXML
    CheckBox stopAfterAllCellsAreGrains;
    @FXML
    TextField grainsField;
    @FXML
    TextField radiusField;
    @FXML
    AnchorPane anchorStat;
    @FXML
    private Canvas canvas;
    private int cellSize = 4;
    EventHandler<MouseEvent> canvasCAOnMouseClicked = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent mouseEvent) {
            int x = (int) (mouseEvent.getX() / cellSize);
            int y = (int) (mouseEvent.getY() / cellSize);
            int[] id = new int[3];
            id[0] = Integer.parseInt(blueField.getText());
            id[1] = Integer.parseInt(greenField.getText());
            id[2] = Integer.parseInt(redField.getText());
            cellularAutomata.setCellAlive(x, y, id);
            showCellularAutomata2D(cellularAutomata);
        }
    };
    private AtomicBoolean isStop = new AtomicBoolean(false);
    private AtomicBoolean isStopCellularAutomata = new AtomicBoolean(false);
    private int timeInterval = 100;
    private String insertChoice = "cell";
    EventHandler<MouseEvent> canvasOnMouseClicked = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent mouseEvent) {
            int x = (int) (mouseEvent.getX() / cellSize);
            int y = (int) (mouseEvent.getY() / cellSize);
            switch (insertChoice) {
                case "cell":
                    lifeGame.setCellAlive(x, y);
                    break;
                case "oscilator2":
                    lifeGame.insertLifeStructure(x, y, new Oscilator2());
                    break;
                case "glider":
                    lifeGame.insertLifeStructure(x, y, new Glider());
                    break;
            }
            showLifeGameArea(lifeGame);
        }
    };

    public void paintLifeStructure(int x, int y) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public void paintCellularAutomata(int x, int y, int[] rgb) {
        Color color = Color.rgb(rgb[0], rgb[1], rgb[2]);
        gcCA.setFill(color);
        gcCA.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public void showLifeGameArea(LifeGame lifeGame) {
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, 1000, 1000);
        for (int i = 0; i < lifeGame.lifeArea.length; i++) {
            for (int j = 0; j < lifeGame.lifeArea[i].length; j++) {
                if (lifeGame.lifeArea[i][j].get()) {
                    paintLifeStructure(i, j);
                }
            }
        }
    }

    public void showCellularAutomata2D(CellularAutomata2D cellularAutomata) {
        gcCA.setFill(Color.WHITE);
        gcCA.clearRect(0, 0, 1000, 1000);
        for (int i = 0; i < cellularAutomata.cellArea.length; i++) {
            for (int j = 0; j < cellularAutomata.cellArea[i].length; j++) {
                if (cellularAutomata.cellArea[i][j].getIsGrain()) {
                    paintCellularAutomata(i, j, cellularAutomata.cellArea[i][j].getGrainId());
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        new HexagonalRandom().getCoordinatesArray();

        gc = canvas.getGraphicsContext2D();
        gcCA = canvasCA.getGraphicsContext2D();
        canvas.setOnMouseClicked(canvasOnMouseClicked);
        canvasCA.setOnMouseClicked(canvasCAOnMouseClicked);
        lifeGame = new LifeGame(100, 100);
        cellularAutomata = new CellularAutomata2D(100, 100);
        showCellularAutomata2D(cellularAutomata);
        //       showLifeGameArea(lifeGame);
    }

    public void setNewDimensions() {
        int columns = Integer.parseInt(columnsField.getText());
        int lines = Integer.parseInt(linesField.getText());
        lifeGame = new LifeGame(columns, lines);
        cellularAutomata = new CellularAutomata2D(columns, lines);
        showLifeGameArea(lifeGame);
    }

    public void test() {
        isStop.set(false);
        Thread thread = new Thread(() -> {
            while (!isStop.get()) {
                lifeGame.nextStep();
                Platform.runLater(() -> {
                    showLifeGameArea(lifeGame);
                });
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    public void startCellularAutomata() {
        isStopCellularAutomata.set(false);
        cAInfo.setText("");
        Thread thread = new Thread(() -> {
            while (!isStopCellularAutomata.get()) {
                cellularAutomata.nextStep();
                if (stopAfterAllCellsAreGrains.isSelected()) {
                    if (cellularAutomata.isAllCellsGrains()) {
                        isStopCellularAutomata.set(true);
                        Platform.runLater(() -> {
                            cAInfo.setText("ALL CELLS ARE GRAINS");
                            showStatistics();
                        });
                    }
                }
                Platform.runLater(() -> {
                    showCellularAutomata2D(cellularAutomata);
                });
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    public void nextStep() {
        lifeGame.nextStep();
        showLifeGameArea(lifeGame);
    }

    public void setChaos() {
        lifeGame.setChaos();
        showLifeGameArea(lifeGame);

    }

    public void generateCellularAutomata() {
        int grains = Integer.parseInt(grainsField.getText());
        int radius = Integer.parseInt(radiusField.getText());
        cellularAutomata.generateCellArea(grains, radius);
        showCellularAutomata2D(cellularAutomata);
    }

    public void reset() {
        lifeGame.reset();
        showLifeGameArea(lifeGame);
    }

    public void resetCA() {
        cellularAutomata.reset();
        showCellularAutomata2D(cellularAutomata);
    }

    public void stop() {
        isStop.set(true);
    }

    public void stopCellularAutomata() {
        isStopCellularAutomata.set(true);
    }

    public void set() {
        cellSize = Integer.parseInt(cellSizeField.getText());
        timeInterval = Integer.parseInt(timeIntervalField.getText());
    }

    public void showCellularAutomataCanvas() {
        canvas.setVisible(false);
        canvasCA.setVisible(true);
    }

    public void showLifeGameCanvas() {
        canvasCA.setVisible(false);
        canvas.setVisible(true);
    }

    public void setCellState() {
        insertChoice = "cell";
    }

    public void setOscilator2State() {
        insertChoice = "oscilator2";
    }

    public void setGliderState() {
        insertChoice = "glider";
    }

    public void setCellularChaos() {
        cellularAutomata.setChaos();
        showCellularAutomata2D(cellularAutomata);
    }

    public void nextStepInCellularAutomata2D() {
        cellularAutomata.nextStep();
        showCellularAutomata2D(cellularAutomata);
    }

    public void changeCellularAutomataPeriodical() {
        cellularAutomata.isPeriodical = isPeriodicalBox.isSelected();
    }

    public void changeNeighberhoodRuleToVonNeumann() {
        cellularAutomata.neighberhoodRule = "vonNeumann";
    }

    public void changeNeighberhoodRuleToMoore() {
        cellularAutomata.neighberhoodRule = "Moore";
    }

    public void changeNeighberhoodRuleToHexLeft() {
        cellularAutomata.neighberhoodRule = "HexLeft";
    }

    public void changeNeighberhoodRuleToHexRight() {
        cellularAutomata.neighberhoodRule = "HexRight";
    }

    public void changeNeighberhoodRuleToPentaLeft() {
        cellularAutomata.neighberhoodRule = "PentaLeft";
    }

    public void changeNeighberhoodRuleToPentaRight() {
        cellularAutomata.neighberhoodRule = "PentaRight";
    }

    public void changeNeighberhoodRuleToPentaRand() {
        cellularAutomata.newPentRand();
        cellularAutomata.neighberhoodRule = "PentRand";
    }

    public void changeNeighberhoodRuleToHexRand() {
        cellularAutomata.newHexRand();
        cellularAutomata.neighberhoodRule = "HexRand";
    }

    public void showStatistics() {
        anchorStat.getChildren().clear();
        List<Occurance> groups = cellularAutomata.calculateField();

        for (int i = 0; i < groups.size(); i++) {
            addFieldToScrollPane(groups.get(i), i);
        }
    }

    private void addFieldToScrollPane(Occurance occurance, int y) {
        Canvas canvas = new Canvas();
        canvas.setWidth(10.0);
        canvas.setHeight(10.0);
        canvas.setLayoutX(10);
        canvas.setLayoutY((20 * y) + 10);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(occurance.getId()[0], occurance.getId()[1], occurance.getId()[2]));
        gc.fillRect(0, 0, 10.0, 10.0);
        Label label = new Label();
        label.setText("Pole: " + occurance.getCountOfTotalOccurances());
        label.setLayoutX(30);
        label.setLayoutY((20 * y) + 7);
        Label label2 = new Label();
        label2.setText("Granica: " + occurance.getBorderLength());
        label2.setLayoutX(110);
        label2.setLayoutY((20 * y) + 7);
        anchorStat.getChildren().add(label);
        anchorStat.getChildren().add(label2);
        anchorStat.getChildren().add(canvas);
    }

    public void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CEL file", "*.cel"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            writeToFile(file);
        }
    }

    private void writeToFile(File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(cellularAutomata.cellArea.length + ";" + cellularAutomata.cellArea[0].length + "\r\n");

            IntStream.range(0, cellularAutomata.cellArea.length)
                    .forEach(i ->
                            IntStream.range(0, cellularAutomata.cellArea[i].length)
                                    .forEach(j -> {
                                        String output = i + ";" + j + ";" + cellularAutomata.cellArea[i][j].toString() + "\r\n";
                                        try {
                                            bufferedWriter.write(output);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }));

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToBMP() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG file", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            WritableImage writableImage = new WritableImage(cellularAutomata.cellArea[0].length * cellSize, cellularAutomata.cellArea.length * cellSize);
            canvasCA.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CEL file", "*.cel"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                try {
                    String line = bufferedReader.readLine();

                    String[] temp = line.split(";");
                    int height = Integer.parseInt(temp[0]);
                    int width = Integer.parseInt(temp[1]);
                    cellularAutomata.cellArea = new AutomataCell[height][width];
                    cellularAutomata.initializeAutomataCellArray(cellularAutomata.cellArea);

                    line = bufferedReader.readLine();
                    System.out.println(line);


                    while (line != null) {
                        String[] values = line.split(";");

                        if(Boolean.parseBoolean(values[2])){


                            int x = Integer.parseInt(values[0]);
                            int y = Integer.parseInt(values[1]);


                            cellularAutomata.cellArea[x][y].setIsGrain(true);

                            int r = Integer.parseInt(values[3]);
                            int g = Integer.parseInt(values[4]);
                            int b = Integer.parseInt(values[5]);

                            cellularAutomata.cellArea[x][y].setGrainId(new int[]{r, g, b});
                        }


                        line = bufferedReader.readLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            showCellularAutomata2D(cellularAutomata);
        }
    }

}
