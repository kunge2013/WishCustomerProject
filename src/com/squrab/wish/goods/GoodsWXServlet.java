package com.squrab.wish.goods;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.customer.GiftBasket;
import com.squrab.wish.customer.GiftBasketService;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;

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

import com.squrab.wish.customer.GiftBasketBean;
import com.squrab.wish.goods.GoodsBean;
import com.squrab.wish.order.RecAddressBean;
import com.squrab.wish.goods.WishlistBean;
import com.squrab.wish.customer.Customer;
import com.squrab.wish.notice.RandomCode;
import com.squrab.wish.order.RecAddress;
import com.squrab.wish.usermember.UserInfo;
import com.squrab.wish.goods.BannerService;

import com.squrab.wish.order.RecAddressService;

@WebServlet(value = {"/goods/*"}, comment = "商品访问服务")
public class GoodsWXServlet extends BaseServlet {

    /**
     * ********************************* 商品 ***********************************
     */
    @HttpMapping(url = "/goods/queryById", auth = false, comment = "商品详情")
    public void queryById(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        int id = Integer.parseInt(req.getParameter("id"));
        resp.finish(goodsService.queryById(customer.getCustomerid(), id));
    }

    @Comment("伴手礼专区;热卖单品;猜你喜欢;首页查询；礼品查询")
    @HttpMapping(url = "/goods/query", auth = false, comment = "伴手礼专区;热卖单品;猜你喜欢;首页查询；礼品查询")
    public void queryForPage(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        GoodsBean bean = req.getJsonParameter(GoodsBean.class, "bean");
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        resp.finish(goodsService.queryForPage(flipper, bean));
    }

    @HttpMapping(url = "/goods/login", auth = false, comment = "测试接口")
    public void test(HttpRequest req, HttpResponse resp) {
        System.out.println("AAA");
    }

//	@Resource
//	private GoodsLabelService labelService;
//	/*********************************** 标签统计 ************************************/
//	@HttpMapping(url = "/label/inLabelcreasebindgoodsnum", auth = false, comment = "标签商品个数统计")
//	public void inLabelcreasebindgoodsnum(HttpRequest req, HttpResponse resp) {
//		Customer customer = req.currentUser();
//		GoodsLabel bean = req.getJsonParameter(GoodsLabel.class, "bean");
//		resp.finish(goodsService.increasebindgoodsnum(customer, bean));
//	}
}
