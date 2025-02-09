package club.iananderson.seasonhud.forge.platform;

import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;

public class ForgeMinimapHelper implements IMinimapHelper {
  // Needed for older versions. Makes it easier to port.
  @Override
  public boolean hideMapAtlases() {
    return false;
  }
}
