package com.example.polynomgui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class PolynomialController {
    public Spinner<Double> coefficient0Spinner;
    public Spinner<Double> coefficient1Spinner;
    public Spinner<Double> coefficient2Spinner;
    public Spinner<Double> coefficient3Spinner;
    public Spinner<Double> coefficient4Spinner;
    public Label functionAsStringLabel;
    public Label symmetryLabel;
    public Label functionTypeLabel;
    public Label yInterceptLabel;
    public Label zeroPointsLabel;
    public Canvas polynomialCanvas;
    public Slider xScaleSlider;
    public Slider yScaleSlider;
    private Polynomial polynomial;
    private GraphicsContext graphicsContext;
    private double yScale = 20;
    private double xScale = 20;


    @FXML
    public void initialize(){
        xScaleSlider.setMax(200);
        xScaleSlider.setMin(1);
        xScaleSlider.setValue(xScale);
        xScaleSlider.setBlockIncrement(20);
        xScaleSlider.setMajorTickUnit(20);
        yScaleSlider.setMax(200);
        yScaleSlider.setMin(1);
        yScaleSlider.setValue(yScale);
        yScaleSlider.setBlockIncrement(20);
        yScaleSlider.setMajorTickUnit(20);

        coefficient0Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.1));
        coefficient1Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.1));
        coefficient2Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.1));
        coefficient3Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.1));
        coefficient4Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.1));
        this.graphicsContext = polynomialCanvas.getGraphicsContext2D();
        drawCoordinateGrid();
    }

    @FXML
    protected void onSubmitButtonClicked(){
        displayPolynomial();
    }

    @FXML
    public void onXSliderMoved() {
        clearCanvas();
        xScale = xScaleSlider.getValue();
        drawCoordinateGrid();
        displayPolynomial();
    }

    @FXML
    public void onYSliderMoved(){
        clearCanvas();
        yScale = yScaleSlider.getValue();
        drawCoordinateGrid();
        displayPolynomial();
        System.out.println(yScale);

    }

    @FXML
    public void onEnterPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            displayPolynomial();
        }
    }

    private void displayPolynomial() {
        polynomial = new Polynomial(getCoefficients());
        setAttributeDisplay();
        clearCanvas();
        drawPolynomialToCanvas(polynomial);
    }

    private void setAttributeDisplay() {
        functionAsStringLabel.setText(polynomial.toString());
        functionTypeLabel.setText(getPolynomialTypeString());
        yInterceptLabel.setText(Double.toString(polynomial.calculateValue(0.0)));
        zeroPointsLabel.setText(Arrays.toString(polynomial.findZeroPoints()));
        symmetryLabel.setText(getSymmetryString());
    }

    private String getSymmetryString(){
        final String symmetry;
        if (polynomial.isAxisSymmetrical()){
            symmetry = "Achsensymetrisch";
        } else if (polynomial.isPointSymmetrical()) {
            symmetry = "Punktsymetirsch";
        }
        else symmetry = "Keine";
        return symmetry;
    }

    private String getPolynomialTypeString(){
        return switch (polynomial.getDegree()) {
            case 0 -> "Konstant";
            case 1 -> "Linear";
            case 2 -> "Quadtradisch";
            case 3 -> "Kubisch";
            case 4 -> "Quartisch";
            default -> throw new IllegalStateException("Unexpected value: " + polynomial.getDegree());
        };
    }

    private void drawPolynomialToCanvas(Polynomial polynomialToDraw){
        drawCoordinateGrid();
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(1);
        double lastX = (-polynomialCanvas.getWidth()/xScale ) /2;
        double lastY = polynomialToDraw.calculateValue(lastX);
        double drawingPrecision = 0.5;
        for (double x = (-polynomialCanvas.getWidth()/2) / xScale; x <= (polynomialCanvas.getWidth()/2) / xScale; x += drawingPrecision){
            double y = polynomialToDraw.calculateValue(x);
            graphicsContext.strokeLine(adaptXCoordinate(lastX), adaptYCoordinate(lastY), adaptXCoordinate(x), adaptYCoordinate(y));
            lastX = x;
            lastY = y;
        }
    }

    private double adaptXCoordinate(double mathematicalXCoordinate){
        return mathematicalXCoordinate * xScale + polynomialCanvas.getWidth()/2;
    }

    private double adaptYCoordinate(double mathematicalYCoordinate){
        return -mathematicalYCoordinate * yScale + polynomialCanvas.getHeight()/2;
    };

    private void drawCoordinateGrid() {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);

        double canvasWidth = polynomialCanvas.getWidth();
        double canvasHeight = polynomialCanvas.getHeight();
        graphicsContext.strokeLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
        graphicsContext.strokeLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);

        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.setLineWidth(0.2);

        double xSquares = canvasWidth / xScale;
        double ySquares = canvasHeight / yScale;
        System.out.println("Squares:" + xSquares);
        double xCoord, yCoord;
        double xLineSpacing = canvasWidth/xSquares;
        double yLineSpacing = canvasHeight/ySquares;

        for (xCoord = canvasWidth / 2; xCoord >= 0; xCoord -= xLineSpacing) {
            graphicsContext.strokeLine(xCoord, 0, xCoord, canvasHeight);
        }
        for (xCoord = canvasWidth / 2 ; xCoord <= canvasWidth; xCoord += xLineSpacing) {
            graphicsContext.strokeLine(xCoord, 0, xCoord, canvasHeight);
        }
        for (yCoord = canvasHeight / 2; yCoord >= 0; yCoord -= yLineSpacing) {
            graphicsContext.strokeLine(0, yCoord, canvasWidth, yCoord);
        }
        for (yCoord = canvasHeight / 2 ; yCoord <= canvasHeight; yCoord += yLineSpacing) {
            graphicsContext.strokeLine(0, yCoord, canvasWidth, yCoord);
        }
        drawNumbers();
    }

    private void drawNumbers() {

    }

    private double[] getCoefficients(){
        final double[] coefficients = new double[5];
        coefficients[0] = coefficient0Spinner.getValue();
        coefficients[1] = coefficient1Spinner.getValue();
        coefficients[2] = coefficient2Spinner.getValue();
        coefficients[3] = coefficient3Spinner.getValue();
        coefficients[4] = coefficient4Spinner.getValue();
        return coefficients;
    }

    private void clearCanvas(){
        graphicsContext.clearRect(0,0, polynomialCanvas.getWidth(), polynomialCanvas.getHeight());
    }
}