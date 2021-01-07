package com.velokofi.hungryvelos;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class PersistenceManager {

    public static synchronized void persistActivity(final AthleteActivity activity) {
        try {
            final File dir = new File(getActivityFilePath());
            if (!dir.exists()) {
                dir.mkdirs();
                //System.out.println("Created dir: " + dir.getAbsolutePath());
            }

            final File file = new File(dir, String.valueOf(activity.getId()));
            if (file.exists()) {
                delete(String.valueOf(activity.getId()));
            }

            //System.out.println("Creating new file: " + file.getAbsolutePath());
            file.createNewFile();

            //System.out.println("Serializing onto file: " + file.getAbsolutePath());

            final FileOutputStream fileOut = new FileOutputStream(file);
            final ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(activity);
            objectOut.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized List<AthleteActivity> retrieveActivities() {
        final List<AthleteActivity> results = new ArrayList<>();

        final String filePath = getActivityFilePath();
        final File[] files = new File(filePath).listFiles();

        if (files != null && files.length > 0) {
            for (final File file : files) {
                try {
                    if (file.isFile()) {
                        //System.out.println("Deserializing from file: " + file.getName());
                        try (final FileInputStream fileIn = new FileInputStream(file);
                             final ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                            final AthleteActivity activity = (AthleteActivity) objectIn.readObject();
                            results.add(activity);
                        }
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return results;
    }

    public static synchronized void persistClient(final OAuth2AuthorizedClient client) {
        try {
            final File dir = new File(getClientFilePath());
            if (!dir.exists()) {
                dir.mkdirs();
                //System.out.println("Created dir: " + dir.getAbsolutePath());
            }

            final File file = new File(dir, String.valueOf(client.getPrincipalName()));
            if (file.exists()) {
                delete(client.getPrincipalName());
            }

            //System.out.println("Creating new file: " + file.getAbsolutePath());
            file.createNewFile();

            //System.out.println("Serializing onto file: " + file.getAbsolutePath());

            final FileOutputStream fileOut = new FileOutputStream(file);
            final ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(client);
            objectOut.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized List<OAuth2AuthorizedClient> retrieveClients() {
        final List<OAuth2AuthorizedClient> results = new ArrayList<>();
        final String filePath = getClientFilePath();
        final File[] files = new File(filePath).listFiles();

        if (files != null && files.length > 0) {
            for (final File file : files) {
                try {
                    if (file.isFile()) {
                        //System.out.println("Deserializing from file: " + file.getName());
                        try (final FileInputStream fileIn = new FileInputStream(file);
                             final ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                            final OAuth2AuthorizedClient client = (OAuth2AuthorizedClient) objectIn.readObject();
                            results.add(client);
                        }
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return results;
    }

    public static synchronized void delete(final String fileName) {
        try {
            final File dir = new File(getClientFilePath());
            final File file = new File(dir, fileName);
            if (file.exists()) {
                //System.out.println("Deleting file: " + file.getName());
                file.delete();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static String getClientFilePath() {
        final StringBuilder path = new StringBuilder();
        path.append(System.getProperty("user.home"));
        path.append(File.separator);
        path.append(HungryVelosApplication.class.getSimpleName());
        path.append(File.separator);
        path.append("clients");
        return path.toString();
    }

    private static String getActivityFilePath() {
        final StringBuilder path = new StringBuilder();
        path.append(System.getProperty("user.home"));
        path.append(File.separator);
        path.append(HungryVelosApplication.class.getSimpleName());
        path.append(File.separator);
        path.append("activities");
        return path.toString();
    }


}
