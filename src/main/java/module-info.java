module com.example.avanceradjavaobjektifierajoelbech {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens com.example.avanceradjavaobjektifierajoelbech to javafx.fxml;
    exports com.example.avanceradjavaobjektifierajoelbech;
}