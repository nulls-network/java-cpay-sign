import com.alibaba.fastjson.JSONObject;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {
//        testRecover();

        testOrder();
    }



    public static  void testOrder(){
        Order order = new Order();
        order.setOut_order_no("1111111111111111111");
        order.setNotify("http://localhost");
        order.setPay_amount("0.01");
        order.setPay_chain("tron");
        order.setPay_token("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");
        order.setPub_key("0x2143d11B31b319C008F59c2D967eBF0E5ad2791d");
        order.setVersion("1.1");
        String privateKey = "f78494eb224f875d7e352a2b017304e11e6a3ce94af57b373ae82a73b3496cdd";
        String sign_str = sign(order,privateKey);
        order.setSignature(sign_str);

        String data = JSONObject.toJSONString(order);
        System.out.println(data);
        String result = HttpclientUtils.doPostJson("http://127.0.0.1:7007/v1/order/create",data);
        System.out.println(result);
    }


    /**
     * bytesData   0xce5d6cc02a1dde3faad8cf348cde36ba36b1c51cf7d77a165edfe51d7bded12b
     * {
     *   out_order_no: '20220604144658',
     *   uuid: 'd197446c-7dbe-4e2f-ab8f-65914e5e953e',
     *   merchant_address: '0x2143d11B31b319C008F59c2D967eBF0E5ad2791d',
     *   type: 'order',
     *   amount: '0.0100',
     *   amount_hex: '10000',
     *   got_amount: '0.0100',
     *   pay_result: 'success',
     *   token: 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t',
     *   bind_uuid: null,
     *   user_id: null
     * }
     * 0xee53bbcd4a2b7f359e8db3092b08dce5627f48d01e4a47ae1306702587dba4012078541ca1ebf1709b8ca8dbe9152adc1909b2f52a8e4d1a8817c6cbee2b2da41c
     */
    public static  void testRecover(){
        NotifyMessage notify = new NotifyMessage();
        notify.setOut_order_no("20220604144658");
        notify.setUuid("d197446c-7dbe-4e2f-ab8f-65914e5e953e");
        notify.setMerchant_address("0x2143d11B31b319C008F59c2D967eBF0E5ad2791d");
        notify.setType("order");
        notify.setAmount("0.0100");
        notify.setAmount_hex("10000");
        notify.setGot_amount("0.0100");
        notify.setPay_result("success");
        notify.setToken("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");
        notify.setSign("0xee53bbcd4a2b7f359e8db3092b08dce5627f48d01e4a47ae1306702587dba4012078541ca1ebf1709b8ca8dbe9152adc1909b2f52a8e4d1a8817c6cbee2b2da41c");
        try {
            String address = recover(notify);
            System.out.println(address);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }


    public static String  sign(Order order,String privateKey) {
        byte[] out_order_no = order.getOut_order_no().getBytes();
        byte[] pay_chain = order.getPay_chain().getBytes();
        byte[] pay_token = order.getPay_token().getBytes();
        byte[] pay_amount = order.getPay_amount().getBytes();
        byte[] notify = order.getNotify().getBytes();
        int length = out_order_no.length + pay_chain.length + pay_token.length+ pay_amount.length+ notify.length;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(out_order_no);
        buffer.put(pay_chain);
        buffer.put(pay_token);
        buffer.put(pay_amount);
        buffer.put(notify);

        byte[]  sign_message;
        try {
             sign_message= Hash.sha3(buffer.array());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String sign_message_hex = Numeric.toHexString(sign_message);
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Sign.SignatureData signature = Sign.signPrefixedMessage(sign_message_hex.getBytes(),ecKeyPair);
        System.out.println(signature.getR().length +"   "+signature.getS().length +"  "+signature.getV().length);
        byte[] sig_data = ByteBuffer.allocate(signature.getR().length + signature.getS().length + signature.getV().length)
                .put(signature.getR())
                .put(signature.getS())
                .put(signature.getV())
                .array();
        String hexSig = Numeric.toHexString(sig_data);
       return hexSig;
    }


    public  static  String  recover(NotifyMessage message) throws SignatureException {
        byte[]  out_order_no = message.getOut_order_no().getBytes();
        byte[]  uuid = message.getUuid().getBytes();
        byte[]  merchant_address = message.getMerchant_address().getBytes();
        byte[]  type = message.getType().getBytes();
        byte[]  amount = message.getAmount().getBytes();
        byte[]  amount_hex = message.getAmount_hex().getBytes();
        byte[]  got_amount = message.getGot_amount().getBytes();
        byte[]  pay_result = message.getPay_result().getBytes();
        byte[]  token = message.getToken().getBytes();

        int length = out_order_no.length+uuid.length+merchant_address.length+type.length+amount.length+amount_hex.length+ got_amount.length+pay_result.length+ token.length;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(out_order_no);
        buffer.put(uuid);
        buffer.put(merchant_address);
        buffer.put(type);
        buffer.put(amount);
        buffer.put(amount_hex);
        buffer.put(got_amount);
        buffer.put(pay_result);
        buffer.put(token);
        byte[] data = buffer.array();
        byte[]  sign_message;
        try {
            sign_message= Hash.sha3(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String sign_message_hex = Numeric.toHexString(sign_message);
        System.out.println(sign_message_hex);

        byte[] sign = Numeric.hexStringToByteArray(message.getSign());
        byte[] r = Arrays.copyOfRange(sign,0,32);
        byte[] s = Arrays.copyOfRange(sign,32,64);
        byte[] v = Arrays.copyOfRange(sign,64,65);
        Sign.SignatureData signature = new Sign.SignatureData(v,r,s);
        BigInteger publicKey = Sign.signedMessageHashToKey(sign_message,signature);
        String sign_address = Keys.getAddress(publicKey);
        return "0x"+sign_address;
    }
}
