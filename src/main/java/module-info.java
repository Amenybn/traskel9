module com.pi.traskel {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires java.mail;


    opens com.pi.traskel to javafx.fxml;
    exports com.pi.traskel;
    exports entities;
    opens entities to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports services;
    opens services to javafx.fxml;
}