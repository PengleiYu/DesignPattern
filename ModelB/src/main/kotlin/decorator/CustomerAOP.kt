package decorator

import java.text.SimpleDateFormat
import java.util.*

/**
 * 使用装饰者模式模拟面向切面编程
 * Created by yupenglei on 17/11/15.
 */
/**
 * 销售单数据
 */
private data class SaleModel(val goods: String, val saleNum: Int) {
    override fun toString(): String = "商品名称=$goods, 购买数量=$saleNum"
}

/**
 * 销售业务接口
 */
private interface GoodsSaleEbi {
    fun sale(user: String, customer: String, saleModel: SaleModel): Boolean
}

/**
 * 基本业务对象
 */
private class GoodsSaleEbo : GoodsSaleEbi {
    override fun sale(user: String, customer: String, saleModel: SaleModel): Boolean {
        println("$user 保存了 $customer 购买 $saleModel 的销售数据")
        return true
    }
}

/**
 * 装饰器接口
 */
private abstract class AbstractDecorator(protected val ebi: GoodsSaleEbi) : GoodsSaleEbi

/**
 * 权限检查装饰器
 */
private class CheckDecorator(ebi: GoodsSaleEbi) : AbstractDecorator(ebi) {
    override fun sale(user: String, customer: String, saleModel: SaleModel): Boolean {
        return if (user != "Tom") {
            println("对不起，$user，你没有保存销售单的权限")
            false
        } else ebi.sale(user, customer, saleModel)
    }
}

/**
 * 日志记录装饰器
 */
private class LogDecorator(ebi: GoodsSaleEbi) : AbstractDecorator(ebi) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")

    override fun sale(user: String, customer: String, saleModel: SaleModel): Boolean {
        return ebi.sale(user, customer, saleModel).apply {
            println("日志记录：$user 于 ${dateFormat.format(Date())} 时保存了一条销售记录，"
                    + "客户是 $customer，购买记录是 $saleModel")
        }
    }
}

fun main(args: Array<String>) {
    val ebi = CheckDecorator(LogDecorator(GoodsSaleEbo()))
    val saleModel = SaleModel("Moto", 2)

    ebi.sale("Tom", "Anna", saleModel)
    ebi.sale("Jack", "Lucy", saleModel)
}
