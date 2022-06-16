import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {

//        testOrder("1.1");
//        testOrder("1.0");
        testSignBindAddress();

//        testRecover();
//        testBindRecover();
    }

    private static  void testOrder(String version) {
        String privateKey = "f78494eb224f875d7e352a2b017304e11e6a3ce94af57b373ae82a73b3496cdd";

        Order order = new Order();
        // The maximum length of orderNo is 32 bytes
        order.setOutOrderNo(UUID.randomUUID().toString().substring(0,32));
        order.setNotify("http://localhost");
        order.setPayAmount("0.01");
        order.setPayChain("tron");
        order.setPayToken("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");
        order.setPubKey(SignUtils.getPublicKey(privateKey));
        order.setVersion(version);

        byte[] data = new byte[0];
        try {
            data = order.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String signature = null;
        if ("1.1".equals(version)) {
            signature =  SignUtils.signV11(data, privateKey);
        } else {
            signature = SignUtils.signV10(data, privateKey);
        }
        order.setSignature(signature);

        String param = JSONObject.toJSONString(order);
        System.out.println(param);
        String result = HttpclientUtils.doPostJson("https://api-tron-v1.dpay.systems/v1/order/create",param);
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
     *   user_id: null,
     *   sign: 0xee53bbcd4a2b7f359e8db3092b08dce5627f48d01e4a47ae1306702587dba4012078541ca1ebf1709b8ca8dbe9152adc1909b2f52a8e4d1a8817c6cbee2b2da41c
     * }
     *
     */
    public static  void testRecover(){
        String jsonData =
                "   {" +
                "        'out_order_no': '20220604144658'," +
                "        'uuid': 'd197446c-7dbe-4e2f-ab8f-65914e5e953e'," +
                "        'merchant_address': '0x2143d11B31b319C008F59c2D967eBF0E5ad2791d'," +
                "        'type': 'order'," +
                "        'amount': '0.0100'," +
                "        'amount_hex': '10000'," +
                "        'got_amount': '0.0100'," +
                "        'pay_result': 'success'," +
                "        'token': 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t'," +
                "        'bind_uuid': null," +
                "        'user_id': null," +
                "        'sign': '0xee53bbcd4a2b7f359e8db3092b08dce5627f48d01e4a47ae1306702587dba4012078541ca1ebf1709b8ca8dbe9152adc1909b2f52a8e4d1a8817c6cbee2b2da41c'" +
                "    }";

        NotifyMessage notify = JSONObject.parseObject(jsonData, NotifyMessage.class);
        try {
            byte[] data = notify.getBytes();
            String address = SignUtils.recover(data, notify.getSign());
            System.out.println(address);
        } catch (SignatureException | IOException e) {
            throw new RuntimeException(e);
        }
    }


//    {
//      out_order_no: '1652342887226',
//      uuid: 'aa24f0a5-c8e2-45da-a062-05693b0112b7',
//      merchant_address: '0x8cf6F24dddb965e6636d46129f28050c3357c43b',
//      type: 'recharge',
//      amount: '0.0100',
//      amount_hex: '10000',
//      got_amount: '0.0100',
//      pay_result: 'success',
//      token: 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t',
//      bind_uuid: '10925df6-221c-44ab-905f-df5e48697815',
//      user_id: '5261807812',
//      sign:'0xdf26ba7ff8d6933bbe63271b9d266440c63cb630676a2c05a55fc72f7d639ae62af7125bef4fea1df29983652d8b934d72602f971fb66591a080daf786ecdb4e1b'
//    }
    public static  void testBindRecover() {
        String jsonData =
                "   {" +
                "        'out_order_no': '1652342887226'," +
                "        'uuid': 'aa24f0a5-c8e2-45da-a062-05693b0112b7'," +
                "        'merchant_address': '0x8cf6F24dddb965e6636d46129f28050c3357c43b'," +
                "        'type': 'recharge'," +
                "        'amount': '0.0100'," +
                "        'amount_hex': '10000'," +
                "        'got_amount': '0.0100'," +
                "        'pay_result': 'success'," +
                "        'token': 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t'," +
                "        'bind_uuid': '10925df6-221c-44ab-905f-df5e48697815'," +
                "        'user_id': '5261807812'," +
                "        'sign': '0xdf26ba7ff8d6933bbe63271b9d266440c63cb630676a2c05a55fc72f7d639ae62af7125bef4fea1df29983652d8b934d72602f971fb66591a080daf786ecdb4e1b'" +
                "    }";

        NotifyMessage notify = JSONObject.parseObject(jsonData, NotifyMessage.class);
        try {
            byte[] data = notify.getBytes();
            String address = SignUtils.recover(data, notify.getSign());
            System.out.println(address);
        } catch (SignatureException | IOException e) {
            throw new RuntimeException(e);
        }
    }



//    {
//      merchant_address: '0x2143d11b31b319c008f59c2d967ebf0e5ad2791d',
//      user_id: '3333333333',
//      notify: 'https://test-notify.vercel.app/api/index',
//      signature: '0x80d071f0c61ae90fd228cb68636ded52bd87f11539e3545f0e549f19492208b165c8a2f34e1b354a9e2ab301847aaf382dc4406b2706ef036779ef5d99b33e341b',
//      chain_name: 'tron'
//    }
    public static  void testSignBindAddress(){
        String privateKey = "f78494eb224f875d7e352a2b017304e11e6a3ce94af57b373ae82a73b3496cdd";

        BindAddress bind = new BindAddress();
        bind.setMerchantAddress(SignUtils.getPublicKey(privateKey));
        bind.setUserId("3333333333");
        bind.setNotify("https://test-notify.vercel.app/api/index");
        bind.setChainName("tron");

        byte[] data = new byte[0];
        try {
            data = bind.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sign = SignUtils.signV10(data, privateKey);

        bind.setSignature(sign);

        System.out.println("sign: " + sign);
        final String res = HttpclientUtils.doPostJson("https://api-tron-v1.dpay.systems/v1/bing/merchantBingAddress", JSONObject.toJSONString(bind));
        System.out.println(res);
    }
}
