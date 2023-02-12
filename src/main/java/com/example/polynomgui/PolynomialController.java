package com.example.polynomgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

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
    public Slider drawingPrecisionSlider;
    private Polynomial polynomial;
    private GraphicsContext graphicsContext;
    private double yScale = 20;
    private double xScale = 20;
    private double drawingPrecision = 0.1;


    @FXML
    public void initialize(){
        initializeSliders();
        initializeSpinners();
        this.graphicsContext = polynomialCanvas.getGraphicsContext2D();
        drawCoordinateGrid();
    }

    private void initializeSpinners(){
        coefficient0Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.01));
        coefficient1Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.01));
        coefficient2Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.01));
        coefficient3Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.01));
        coefficient4Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-200, 200,0.0,0.01));
    }

    private void initializeSliders(){
        xScaleSlider.setMax(polynomialCanvas.getWidth() / 2);
        xScaleSlider.setMin(1);
        xScaleSlider.setValue(xScale);
        xScaleSlider.setBlockIncrement(20);
        xScaleSlider.setMajorTickUnit(20);

        yScaleSlider.setMax(polynomialCanvas.getHeight() / 2);
        yScaleSlider.setMin(1);
        yScaleSlider.setValue(yScale);
        yScaleSlider.setBlockIncrement(20);
        yScaleSlider.setMajorTickUnit(20);

        drawingPrecisionSlider.setMax(1);
        drawingPrecisionSlider.setMin(0.01);
        drawingPrecisionSlider.setValue(0.1);
        drawingPrecisionSlider.setBlockIncrement(0.01);
        drawingPrecisionSlider.setMajorTickUnit(0.01);
    }

    @FXML
    protected void onSubmitButtonClicked(){
        setPolynomial();
    }

    @FXML
    public void onXSliderMoved(){
        xScale = xScaleSlider.getValue();
        updateCanvas();
    }

    @FXML
    public void onYSliderMoved(){
        yScale = yScaleSlider.getValue();
        updateCanvas();
    }

    @FXML
    public void onPrecisionSliderMoved(){
        drawingPrecision = drawingPrecisionSlider.getValue();
        updateCanvas();
    }

    @FXML
    public void onEnterPressed(KeyEvent keyEvent){
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            setPolynomial();
        }
    }

    @FXML
    public void onScrolledOnCanvas(ScrollEvent scrollEvent){
        if(xScale + scrollEvent.getDeltaY() / 10 > 0 && yScale + scrollEvent.getDeltaY() / 10 > 0){
            xScale += (scrollEvent.getDeltaY() / 10);
            yScale += (scrollEvent.getDeltaY() / 10);
            xScaleSlider.setValue(xScale);
            yScaleSlider.setValue(yScale);
            updateCanvas();}
    }

    @FXML
    public void onResetButtonClicked() {
        initializeSpinners();
        this.polynomial = null;
        resetPolynomialInformation();
        clearCanvas();
        drawCoordinateGrid();
    }


    private void setPolynomial(){
        polynomial = new Polynomial(getCoefficientsFromCoefficientSpinners());
        setPolynomialAttributeLabels();
        updateCanvas();
    }

    private void updateCanvas(){
        clearCanvas();
        drawCoordinateGrid();
        if(polynomial != null){
        drawPolynomialToCanvas(polynomial);
        }
    }

    private void setPolynomialAttributeLabels(){
        functionAsStringLabel.setText(polynomial.toString());
        functionTypeLabel.setText(getPolynomialTypeString());
        yInterceptLabel.setText(Double.toString(polynomial.calculateValue(0.0)));
        zeroPointsLabel.setText(getZeroPointsString());
        symmetryLabel.setText(getSymmetryString());
    }

    private String getZeroPointsString(){
        ZeroPoint[] zeroPoints = polynomial.getZeroPoints();
        if(zeroPoints.length == 0){
            return "-";
        }
        else{
            StringBuilder zeroPointsString = new StringBuilder();
            for (ZeroPoint zeroPoint : zeroPoints) {
                zeroPointsString.append(zeroPoint.toString());
            }
            return zeroPointsString.toString();
        }
    }

    private String getSymmetryString(){
        if (polynomial.isAxisSymmetrical()){
            return "Achsensymetrisch";
        }
        else if (polynomial.isPointSymmetrical()){
            return "Punktsymetirsch";
        }
        else{
            return "Keine";
        }
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
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(1);

        double lastX = (-polynomialCanvas.getWidth() / xScale) / 2;
        double lastY = polynomialToDraw.calculateValue(lastX);

        for (double x = (-polynomialCanvas.getWidth() / 2) / xScale; x <= (polynomialCanvas.getWidth() / 2) / xScale; x += drawingPrecision){
            double y = polynomialToDraw.calculateValue(x);
            graphicsContext.strokeLine(adaptXCoordinate(lastX), adaptYCoordinate(lastY), adaptXCoordinate(x), adaptYCoordinate(y));
            lastX = x;
            lastY = y;
        }

        highLightPoints(polynomial.getMinima(),Color.CYAN);
        highLightPoints(polynomial.getMaxima(),Color.LIME);
        highLightPoints(polynomial.getZeroPoints(), Color.DARKGOLDENROD);
    }

    private double adaptXCoordinate(double mathematicalXCoordinate){
        return mathematicalXCoordinate * xScale + polynomialCanvas.getWidth() / 2;
    }

    private double adaptYCoordinate(double mathematicalYCoordinate){
        return -mathematicalYCoordinate * yScale + polynomialCanvas.getHeight() / 2;
    }

    private void drawCoordinateGrid(){
        double canvasWidth = polynomialCanvas.getWidth();
        double canvasHeight = polynomialCanvas.getHeight();

        //Draws x and y axis
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
        graphicsContext.strokeLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);

        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.setLineWidth(0.2);

        double columns = canvasWidth / xScale;
        double rows = canvasHeight / yScale;
        double xCoord, yCoord;
        double xLineSpacing = canvasWidth/columns;
        double yLineSpacing = canvasHeight/rows;

        for (xCoord = canvasWidth / 2; xCoord >= 0; xCoord -= xLineSpacing){
            graphicsContext.strokeLine(xCoord, 0, xCoord, canvasHeight);
            graphicsContext.strokeLine(canvasWidth - xCoord, 0, canvasWidth - xCoord, canvasHeight);
        }

        for (yCoord = canvasHeight / 2; yCoord >= 0; yCoord -= yLineSpacing){
            graphicsContext.strokeLine(0, yCoord, canvasWidth, yCoord);
            graphicsContext.strokeLine(0, canvasHeight - yCoord, canvasWidth, canvasHeight - yCoord);
        }

    }

    private double[] getCoefficientsFromCoefficientSpinners(){
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

    private void highLightPoints(SpecialPoint[] specialPoints, Color color){
        graphicsContext.setStroke(color);
        graphicsContext.setLineWidth(5);
        for(SpecialPoint specialPoint : specialPoints){
            System.out.println(specialPoint.toString());
            double xCoordinateOnCanvas = adaptXCoordinate(specialPoint.getxValue());
            double yCoordinateOnCanvas = adaptYCoordinate(specialPoint.getyValue());
            graphicsContext.strokeLine(xCoordinateOnCanvas, yCoordinateOnCanvas, xCoordinateOnCanvas, yCoordinateOnCanvas);
        }
    }

    private void resetPolynomialInformation() {
        this.symmetryLabel.setText("");
        this.zeroPointsLabel.setText("");
        this.yInterceptLabel.setText("");
        this.functionTypeLabel.setText("");
        this.functionAsStringLabel.setText("");
    }
}