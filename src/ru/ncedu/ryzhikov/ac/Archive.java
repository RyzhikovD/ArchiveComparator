package ru.ncedu.ryzhikov.ac;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class is used to represent a ZIP archive
 */
public class Archive {
    private final String path;
    private final LinkedList<ZipEntry> storage = new LinkedList<>();
    public Archive(String path) {
        this.path = path;
        read();
    }

    private String getPath() {
        return path;
    }

    private LinkedList<ZipEntry> getStorage() {
        return storage;
    }

    private void read() {
        try(FileInputStream file = new FileInputStream(path);
            ZipInputStream zip = new ZipInputStream(file))
        {
            ZipEntry entry;
            while((entry = zip.getNextEntry()) != null) {
                storage.add(entry);
            }
            zip.closeEntry();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It creates copies of the storage of two archives and compares
     * each item of repository that called the method with the items
     * of repository passed as an argument.
     *
     * Items that have found the correspond pair are removed in order
     * not to be compared with other items. After that, the iterator continues
     * to compare the next element of this archive with all elements of
     * another archive from the beginning.
     * Items that have not found a correspond pair are either new or removed.
     *
     * @see Difference
     * @param anotherArchive archive with which compares
     * @return the difference between this archive and archive with which compares
     */
    protected Difference compareTo(Archive anotherArchive) {
        Difference difference = new Difference(path, anotherArchive.getPath());
        LinkedList<ZipEntry> archiveCopy = new LinkedList<>(this.storage);
        LinkedList<ZipEntry> anotherArchiveCopy  = new LinkedList<>(anotherArchive.getStorage());

        ListIterator<ZipEntry> firstIterator = archiveCopy.listIterator();
        ListIterator<ZipEntry> secondIterator;
        ZipEntry archiveEntry, anotherArchiveEntry;

        while(firstIterator.hasNext()){
            archiveEntry = firstIterator.next();
            secondIterator = anotherArchiveCopy.listIterator();
            while(secondIterator.hasNext()){
                anotherArchiveEntry = secondIterator.next();
                if(archiveEntry.getName().equals(anotherArchiveEntry.getName())) {
                    if(archiveEntry.getSize() != anotherArchiveEntry.getSize()){
                        difference.getUpdated().add(archiveEntry.getName());
                    }
                    firstIterator.remove(); //same or updated files
                    secondIterator.remove();
                    break;
                } else if(archiveEntry.getSize() == anotherArchiveEntry.getSize()){
                    difference.getRenamed().put(archiveEntry.getName(), anotherArchiveEntry.getName());
                    firstIterator.remove();
                    secondIterator.remove();
                    break;
                }
            }
        }
        for(ZipEntry z : archiveCopy) {
            difference.getDeleted().add(z.getName());
        }
        for(ZipEntry z : anotherArchiveCopy) {
            difference.getAdded().add(z.getName());
        }
        return difference;
    }
}