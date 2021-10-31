package se.iths.helena.javafx.labb3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SvgWriter {

    public void save(Model model) {
        Path path = Path.of(System.getProperty("user.home"), "shapes.svg");
        List<String> svg = getSvg(model);
        writeToFile(path, svg);
    }

    private List<String> getSvg(Model model) {
        List<String> strings = new ArrayList<>();
        strings.add("<svg " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "version=\"1.1\"\n " +
                "width=\"800.0\" " +
                "height=\"600.0\">");
        model.getShapes().forEach(shape -> strings.add(shape.getAsSvg()));
        strings.add("</svg>");
        return strings;
    }

    private void writeToFile(Path path, List<String> strings) {
        try {
            Files.write(path, strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
