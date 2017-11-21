package composite

/**
 * 组合模式
 *
 * 优点:
 * 1, 定义 了 包含 基本 对象 和 组合 对象 的 类 层次 结构
 * 2, 统一 了 组合 对象 和 叶子 对象
 * 3, 简化 了 客户 端
 * 4, 更容易 扩展
 * 缺点:
 * 是很难限制组合中的组件类型。
 * 这在需要检测组件类型的时候，使得我们不能依靠编译期的类型约束来完成，必须在运行期间动态检测。
 * 总结：用于实现树形结构，提供统一API
 * Created by yupenglei on 17/11/10.
 */
private abstract class Component(val name: String) {
    private val error = { throw UnsupportedOperationException("对象不支持这个功能") }

    var parent: Component? = null
    var path = mutableListOf<String>()

    abstract fun printStruct(preStr: String)
    open fun addChild(child: Component): Unit = error()
    open fun removeChild(child: Component): Unit = error()
    open fun getChildAt(index: Int): Component = error()
}

private class Leaf(name: String) : Component(name) {
    override fun printStruct(preStr: String) {
        println("$preStr-$name")
    }
}

private class Composite(name: String) : Component(name) {
    /**
     *
     */
    val children by lazy { mutableListOf<Component>() }

    override fun addChild(child: Component) {
        children.add(child)
        child.parent = this

        //root
        if (path.isEmpty()) path.add(name)
        //已添加过
        if (path.contains(child.name)) throw IllegalArgumentException("本路径上，组件${child.name}已经添加过了")
        else {
            child.path.addAll(path)
            child.path.add(child.name)
        }
    }

    override fun removeChild(child: Component) {
        val index = children.indexOf(child)
        if (index != -1) {
            if (child is Composite)
                for (c in child.children) {
                    c.parent = this
                    children.add(c)
                }
            children.removeAt(index)
        }
    }

    override fun printStruct(preStr: String) {
        println("$preStr-$name")
        val s = "$preStr "
        for (child in children) child.printStruct(s)
    }
}

fun main(args: Array<String>) {
    val root: Component = Composite("服装")
    val c1: Component = Composite("男装")
    val c2: Component = Composite("女装")

    val leaf1: Component = Leaf("夹克")
    val leaf2: Component = Leaf("衬衣")
    val leaf3: Component = Leaf("裙子")
    val leaf4: Component = Leaf("套装")

    root.addChild(c1)
    root.addChild(c2)

    c1.addChild(leaf1)
    c1.addChild(leaf2)
    c2.addChild(leaf3)
    c2.addChild(leaf4)

//    c1.addChild(root)
//    leaf4.addChild(c1)
    c1.addChild(c2)

//    root.printStruct("")
//
//    root.removeChild(c1)
//    root.printStruct("")
}
