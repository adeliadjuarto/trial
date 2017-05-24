package app.chatbot.commons;

import java.io.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;


/**
 * Created by willemchua on 5/4/17.
 */
public class Hasher {

    public Hasher() throws Exception{

    }

    public String hash(String filePath) throws Exception{
        File file = new File(filePath);
        return hash(file);
    }

    public static String hash(File file) throws Exception{
        String hashValue = "";
        FileInputStream inputStream = null;

        try{
            inputStream = new FileInputStream(file);
            hashValue = DigestUtils.md5Hex(IOUtils.toByteArray(inputStream));
        }
        catch (IOException e){}
        finally {
            IOUtils.closeQuietly(inputStream);
        }

        return hashValue;
    }

    public String getHash(String filePath) throws Exception {
        File file = new File(filePath);
        return getHash(file);
    }

    public static String getHash(File file) throws Exception {
        if(file.exists()) {
            BufferedReader hashReader = new BufferedReader(new FileReader(file));
            return hashReader.readLine();
        }

        return "";
    }

    public void createHashFile(String filePath) throws Exception {
        String hashPath = filePath.replace("txt", "hash");

        BufferedWriter hashWriter = new BufferedWriter(new FileWriter(hashPath));

        hashWriter.write(hash(filePath));
        hashWriter.close();
    }

}
