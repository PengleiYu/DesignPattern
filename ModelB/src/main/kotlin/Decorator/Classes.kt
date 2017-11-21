package Decorator

import java.util.*

/**
 * Created by yupenglei on 17/11/14.
 */
private class TempDB private constructor() {
    companion object {
        val mapMonthSaleMoney: Map<String, Double> =
                mapOf("Tom" to 1000.0, "Jack" to 2000.0, "Anna" to 3000.0)
    }
}

private abstract class Component {
    abstract fun calcPrize(user: String, begin: Date?, end: Date?): Double
}

private class ConcreteComponent : Component() {
    override fun calcPrize(user: String, begin: Date?, end: Date?): Double = 0.0
}

private abstract class Decorator(val component: Component) : Component() {
    override fun calcPrize(user: String, begin: Date?, end: Date?): Double =
            component.calcPrize(user, begin, end)
}

private class MonthPrizeDecorator(component: Component) : Decorator(component) {
    override fun calcPrize(user: String, begin: Date?, end: Date?): Double {
        val money = super.calcPrize(user, begin, end)
        val prize = 0.03 * (TempDB.mapMonthSaleMoney[user] ?: 0.0)
        println("$user 当月业务奖金: $prize")
        return money + prize
    }
}

private class SumPrizeDecorator(component: Component) : Decorator(component) {
    override fun calcPrize(user: String, begin: Date?, end: Date?): Double {
        val money = super.calcPrize(user, begin, end)
        val prize = 0.001 * 100000.0
        println("$user 累计奖金: $prize")
        return money + prize
    }
}

private class GroupPrizeDecorator(component: Component) : Decorator(component) {
    override fun calcPrize(user: String, begin: Date?, end: Date?): Double {
        val money = super.calcPrize(user, begin, end)
        val prize = TempDB.mapMonthSaleMoney.values.sum() * 0.01
        println("$user 当月团队业务奖金: $prize")
        return money + prize
    }
}

fun main(args: Array<String>) {
    val component1 = ConcreteComponent()
    val decorator1 = MonthPrizeDecorator(component1)
    val decoratorPlayer = SumPrizeDecorator(decorator1)
    val decoratorMaster = GroupPrizeDecorator(decoratorPlayer)

    println("==================Tom prize: ${decoratorPlayer.calcPrize("Tom", null, null)}")
    println("==================Jack prize: ${decoratorPlayer.calcPrize("Jack", null, null)}")
    println("==================Anna prize: ${decoratorMaster.calcPrize("Anna", null, null)}")
}
