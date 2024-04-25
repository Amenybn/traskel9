module WorkshopJDBC3A56 {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.desktop;
    requires javafx.swing;
    exports tests;
    opens services;
    exports services;
    opens entities to javafx.base;
    exports Controllers;
    opens Controllers;

}