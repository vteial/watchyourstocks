package io.wybis.watchyourstocks.model

import groovy.transform.Canonical
import groovy.transform.ToString
import io.wybis.watchyourstocks.dto.SessionDto

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = 'account')
@Canonical
@ToString(includeNames = true)
class Account extends AbstractModel {

    static final String ID_KEY = 'accountId'

    @Id
    @Column(name = 'id')
    long id;

    @Column(name = 'name')
    String name;

    @Column(name = 'alias_name')
    String aliasName;

    @Column(name = 'type')
    String type

    @Column(name = 'is_minus')
    boolean isMinus;

    @Column(name = 'balance')
    double amount;

    @Column(name = 'amount_virtual_buy')
    double amountVirtualBuy;

    @Column(name = 'amount_virtual_sell')
    double amountVirtualSell;

    @Column(name = 'status')
    String status;

    @Column(name = 'hand_stock')
    double handStock

    @Column(name = 'hand_stock_move')
    double handStockMove

    @Column(name = 'virtual_stock_buy')
    double virtualStockBuy

    @Column(name = 'virtual_stock_sell')
    double virtualStockSell

    @Column(name = 'available_stock')
    double availableStock

    @Column(name = 'product_id')
    long productId

    transient Product product

    @Column(name = 'user_id')
    long userId

    transient User user

    @Column(name = 'branch_id')
    long branchId

    transient Branch branch

    // common fields...
    @Column(name = 'create_time', nullable = false)
    Date createTime;

    @Column(name = 'update_time', nullable = false)
    Date updateTime;

    @Column(name = 'create_by', nullable = false)
    String createBy;

    @Column(name = 'update_by', nullable = false)
    String updateBy;

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
    boolean hasSufficientBalance(double amount) {

        if (this.isMinus) {
            return true
        }

        return this.amount >= amount
    }

    void withdraw(double unit) {
        this.amount -= unit
    }

    void deposit(double unit) {
        this.amount += unit
    }

    boolean hasSufficientHandStock(double unit) {
        return this.isMinus ? true : unit <= this.handStock
    }

    void withdrawHandStock(double unit) {
        this.handStock -= unit
        this.computeAmount();
        this.product.withdrawHandStock(unit)
    }

    void depositHandStock(double unit) {
        this.handStock += unit
        this.computeAmount();
        this.product.depositHandStock(unit)
    }

    boolean hasSufficientHandStockMove(double unit) {
        return this.isMinus ? true : unit <= this.handStockMove
    }

    void withdrawHandStockMove(double unit) {
        this.handStockMove -= unit
        this.computeAmount();
        //this.product.withdrawHandStockMove(unit)
    }

    void depositHandStockMove(double unit) {
        this.handStockMove += unit
        this.computeAmount();
        //this.product.depositHandStockMove(unit)
    }

    void computeAmount() {
        double value = (this.product.handStockAverage / this.product.baseUnit);
        value = (this.handStock + this.handStockMove) * value;
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
        this.product.withdrawVirtualStockBuy(unit)
    }

    void depositVirtualStockBuy(double unit) {
        this.virtualStockBuy += unit
        this.product.depositVirtualStockBuy(unit)
    }

    void withdrawVirtualStockSell(double unit) {
        this.virtualStockSell -= unit
        this.product.withdrawVirtualStockSell(unit)
    }

    void depositVirtualStockSell(double unit) {
        this.virtualStockSell += unit
        this.product.depositVirtualStockSell(unit)
    }

    void computeAvailableStock() {
        this.availableStock = this.getVirtualStock() + (this.handStock + this.handStockMove)
        this.product.computeAvailableStock()
    }
}
