import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.parseLong;

public class StartApplication {

    static String currentPath = System.getProperty("user.dir");
    static String directoryHome = "\\Home";
    static String directoryDev = "\\Home\\Dev";
    static String directoryTest = "\\Home\\Test";

    static String directorySaveToHome = currentPath + directoryHome + "\\";
    static String directoryMoveToDev = currentPath + directoryDev + "\\";
    static String directoryMoveToTest = currentPath + directoryTest + "\\";

    static String firstFileXml = "firstFileXml.xml";
    static String secondFileXml = "secondFileXml.xml";

    static String firstOddFileJar = "firstOddFileJar.jar";
    static String secondOddFileJar = "secondOddFileJar.jar";

    static String firstEvenFileJar = "firstEvenFileJar.jar";
    static String secondEvenFileJar = "secondEvenFileJar.jar";

    static String countFileTxt = "count.txt";
    static int count = 0;

    static String time;

    public static void CreateFolder(String path) {
        boolean success = (new File(path)).mkdirs();
        if (!success) {
            System.out.append("Folder nie utworzony bo już istnieje:");
        } else {
            System.out.append("Folder utworzony");
        }
    }

    public static void CreateFile(String path) {
        try {
            File file = new File(path);

            if (file.createNewFile()) {
                System.out.println("Plik zostal stworzony!");
            } else {
                System.out.println("Plik juz istnieje");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MoveFileToDirectory(String pathFile, String pathFile2) {
        InputStream inStream;
        OutputStream outStream;

        try {
            File afile = new File(pathFile);
            File bfile = new File(pathFile2);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
            afile.delete();
            System.out.println("Plik zostal przeniesiony poprawnie!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SaveToFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(count);
            bw.flush();
            bw.close();
            System.out.println("Ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        CreateFolder(currentPath + directoryHome);
        CreateFolder(currentPath + directoryDev);
        CreateFolder(currentPath + directoryTest);

        CreateFile(directorySaveToHome + firstEvenFileJar);
        CreateFile(directorySaveToHome + secondEvenFileJar);

        TimeUnit.MINUTES.sleep(5);

        CreateFile(directorySaveToHome + firstOddFileJar);
        CreateFile(directorySaveToHome + secondOddFileJar);

        CreateFile(directorySaveToHome + firstFileXml);
        CreateFile(directorySaveToHome + secondFileXml);

        CreateFile(directorySaveToHome + countFileTxt);

        List<String> listFile = new ArrayList<String>();
        listFile.add(firstFileXml);
        listFile.add(secondFileXml);
        listFile.add(firstOddFileJar);
        listFile.add(secondOddFileJar);
        listFile.add(firstEvenFileJar);
        listFile.add(secondEvenFileJar);

        try {
            Path path = Path.of(currentPath);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime fileTime = attributes.creationTime();
            DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            String cTime = (df.format(fileTime.toMillis()));
            time = cTime;
        } catch (IOException ex) {
            // handle exception
        }
        for (int i = 0; i < listFile.size(); i++) {
            if (listFile.get(i).contains(".xml")) {
                MoveFileToDirectory(directorySaveToHome + listFile.get(i), directoryMoveToTest + listFile.get(i));
                count = count++;
            } else if (listFile.get(i).contains(".jar")) {
                if (parseLong(time) % 2 == 0) {
                    MoveFileToDirectory(directorySaveToHome + listFile.get(i), directoryMoveToDev + listFile.get(i));
                    count = count++;
                } else {
                    MoveFileToDirectory(directorySaveToHome + listFile.get(i), directoryMoveToTest + listFile.get(i));
                    count = count++;
                }
            }
        }
        SaveToFile(countFileTxt);
    }
}