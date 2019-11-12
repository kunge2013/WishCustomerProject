/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.giftbasket;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.customer.Customer;
import com.squrab.wish.customer.GiftBasket;
import com.squrab.wish.customer.GiftBasketBean;
import com.squrab.wish.customer.GiftBasketService;
import javax.annotation.Resource;
import org.redkale.net.http.HttpMapping;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;
import org.redkale.source.Flipper;

/**
 *
 * @author SquRab
 */
@WebServlet(name = "/giftbasket/*", repair = false, moduleid = -1, comment = "礼品篮")
public class GiftbasketServlet extends BaseServlet {

    @Resource
    private GiftBasketService giftBasketService;

    /**
     * ********************************* 礼品篮 ***********************************
     */
    @HttpMapping(url = "/giftbasket/addGiftBasket", auth = false, comment = "添加删除礼品蓝")
    public void addGiftBasket(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        GiftBasket bean = req.getJsonParameter(GiftBasket.class, "bean");
        resp.finish(giftBasketService.create(customer, bean));
    }

    @HttpMapping(url = "/giftbasket/query", auth = true, comment = "添加删除礼品蓝")
    public void queryGiftBasket(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        GiftBasketBean bean = req.getJsonParameter(GiftBasketBean.class, "bean");
        bean.setCustomerid(customer.getCustomerid());
        resp.finish(giftBasketService.queryForPage(flipper, bean));
    }

    @HttpMapping(url = "/giftbasket/delete", auth = false, comment = "清除礼物篮")
    public void deleteGiftBasket(HttpRequest req, HttpResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        resp.finish(giftBasketService.delete(id));
    }
}
