package sunbatheproductions28.guardribbits.entity;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.monster.zombie.Zombie;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class GuardRibbitEntity extends AgeableMob implements GeoEntity, NeutralMob {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // The shipped model only defines idle and walk; asking for an animation it does not have
    // throws at render time.
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<Boolean> PATROLLING =
            SynchedEntityData.defineId(GuardRibbitEntity.class, EntityDataSerializers.BOOLEAN);

    private int guardAnimationTick = 0;
    private int attackAnimationTick = 0;
    private long persistentAngerEndTime = -1L;
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;

    public GuardRibbitEntity(EntityType<GuardRibbitEntity> entityType, Level level) {
        super(entityType, level);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Movement and patrol goals
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true)); // Attack mobs effectively
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D)); // Adjust speed
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Targeting goals
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers()); // Retaliation
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(6, new ResetUniversalAngerTargetGoal<>(this, false));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PolarBear.class, 12.0F, 1.0D, 1.5D));
    }

    public static AttributeSupplier.Builder createGuardRibbitAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
            if (this.attackAnimationTick == 10) { // Trigger attack at a specific tick, e.g., 10
                this.performAttack();
            }
        }

        if (this.guardAnimationTick > 0) {
            this.guardAnimationTick--;
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.knockback(
                    0.5D,
                    Math.sin(this.getYRot() * Math.PI / 180),
                    -Math.cos(this.getYRot() * Math.PI / 180),
                    this.damageSources().mobAttack(this),
                    (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
        return super.doHurtTarget(level, target);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setPatrolling(valueInput.getBooleanOr("Patrolling", false));
        this.readPersistentAngerSaveData(this.level(), valueInput);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("Patrolling", this.isPatrolling());
        this.addPersistentAngerSaveData(valueOutput);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GuardRibbitEntity>("controller", 10, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationTest<E> state) {
        AnimationController<E> controller = state.controller();

        if (state.isMoving()) {
            controller.setAnimation(WALK);
        } else {
            controller.setAnimation(IDLE);
        }

        return PlayState.CONTINUE;
    }

    private boolean isAttacking() {
        return this.getTarget() != null && this.attackAnim > 0; // Ensure the entity is actively attacking
    }

    private boolean isGuarding() {
        // Simplified logic for guarding: checking for nearby mobs
        return this.getTarget() == null && !this.level().getEntitiesOfClass(Mob.class,
                this.getBoundingBox().inflate(10), mob -> mob != this).isEmpty();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PATROLLING, false); // Initialize patrolling as false
    }

    public boolean isPatrolling() {
        return this.entityData.get(PATROLLING);
    }

    public void setPatrolling(boolean patrolling) {
        this.entityData.set(PATROLLING, patrolling);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        Entity attacker = source.getEntity();

        if (attacker instanceof LivingEntity && this.isFacing(attacker)) {
            // Play guarding animation
            this.level().broadcastEntityEvent(this, (byte) 5); // Custom guarding animation event

            // Reduce damage for guarding
            return super.hurtServer(level, source, damage * 0.25F); // Takes only 25% damage when guarding
        }

        // Normal damage otherwise
        return super.hurtServer(level, source, damage);
    }

    /**
     * Whether this guard retaliates on a Ribbit's behalf against the given attacker. Players count:
     * the guards exist to punish anything that harms a Ribbit. Ribbits and other guards do not, so
     * they never turn on each other.
     */
    public boolean defendsRibbitAgainst(@Nullable LivingEntity attacker) {
        if (attacker == null || attacker == this) {
            return false;
        }
        return !(attacker instanceof GuardRibbitEntity) && !(attacker instanceof RibbitEntity);
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.persistentAngerEndTime = endTime;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    private boolean isFacing(Entity attacker) {
        // Calculate angle between attacker and the Guard Ribbit
        double deltaX = attacker.getX() - this.getX();
        double deltaZ = attacker.getZ() - this.getZ();
        double attackerAngle = Math.toDegrees(Math.atan2(deltaZ, deltaX)); // Angle of the attacker
        double ribbitAngle = this.getYRot() % 360.0; // Ribbit's current yaw

        // Normalize angles to [0, 360)
        attackerAngle = (attackerAngle + 360.0) % 360.0;
        ribbitAngle = (ribbitAngle + 360.0) % 360.0;

        // Check if the attacker's angle is within a ~90 degree cone in front of the Guard Ribbit
        double angleDifference = Math.abs(attackerAngle - ribbitAngle);
        return angleDifference <= 45.0 || angleDifference >= 315.0;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) { // Attack animation
            this.attackAnimationTick = 20;
        } else if (id == 5) { // Guarding animation
            this.guardAnimationTick = 20; // Duration for the guarding animation
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void performAttack() {
        LivingEntity target = this.getTarget();
        if (target != null
                && this.level() instanceof ServerLevel serverLevel
                && this.distanceToSqr(target) < this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F) {
            this.doHurtTarget(serverLevel, target);
        }
    }
}
