module Java.Sorting.Algorithm {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports org.sortingAlgorithm.main to javafx.graphics;
    opens org.sortingAlgorithm.controller to javafx.fxml;
}