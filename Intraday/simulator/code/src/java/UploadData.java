
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nishant.vibhute
 */
public class UploadData extends ActionSupport {

    String date;
    private File zipFile;
    private String zipFileFileName;
    String zipFilePath;
    String destDir = "E://oneminutedata//";

    public String message;

    public String execute() {
        return ActionSupport.SUCCESS;
    }

    public String uploadData() throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now)); //2016/11/16 12:08:43

        String outerPath = "E:\\Shared\\oneminutedata\\2017\\NOV";//Remove
//        File folderOuter = new File(outerPath); //Remove
//        String[] filesOuter = folderOuter.list();//Remove

//        for (String fileOuter : filesOuter) {//Remove
        if (unzip(zipFile.getAbsolutePath(), destDir)) { //uncomment
//            if (unzip(outerPath + "//" + fileOuter, destDir)) { //remove
            String filename = getBaseName(zipFileFileName); //uncomment
//                String filename = getBaseName(fileOuter);//remove
            String folderName = destDir + filename;
            File folder = new File(folderName);
            String tableName = "data_" + date.replaceAll("-", "_");

            try {
                Connection con = DBUtil.getConnection();
                String createTableSql = "CREATE TABLE " + tableName + "(id INT PRIMARY KEY AUTO_INCREMENT, scrip_name VARCHAR(255), s_date DATE, s_time VARCHAR(50),s_open DOUBLE, s_high DOUBLE,\n"
                        + "s_low DOUBLE, s_close DOUBLE, s_volume INT)";
                PreparedStatement ps = con.prepareStatement(createTableSql);
                ps.executeUpdate();
                DBUtil.closeConnection(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] files = folder.list();
            for (String file : files) {
                String path = folder + "\\" + file;
                path = path.replaceAll(Matcher.quoteReplacement("\\"), "/");
                String insertQuery = "LOAD DATA INFILE '" + path + "' \n"
                        + "INTO TABLE " + tableName + " \n"
                        + "FIELDS TERMINATED BY ',' \n"
                        + "ENCLOSED BY '\"'\n"
                        + "LINES TERMINATED BY '\\n' (`scrip_name`,`s_date`,`s_time`,`s_open`,`s_high`,`s_low`,`s_close`,`s_volume`)";
                Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(insertQuery);

                ps.executeUpdate();
                DBUtil.closeConnection(con);
            }
        }

//        }//Remove
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println(dtf1.format(now1)); //2016/11/16 12:08:43 //2016/11/16 12:08:43

        message = "Uploaded " + date + " Start @" + now + " End @" + now1;

        return ActionSupport.SUCCESS;

    }

    private static String convertDate(String strDate) {
        //for strdate = 2017 July 25

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
                .toFormatter();

        LocalDate parsedDate = LocalDate.parse(strDate, f);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String newDate = parsedDate.format(f2);

        return newDate;
    }

    private boolean unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
//                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }

    public static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public File getZipFile() {
        return zipFile;
    }

    public void setZipFile(File zipFile) {
        this.zipFile = zipFile;
    }

    public String getZipFileFileName() {
        return zipFileFileName;
    }

    public void setZipFileFileName(String zipFileFileName) {
        this.zipFileFileName = zipFileFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
