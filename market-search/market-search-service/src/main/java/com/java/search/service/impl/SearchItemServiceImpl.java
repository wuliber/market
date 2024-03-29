package com.java.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.java.dto.ItemSearch;
import com.java.dto.MarketResult;
import com.java.dto.SearchResult;
import com.java.search.dao.SearchSolrDao;
import com.java.search.mapper.ItemSearchMapper;
import com.java.search.service.SearchItemServiceI;

@Service
public class SearchItemServiceImpl implements SearchItemServiceI {

	@Resource
	private ItemSearchMapper itemSearchMapper;

	@Resource
	private SolrServer solrServer;

	@Resource
	private SearchSolrDao searchSolrDao;

	@Override
	public MarketResult importItemToSolr() {
		// 查询
		List<ItemSearch> items = itemSearchMapper.getAllItems();
		// 导入索引库
		try {
			for (ItemSearch itemSearch : items) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", itemSearch.getId());
				document.addField("item_title", itemSearch.getTitle());
				document.addField("item_sell_point", itemSearch.getSellPoint());
				document.addField("item_price", itemSearch.getPrice());
				document.addField("item_image", itemSearch.getImage());
				document.addField("item_desc", itemSearch.getItemDesc());
				document.addField("item_category_name", itemSearch.getCategoryName());
				solrServer.add(document);
			}
			solrServer.commit();
		} catch (Exception e) {
			MarketResult.build(100, "数据导入失败");
			e.printStackTrace();
		}
		return MarketResult.ok();
	}

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		// 默认搜索域
		query.set("df", "item_keywords");
		// 设置分页
		if (page < 1) {
			page = 1;
		}
		query.setStart((page - 1) * rows);
		if (rows < 1) {
			rows = 10;
		}
		query.setRows(rows);
		// 高亮
		query.setHighlight(true);
		// 设置高亮域，用多个空格隔开
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<font color='orange'>");
		query.setHighlightSimplePost("</font>");
		SearchResult searchResult = searchSolrDao.search(query);
		long recordNum = searchResult.getRecordNum();
		long pageNum = recordNum / rows;
		if (recordNum % rows > 0) {
			pageNum = pageNum + 1;
		}
		searchResult.setPageNum(pageNum);
		return searchResult;
	}

	@Override
	public MarketResult addSolrByItemId(Long itemId) {
		ItemSearch item = itemSearchMapper.findItemById(itemId);
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", itemId);
		document.addField("item_title", item.getTitle());
		document.addField("item_sell_point", item.getSellPoint());
		document.addField("item_price", item.getPrice());
		document.addField("item_image", item.getImage());
		document.addField("item_desc", item.getItemDesc());
		document.addField("item_category_name", item.getCategoryName());
		try {
			solrServer.add(document);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MarketResult.ok();
	}

	@Override
	public MarketResult editSolrByItemId(Long itemId) {
		Integer statusById = itemSearchMapper.findItemStatusById(itemId);
		if (statusById.equals(3)) {
			try {
				solrServer.deleteById(itemId+"");
				solrServer.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ItemSearch item = itemSearchMapper.findItemById(itemId);
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", itemId);
			document.addField("item_title", item.getTitle());
			document.addField("item_sell_point", item.getSellPoint());
			document.addField("item_price", item.getPrice());
			document.addField("item_image", item.getImage());
			document.addField("item_desc", item.getItemDesc());
			document.addField("item_category_name", item.getCategoryName());
			try {
				solrServer.add(document);
				solrServer.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return MarketResult.ok();
	}
 }