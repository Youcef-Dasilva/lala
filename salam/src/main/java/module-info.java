module com.aldrin.salam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.aldrin.salam to javafx.fxml;
    opens com.aldrin.salam.model to javafx.base;
    exports com.aldrin.salam;
}