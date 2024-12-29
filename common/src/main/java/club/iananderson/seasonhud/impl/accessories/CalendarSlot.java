package club.iananderson.seasonhud.impl.accessories;

public class CalendarSlot {//implements UniqueSlotHandling.RegistrationCallback {
//  private final ResourceLocation slotPredicate = Common.location("calendar_slot_equipment");
//
//  public static final CalendarSlot INSTANCE = new CalendarSlot();
//
//  private CalendarSlot() {
//  }
//
//  public void init() {
//    UniqueSlotHandling.EVENT.register(this);
//
//    if (Services.PLATFORM.getModVersion("accessories").startsWith("1.1.0") || Services.PLATFORM.getModVersion(
//        "accessories").startsWith("1.0.0")) {
//      AccessoriesAPI.registerPredicate(slotPredicate,
//                                       SlotBasedPredicate.ofItem(item -> item.equals(Services.SEASON.calendar())));
//    } else {
//      SlotPredicateRegistry.register(slotPredicate, SlotBasedPredicate.ofItem(
//          item -> item.equals(Services.SEASON.calendar())));
//    }
//  }
//
//  private static SlotTypeReference calendarSlotGetter;
//
//  @Override
//  public void registerSlots(UniqueSlotBuilderFactory factory) {
//    calendarSlotGetter = factory.create(Common.location("calendarslot"), 1)
//                                .slotPredicates(slotPredicate)
//                                .validTypes(EntityType.PLAYER)
//                                .build();
//  }
//
//  @Nullable
//  public static SlotTypeReference calendarSlotRef() {
//    return calendarSlotGetter;
//  }
}
