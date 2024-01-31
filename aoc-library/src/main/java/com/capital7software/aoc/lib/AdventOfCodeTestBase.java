package com.capital7software.aoc.lib;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 * A simple class that can be used in testing to ease the loading of input files.
 */
public abstract class AdventOfCodeTestBase {
  /**
   * Contains the loaded lines of text.
   */
  protected List<String> lines;

  /**
   * The Path that the lines of text have been loaded for.
   */
  protected Path path;

  /**
   * Instantiates a new and empty instance.
   */
  public AdventOfCodeTestBase() {
    this.path = null;
    this.lines = new ArrayList<>();
  }

  /**
   * Reads and loads the file referred to by the specified filename.
   * After this method returns, the path variable points to the file
   * and the lines variable has been loaded with the text from the file.
   *
   * @param filename The filename to load and process.
   */
  protected void setupFromFile(String filename) {
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      var url = classloader.getResource(filename);
      assert url != null;
      path = Paths.get(url.toURI());
      lines = Files.readAllLines(path);
    } catch (URISyntaxException | IOException e) {
      getLogger().error("Unable to load input data from: {}", path);
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the Logger instance to use.
   *
   * @return The Logger instance to use.
   */
  protected abstract Logger getLogger();
}
