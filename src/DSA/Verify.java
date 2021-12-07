package DSA;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static demo.HashAlgorithm.hashFile;

public class Verify {

    // Get key from file
    public static PrivateKey getPrivateKey(String path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(path).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        return kf.generatePrivate(spec);
    }

    //
    public static PublicKey getPublicKey(String path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(path).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        return kf.generatePublic(spec);
    }

    // Kiểm tra chữ ký
    public static boolean verifyFile(File file, PublicKey _publicKey, byte[] _signature) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        // Hash file
        String hashFile = hashFile(file);

        // Create cipher with public key to decrypt
//        Cipher cipher = Cipher.getInstance("DSA");
//        cipher.init(Cipher.DECRYPT_MODE, _publicKey);

        Signature verifyAlgorithm  = Signature.getInstance("DSA");
        verifyAlgorithm.initVerify(_publicKey);

        try {
            //Decrypt signature
        //    byte[] byteDecrypted = cipher.doFinal(Base64.getDecoder().decode(_signature));
            verifyAlgorithm.update(_signature);
            String hashFileDecrypted = new String(_signature);

            // Verify
            if ( hashFile.equals(hashFileDecrypted) ) {
                System.out.println("Xác nhận thành công!");
                return true;
            } else {
                System.out.println("Xác nhận không thành công! File có thể đã bị chỉnh sửa!!!");
                return false;
            }
        } catch (SignatureException e) {
            System.out.println("Xác nhận không thành công! File có thể được gửi từ người khác!!!");
            return false;
        }
    }

    // Kiểm tra chữ ký
    public static boolean verifyFile(File file, File _publicKey, File _signature) throws Exception {
       PublicKey pkey = getPublicKey(_publicKey.getPath());
       byte[] signature = Files.readAllBytes(_signature.toPath());
       return verifyFile(file, pkey, signature);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("file/Contract.txt");
        File publicKeyFile = new File("file/publicKey");
        File signatureFile = new File("file/signature");

        System.out.println("Verify FILE: " + verifyFile(file, publicKeyFile, signatureFile));
    }
}
