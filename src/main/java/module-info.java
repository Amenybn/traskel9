module Sportify {
    requires java.sql;
    requires java.mail;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires okhttp3;
    opens  controllers;
    opens entities to javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.desktop;
    requires javafx.swing;
    requires org.jfree.jfreechart;
    requires org.apache.pdfbox;

    exports tests;
    opens services;
    exports services;
    exports controllers;
}