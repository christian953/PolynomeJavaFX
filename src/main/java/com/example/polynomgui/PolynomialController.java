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
    public Slider hScaleSlider;
    public Slider vScaleSlider;
    private Polynomial polynomial;
    private GraphicsContext graphicsContext;
    private double vScale = 20;
    private double hScale = 20;


    @FXML
    public void initialize(){
        hScaleSlider.setMax(200);
        hScaleSlider.setMin(1);
        hScaleSlider.setValue(hScale);
        hScaleSlider.setBlockIncrement(5);
        vScaleSlider.setMax(200);
        vScaleSlider.setMin(1);
        vScaleSlider.setValue(vScale);
        vScaleSlider.setBlockIncrement(5);
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
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(polynomialCanvas.getWidth()/2, 0, polynomialCanvas.getWidth()/2, polynomialCanvas.getHeight());
        graphicsContext.strokeLine(0, polynomialCanvas.getHeight()/2, polynomialCanvas.getWidth(),polynomialCanvas.getHeight()/2);
        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.setLineWidth(0.2);
        for(double xCoordinate = (polynomialCanvas.getWidth()/vScale) % polynomialCanvas.getWidth(); xCoordinate <= polynomialCanvas.getWidth(); xCoordinate += polynomialCanvas.getWidth()/vScale){
            System.out.println(xCoordinate);
            graphicsContext.strokeLine(xCoordinate, 0, xCoordinate, polynomialCanvas.getHeight());
        }
    }

    public void onHSlider(MouseEvent mouseEvent) {
        clearCanvas();
        hScale = hScaleSlider.getValue();
        drawSquares();
        onSubmitButtonClicked();
    }
    public void onVSlider(){
        clearCanvas();
        vScale = vScaleSlider.getValue();
        drawSquares();
        onSubmitButtonClicked();
    }

    private void clearCanvas(){
        graphicsContext.clearRect(0,0, polynomialCanvas.getWidth(), polynomialCanvas.getHeight());
    }
}