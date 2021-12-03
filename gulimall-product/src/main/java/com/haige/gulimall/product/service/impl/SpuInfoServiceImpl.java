package com.haige.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.haige.common.constant.ProductConstant;
import com.haige.common.entity.es.SkuEsModel;
import com.haige.common.entity.to.SkuReductionTo;
import com.haige.common.entity.to.SpuBoundTo;
import com.haige.common.entity.vo.HasStockVo;
import com.haige.common.utils.R;
import com.haige.gulimall.product.dao.SpuImagesDao;
import com.haige.gulimall.product.dao.SpuInfoDescDao;
import com.haige.gulimall.product.entity.*;
import com.haige.gulimall.product.feign.CouponFeignService;
import com.haige.gulimall.product.feign.SearchFeignService;
import com.haige.gulimall.product.feign.WareFeignService;
import com.haige.gulimall.product.service.*;
import com.haige.gulimall.product.vo.spuinfo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService infoDescService;

    @Autowired
    private SpuImagesService imagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService valueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService attrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo spuInfoVo) {

        // 1. 保存SPU基本信息
        // 1.1 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVo,spuInfoEntity);
        this.saveBaseSpuInfo(spuInfoEntity);

        // 1.2 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",spuInfoVo.getDecript()));
        infoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 1.3 pms_spu_images
        List<String> infoVoImages = spuInfoVo.getImages();
        imagesService.saveSpuImages(spuInfoEntity.getId(),infoVoImages);

        // 1.4 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuInfoVo.getBaseAttrs();
        List<ProductAttrValueEntity> valueEntities = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setAttrId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        valueService.saveProductAttr(valueEntities);

        // 3.2 spu商品积分信息 sms_spu_bounds
        Bounds bounds = spuInfoVo.getBounds();
        SpuBoundTo boundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,boundTo);
        boundTo.setSpuId(spuInfoEntity.getId());
        R spuBoundsFeignResult = couponFeignService.saveSpuBounds(boundTo);
        if(spuBoundsFeignResult.getCode()!=0){
            log.error("远程保存spu商品积分信息失败....");
        }

        // 2. 保存SPU对应的所有SKU信息
        // 2.1 pms_sku_info
        List<Skus> skusList = spuInfoVo.getSkus();
        if(!CollectionUtils.isEmpty(skusList)){
            skusList.forEach(skus-> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(skus,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());

                // images
                String defaultImage = "";
                for (Images image : skus.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImage = image.getImgUrl();
                        break;
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // 2.2 pms_sku_images
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntityList = skus.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    // 过滤掉图片路径为空的数据
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveSkuImages(imagesEntityList);

                // 2.3 pms_sku_sale_attr_value
                List<Attr> attrList = skus.getAttr();
                List<SkuSaleAttrValueEntity> attrValueEntityList = attrList.stream().map(attr -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                attrValueService.saveSkuSaleAttrList(attrValueEntityList);

                // 3 跨库 gulimall_sms
                // 需要远程调用

                // 3.1 优惠满减 sms_sku_ladder , sms_sku_full_reduction , sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(skus,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) == 1){
                    R skuReductionFeignResult= couponFeignService.saveSkuReduction(skuReductionTo);
                    if(skuReductionFeignResult.getCode()!=0){
                        log.error("远程保存spu优惠积分信息失败....");
                    }
                }

            });
        }
    }

    /**
     * 保存到表pms_spu_info
     * @param spuInfoEntity
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String status = (String) params.get("status");

        // 需要用and嵌套
        // (key=1 or key like 'a')
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper -> {
                wrapper.eq("id",key).or().like("spu_name",key);
            }));
        }

        // and
        if(!StringUtils.isEmpty(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }

        if(!StringUtils.isEmpty(catelogId)){
            queryWrapper.eq("catalog_id",catelogId);
        }

        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("publish_status",status);
        }


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }


    @Override
    public void up(Long spuId) {

        // 1. 查出当前spuId对应的所有sku信息，品牌的名字等
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 2.4 attrs
        // 查询当前查询的所有可以被用来检索的规格属性，可以放在外面查询
        List<ProductAttrValueEntity> attrValueEntities = valueService.BaseAttrListForSpu(spuId);
        List<Long> attrIds = attrValueEntities.stream().map(attrValueEntity -> {
            return attrValueEntity.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchIds = attrService.selectSearchAttrs(attrIds);
        HashSet<Long> idsSet = new HashSet<>(searchIds);
        List<SkuEsModel.Attrs> attrsList = attrValueEntities.stream().filter(item -> {
            return idsSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        Map<Long, Boolean> hasStockMap = null;
        // 远程调用
        try{
            R skuHasStockRes = wareFeignService.getSkuHasStock(skuIds);
            if(skuHasStockRes.getCode()==0){
                TypeReference<List<HasStockVo>> typeReference = new TypeReference<List<HasStockVo>>() {};
                hasStockMap = skuHasStockRes.getData(typeReference).stream().collect(Collectors.toMap(HasStockVo::getSkuId, HasStockVo::getHasStock));
            }
        }catch (Exception e){
            log.error("库存服务远程调用出现异常...{}",e);
        }

        // 2. 封装每个SKU的信息
        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        List<SkuEsModel> upProducts = skuInfoEntities.stream().map(skuInfoEntity -> {
            // ES的数据格式
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity,skuEsModel);
            // 2.1 skuPrice,skuImg,
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());

            // 2.2 hasStock,hotScore
            if(finalHasStockMap == null){
                skuEsModel.setHasStock(true);
            }else{
                skuEsModel.setHasStock(finalHasStockMap.get(skuInfoEntity.getSkuId()));
            }
            skuEsModel.setHotScore(0L);

            // 2.3 brandName,brandImg,catelogName
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogId(categoryEntity.getCatId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            // 设置检索属性
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());

        // 5. 发送到ES服务进行存储
        R res = searchFeignService.productStatusUp(upProducts);
        if(res.getCode()==0){
            // 6. 修改上架状态
            this.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else{
            // TODO 远程调用失败？接口幂等性

        }

    }

    private void updateSpuStatus(Long spuId, int code) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setPublishStatus(code);
        spuInfoEntity.setId(spuId);
        this.updateById(spuInfoEntity);
    }


}