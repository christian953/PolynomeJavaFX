module com.example.polynomgui {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.polynomgui to javafx.fxml;
    exports com.example.polynomgui;
}