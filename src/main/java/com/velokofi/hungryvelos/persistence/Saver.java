package com.velokofi.hungryvelos.persistence;

import com.velokofi.hungryvelos.Application;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Saver {

    public static void persistActivities(final String clientId, final String string) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(System.getProperty("user.home"));
            sb.append(File.separator);
            sb.append(Application.class.getName());
            sb.append(File.separator);
            sb.append("activities");

            final File dir = new File(sb.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            final File file = new File(dir, clientId);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            Files.write(Paths.get(file.getPath()), string.getBytes(), StandardOpenOption.WRITE);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
