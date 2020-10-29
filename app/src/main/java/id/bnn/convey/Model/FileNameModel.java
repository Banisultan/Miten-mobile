package id.bnn.convey.Model;

import java.io.File;

public class FileNameModel {
    String name;
    File file;

    public FileNameModel(
            String name,
            File file
    ){
        this.file = file;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}
