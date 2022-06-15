import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {

        testOrder("1.1");
        testOrder("1.0");
        testSignBindAddress();

        testRecover();
        testBindRecover();

    }

    private static  void testOrder(String version) {
        Order order = new Order();
        order.setOut_order_no(UUID.randomUUID().toString());
        order.setNotify("http://localhost");
        order.setPay_amount("0.01");
        order.setPay_chain("tron");
        order.setPay_token("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");
        order.setPub_key("0x2143d11B31b319C008F59c2D967eBF0E5ad2791d");
        order.setVersion(version);
        String privateKey = "f78494eb224f875d7e352a2b017304e11e6a3ce94af57b373ae82a73b3496cdd";

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
            byte[] data = notify.getBytes();
            String address = SignUtils.recover(data, notify.getSign());
            System.out.println(address);
        } catch (SignatureException | IOException e) {
            throw new RuntimeException(e);
        }
    }


//    {
//        out_order_no: '1652342887226',
//                uuid: 'aa24f0a5-c8e2-45da-a062-05693b0112b7',
//            merchant_address: '0x8cf6F24dddb965e6636d46129f28050c3357c43b',
//            type: 'recharge',
//            amount: '0.0100',
//            amount_hex: '10000',
//            got_amount: '0.0100',
//            pay_result: 'success',
//            token: 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t',
//            bind_uuid: '10925df6-221c-44ab-905f-df5e48697815',
//            user_id: '5261807812',
//            sign:'0xdf26ba7ff8d6933bbe63271b9d266440c63cb630676a2c05a55fc72f7d639ae62af7125bef4fea1df29983652d8b934d72602f971fb66591a080daf786ecdb4e1b\n'
//    }
    public static  void testBindRecover() {
        NotifyMessage notify = new NotifyMessage();
        notify.setOut_order_no("1652342887226");
        notify.setUuid("aa24f0a5-c8e2-45da-a062-05693b0112b7");
        notify.setMerchant_address("0x8cf6F24dddb965e6636d46129f28050c3357c43b");
        notify.setType("recharge");
        notify.setAmount("0.0100");
        notify.setAmount_hex("10000");
        notify.setGot_amount("0.0100");
        notify.setPay_result("success");
        notify.setToken("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");
        notify.setBind_uuid("10925df6-221c-44ab-905f-df5e48697815");
        notify.setUser_id("5261807812");
        notify.setSign("0xdf26ba7ff8d6933bbe63271b9d266440c63cb630676a2c05a55fc72f7d639ae62af7125bef4fea1df29983652d8b934d72602f971fb66591a080daf786ecdb4e1b");

        try {
            byte[] data = notify.getBytes();
            String address = SignUtils.recover(data, notify.getSign());
            System.out.println(address);
        } catch (SignatureException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    //    { merchant_address: '0xDC87b44A19d7C69b56cCed2e3C7c9819263611bd',
//    user_id: '3333333333',
//    notify: 'https://test-notify.vercel.app/api/index',
//    signature: '0x42a06002b076686a46c61f315d95ae7426dbb7804d35af83e1568b7523180bdf5d7490ac3d3899dda0c11e343dc39198ee06af443218f7fa86f4741b6218ff381b',
//    chain_name: 'tron'
//    }
    public static  void testSignBindAddress(){
        String privateKey = "f78494eb224f875d7e352a2b017304e11e6a3ce94af57b373ae82a73b3496cdd";
        BindAddress   bind = new BindAddress();
        bind.setMerchant_address("0x2143d11B31b319C008F59c2D967eBF0E5ad2791d");
        bind.setUser_id("3333333333");
        bind.setNotify("https://test-notify.vercel.app/api/index");
        bind.setChain_name("tron");

        byte[] data = new byte[0];
        try {
            data = bind.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sign = SignUtils.signV10(data, privateKey);

        bind.setSignature(sign);

        System.out.println(sign);
        final String res = HttpclientUtils.doPostJson("https://api-tron-v1.dpay.systems/v1/bing/merchantBingAddress", JSONObject.toJSONString(bind));
        System.out.println(res);
    }
}
