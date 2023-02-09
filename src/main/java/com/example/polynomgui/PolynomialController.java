package com.example.polynomgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class PolynomialController {
    public Spinner<Double> coefficient0TextField;
    public Spinner<Double> coefficient1TextField;
    public Spinner<Double> coefficient2TextField;
    public Spinner<Double> coefficient3TextField;
    public Spinner<Double> coefficient4TextField;
    public Label functionAsStringLabel;
    public Label symmetryLabel;
    public Label functionTypeLabel;
    public Label yInterceptLabel;
    public Label zeroPointsLabel;
    private Polynomial polynomial;

    @FXML
    public void initialize(){
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1,0.1);
        coefficient0TextField.setValueFactory(valueFactory);
        coefficient1TextField.setValueFactory(valueFactory);
        coefficient2TextField.setValueFactory(valueFactory);
        coefficient3TextField.setValueFactory(valueFactory);
        coefficient4TextField.setValueFactory(valueFactory);

    }

    @FXML
    protected void onSubmitButtonClicked(){
        polynomial = new Polynomial(getCoefficients());


    }
    private double[] getCoefficients(){
        double[] coefficients = new double[5];
        coefficients[0] = coefficient0TextField.getValue();
        coefficients[1] = coefficient0TextField.getValue();
        coefficients[2] = coefficient0TextField.getValue();
        coefficients[3] = coefficient0TextField.getValue();
        coefficients[4] = coefficient0TextField.getValue();
        return coefficients;


    }
}