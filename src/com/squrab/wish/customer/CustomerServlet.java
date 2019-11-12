/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.customer;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.goods.Wishlist;
import com.squrab.wish.goods.WishlistBean;
import com.squrab.wish.notice.RandomCode;
import com.squrab.wish.order.RecAddress;
import com.squrab.wish.order.RecAddressBean;
import com.squrab.wish.order.RecAddressService;
import com.squrab.wish.usermember.UserInfo;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.Base64;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.redkale.net.http.HttpMapping;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;
import org.redkale.service.RetResult;
import org.redkale.source.Flipper;
import org.redkale.util.Comment;

/**
 *
 * @author 86176
 */
@WebServlet(value = {"/customer/*"},  moduleid = -1, comment = "用户")
public class CustomerServlet extends BaseServlet {

    @Resource
    private RecAddressService recAddressService;
    /**
     * 手机号注册绑定
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @HttpMapping(auth = false, url = "/customer/smsregcode", comment = "短信验证码")
    public void smsreg(HttpRequest req, HttpResponse resp) throws IOException {
        smsvercode(RandomCode.TYPE_SMSREG, req, resp);
    }

    private void smsvercode(final short type, HttpRequest req, HttpResponse resp) throws IOException {
        String mobile = req.getRequstURIPath("mobile:", req.getParameter("mobile"));
        if (type == RandomCode.TYPE_SMSODM) { // 给原手机号码发送验证短信
            UserInfo user = req.currentUser();
            if (user != null) {
                mobile = user.getMobile();
            }
        }
        RetResult rr = service.smscode(type, mobile);
        if (finest) {
            logger.finest(req.getRequestURI() + ", mobile = " + mobile + "---->" + rr);
        }
        resp.finishJson(rr);
    }

    @HttpMapping(auth = false, url = "/customer/fetchPhoneNumber", comment = "获取手机号")
    public void fetchPhoneNumber(HttpRequest req, HttpResponse resp) throws IOException {
        String encryptedData = req.getRequstURIPath("encryptedData:", req.getParameter("encryptedData"));
        String sessionKey = req.getRequstURIPath("sessionKey:", req.getParameter("sessionKey"));
        String iv = req.getRequstURIPath("iv", req.getParameter("iv"));
        resp.finish(RetResult.success(getPhoneNumber(encryptedData, sessionKey, iv)));
    }

    public Object getPhoneNumber(String encryptedData, String session_key, String iv) {
        Base64.Decoder decode = Base64.getDecoder();
        // 被加密的数据
        byte[] dataByte = decode.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = decode.decode(session_key);
        // 偏移量
        byte[] ivByte = decode.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
//                Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @HttpMapping(url = "/customer/fetchOpenid", auth = false, comment = "获取微信openId")
    public void fetchOpenid(HttpRequest req, HttpResponse resp) {
        String code = req.getRequstURIPath("code:", req.getParameter("code"));
        resp.finishJson(service.fetchOpenid(code));
    }

    @Comment("绑定电话号码")
    @HttpMapping(url = "/customer/bindMobile", auth = true, comment = "绑定电话号码")
    public void bindMobile(HttpRequest req, HttpResponse resp) {
        CustomerBean bean = req.getJsonParameter(CustomerBean.class, "bean");
        resp.finishJson(service.bindMobile(bean));
    }

    @Comment("添加标签到用户表字段以分号分割")
    @HttpMapping(url = "/customer/addLabels", auth = true, comment = "添加标签到用户表字段以分号分割")
    public void addLabels(HttpRequest req, HttpResponse resp) {
        Customer bean = req.getJsonParameter(CustomerBean.class, "bean");
        resp.finishJson(service.create(bean));
    }

    /**
     * ********************************
     * 用户收货地址
	 ***********************************************
     */
    @Comment("用户收获地址绑定")
    @HttpMapping(url = "/customer/bindRecAddress", auth = true, comment = "用户收获地址绑定")
    public void bindRecAddress(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        RecAddress bean = req.getJsonParameter(RecAddress.class, "bean");
        resp.finishJson(recAddressService.create(customer, bean));
    }

    @Comment("分页查询")
    @HttpMapping(url = "/customer/queryRecAddress", auth = false, comment = "查询收货地址列表")
    protected void queryRecAddress(HttpRequest req, HttpResponse resp) {
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        RecAddressBean bean = req.getJsonParameter(RecAddressBean.class, "bean");
        resp.finishJson(recAddressService.queryForPage(flipper, bean));
    }

}
