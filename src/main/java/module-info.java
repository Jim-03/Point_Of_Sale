module main.hardware_pos_v2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.hardware_pos_v2 to javafx.fxml;
    exports main.hardware_pos_v2;
}