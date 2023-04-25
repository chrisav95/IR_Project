import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    public static StringBuffer ReadFileByLine(String file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        return stringBuffer;
    }
}
