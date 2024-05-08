module Sportify {
    requires java.sql;
    requires java.mail;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires okhttp3;
    opens  Controllers;
    exports Controllers;
    // other required modules
    requires javafx.controls;
    requires javafx.fxml;
    opens entities to javafx.base;
    requires javafx.graphics;

    requires mysql.connector.j;
    requires java.desktop;
    requires javafx.swing;
    requires org.jfree.jfreechart;
    requires org.apache.pdfbox;
    requires kernel;
    requires layout;

    exports tests;
    opens services;
    exports services;

}