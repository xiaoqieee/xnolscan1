package com.lanbing.spring.xnolscan.service;


import com.lanbing.spring.xnolscan.helper.ProductMaxIdHelper;
import com.lanbing.spring.xnolscan.helper.StatusHelper;
import com.lanbing.spring.xnolscan.model.ProductIdBO;
import com.lanbing.spring.xnolscan.util.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class XnolDetailScanService extends XnolProductScanHelper {


    public void doDetail(ProductIdBO productIdBO) {
        while (true) {
            if (!StatusHelper.isStarting()) {
                logger.info("已经手动关闭");
                break;
            }
            try {
                boolean isEnd = doDetailLoop(productIdBO);
                if (isEnd) {
                    productIdBO.resetCustProductId();
                }
            } catch (Exception e) {
                logger.error("处理详情异常", e);
            }
        }
    }


    private boolean doDetailLoop(ProductIdBO productIdBO) {
        while (true) {
            if (!StatusHelper.isStarting()) {
                return false;
            }
            try {
                Integer productId = productIdBO.getNextProductId();
                boolean hasProduct = doDetailLoop(productId);
                if (hasProduct) {
                    ProductMaxIdHelper.setCurMaxProductId(productId);
                }
            } catch (Exception e) {
                logger.error("循环处理详情异常", e);
            }
            return true;
        }
    }

    public boolean doDetailLoop(Integer productId) throws Exception {
        boolean hasTheProduct = false;
        long start = System.currentTimeMillis();
        int i = 0;
        while (!hasTheProduct) {
            if (!StatusHelper.isStarting()) {
                return false;
            }
            i++;
            if (System.currentTimeMillis() - start > 10 * 60 * 1000) {
                return false;
            }
            if (System.currentTimeMillis() - start > 60 * 1000) {
                productId = productId + getInterval(i);
            }
            hasTheProduct = doDetail(productId);
            DateUtils.sleep(200);
        }
        return true;
    }

    public int getInterval(int i) {
        return i % 3 - 1;
    }
}
