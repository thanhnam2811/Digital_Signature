package demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashAlgorithm {
    public static String hashFile(File file) throws IOException, NoSuchAlgorithmException {
        // Use SHA-1 algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Get files byte[]
        byte[] messageBytes = Files.readAllBytes(file.toPath());

        // Hash
        byte[] messageHash = digest.digest(messageBytes);

        // Return
        return Base64.getEncoder().encodeToString(messageHash);
    }
/*
    public static String hashFile(File file) throws IOException, NoSuchAlgorithmException {
        // Use SHA-1 algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        // Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        // Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        // close the stream; We don't need it now.
        fis.close();

        // Get the hash's bytes
        byte[] bytes = digest.digest();

        // This bytes[] has bytes in decimal format;
        // Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        // return complete hash
        return sb.toString();
    }

 */
}
