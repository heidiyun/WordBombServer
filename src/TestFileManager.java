import javax.xml.soap.Text;
import java.io.IOException;

public class TestFileManager {
    public static void main(String[] args) {
        try {
            TextFileManager.writeFile("./data/input.txt", "hello, i'm a guy");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TextFileManager.appendFile("./data/input.txt", "\n");
            TextFileManager.appendFile("./data/input.txt", "Hi, who are you");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(TextFileManager.readFile("./data/input.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
