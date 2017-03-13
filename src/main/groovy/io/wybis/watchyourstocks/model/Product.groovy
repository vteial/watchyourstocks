package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'product')
@Canonical
@ToString(includeNames = true)
class Product extends AbstractModel {

    static final String ID_KEY = "productId"

    @Id
    @Column(name = 'id')
    long id

    @Column(name = 'type')
    String type

    @Column(name = 'code')
    String code

    @Column(name = 'name')
    String name

    @Column(name = 'base_unit')
    int baseUnit

    @Column(name = 'denominator')
    int denominator

    @Column(name = 'buy_rate')
    double buyRate

    @Column(name = 'buy_percent')
    double buyPercent

    @Column(name = 'sell_rate')
    double sellRate

    @Column(name = 'sell_percent')
    double sellPercent

    @Column(name = 'amount')
    double amount;

    @Column(name = 'hand_stock')
    double handStock

    @Column(name = 'hand_stock_average')
    double handStockAverage

    transient double handStockValue

    @Column(name = 'virtual_stock_buy')
    double virtualStockBuy

    @Column(name = 'virtual_stock_average')
    double virtualStockAverage

    @Column(name = 'virtual_stock_sell')
    double virtualStockSell

    @Column(name = 'available_stock')
    double availableStock

    @Column(name = 'available_stock_average')
    double availableStockAverage

    @Column(name = 'status')
    String status

    @Column(name = 'branch_id')
    long branchId

    transient Branch branch

    // common fields...
    @Column(name = 'create_time', nullable = false)
    Date createTime;

    @Column(name = 'update_time', nullable = false)
    Date updateTime;

    @Column(name = 'create_by', nullable = false)
    long createBy;

    @Column(name = 'update_by', nullable = false)
    long updateBy;

    // persistence operations
    public void preUpdate(SessionDto sessionUser, Date now) {
        this.updateTime = now
        this.updateBy = sessionUser.id
    }

    public void prePersist(SessionDto sessionUser, Date now) {
        this.createTime = now
        this.updateTime = now
        this.createBy = sessionUser.id
        this.updateBy = sessionUser.id
    }

    // domain operations
    double getBuyPercentageRate() {
        double value = this.buyRate * (this.buyPercent / 100)
        value = this.buyRate + value
        return value
    }

    double getSellPercentageRate() {
        double value = this.sellRate * (this.sellPercent / 100)
        value = this.sellRate - value
        return value
    }

    boolean isRateIsAllowableForBuy(double rate) {
        double bpr = this.getBuyPercentageRate()
        return rate <= bpr
    }

    boolean isRateIsAllowableForSell(double rate) {
        double spr = this.getSellPercentageRate()
        return rate >= spr
    }

    //	boolean hasSufficientAmount(double amount) {
    //		return amount <= this.amount
    //	}
    //
    //	void withdrawAmount(double amount) {
    //		this.amount -= amount
    //	}
    //
    //	void depositAmount(double amount) {
    //		this.amount += amount
    //	}

    boolean hasSufficientHandStock(double unit) {
        return unit <= this.handStock
    }

    void withdrawHandStock(double unit) {
        this.handStock -= unit
        this.computeAmount();
    }

    void depositHandStock(double unit) {
        this.handStock += unit
        this.computeAmount();
    }

    void computeAmount() {
        double value = (this.handStockAverage / this.baseUnit);
        value = this.handStock * value;
        this.amount = value;
    }

    double getVirtualStock() {
        return this.virtualStockBuy - this.virtualStockSell
    }

    boolean hasSufficientVirtualStockBuy(double unit) {
        return unit <= this.virtualStockBuy
    }

    boolean hasSufficientVirtualStockSell(double unit) {
        return unit <= this.virtualStockSell
    }

    void withdrawVirtualStockBuy(double unit) {
        this.virtualStockBuy -= unit
    }

    void depositVirtualStockBuy(double unit) {
        this.virtualStockBuy += unit
    }

