package kniznica;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    public GridPane grid;

    private Database database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = new Database();
        loadEverything();
    }

    public void loadEverything(){
        try {
            ResultSet rs = database.loadEverything();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String book = rs.getString("Book");
                String genre = rs.getString("Genre");
                String author = rs.getString("Author");
                int read = rs.getInt("Read");
                boolean isReading = rs.getBoolean("IsReading");
                createBookWindow(book, genre, author, read, isReading);

            }
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    private void createBookWindow(String book, String genre, String author,
                                  int read, boolean isReading) throws IOException {
        ImageView imageView = new ImageView(database.loadImage(book, genre));
        imageView.setFitHeight(250);
        imageView.setFitWidth(150);
        HBox hbox = new HBox();
        VBox box = new VBox();
        hbox.getChildren().add(imageView);
        box.getChildren().add(new Label(book));
        box.getChildren().add(new Label(author));
        box.getChildren().add(new Label(genre));
        box.getChildren().add(new Label(Integer.toString(read)));
        box.getChildren().add(new Label(Boolean.toString(isReading)));
        hbox.getChildren().add(box);
        grid.setGridLinesVisible(true);
        grid.getChildren().add(hbox);
    }
}
