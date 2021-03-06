package se.iths.helena.javafx.labb3;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {
    private PrintWriter writer;
    private BufferedReader reader;
    private final BooleanProperty connected = new SimpleBooleanProperty();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void connect(ObservableList<Shape> shapes) {
        if (connected.get())
            return;

        try {
            Socket socket = new Socket("178.174.162.51", 8000);
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            connected.set(true);
            System.out.println("Connected to server");
            executorService.submit(() -> networkHandler(shapes));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(Shape shape) {
        if (connected.getValue())
            writer.println(shape.getAsSvg());
    }

    private void networkHandler(ObservableList<Shape> shapes) {
        try {
            while (true) {
                String line = reader.readLine();
                System.out.println(line);

                if (!line.contains("[you]") && !line.contains("[SERVER]")) {
                    String[] separate = line.split("]");
                    Platform.runLater(() -> shapes.add(ShapeFactory.getShapeFromSvg(separate[1].trim())));
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error. Disconnected.");
            Platform.runLater(() -> connected.set(false));
        }
    }

}

