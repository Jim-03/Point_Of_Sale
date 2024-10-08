module main.hardware_pos_v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jbcrypt;


    opens main.hardware_pos_v2 to javafx.fxml;
    opens main.hardware_pos_v2.Controllers to javafx.fxml;
    opens main.hardware_pos_v2.Database.Entity to org.hibernate.orm.core;
    exports main.hardware_pos_v2;
}