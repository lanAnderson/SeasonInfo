package club.iananderson.seasonhud;

import static club.iananderson.seasonhud.Common.LOG;

import club.iananderson.seasonhud.config.Config;
import fuzs.forgeconfigapiport.forge.api.neoforge.v4.NeoForgeConfigRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Common.MOD_ID)
public class SeasonHUD {

  public SeasonHUD() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    MinecraftForge.EVENT_BUS.register(this);

    //modEventBus.addListener(this::enqueue);
    modEventBus.addListener(this::commonSetup);

    Common.init();

    NeoForgeConfigRegistry.INSTANCE.register(ModConfig.Type.CLIENT, Config.GENERAL_SPEC, "SeasonHUD-client.toml");
  }

    public void enqueue(InterModEnqueueEvent event) {
//        LOG.info("Talking to Curios");
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm")
//                .size(1)
//                .build());
    }

  private void commonSetup(final FMLCommonSetupEvent event) {
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
  }

  @Mod.EventBusSubscriber(modid = Common.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }
  }
}