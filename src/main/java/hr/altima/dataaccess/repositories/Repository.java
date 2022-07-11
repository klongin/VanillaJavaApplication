package hr.altima.dataaccess.repositories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

abstract class Repository {

    private final String directory;

    public Repository(String directory) {
        this.directory = directory;
    }

    public void deleteFiles() {
        for (File f : getFiles()) {
            if (!f.delete()) {
                return;
            }
        }
    }

    public File[] getFiles() {
        File dir = new File(this.directory);
        if (dir.exists() && dir.isDirectory()) {
            return dir.listFiles();
        }
        return new File[0];
    }

    public List<File> getFilesByExtension(String extension) {
        List<File> filteredList = new ArrayList<>();
        for (File f : getFiles()) {
            if (f.getName().endsWith(extension)) {
                filteredList.add(f);
            }
        }
        return filteredList;
    }

    public String getDirectory() {
        return this.directory;
    }

}
