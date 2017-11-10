package com.java.service;

import com.java.dto.EazyUIStyle;
import com.java.dto.MarketResult;
import com.java.model.TbItem;
import com.java.model.TbItemDesc;

public interface ItemServiceI {

	TbItem getItemById(Long id);

	EazyUIStyle getAllList(Integer pagenum, Integer rows);

	MarketResult insertItem(TbItem tbItem, String desc);

	MarketResult updateItem(TbItem item);

	MarketResult queryItemById(Long id);

	MarketResult queryParamItemById(Long id);
	
	TbItemDesc getItemDescById(Long itemId);

}