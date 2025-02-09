package club.iananderson.seasonhud.forge.platform;

import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.Minimap;
import club.iananderson.seasonhud.platform.Services;
import pepjebs.dicemc.util.MapAtlasesAccessUtils;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ForgeMinimapHelper implements IMinimapHelper {
  // Needed for older versions. Makes it easier to port.
  @Override
  public boolean hideMapAtlases() {
    if (CurrentMinimap.mapAtlasesLoaded()) {
      Minecraft mc = Minecraft.getInstance();

      if (mc.level == null || mc.player == null) {
        return true;
      }

      ItemStack atlas = MapAtlasesAccessUtils.getAtlasFromPlayerByConfig(mc.player.inventory);

      boolean drawMinimapHud = pepjebs.dicemc.config.Config.DRAW_MINIMAP_HUD.get();

      boolean hasAtlas = atlas.getCount() > 0;

      return !drawMinimapHud || !hasAtlas;
    }
    else {
      return false;
    }
  }
}
