import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class BindAddress {

    @JSONField(name = "merchant_address")
    private String merchantAddress;

    @JSONField(name = "user_id")
    private String userId;

    private String notify;

    private String signature;

    @JSONField(name = "chain_name")
    private String chainName;

    public byte[] getBytes() throws IOException {
        List<byte[]> list = new ArrayList<>();

        list.add(this.getMerchantAddress().getBytes());
        list.add(this.getUserId().getBytes());
        list.add(this.getNotify().getBytes());
        list.add(this.getChainName().getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(byte[] bs : list) {
            byteArrayOutputStream.write(bs);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
