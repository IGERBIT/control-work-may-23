module com.example.controlwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.controlwork to javafx.fxml;
    exports com.example.controlwork;
}