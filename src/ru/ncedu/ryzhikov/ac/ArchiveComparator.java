package ru.ncedu.ryzhikov.ac;

import javax.swing.*;
import java.io.File;

public class ArchiveComparator {
    private static void compare(String[] args) {
        Archive archive;
        Archive anotherArchive;
        if(args.length != 2) {
            File[] files = new File[2];
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setDialogTitle("Archive Comparator");
            if( fileChooser.showDialog(null, "Choose ZIP archives") == JFileChooser.APPROVE_OPTION ) {
                files = fileChooser.getSelectedFiles();
            }
            archive = new Archive(files[0].getPath());
            anotherArchive = new Archive(files[1].getPath());
        } else {
            archive = new Archive(args[0]);
            anotherArchive = new Archive(args[1]);
        }
        Difference difference = archive.compareTo(anotherArchive);
        difference.createFile();
    }

    public static void main(String[] args) {
        compare(args);
    }
}