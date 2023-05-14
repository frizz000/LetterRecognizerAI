import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<BufferedImage> loadImages(String basePath, String[] labels) throws IOException {
        List<BufferedImage> images = new ArrayList<>();

        for (String label : labels) {
            File folder = new File(basePath + File.separator + label);
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                        BufferedImage image = ImageIO.read(file);
                        images.add(image);
                    }
                }
            }
        }

        return images;
    }

    public static List<Integer> loadLabels(String basePath, String[] labels) throws IOException {
        List<Integer> labelList = new ArrayList<>();

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            File folder = new File(basePath + File.separator + label);
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                        labelList.add(i);
                    }
                }
            }
        }
//        for (int i = 0; i < labelList.size(); i++) {
//            for (int j = i + 1; j < labelList.size(); j++) {
//                if (labelList.get(i).equals(labelList.get(j))) {
//                    labelList.remove(j);
//                    j--;
//                }
//            }
//        }

        return labelList;
    }
}
