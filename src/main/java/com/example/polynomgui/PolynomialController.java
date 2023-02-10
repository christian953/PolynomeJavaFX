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
    public Slider scaleSlider;
    private Polynomial polynomial;
    private GraphicsContext graphicsContext;
    private double vScale = 20;
    private double hScale = 10;


    @FXML
    public void initialize(){
        scaleSlider.setMax(200);
        scaleSlider.setMin(1);
        scaleSlider.setValue(vScale);
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
        System.out.println(Arrays.toString(coefficients));
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
        double lastX = (-polynomialCanvas.getWidth()/2)/ hScale;
        double lastY = polynomial.calculateValue(lastX);
        for (double x = (-polynomialCanvas.getWidth()/2)/ hScale; x <= (polynomialCanvas.getWidth()/2)/ hScale; x += 0.1){
            double y = polynomial.calculateValue(x);
            graphicsContext.strokeLine(lastX* hScale + polynomialCanvas.getWidth()/2, -lastY * vScale + polynomialCanvas.getHeight()/2,
                                    x * hScale + polynomialCanvas.getWidth()/2, -y * vScale + polynomialCanvas.getHeight()/2);
            lastX = x;
            lastY = y;
        }
    }

    private void drawSquares(){
        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.setLineWidth(0.5);
        int hSquares = (int) (polynomialCanvas.getWidth() / hScale);
        int vSquares = (int) (polynomialCanvas.getHeight() / vScale);
        double xCoord = 0;
        for (int i = 0; i <= hSquares; i++){
            if(i == hSquares / 2){
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setLineWidth(1);
                graphicsContext.strokeLine(polynomialCanvas.getWidth()/2, 0, polynomialCanvas.getWidth()/2, polynomialCanvas.getHeight());
                graphicsContext.setStroke(Color.GRAY);
                graphicsContext.setLineWidth(0.5);
            }
            graphicsContext.strokeLine(xCoord, 0, xCoord, polynomialCanvas.getHeight());
            xCoord += hScale;
        }
        double yCoord = 0;
        for (int i = 0; i <= vSquares; i++){
            if(i == vSquares /2){
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setLineWidth(1);
                graphicsContext.strokeLine(0, yCoord, polynomialCanvas.getWidth(), yCoord);
                graphicsContext.setStroke(Color.GRAY);
                graphicsContext.setLineWidth(0.5);
            }
            graphicsContext.strokeLine(0, yCoord, polynomialCanvas.getWidth(), yCoord);
            yCoord += vScale;
        }
    }

    public void onSlider(MouseEvent mouseEvent) {
        clearCanvas();
        vScale = scaleSlider.getValue();
        drawSquares();
    }

    private void clearCanvas(){
        graphicsContext.clearRect(0,0, polynomialCanvas.getWidth(), polynomialCanvas.getHeight());
    }
}