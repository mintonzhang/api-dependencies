package cn.minsin.meituan.peisong.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.vo.VO;
import cn.minsin.meituan.peisong.config.MutilsMTPeiSongProperties;
import cn.minsin.meituan.peisong.model.notify.AbstractMeituanNotifyModel;
import cn.minsin.meituan.peisong.model.notify.OrderExceptionNotifyModel;
import cn.minsin.meituan.peisong.model.notify.OrderStateNotifyModel;
import cn.minsin.meituan.peisong.model.notify.ShopRangeChangeNotifyModel;
import cn.minsin.meituan.peisong.model.receive.ReceiveInfoModel;
import cn.minsin.meituan.peisong.model.receive.ReceiveQueryInfoModel;
import cn.minsin.meituan.peisong.model.receive.ReceiveRiderLocationModel;
import cn.minsin.meituan.peisong.model.send.AbstractMeituanSendModel;
import cn.minsin.meituan.peisong.model.send.CheckShopModel;
import cn.minsin.meituan.peisong.model.send.OrderByCoordinatesModel;
import cn.minsin.meituan.peisong.model.send.OrderByShopModel;
import cn.minsin.meituan.peisong.model.send.OrderCancelModel;
import cn.minsin.meituan.peisong.model.send.QueryOrderModel;
import cn.minsin.meituan.peisong.model.send.RiderEvaluateModel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 美团配送功能列表
 *
 * @author mintonzhang
 * @date 2019年2月18日
 * @since 0.3.4
 */
@Component
public class MeituanPeisongFunctions extends AbstractFunctionRule {

    @Autowired
    private MutilsMTPeiSongProperties mutilsMTPeiSongProperties;

    @PostConstruct
    private void init() {
        AbstractMeituanSendModel.config = mutilsMTPeiSongProperties;
    }


    /**
     * 合作方根据已录入的门店信息 将订单发送给美团配送平台。
     *
     * @param model 预下单实体
     * @throws IOException
     * @throws MutilsErrorException
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public ReceiveInfoModel createByShop(OrderByShopModel model)
            throws IOException, NoSuchAlgorithmException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/createByShop";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveInfoModel.class);
    }

    /**
     * 根据合作方的送件地址，自动选择合适的中间仓库作为线下货物的交接地点，并完成末端配送。
     *
     * @param model 预下单实体
     * @throws IOException
     * @throws MutilsErrorException
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public ReceiveInfoModel createByCoordinates(OrderByCoordinatesModel model)
            throws IOException, NoSuchAlgorithmException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/createByCoordinates";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveInfoModel.class);
    }

    /**
     * 查询订单
     *
     * @param model 订单查询model
     * @throws NoSuchAlgorithmException
     * @throws MutilsErrorException
     * @throws IOException
     */
    public ReceiveQueryInfoModel queryOrder(QueryOrderModel model)
            throws NoSuchAlgorithmException, IOException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/status/query";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveQueryInfoModel.class);
    }

    /**
     * 获取骑手位置
     *
     * @param model 订单查询model
     * @throws NoSuchAlgorithmException
     * @throws MutilsErrorException
     * @throws IOException
     */
    public ReceiveRiderLocationModel getRiderLocation(QueryOrderModel model)
            throws NoSuchAlgorithmException, IOException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/rider/location";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveRiderLocationModel.class);
    }

    /**
     * 取消订单
     *
     * @param model 订单查询model
     * @throws NoSuchAlgorithmException
     * @throws MutilsErrorException
     * @throws IOException
     */
    public ReceiveInfoModel cancelOrder(OrderCancelModel model)
            throws NoSuchAlgorithmException, IOException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/delete";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveInfoModel.class);
    }

    /**
     * 评价骑手
     *
     * @param model 骑手评价model
     * @throws NoSuchAlgorithmException
     * @throws MutilsErrorException
     * @throws IOException
     */
    public ReceiveInfoModel riderEvaluate(RiderEvaluateModel model)
            throws NoSuchAlgorithmException, IOException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/evaluate";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveInfoModel.class);
    }

    /**
     * 配送能力校验 根据合作方提供的模拟发单参数，确定美团是否可配送。主要校验项：门店是否存在、门店配送范围、门店营业时间、门店支持的服务包。
     *
     * @param model 校验门店model
     * @throws NoSuchAlgorithmException
     * @throws MutilsErrorException
     * @throws IOException
     */
    public ReceiveInfoModel checkShop(CheckShopModel model)
            throws NoSuchAlgorithmException, IOException {
        Map<String, String> params = model.toMap();
        String serverUrl = model.getServerUrl() + "/order/check";
        String post = HttpClientUtil.post(serverUrl, params);
        return JSON.parseObject(post, ReceiveInfoModel.class);
    }

    /**
     * 解析订单状态回调 每次订单状态发生变化时，会对合作方提供的回调url进行回调。 详见
     * ：https://page.peisong.meituan.com/open/doc#section2-5
     *
     * @param req
     * @throws UnsupportedEncodingException
     */
    public OrderExceptionNotifyModel parseExceptionNotify(HttpServletRequest req)
            throws UnsupportedEncodingException {
        return parseNotify(OrderExceptionNotifyModel.class, req);
    }

    /**
     * 订单异常回调 每次配送员上报订单异常（如商家未营业、顾客留错电话等等）时，会对合作方提供的异常回调url进行回调。 详见
     * ：https://page.peisong.meituan.com/open/doc#section2-6
     *
     * @param req
     * @throws UnsupportedEncodingException
     */
    public OrderStateNotifyModel parseOrderStateNotify(HttpServletRequest req)
            throws UnsupportedEncodingException {
        return parseNotify(OrderStateNotifyModel.class, req);
    }

    /**
     * 配送范围变更回调 每次美团运营新增、修改配送范围时，会对合作方提供的配送范围变更回调url进行回调。 详见
     * ：https://page.peisong.meituan.com/open/doc#section2-11
     *
     * @param req
     * @throws UnsupportedEncodingException
     */
    public ShopRangeChangeNotifyModel parseShopRangeChangeNotify(HttpServletRequest req)
            throws UnsupportedEncodingException {
        return parseNotify(ShopRangeChangeNotifyModel.class, req);
    }

    protected <T extends AbstractMeituanNotifyModel> T parseNotify(Class<T> clazz, HttpServletRequest req)
            throws UnsupportedEncodingException {
        req.setCharacterEncoding(CharSetConstant.UTF_8);
        VO init = VO.init();
        Map<String, String[]> requestParams = req.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            init.put(name, valueStr);
        }
        return init.toObject(clazz);
    }

}