    void withdrawVirtualStockSell(double unit) {
        this.virtualStockSell -= unit
    }

    void depositVirtualStockSell(double unit) {
        this.virtualStockSell += unit
    }

    void computeHandStockValue() {
        if (this.handStockAverage > 0) {
            this.handStockValue = this.handStock * (this.handStockAverage / this.baseUnit)
        }
    }

    void computeAvailableStock() {
        this.availableStock = this.getVirtualStock() + this.handStock
    }

    void computeHandStockAverage(double unit, double rate) {
        // println("unit = " + unit)
        // println("rate = " + rate)
        // println("total = " + this.handStock)
        double value1 = this.handStock * (this.handStockAverage / this.baseUnit)
        // println("value1 = " + value1)
        double value2 = unit * (rate / this.baseUnit)
        // println("value2 = " + value2)
        double value3 = value1 + value2
        // println("value3 = " + value3)
        double value4 = this.handStock + unit
        // println("value4 = " + value4)
        double value5 = (value3 / value4) * this.baseUnit
        // println("value5 = " + value5)
        this.handStockAverage = value5
    }

    void revertHandStockAverage(double unit, double rate) {
        double hst = this.handStock - unit
        if (hst > 0) {
            // println("unit = " + unit)
            // println("rate = " + rate)
            // println("total = " + this.handStock)
            double value1 = this.handStock * (this.handStockAverage / this.baseUnit)
            // println("value1 = " + value1)
            double value2 = unit * (rate / this.baseUnit)
            // println("value2 = " + value2)
            double value3 = value1 - value2
            // println("value3 = " + value3)
            double value4 = this.getHandStock - unit
            // println("value4 = " + value4)
            double value5 = (value3 / value4) * this.baseUnit
            // println("value5 = " + value5)
            this.handStockAverage = value5
        }
    }

    void computeVirtualStockAverage(double unit, double rate) {
        // println("unit = " + unit)
        // println("rate = " + rate)
        // println("stockBuy = this.virtualStockBuy)
        double value1 = this.virtualStockBuy * (this.virtualStockAverage / this.baseUnit)
        // println("value1 = " + value1)
        double value2 = unit * (rate / this.baseUnit)
        // println("value2 = " + value3)
        double value3 = value1 + value2
        // println("value3 = " + value3)
        double value4 = this.virtualStockBuy + unit
        // println("value4 = " + value4)
        double value5 = (value3 / value4) * this.baseUnit
        // println("value5 = " + value5)
        this.virtualStockAverage = value5
    }

    void revertVirtualStockAverage(double unit, double rate) {
        double vsb = this.virtualStockBuy - unit
        if (vsb != 0) {
            // println("unit = " + unit)
            // println("rate = " + rate)
            // println("stockBuy = this.virtualStockBuy)
            double value1 = this.virtualStockBuy * (this.virtualStockAverage / this.baseUnit)
            // println("value1 = " + value1)
            double value2 = unit * (rate / this.baseUnit)
            // println("value2 = " + value2)
            double value3 = value1 - value2
            // println("value3 = " + value3)
            double value4 = this.virtualStockBuy - unit
            // println("value4 = " + value4)
            double value5 = (value3 / value4) * this.baseUnit
            // println("value5 = " + value5)
            this.virtualStockAverage = value5
        }
    }

    void computeAvailableStockAverage(double rate) {
        double value1 = this.virtualStockBuy * (this.virtualStockAverage / this.baseUnit)
        // println("value1 = " + value1)
        double value2 = this.handStock * (this.handStockAverage / this.baseUnit)
        // println("value2 = " + value2)
        double value3 = value1 + value2
        // println("value3 = " + value3)
        double value4 = this.virtualStockBuy + this.handStock
        // println("value4 = " + value4)
        double value5 = (value3 / value4) * this.baseUnit
        // println("value5 = " + value5)
        this.availableStockAverage = Double.isNaN(value5) ? 0 : value5
    }
}
