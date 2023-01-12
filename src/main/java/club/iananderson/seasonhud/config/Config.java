package club.iananderson.seasonhud.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {


    //Config Builder
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        setupConfig(BUILDER);
        GENERAL_SPEC = BUILDER.build();
    }
    public enum Location{
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
    public static ForgeConfigSpec.BooleanValue enableMod;
    public static ForgeConfigSpec.ConfigValue<Integer> hudX;
    public static ForgeConfigSpec.ConfigValue<Integer> hudY;
    public static ForgeConfigSpec.ConfigValue<Location> hudLocation;
    public static ForgeConfigSpec.BooleanValue showSubSeason;
    public static ForgeConfigSpec.BooleanValue showDay;


    private static void setupConfig(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("Configs for SeasonHUD");
        BUILDER.push("HUD:");

        enableMod = BUILDER
                .comment("Enable the mod? \n (true/false)")
                .define("enable_mod",true);

        hudX = BUILDER
                .comment("The horizontal offset of the HUD when no minimap is installed (in pixels)\n Default is 0")
                .define("hud_x_position",0);

        hudY = BUILDER
                .comment("The vertical offset of the HUD when no minimap is installed (in pixels)\n Default is 0")
                .define("hud_y_position",0);

        hudLocation = BUILDER
                .comment("Part of the screen to display the HUD when no minimap is installed\n Default is TOP_LEFT")
                .defineEnum("hud_location",Location.TOP_LEFT);

        showSubSeason = BUILDER
                .comment("Show sub-season (i.e. Early Winter, Late Autumn) instead of basic season? \n (true/false)")
                .define("enable_show_sub_season",true);

        showDay = BUILDER
                .comment("Show the current day of the season/sub-season? \n (true/false)")
                .define("enable_show_day",true);

        BUILDER.pop();
        BUILDER.pop();
    }

    public static void setShowSubSeason(boolean showSubSeason) {
        Config.showSubSeason.set(showSubSeason);
    }
    public static void setShowDay(boolean showDay) {
        Config.showDay.set(showDay);
    }
    public static void setHudX(int hudX) {
        Config.hudX.set(hudX);
    }
    public static void setHudY(int hudY) {
        Config.hudX.set(hudY);
    }
    public static void setHudLocation(Location hudLocation) {
        Config.hudLocation.set(hudLocation);
    }


    public static void setEnableMod(boolean enableMod) {
        Config.enableMod.set(enableMod);
    }
}
