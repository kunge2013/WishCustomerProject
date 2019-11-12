/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.squrab.wish.base;

import com.squrab.wish.customer.*;
import com.squrab.wish.goods.GoodsService;
import java.io.IOException;
import java.util.logging.*;
import javax.annotation.Resource;
import org.redkale.convert.json.JsonConvert;
import org.redkale.net.http.*;
import org.redkale.service.RetResult;
import org.redkale.util.AnyValue;

/**
 *
 * @author 86176
 */
@HttpUserType(Customer.class)
public class BaseServlet extends HttpServlet {

    public static final String COOKIE_AUTOLOGIN = "UNF";

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    protected final boolean fine = logger.isLoggable(Level.FINE);

    protected final boolean finer = logger.isLoggable(Level.FINER);

    protected final boolean finest = logger.isLoggable(Level.FINEST);

    protected static final RetResult RET_UNLOGIN = RetCodes.retResult(RetCodes.RET_USER_UNLOGIN);

    protected static final RetResult RET_AUTHILLEGAL = RetCodes.retResult(RetCodes.RET_USER_AUTH_ILLEGAL);

    @Resource
    protected JsonConvert convert;

    @Resource
    protected CustomerService service;

    @Resource
    protected GoodsService goodsService;

    @Override
    public void init(HttpContext context, AnyValue config) {
        super.init(context, config);
    }

    @Override
    public void preExecute(final HttpRequest request, final HttpResponse response) throws IOException {
        if (finer) {
            response.recycleListener((req, resp) -> { // 记录处理时间比较长的请求
                long e = System.currentTimeMillis() - ((HttpRequest) req).getCreatetime();
                if (e > 200) {
                    logger.finer("http-execute-cost-time: " + e + " ms. request = " + req);
                }
            });
        }
        String openId = request.getHeader("openId");
        if (null != openId && !openId.isEmpty()) {
            request.setCurrentUser(service.current(openId));
        }
        response.nextEvent();
    }

    /**
     * 查看当前校验是否成功！！！
     */
    protected void authenticate(HttpRequest request, HttpResponse response) throws IOException {
        Customer info = request.currentUser();
        if (info == null) {
            response.finishJson(RET_UNLOGIN);
            return;
        }
        response.nextEvent();
    }

}
