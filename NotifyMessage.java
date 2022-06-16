import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class NotifyMessage {

    @JSONField(name = "out_order_no")
    private String outOrderNo;

    private String uuid;

    @JSONField(name = "merchant_address")
    private String merchantAddress;

    private String type;

    private String amount;

    @JSONField(name = "amount_hex")
    private String amountHex;

    @JSONField(name = "got_amount")
    private String gotAmount;

    @JSONField(name = "pay_result")
    private String payResult;

    @JSONField(name = "bind_uuid")
    private String bindUuid;

    @JSONField(name = "user_id")
    private String userId;

    private String token;

    private String sign;

    public byte[] getBytes() throws IOException {
        List<byte[]> list = new ArrayList<>();
        list.add(this.getOutOrderNo().getBytes());
        list.add(this.getUuid().getBytes());
        list.add(this.getMerchantAddress().getBytes());
        list.add(this.getType().getBytes());
        list.add(this.getAmount().getBytes());
        list.add(this.getAmountHex().getBytes());
        list.add(this.getGotAmount().getBytes());
        list.add(this.getPayResult().getBytes());
        list.add(this.getToken().getBytes());
        if ("recharge".equals(this.getType())) {
            list.add(this.getBindUuid().getBytes());
            list.add(this.getUserId().getBytes());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(byte[] bs : list) {
            byteArrayOutputStream.write(bs);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
