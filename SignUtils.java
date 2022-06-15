import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SignatureException;
import java.util.Arrays;

public class SignUtils {
    private static String  sign(byte[] message, String privateKey, boolean hasPrefix) {
        byte[] signMessage;
        try {
            signMessage= Hash.sha3(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String signMessageHex = Numeric.toHexString(signMessage);
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Sign.SignatureData signature = null;
        if (hasPrefix) {
            signature = Sign.signPrefixedMessage(signMessageHex.getBytes(),ecKeyPair);
        } else {
            signature = Sign.signMessage(signMessage, ecKeyPair, false);
        }
        byte[] sigData = ByteBuffer.allocate(signature.getR().length + signature.getS().length + signature.getV().length)
                .put(signature.getR())
                .put(signature.getS())
                .put(signature.getV())
                .array();
        return Numeric.toHexString(sigData);
    }

    public static String signV10(byte[] message, String privateKey) {
        return sign(message, privateKey, false);
    }

    public static String signV11(byte[] message, String privateKey) {
        return sign(message, privateKey, true);
    }

    public static String recover(byte[] data, String signHexString) throws SignatureException {
        byte[] signMessage;
        try {
            signMessage= Hash.sha3(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String signMessageHex = Numeric.toHexString(signMessage);
        System.out.println(signMessageHex);

        byte[] sign = Numeric.hexStringToByteArray(signHexString);
        byte[] r = Arrays.copyOfRange(sign,0,32);
        byte[] s = Arrays.copyOfRange(sign,32,64);
        byte[] v = Arrays.copyOfRange(sign,64,65);
        Sign.SignatureData signature = new Sign.SignatureData(v,r,s);
        BigInteger publicKey = Sign.signedMessageHashToKey(signMessage,signature);
        String signAddress = Keys.getAddress(publicKey);
        return "0x" + signAddress;
    }
}
