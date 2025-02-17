package club.iananderson.seasonhud.mixin;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

public class SeasonHUDMixinPlugin implements IMixinConfigPlugin {

  private static final boolean HAS_XAERO;
  private static final boolean HAS_MAP_ATLASES;

  static {
    HAS_XAERO = hasClass("xaero.common.HudMod");
    HAS_MAP_ATLASES = hasClass("pepjebs.mapatlases.MapAtlasesMod");
  }

  private String prefix = null;

  public SeasonHUDMixinPlugin() {

  }

  private static boolean hasClass(String modClass) {
    try {
      MixinService.getService().getBytecodeProvider().getClassNode(modClass);
      return true;
    } catch (ClassNotFoundException | IOException e) {
      return false;
    }
  }

  @Override
  public void onLoad(String mixinPackage) {
    prefix = mixinPackage + ".";
  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    Preconditions.checkState(mixinClassName.startsWith(prefix), "Unexpected prefix on " + mixinClassName);
    if (mixinClassName.startsWith("club.iananderson.seasonhud.mixin.xaero")) {
      return HAS_XAERO;
    }
    else if (mixinClassName.startsWith("club.iananderson.seasonhud.mixin.mapatlases")) {
      return HAS_MAP_ATLASES;
    }
    else {
      return true;
    }
  }

  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  public List<String> getMixins() {
    return null;
  }

  public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }
}