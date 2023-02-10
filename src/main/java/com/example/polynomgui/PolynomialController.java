package com.example.polynomgui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
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
        drawSquares();
    }

    @FXML
    protected void onSubmitButtonClicked(){
        displayPolynomial();
    }

    private void displayPolynomial() {
        polynomial = new Polynomial(getCoefficients());
        setAttributeDisplay();
        drawPolynomialToCanvas(polynomial);
    }

    private void setAttributeDisplay() {
        functionAsStringLabel.setText(polynomial.toString());
        functionTypeLabel.setText(getPolynomialTypeString());
        yInterceptLabel.setText(Double.toString(polynomial.calculateValue(0.0)));
        zeroPointsLabel.setText(Arrays.toString(polynomial.findZeroPoints()));
        symmetryLabel.setText(getSymmetryString());

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
        clearCanvas();
        drawSquares();
        graphicsContext.setStroke(Color.GREEN);
        graphicsContext.setLineWidth(2);
        double lastX = (-polynomialCanvas.getWidth()/2)/ xScale;
        double lastY = polynomial.calculateValue(lastX);
        double drawingPrecision = 0.1;
        for (double x = (-polynomialCanvas.getWidth()/2)/ xScale; x <= (polynomialCanvas.getWidth()/2)/ xScale; x += drawingPrecision){
            double y = polynomial.calculateValue(x);
            graphicsContext.strokeLine(lastX* xScale + polynomialCanvas.getWidth()/2, -lastY * yScale + polynomialCanvas.getHeight()/2,
                                    x * xScale + polynomialCanvas.getWidth()/2, -y * yScale + polynomialCanvas.getHeight()/2);
            lastX = x;
            lastY = y;
        }
    }

    private void drawSquares() {
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
        double xCoord, yCoord;

        for (xCoord = canvasWidth / 2; xCoord >= 0; xCoord -= xSquares) {
            graphicsContext.strokeLine(xCoord, 0, xCoord, canvasHeight);
        }
        for (xCoord = canvasWidth / 2 + xSquares; xCoord <= canvasWidth; xCoord += xSquares) {
            graphicsContext.strokeLine(xCoord, 0, xCoord, canvasHeight);
        }

        for (yCoord = canvasHeight / 2; yCoord >= 0; yCoord -= ySquares) {
            graphicsContext.strokeLine(0, yCoord, canvasWidth, yCoord);
        }
        for (yCoord = canvasHeight / 2 + ySquares; yCoord <= canvasHeight; yCoord += ySquares) {
            graphicsContext.strokeLine(0, yCoord, canvasWidth, yCoord);
        }
    }

    public void onXSlider() {
        clearCanvas();
        xScale = xScaleSlider.getValue();
        drawSquares();
        displayPolynomial();
    }
    public void onYSlider(){
        clearCanvas();
        yScale = yScaleSlider.getValue();
        drawSquares();
        displayPolynomial();

    }

    private void clearCanvas(){
        graphicsContext.clearRect(0,0, polynomialCanvas.getWidth(), polynomialCanvas.getHeight());
    }
}