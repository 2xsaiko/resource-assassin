package therealfarfetchd.resourceassassin

import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.play.client.CPacketResourcePackStatus
import net.minecraft.network.play.server.SPacketResourcePackSend
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager

const val ModID = "resourceassassin"

@Mod(modid = ModID, useMetadata = true, clientSideOnly = true, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object ResourceAssassin {

  var coreModInitialized = false

  val Logger = LogManager.getLogger(ModID)

  @Mod.EventHandler
  fun preInit(e: FMLPreInitializationEvent) {
    Logger.info("lmao")

    if (!coreModInitialized) {
      Logger.fatal("Coremod didn't load! WTF!")
      error("Coremod didn't load! WTF!")
    }
  }

  @JvmStatic
  fun NetHandlerPlayClient.handleResourcePack(packet: SPacketResourcePackSend) {
    networkManager.sendPacket(CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED))
  }

}