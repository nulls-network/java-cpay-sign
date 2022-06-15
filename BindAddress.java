import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * desc: TODO
 *
 * @author : yky
 * @version : 1.0
 * @date : Created in 2022/6/14 6:29 下午
 */
public class BindAddress {

    private String merchant_address;

    private String user_id;

    private String notify;

    private String signature;

    private String chain_name;


    public String getMerchant_address() {
        return merchant_address;
    }

    public void setMerchant_address(String merchant_address) {
        this.merchant_address = merchant_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getChain_name() {
        return chain_name;
    }

    public void setChain_name(String chain_name) {
        this.chain_name = chain_name;
    }

    public byte[] getBytes() throws IOException {
        List<byte[]> list = new ArrayList<>();

        list.add(this.getMerchant_address().getBytes());
        list.add(this.getUser_id().getBytes());
        list.add(this.getNotify().getBytes());
        list.add(this.getChain_name().getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(byte[] bs : list) {
            byteArrayOutputStream.write(bs);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
