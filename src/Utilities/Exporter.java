package Utilities;

import de.halirutan.keypromoterx.statistic.StatisticsItem;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Exporter {
  private final String basePath;
  private final String outputFileName;
  private final ArrayList<String[]> reportData;

  public Exporter(String basePath, String outputFileName, ArrayList<StatisticsItem> statisticsData) {
    this.basePath = basePath;
    this.outputFileName = outputFileName;
    this.reportData = preprocess(statisticsData);
  }

  public void export() throws IOException {
    createFilesAndDirectories();
    CSVWriter writer = new CSVWriter(new FileWriter(basePath + outputFileName));
    writer.writeAll(reportData);
    writer.flush();
  }

  private void createFilesAndDirectories() throws IOException {
    new File(basePath).mkdirs();
    new File(basePath + outputFileName).createNewFile();
  }

  private ArrayList<String[]> preprocess(ArrayList<StatisticsItem> statisticsItems) {
    ArrayList<String[]> normalizedData = new ArrayList<>();
    normalizedData.add(new String[]{"shortcut", "description", "count", "ideaActionID"});
    for (StatisticsItem statisticsItem : statisticsItems) {
      normalizedData.add(new String[]{convertToUnicode(statisticsItem.shortCut), statisticsItem.description, String.valueOf(statisticsItem.count), statisticsItem.ideaActionID});
    }
    return normalizedData;
  }

  private String convertToUnicode(String originalString) {
    StringBuilder unicodeString = new StringBuilder();
    for (int characterPosition = 0; characterPosition < originalString.length(); characterPosition++) {
      if (Character.isSurrogate(originalString.charAt(characterPosition))) {
        Integer res = Character.codePointAt(originalString, characterPosition);
        characterPosition++;
        unicodeString.append("U+").append(Integer.toHexString(res).toUpperCase());
      } else {
        unicodeString.append(originalString.charAt(characterPosition));
      }
    }
    return unicodeString.toString();
  }
}
