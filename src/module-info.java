module com.ibntaymiyya.gym {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.ibntaymiyya.gym to javafx.fxml;
    exports com.ibntaymiyya.gym;
}