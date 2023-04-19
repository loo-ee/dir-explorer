module idealist.dir_explorer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens idealist.dir_explorer to javafx.fxml;
    exports idealist.dir_explorer;
}