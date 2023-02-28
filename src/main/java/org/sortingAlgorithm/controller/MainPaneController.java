package org.sortingAlgorithm.controller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class MainPaneController  {
     Random random = new Random();
    @FXML
    private VBox box;

    @FXML
    private ChoiceBox<String> algorithmChoiceBox;
    private final String[] Algorithms = {"BubbleSort"};
    GraphicsContext graphicsContext2D;

    @FXML
    private Slider barSlider;

    @FXML
    private HBox menuPanel;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasContainer;

    @FXML
    private Button resetButton;

    @FXML
    private Button startButton;

    @FXML
    private Label titleLabel;
    private int[] numbers;
    private double barWith;
    private double height;


    public void initialize() {

        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        int numberOfBars =  (int)barSlider.getValue();
        this.numbers = new int[numberOfBars];
        this.barWith = canvas.getWidth()/numberOfBars;
        this.height = canvas.getHeight();
        this.graphicsContext2D = canvas.getGraphicsContext2D();
        algorithmChoiceBox.getItems().addAll(Algorithms);
        algorithmChoiceBox.setOnAction(this::getTitle);
        barSlider.valueProperty().addListener((observableValue, oldV, newV) -> {
            int roundedValue = roundTheSliderValue(newV);
            this.barWith = canvas.getWidth()/(double)roundedValue;
            graphicsContext2D.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
            this.numbers = new int[newV.intValue()];
            generateRandomNumbers(this.numbers);
            printArray(this.numbers, graphicsContext2D ,this.barWith, height);}
        );
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.barWith = newVal.doubleValue()/this.numbers.length;
            this.graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            printArray(this.numbers, graphicsContext2D, this.barWith, this.height);
        });

        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            this.height = newVal.doubleValue();
            this.graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            printArray(this.numbers, graphicsContext2D, this.barWith, this.height);
        });

        startButton.setOnAction(event -> sortArray(algorithmChoiceBox.getValue()));
        resetButton.setOnAction(event -> {

            graphicsContext2D.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
            generateRandomNumbers(numbers);
            printArray(numbers, graphicsContext2D ,barWith, height);

        });
        
        


    }

    private static int roundTheSliderValue(Number newV) {
        if (newV.doubleValue() == (double) newV.intValue()){
            return newV.intValue();
        }
        else if (newV.doubleValue() > ((double) newV.intValue())+0.5){
            return newV.intValue()+1;
        }
        else if (newV.doubleValue() < ((double) newV.intValue())+0.5) {
            return newV.intValue();
        }
        return 0;
    }

    private void sortArray( String value) {
        if (value.equals("BubbleSort")) {
            bubbleSort();
        }
    }

    public void bubbleSort() {
        int length = numbers.length;
        AnimationTimer timer = new AnimationTimer() {
            int i = 0;
            int j = 0;
            @Override
            public void handle(long now) {
                if(length != numbers.length || resetButton.isPressed()){
                    stop();
                }
                if (i < length - 1) {
                    if (j < length - i - 1) {
                        if (numbers[j] > numbers[j+1]) {
                            swapper(j);
                        }
                        j++;
                    } else {
                        i++;
                        j = 0;
                    }
                } else {
                    stop();
                    paintArray(numbers, graphicsContext2D, barWith, canvas.getHeight());
                }
            }

        };
        timer.start();

    }

    private void swapper(int j) {
        int temp = numbers[j+1];
        numbers[j + 1] = numbers[j];
        numbers[j] = temp;
        graphicsContext2D.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        paintArray(numbers, graphicsContext2D, barWith, canvas.getHeight());
        graphicsContext2D.setFill(Color.RED);
        int barHeight1 = numbers[j+1] * 2;
        double x1 = j+1 + (barWith - 1) * (j+1);
        double y1 = canvas.getHeight() - barHeight1;
        graphicsContext2D.fillRect(x1, y1, barWith, barHeight1);
        int barHeight2 = numbers[j] * 2;
        double x2 = j + (barWith - 1) * j;
        double y2 = canvas.getHeight() - barHeight2;
        graphicsContext2D.fillRect(x2, y2, barWith, barHeight2);
        sleepFor(20000000);
    }

    private void sleepFor(long nanoseconds) {
        long timeElapsed;
        final long startTime = System.nanoTime();
        do {
            timeElapsed = System.nanoTime() - startTime;
        }while (timeElapsed < nanoseconds);
    }
    public void paintArray(int[] numbers, GraphicsContext graphicsContext2D, double barWith, double height) {
        graphicsContext2D.setFill(Color.BLUE);
        for (int i = 0; i < numbers.length; i++) {
            int barHeight = numbers[i] *2;
            double x = i + (barWith -1) *i;
            double y = height - barHeight;
            graphicsContext2D.fillRect(x,y, barWith,barHeight);
        }
    }

    private void printArray(int[] numbers, GraphicsContext graphicsContext2D, double barWith, double height) {
        int number = random.nextInt((int) canvas.getHeight()/2);
        paintArray(numbers, graphicsContext2D, barWith, height);
        if (numbers.length * barWith < canvas.getWidth()){
            addBar(numbers, graphicsContext2D, barWith, height, number);
        }
        else if (numbers.length * barWith > canvas.getWidth()){
            takeBar(numbers, graphicsContext2D, barWith);


        }
    }



    private void takeBar(int[] numbers, GraphicsContext graphicsContext2D, double barWith) {
        graphicsContext2D.clearRect(canvas.getWidth()+ barWith,0, barWith,canvas.getHeight());
        int[] newInts = new int[numbers.length -1];
        if (numbers.length - 2 >= 0) System.arraycopy(numbers, 0, newInts, 0, numbers.length - 2);
        this.numbers = newInts;
    }

    private void addBar
            (int[] numbers, GraphicsContext graphicsContext2D, double barWith, double height, int number) {
        graphicsContext2D.setFill(Color.BLUE);
        graphicsContext2D.fillRect(canvas.getWidth()- barWith, height - number, barWith, number *2);
        int[] newInts = new int[numbers.length + 1];
        if (numbers.length - 1 >= 0) System.arraycopy(numbers, 0, newInts, 0, numbers.length - 1);
        newInts[newInts.length-1] = number;
        this.numbers = newInts;
    }

    private void generateRandomNumbers(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt ((int) canvas.getHeight()/2);

        }
        
        
    }


    public void getTitle(ActionEvent event){
        String myAlgorithm = algorithmChoiceBox.getValue();
        titleLabel.setText(myAlgorithm + " Algorithm");
    }

}
