package Model.FileTable;

import java.io.BufferedReader;

public class FileDescriptor {
    private String fileName;
    private BufferedReader bufferedReader;

    public FileDescriptor(String fName, BufferedReader bufReader){
        fileName = fName;
        bufferedReader = bufReader;
    }

    @Override
    public String toString() {
        return "FileDescriptor{" +
                "fileName='" + fileName + '\'' +
                ", bufferedReader=" + bufferedReader +
                '}';
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public String getFileName() {
        return fileName;
    }

}
