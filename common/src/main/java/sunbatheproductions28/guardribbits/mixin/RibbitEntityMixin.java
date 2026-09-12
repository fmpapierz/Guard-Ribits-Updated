package sunbatheproductions28.guardribbits.mixin;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunbatheproductions28.guardribbits.entity.GuardRibbitEntity;

import java.util.List;

/**
 * Sends nearby Guard Ribbits after whatever just attacked a Ribbit.
 * <p>
 * Ribbits does not override the damage entry point itself, so the hook sits on
 * {@link LivingEntity} and filters for Ribbits.
 */
@Mixin(LivingEntity.class)
public abstract class RibbitEntityMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void guardribbits$alertGuards(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof RibbitEntity ribbit)) return;

        Entity attackerEntity = source.getEntity();
        if (attackerEntity instanceof LivingEntity attacker) {
            alertOthers(ribbit, attacker, level);
        }
    }

    private static void alertOthers(RibbitEntity ribbit, LivingEntity attacker, ServerLevel serverLevel) {
        double followDistance = ribbit.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB alertBox = AABB.unitCubeFromLowerCorner(ribbit.position()).inflate(followDistance, 10.0D, followDistance);

        List<GuardRibbitEntity> guards = serverLevel.getEntitiesOfClass(
                GuardRibbitEntity.class,
                alertBox,
                EntitySelector.NO_SPECTATORS
        );

        for (GuardRibbitEntity guard : guards) {
            if (guard.getTarget() == null && !guard.isAlliedTo(attacker) && guard.defendsRibbitAgainst(attacker)) {
                guard.setTarget(attacker);
                guard.setPersistentAngerTarget(EntityReference.of(attacker));
                guard.startPersistentAngerTimer();
            }
        }
    }
}
