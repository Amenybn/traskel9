module WorkshopJDBC3A56 {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    exports tests;
    opens services;
    exports services;
    opens entities to javafx.base;

}