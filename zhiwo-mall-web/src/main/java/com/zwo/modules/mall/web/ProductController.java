package com.zwo.modules.mall.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zwo.modules.mall.domain.PrProduct;
import com.zwo.modules.mall.domain.PrProductPackagePrice;
import com.zwo.modules.mall.domain.PrProductProperty;
import com.zwo.modules.mall.domain.PrProductPropertyValue;
import com.zwo.modules.mall.domain.PrProductWithBLOBs;
import com.zwo.modules.mall.service.IPrProductPackagePriceService;
import com.zwo.modules.mall.service.IPrProductPropertyService;
import com.zwo.modules.mall.service.IPrProductPropertyValueService;
import com.zwo.modules.mall.service.IPrductService;
import com.zwo.modules.system.domain.TbUser;
import com.zwotech.common.web.BaseController;

@Controller
@RequestMapping("product")
@Lazy(true)
public class ProductController extends BaseController<PrProduct> {
	@Autowired
	@Lazy(true)
	private IPrductService productService;
	@Autowired
	@Lazy(true)
	private IPrProductPropertyValueService productPropertyValueService;
	@Autowired
	@Lazy(true)
	private IPrProductPropertyService productPropertyService;
	@Autowired
	@Lazy(true)
	private IPrProductPackagePriceService packagePriceService;
	
	private static final String basePath = "views/mall/product/";
	
	@RequiresPermissions("mall:product:view")
	@RequestMapping(value = { "", "list" })
	public String list(HttpServletRequest httpServletRequest) {
		return basePath+"product_list";
	}
	
	@RequiresPermissions("mall:product:create")
	@RequestMapping(value = { "create" }, method = RequestMethod.GET)
	public String tocreate(@Valid PrProductWithBLOBs product,@RequestParam(required=false) String propertyValues,
			@RequestParam(required=false) String propertyPrices,BindingResult result, Model uiModel,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		product.setId(System.currentTimeMillis()+""+Math.round(Math.random()*100));
		uiModel.addAttribute("product", product);
		return basePath + "product_edit";
	}

	@RequiresPermissions("mall:product:view")
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") String id,@RequestParam(required=false) String propertyValues,
			@RequestParam(required=false) String propertyPrices, Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		PrProductWithBLOBs product = productService.selectByPrimKey(id);

		uiModel.addAttribute("product", product);
		uiModel.addAttribute("operation", "edit");
		return basePath + "product_edit";
	}
	
	@RequiresPermissions("mall:product:create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid PrProductWithBLOBs product,@RequestParam String propertyValues,
			@RequestParam String propertyPrices, BindingResult result, Model uiModel,
			RedirectAttributes redirectAttributes,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("product", product);
			redirectAttributes.addFlashAttribute("message", "数据绑定有误！");
			return "redirect:/product/create";
		}
		if("".equals(product.getCategoryId())){
			product.setCategoryId(null);
		}
		
		/*Subject currentUser = SecurityUtils.getSubject(); 
		if(currentUser!=null){
			TbUser user =  (TbUser) currentUser.getSession().getAttribute("user");
			if(user!=null){
				product.setUpdater(user.getUsername());
				product.setUserId(user.getId());
			}
		}*/
		
		
		//[{"id":"15028412955799","name":"款式","propertyValueArray":[{"propertyId":"15028412955799","name":"白色","id":"1503100384366695"},{"propertyId":"15028412955799","name":"黑色","id":"1503100392023593"}]},{"id":"150284156334250","name":"套餐","propertyValueArray":[{"propertyId":"150284156334250","name":"买一送一","id":"15031004074012"}]}]
		//[{"groupPrice":"23","indepentPrice":"23","propertyValueId":"1503100384366695_15031004074012"},{"groupPrice":"23","indepentPrice":"23","propertyValueId":"1503100392023593_15031004074012"}]
		int res = productService.insertSelective(product);
		if(res==1){
			redirectAttributes.addFlashAttribute("product", product);
			redirectAttributes.addFlashAttribute("message", "保存成功！");
		}
		JSONArray perpertyArray = null;
		if(null != propertyValues && !"".equals(propertyValues)){
			perpertyArray =  (JSONArray) JSONArray.parse(propertyValues);
			for (Object obj : perpertyArray) {
				JSONObject json = (JSONObject) obj;
				JSONArray perpertyValueArray = (JSONArray) json.get("propertyValueArray");
				for (Object object : perpertyValueArray) {
					JSONObject jsonObject = (JSONObject) object;
					PrProductPropertyValue productProperty = new PrProductPropertyValue();
					productProperty.setId((String) jsonObject.get("id"));
					productProperty.setProductId(product.getId());
					productProperty.setPropertyId(jsonObject.getString("propertyId"));
					productProperty.setName((String) jsonObject.get("name"));
					productPropertyValueService.insertSelective(productProperty);
				}
				
			}
		}
		
		JSONArray perPriceArray = null;
		if(null != propertyPrices && !"".equals(propertyPrices)){
			perPriceArray =  (JSONArray) JSONArray.parse(propertyPrices);
			for (Object obj : perPriceArray) {
				JSONObject json = (JSONObject) obj;
				PrProductPackagePrice packagePrice = new PrProductPackagePrice(); 
				packagePrice.setId(System.currentTimeMillis() + "" + Math.round(Math.random() * 99));
				packagePrice.setGourpPrice((Double)json.get("groupPrice")+"");
				packagePrice.setIndependentPrice((Double) json.get("indepentPrice"));
				packagePrice.setProductId(product.getId());
				packagePrice.setPropertyValueId(json.getString("propertyValueId"));
				packagePriceService.insertSelective(packagePrice);
			}
		}
		
		return "redirect:/product/create";
	}
	 
	@RequiresPermissions("mall:product:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid PrProductWithBLOBs product,@RequestParam String propertyValues,
			@RequestParam String propertyPrices, BindingResult result, Model uiModel,
			RedirectAttributes redirectAttributes,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("product", product);
			redirectAttributes.addFlashAttribute("message", "填入的数据有误！");
		}
		Subject currentUser = SecurityUtils.getSubject(); 
		if(currentUser!=null){
			TbUser user =  (TbUser) currentUser.getSession().getAttribute("user");
			if(user!=null){
				product.setUpdater(user.getUsername());
				product.setUserId(user.getId());
			}
		}
		
		int res = this.productService.updateByPrimaryKeySelective(product);
		if(res==1){
			redirectAttributes.addFlashAttribute("product", product);
			redirectAttributes.addFlashAttribute("message", "保存成功！");
		}
		redirectAttributes.addAttribute("operation", "edit");
		return "redirect:/product/edit/"+product.getId();
	}
}
