/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.home;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.customer.*;
import com.squrab.wish.goods.*;
import javax.annotation.Resource;
import org.redkale.net.http.*;
import org.redkale.source.Flipper;
import org.redkale.util.Comment;

/**
 *
 * @author 86176
 */
@WebServlet(name = "/homepage/*", repair = false, moduleid = -1, comment = "首頁接口")
public class HomePageServlet extends BaseServlet {

    @Resource
    private BannerService bannerService;

    @HttpMapping(url = "/homepage/queryGoodsById", auth = false, comment = "商品详情")
    public void queryGoodsById(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        int id = Integer.parseInt(req.getParameter("id"));
        resp.finish(goodsService.queryById(customer.getCustomerid(), id));
    }

    @Comment("伴手礼专区;热卖单品;猜你喜欢;首页查询；礼品查询")
    @HttpMapping(url = "/homepage/queryGoods/", auth = false, comment = "伴手礼专区;热卖单品;猜你喜欢;首页查询；礼品查询")
    public void queryGoods(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        GoodsBean bean = req.getJsonParameter(GoodsBean.class, "bean");
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        resp.finish(goodsService.queryForPage(flipper, bean));
    }

}
