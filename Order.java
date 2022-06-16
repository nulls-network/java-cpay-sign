import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    @JSONField(name = "out_order_no")
    private String outOrderNo;
    @JSONField(name = "pay_chain")
    private String payChain;
    @JSONField(name = "pay_token")
    private String payToken;
    @JSONField(name = "pay_amount")
    private String payAmount;
    private String signature;
    private String notify;
    @JSONField(name = "pub_key")
    private String pubKey;
    private String version;

    public byte[] getBytes() throws IOException {
        List<byte[]> list = new ArrayList<>();

        list.add(this.getOutOrderNo().getBytes());
        list.add(this.getPayChain().getBytes());
        list.add(this.getPayToken().getBytes());
        list.add(this.getPayAmount().getBytes());
        list.add(this.getNotify().getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(byte[] bs : list) {
            byteArrayOutputStream.write(bs);
        }

        return byteArrayOutputStream.toByteArray();
    }
}


