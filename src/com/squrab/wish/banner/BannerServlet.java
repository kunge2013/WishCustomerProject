/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.banner;

import com.squrab.wish.base.BaseServlet;
import com.squrab.wish.goods.BannerBean;
import com.squrab.wish.goods.BannerService;
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
@WebServlet(value = {"/banner/*"}, moduleid = -1, comment = "轮播图")
public class BannerServlet extends BaseServlet {

    @Resource
    private BannerService bannerService;

    @HttpMapping(url = "/banner/query", auth = false, comment = "查询轮播图可以根据ids 和其它条件")
    public void queryBanner(HttpRequest req, HttpResponse resp) {
        Flipper flipper = req.getJsonParameter(Flipper.class, "flipper");
        BannerBean bean = req.getJsonParameter(BannerBean.class, "bean");
        resp.finish(bannerService.queryForPage(flipper, bean));
    }

}
