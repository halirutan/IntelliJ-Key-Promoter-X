package Utilities;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Exporter {
  private final String basePath;
  private final String outputFileName;
  private final ArrayList<String[]> reportData;

  public Exporter(String basePath, String outputFileName, ArrayList<String[]> reportData) {
    this.basePath = basePath;
    this.outputFileName = outputFileName;
    this.reportData = reportData;
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
}
