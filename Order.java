import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String  out_order_no;
    private String pay_chain;
    private String pay_token;
    private String pay_amount;
    private String signature;
    private String notify;
    private String pub_key;
    private String version;

    public String getOut_order_no() {
        return out_order_no;
    }

    public void setOut_order_no(String out_order_no) {
        this.out_order_no = out_order_no;
    }

    public String getPay_chain() {
        return pay_chain;
    }

    public void setPay_chain(String pay_chain) {
        this.pay_chain = pay_chain;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getBytes() throws IOException {
        List<byte[]> list = new ArrayList<>();

        list.add(this.getOut_order_no().getBytes());
        list.add(this.getPay_chain().getBytes());
        list.add(this.getPay_token().getBytes());
        list.add(this.getPay_amount().getBytes());
        list.add(this.getNotify().getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(byte[] bs : list) {
            byteArrayOutputStream.write(bs);
        }

        return byteArrayOutputStream.toByteArray();
    }
}


