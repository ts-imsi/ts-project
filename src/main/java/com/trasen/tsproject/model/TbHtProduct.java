package com.trasen.tsproject.model;

import javax.persistence.*;

@Table(name = "tb_ht_product")
public class TbHtProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkid;

    /**
     * 合同编号
     */
    @Column(name = "ht_no")
    private String htNo;

    /**
     * 产品ID
     */
    @Column(name = "product_id")
    private String productId;

    /**
     * 产值
     */
    @Column(name = "output_value")
    private String outputValue;

    /**
     * 部门id
     */
    @Column(name = "dep_id")
    private String depId;

    /**
     * 小计
     */
    private Double subtotal;

    /**
     * 分解
     */
    private String decompose;

    /**
     * @return pkid
     */
    public Integer getPkid() {
        return pkid;
    }

    /**
     * @param pkid
     */
    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    /**
     * 获取合同编号
     *
     * @return ht_no - 合同编号
     */
    public String getHtNo() {
        return htNo;
    }

    /**
     * 设置合同编号
     *
     * @param htNo 合同编号
     */
    public void setHtNo(String htNo) {
        this.htNo = htNo;
    }

    /**
     * 获取产品ID
     *
     * @return product_id - 产品ID
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 设置产品ID
     *
     * @param productId 产品ID
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 获取产值
     *
     * @return output_value - 产值
     */
    public String getOutputValue() {
        return outputValue;
    }

    /**
     * 设置产值
     *
     * @param outputValue 产值
     */
    public void setOutputValue(String outputValue) {
        this.outputValue = outputValue;
    }

    /**
     * 获取部门id
     *
     * @return dep_id - 部门id
     */
    public String getDepId() {
        return depId;
    }

    /**
     * 设置部门id
     *
     * @param depId 部门id
     */
    public void setDepId(String depId) {
        this.depId = depId;
    }

    /**
     * 获取小计
     *
     * @return subtotal - 小计
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * 设置小计
     *
     * @param subtotal 小计
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * 获取分解
     *
     * @return decompose - 分解
     */
    public String getDecompose() {
        return decompose;
    }

    /**
     * 设置分解
     *
     * @param decompose 分解
     */
    public void setDecompose(String decompose) {
        this.decompose = decompose;
    }
}