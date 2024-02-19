package org.sxuang.alta.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;


public class Logging {
    public static final Logger LOG = Logger.getLogger("ALTA");

    static {
        LOG.setLevel(Level.ALL);
        LOG.setUseParentHandlers(false);
    }

    public static void start(Path logFolder) {
        addConsoleHandler(Level.INFO);
        try {
            addFileHandler(logFolder);
        } catch (IOException | NullPointerException e) {
            LOG.warning("Unable to create log file\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void initForTest() {
        addConsoleHandler(Level.ALL);
    }

    private static void addConsoleHandler(Level level) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new DefaultFormatter());
        consoleHandler.setLevel(level);
        LOG.addHandler(consoleHandler);
    }

    private static void addFileHandler(Path logFolder) throws IOException {
        if (Files.isRegularFile(logFolder)) {
            Files.delete(logFolder);
        }
        Files.createDirectories(logFolder);
        FileHandler fileHandler = new FileHandler(logFolder.resolve("alta.log").toAbsolutePath().toString());
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new DefaultFormatter());
        fileHandler.setEncoding("UTF-8");
        LOG.addHandler(fileHandler);
    }

    private static final class DefaultFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return String.format(
                    "[%s][%s.%s/%s] %s%n",
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(record.getMillis())),
                    record.getSourceClassName(),
                    record.getSourceMethodName(),
                    record.getLevel().toString(),
                    record.getMessage()
            );
        }
    }
}
