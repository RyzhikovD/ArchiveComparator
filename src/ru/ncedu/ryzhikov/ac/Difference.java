package ru.ncedu.ryzhikov.ac;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class is used to represent a difference between two compared archives
 */
public class Difference {
    private final String firstArchiveName;
    private final String secondArchiveName;
    private LinkedList<String> deleted = new LinkedList<>();
    private LinkedList<String> added   = new LinkedList<>();
    private LinkedList<String> updated = new LinkedList<>();
    private HashMap<String, String> renamed = new HashMap<>();

    /**
     * All differences have the state used by Archive during a comparison.
     */
    protected Difference(String firstPath, String secondPath) {
        int start = firstPath.lastIndexOf("\\");
        int end = firstPath.lastIndexOf(".");
        firstArchiveName = firstPath.substring(start + 1, end);

        start = secondPath.lastIndexOf("\\");
        end = secondPath.lastIndexOf(".");
        secondArchiveName = secondPath.substring(start + 1, end);
    }

    protected LinkedList<String> getDeleted() {
        return deleted;
    }

    protected LinkedList<String> getAdded() {
        return added;
    }

    protected LinkedList<String> getUpdated() {
        return updated;
    }

    protected HashMap<String, String> getRenamed() {
        return renamed;
    }

    void createFile(){
        Set<String> keySet = renamed.keySet();
        try(FileWriter result = new FileWriter("Difference.txt"))
        {
            result.write(String.format("  %-30s |   %-30s%n", firstArchiveName, secondArchiveName));
            result.write(String.format("%33s+%34s", "", "").replace(" ", "-") + "\r\n");
            for(String updatedFile : updated) {
                result.write(String.format("* %-30s | * %-30s%n", updatedFile, updatedFile));
            }
            for(String renamedFile : keySet) {
                result.write(String.format("? %-30s | ? %-30s%n", renamedFile, renamed.get(renamedFile)));
            }
            for(String deletedFile : deleted) {
                result.write(String.format("- %-30s |   %-30s%n", deletedFile, ""));
            }
            for(String newFile : added) {
                result.write(String.format("  %-30s | + %-30s%n", "", newFile));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
