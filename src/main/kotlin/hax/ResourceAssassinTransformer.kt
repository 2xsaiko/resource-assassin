package therealfarfetchd.resourceassassin.hax

import net.minecraft.launchwrapper.IClassTransformer
import net.minecraft.launchwrapper.Launch
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*
import therealfarfetchd.resourceassassin.ResourceAssassin

class ResourceAssassinTransformer : IClassTransformer {

  val isDeobf = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean

  fun getObf(obf: String, deobf: String): String = if (!isDeobf) obf else deobf

  val handleResourcePack = getObf(
    "func_175095_a(Lnet/minecraft/network/play/server/SPacketResourcePackSend;)V",
    "handleResourcePack(Lnet/minecraft/network/play/server/SPacketResourcePackSend;)V")

  override fun transform(name: String, transformedName: String, basicClass: ByteArray): ByteArray =
    try {
      when (transformedName) {
        "net.minecraft.client.network.NetHandlerPlayClient" -> transformNetHandler(basicClass)
        else -> basicClass
      }
    } catch (e: Exception) {
      e.printStackTrace()
      basicClass
    }

  fun transformNetHandler(bytes: ByteArray): ByteArray {
    ResourceAssassin.Logger.info("Transforming 'net.minecraft.client.network.NetHandlerPlayClient'!")
    ResourceAssassin.Logger.info("Running in ${if (isDeobf) "de" else ""}obfuscated environment.")

    val cr = ClassReader(bytes)
    val cw = ClassWriter(0)
    val cn = ClassNode()

    cr.accept(cn, 0)

    val method = cn.methods.first { handleResourcePack == it.name + it.desc }
    ResourceAssassin.Logger.info("Transforming '${method.name}${method.desc}'!")

    val list = InsnList()

    val Names = object {
      val NetHandlerPlayClient = "net/minecraft/client/network/NetHandlerPlayClient"
      val SPacketResourcePackSend = "net/minecraft/network/play/server/SPacketResourcePackSend"
    }

    listOf(
      VarInsnNode(ALOAD, 0),
      VarInsnNode(ALOAD, 1),
      MethodInsnNode(INVOKESTATIC, "therealfarfetchd/resourceassassin/ResourceAssassin", "handleResourcePack", "(L${Names.NetHandlerPlayClient};L${Names.SPacketResourcePackSend};)V", false),
      InsnNode(RETURN)
    ).forEach(list::add)

    method.instructions.insert(list)

    cn.accept(cw)

    ResourceAssassin.coreModInitialized = true

    return cw.toByteArray()
  }

}