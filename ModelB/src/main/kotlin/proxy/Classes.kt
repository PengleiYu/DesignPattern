package proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by yupenglei on 17/11/21.
 */
private interface OrderApi

private class Order : OrderApi

private class DynamicProxy private constructor() : InvocationHandler {
    private lateinit var order: OrderApi

    fun getProxyInterface(order: Order): OrderApi {
        this.order = order
        return Proxy.newProxyInstance(order.javaClass.classLoader,
                order.javaClass.interfaces, this) as OrderApi
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any =
            method.invoke(this, args)
}
