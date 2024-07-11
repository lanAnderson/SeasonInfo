package club.iananderson.seasonhud.client.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SeasonHUDOverlay implements IGuiOverlay {
  public static SeasonHUDOverlay HUD_INSTANCE;

  public static void init() {
    HUD_INSTANCE = new SeasonHUDOverlay();
  }

  public void render(ForgeGui gui, PoseStack graphics, float partialTick, int screenWidth, int screenHeight) {
    SeasonHUDOverlayCommon.render(graphics);
  }
}
