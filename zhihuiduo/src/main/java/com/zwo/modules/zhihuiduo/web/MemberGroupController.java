package com.zwo.modules.zhihuiduo.web;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zwo.modules.mall.domain.PrImage;
import com.zwo.modules.mall.domain.PrImageType;
import com.zwo.modules.mall.domain.PrProduct;
import com.zwo.modules.mall.domain.PrProductPackagePrice;
import com.zwo.modules.mall.domain.PrProductProperty;
import com.zwo.modules.mall.domain.PrProductPropertyValue;
import com.zwo.modules.mall.domain.PrProductWithBLOBs;
import com.zwo.modules.mall.service.IPrImageService;
import com.zwo.modules.mall.service.IPrProductPackagePriceService;
import com.zwo.modules.mall.service.IPrProductPropertyService;
import com.zwo.modules.mall.service.IPrProductPropertyValueService;
import com.zwo.modules.mall.service.IPrductService;
import com.zwo.modules.member.domain.GroupPurcse;
import com.zwo.modules.member.domain.GroupPurcseMember;
import com.zwo.modules.member.service.IGroupPurcseMemberService;
import com.zwo.modules.member.service.IGroupPurcseService;
import com.zwo.modules.member.service.IMemberService;
import com.zwo.modules.member.service.impl.GroupPurcseMemberServiceImpl;
import com.zwo.modules.shop.domain.Shop;
import com.zwo.modules.shop.service.IShopCategoryService;
import com.zwo.modules.shop.service.IShopService;
import com.zwo.modules.zhihuiduo.dto.ProductExtention;
import com.zwotech.common.utils.SpringContextHolder;
import com.zwotech.common.web.BaseController;

/**
 * 会员登录控制器。
 * 
 * @author 黄记新 2017.8.8
 *
 */
@Controller
@Lazy(true)
public class MemberGroupController extends BaseController {
	@Autowired
	@Lazy(true)
	private IPrImageService imageService;

	@Autowired
	@Lazy(true)
	private IMemberService memberService;
	@Autowired
	@Lazy(true)
	private IPrductService prductService;
	@Autowired
	@Lazy(true)
	private IGroupPurcseService groupPurcseService;

	@Autowired
	@Lazy(true)
	private IPrProductPropertyService productPropertyService;

	@Autowired
	@Lazy(true)
	private IPrProductPackagePriceService packagePriceService;
	@Autowired
	@Lazy(true)
	private IPrProductPropertyValueService propertyValueService;
	@Autowired
	@Lazy(true)
	private IShopService shopService;
	@Autowired
	@Lazy(true)
	private IShopCategoryService shopCategoryService;
	@Autowired
	@Lazy(true)
	private IGroupPurcseMemberService groupPurcseMemberService;

	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;

	private static final String basePath = "views/member/";

	public MemberGroupController() {
		super();
		if(SpringContextHolder.getApplicationContext().containsBean("redisTemplate")){
			redisTemplate = SpringContextHolder.getBean("redisTemplate");
		}
	}

	/**
	 * 参团页面。
	 * 
	 * @param goodsId
	 * @param uiModel
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "memberGroup" }, method = RequestMethod.GET)
	public String memberGroup(@RequestParam String goodsId,@RequestParam String groupPurcseId, Model uiModel,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		String jsonString=null;
		ProductExtention productExtention = null;
		List<GroupPurcseMember> groupPurcseMembers = null;
		GroupPurcse groupPurcse = null;
		PrProductWithBLOBs product = null;
		List<PrProductPackagePrice> packagePrices = null;
		List<PrProductPropertyValue> productPropertyValues = null;
		
		String key = groupPurcseId+GroupPurcseMemberServiceImpl.KEY_GROUP_PURCSE_ID_MEMBERS;
		if(redisTemplate!=null){
			productExtention = (ProductExtention) redisTemplate.opsForValue().get(key);
			
			if(productExtention == null){
				productExtention = new ProductExtention();
				
				product = prductService.selectByPrimKey(goodsId);
				try {
					BeanUtils.copyProperties(productExtention, product);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				groupPurcse = groupPurcseService.selectByPrimaryKey(groupPurcseId);
				productExtention.setGroupPurcse(groupPurcse);
				
				if(groupPurcse!=null && groupPurcse.getDisable()==false){
					groupPurcseMembers = groupPurcseMemberService.selectByGroupPurcseId(groupPurcse.getId());
				}
				
				packagePrices = packagePriceService.selectByProductId(product
						.getId());
				productPropertyValues = this.propertyValueService
						.selectByProductId(product.getId());
				productExtention.setPackagePrices(packagePrices);
				productExtention.setProductPropertyValues(productPropertyValues);
				productExtention.setGroupPurcseMembers(groupPurcseMembers);
				
				redisTemplate.opsForValue().set(key, productExtention);
				redisTemplate.expire(key, 30, TimeUnit.MINUTES);
				
				jsonString = JSONObject.toJSONString(productExtention);
				uiModel.addAttribute("rawData", jsonString);
			}else{
				jsonString = JSONObject.toJSONString(productExtention);
				uiModel.addAttribute("rawData", jsonString);
			}
		}else{
			productExtention = new ProductExtention();
			
			product = prductService.selectByPrimKey(goodsId);
			try {
				BeanUtils.copyProperties(productExtention, product);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			groupPurcse = groupPurcseService.selectByPrimaryKey(groupPurcseId);
			productExtention.setGroupPurcse(groupPurcse);
			
			if(groupPurcse!=null && groupPurcse.getDisable()==false){
				groupPurcseMembers = groupPurcseMemberService.selectByGroupPurcseId(groupPurcse.getId());
			}
			
			packagePrices = packagePriceService.selectByProductId(product
					.getId());
			productPropertyValues = this.propertyValueService
					.selectByProductId(product.getId());
			productExtention.setPackagePrices(packagePrices);
			productExtention.setProductPropertyValues(productPropertyValues);
			productExtention.setGroupPurcseMembers(groupPurcseMembers);
			
			redisTemplate.opsForValue().set(key, productExtention);
			redisTemplate.expire(key, 30, TimeUnit.MINUTES);
			
			jsonString = JSONObject.toJSONString(productExtention);
			uiModel.addAttribute("rawData", jsonString);
		}
		
		uiModel.addAttribute("product", product);
		uiModel.addAttribute("groupPurcse", groupPurcse);
		uiModel.addAttribute("groupPurcseMembers", groupPurcseMembers);
		uiModel.addAttribute("packagePrices", packagePrices);
		uiModel.addAttribute("productPropertyValues", productPropertyValues);
		
		return basePath + "memberGroup";
	}

}
