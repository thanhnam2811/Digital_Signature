package demo;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.util.Base64;

import static demo.HashAlgorithm.hashFile;
import static demo.Verify.verifyFile;
import static java.lang.Thread.sleep;

public class Sign {
    private final KeyPairGenerator keyGenerator;
    public PublicKey publicKey;
    public String signature;
    private final KeyPair keyPair;
    private final PrivateKey privateKey;

    public Sign(int keyLength) throws NoSuchAlgorithmException {
        keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(keyLength, new SecureRandom());
        keyPair = keyGenerator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    // Viết file
    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    // Sign
    public void signFile(File file) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Hash file
        String hashString = hashFile(file);

        // Encrypt hashFile
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] signatureByte = cipher.doFinal(hashString.getBytes());
        signature =  Base64.getEncoder().encodeToString(signatureByte);

        // Save file
        writeToFile("file/publicKey", publicKey.getEncoded());
        writeToFile("file/privateKey", privateKey.getEncoded());
        writeToFile("file/signature", signatureByte);
    }

    public static void main(String[] args) throws Exception {
        // Get file
        File file = new File("file/Contract.txt");

        // Sign
        Sign mySign = new Sign(2048);
        mySign.signFile(file);

        System.out.println("Ký tên thành công!!!");
    }
}
