package therealfarfetchd.resourceassassin.hax

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

@IFMLLoadingPlugin.SortingIndex(1001)
class ResourceAssassinPlugin : IFMLLoadingPlugin {

  override fun getModContainerClass(): String? = null

  override fun getASMTransformerClass(): Array<String> = arrayOf("therealfarfetchd.resourceassassin.hax.ResourceAssassinTransformer")

  override fun getSetupClass(): String? = null

  override fun injectData(data: MutableMap<String, Any>?) {}

  override fun getAccessTransformerClass(): String? = null

}