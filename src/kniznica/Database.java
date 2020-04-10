package kniznica;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.Normalizer;

public class Database {

    private Connection c;
    private Statement stmt;

    public Database(){
        c = null;
        stmt = null;
        try {
            boolean missing = false;
            File tempFile = new File(System.getProperty("user.dir") + "/library.db");
            if(!tempFile.exists()){
                missing = true;
            }
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:library.db");
            if(missing){
                create();
            }
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }


    public ResultSet loadEverything() throws SQLException {
        StringBuilder builder = new StringBuilder();
        ResultSet rs;
        stmt = c.createStatement();
        builder.append("SELECT * FROM Library ORDER BY Book ASC;");
        return stmt.executeQuery(builder.toString());
    }


    private void create() throws SQLException {
        System.out.println("Creating database...");
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE Library (Id INT PRIMARY KEY NOT NULL,");
        builder.append("Book TEXT NOT NULL,");
        builder.append("Genre TEXT NOT NULL,");
        builder.append("Author TEXT NOT NULL,");
        builder.append("Pages INT NOT NULL,");
        builder.append("Read INT NOT NULL,");
        builder.append("IsReading BOOLEAN NOT NULL)");
        stmt = c.createStatement();
        stmt.executeUpdate(builder.toString());
        stmt.close();
        System.out.println("Database created");
    }


    public Image loadImage(String book, String genre) throws IOException {
        StringBuilder builder1 = normalizeString(genre);
        StringBuilder builder2 = normalizeString(book);

        String url = "https://www.pantarhei.sk/knihy/beletria/" + builder1.toString() + "/" + builder2.toString() + ".html";
        Document doc = Jsoup.connect(url).get();

        String image = "";
        Elements images = doc.getElementsByTag("img");
        for(Element src : images){
            String string = src.attr("abs:src");
            if(string.contains(builder2.toString())){
                image = string;
                System.out.println(image);
                break;
            } else {
                image = string;
            }
        }
        return new Image(image);
    }


    private StringBuilder normalizeString(String string){
        String[] split = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for(String tmp : split){
            String pom = Normalizer.normalize(tmp, Normalizer.Form.NFD);
            pom = pom.replaceAll("[^\\p{ASCII}]", "");
            pom = pom.toLowerCase();
            builder.append(pom + "-");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder;
    }
}
