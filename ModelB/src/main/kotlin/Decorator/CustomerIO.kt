package Decorator

import java.io.*

/**
 * Created by yupenglei on 17/11/15.
 */

/**
 * 装饰者模式
 *
 * 没有继承装饰者父类，直接继承组件类
 * write和flush的行为与其他装饰者不一致，在[write2]中导致未写入文件
 */
private class EncryptOutputStream(private val outputStream: OutputStream) : OutputStream() {
    override fun write(b: Int) {
        val a = b + 2
        val c = if (a >= (97 + 26)) a - 26 else a
        outputStream.write(c)
    }
}

/**
 * 装饰者模式
 *
 * 继承装饰者组件
 * 行为与其他装饰者一致可随意替换
 */
private class EncryptOutputStream2(outputStream: OutputStream) : FilterOutputStream(outputStream) {
    override fun write(b: Int) {
        val a = b + 2
        val c = if (a >= (97 + 26)) a - 26 else a
        super.write(c)
    }
}

private val file = File("Encrypt.txt")

private fun write1() {
    if (file.exists()) file.delete()
    val dataOutput = DataOutputStream(BufferedOutputStream(EncryptOutputStream(FileOutputStream(file))))
    dataOutput.write("abcxyz".toByteArray())
    dataOutput.close()
}

private fun write2() {
    if (file.exists()) file.delete()
    val dataOutput = DataOutputStream(EncryptOutputStream2(BufferedOutputStream(FileOutputStream(file))))
    dataOutput.write("abcxyz".toByteArray())
    dataOutput.close()
}

fun main(args: Array<String>) {
    write2()
}
