package demo.DSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

import static demo.HashAlgorithm.hashFile;

public class Sign {
    private final KeyPairGenerator keyGenerator;
    public PublicKey publicKey;
    public String signature;
    private final KeyPair keyPair;
    private final PrivateKey privateKey;

    public Sign(int keyLength) throws NoSuchAlgorithmException {
        keyGenerator = KeyPairGenerator.getInstance("DSA");
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
//        Cipher cipher = Cipher.getInstance("demo.DSA");
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//        byte[] signatureByte = cipher.doFinal(hashString.getBytes());
//        signature =  Base64.getEncoder().encodeToString(signatureByte);

        Signature signAlgorithm = Signature.getInstance("DSA");

        signAlgorithm.initSign(privateKey);
        signAlgorithm.update(hashString.getBytes());


        // Save file
        writeToFile("file/publicKey", publicKey.getEncoded());
        writeToFile("file/privateKey", privateKey.getEncoded());
        writeToFile("file/signature", hashString.getBytes());
    }

    public static void main(String[] args) throws Exception {
        // Get file
        File file = new File("file/Contract.txt");

        // Sign
        Sign mySign = new Sign(1024);
        mySign.signFile(file);

        System.out.println("Ký tên thành công!!!");
    }
}
