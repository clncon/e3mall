package cn.e3mall.order.service;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
/**
 * 订单处理服务
 * @author 孔旻
 *
 */
public interface OrderService {
	
	E3Result createOrder(OrderInfo orderInfo);

}
