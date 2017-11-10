package com.java.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.json.JSON;
import com.java.model.TbItem;
import com.java.utils.CookieUtils;
import com.java.utils.JsonUtils;

@Controller
@RequestMapping("order")
public class IndexController {

	@Value("${MARKET_CART_TOKEN}")
	private String MARKET_CART_TOKEN;

	@RequestMapping("order-cart")
	public String showIndex(HttpServletRequest request) {
		List<TbItem> items = getCartList(request);
		request.setAttribute("cartList", items);
		return "order-cart";
	}

	/**
	 * 
	 * @Author:sangjin
	 * @Date: 2017年11月9日 下午5:31:30
	 * @Title: getCartList
	 * @Description:获取用户购买的商品列表
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		// 从cookie中获取信息
		String cookieValue = CookieUtils.getCookieValue(request, MARKET_CART_TOKEN, true);
		// 将json对象转换成list
		if (StringUtils.isNotBlank(cookieValue)) {
			List<TbItem> list = JsonUtils.jsonToList(cookieValue, TbItem.class);
			// 不为空 返回list 结束此方法
			return list;
		}
		return new ArrayList<>();
	}

}
