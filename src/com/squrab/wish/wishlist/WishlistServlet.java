/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.wishlist;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.customer.Customer;
import com.squrab.wish.customer.WishlistService;
import com.squrab.wish.goods.Wishlist;
import com.squrab.wish.goods.WishlistBean;
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
@WebServlet(name = "/wishlist/*", repair = false, moduleid = -1, comment = "心愿清单接口")
public class WishlistServlet extends BaseServlet {

    @Resource
    private WishlistService wishlistService;

    /**
     * ********************************* 心愿清单 ***********************************
     */
    @HttpMapping(url = "/wishlist/create", auth = false, comment = "创建心愿清单")
    public void createWishlist(HttpRequest req, HttpResponse resp) {
        Customer customer = req.currentUser();
        Wishlist bean = req.getJsonParameter(Wishlist.class, "bean");
        resp.finish(wishlistService.create(customer, bean)); // 心愿清单已存在
    }

    @HttpMapping(url = "/wishlist/query", auth = false, comment = "查询轮播图可以根据ids 和其它条件")
    public void queryWishlist(HttpRequest req, HttpResponse resp) {
        WishlistBean bean = req.getJsonParameter(WishlistBean.class, "bean");
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        resp.finish(wishlistService.queryForPage(flipper, bean)); // 心愿清单已存在
    }

    @HttpMapping(url = "/wishlist/delete", auth = false, comment = "根据Id删除")
    public void deletewishlist(HttpRequest req, HttpResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        resp.finish(wishlistService.delete(id));
    }
}
