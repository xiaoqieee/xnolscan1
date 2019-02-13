package com.lanbing.spring.xnolscan.service;

import com.lanbing.spring.xnolscan.helper.ScanedProductIdHelper;
import com.lanbing.spring.xnolscan.helper.XnolHttpRequestHelper;
import com.lanbing.spring.xnolscan.model.Product;
import com.lanbing.spring.xnolscan.util.DataToDiscUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class XnolProductScanHelper extends BaseService {

    @Autowired
    private ProductBuyService productBuyService;

    protected void doPageList() throws Exception {
        List<Product> productList = XnolHttpRequestHelper.getProductList();
        if (null == productList) {
            return;
        }
        for (Product p : productList) {
            logger.info(p.getProductId() + ":<<<<<:" + p);
            productBuyService.checkBuy(DataToDiscUtils.TYPE_LIST, p);
        }
    }

    protected void doPageList2() throws Exception {
        List<Integer> productIdList = XnolHttpRequestHelper.getProductIdList();
        if (null == productIdList) {
            return;
        }
        for (Integer productId : productIdList) {
            try {
                if (ScanedProductIdHelper.add(productId)) {
                    doDetail(productId);
                }
            } catch (Exception e) {
                logger.error("循环处理产品ID列表异常", e);
            }
        }
    }


    protected boolean doDetail(Integer productId) throws Exception {
        Product p = XnolHttpRequestHelper.getProductById2(productId, false);
        logger.info(productId + ":>>>>>:" + p);
        if (null == p) {
            return false;
        }
//        DataToDiscUtils.saveToProduct(p);
        productBuyService.checkBuy(DataToDiscUtils.TYPE_DETAIL, p);
        return true;
    }


}
