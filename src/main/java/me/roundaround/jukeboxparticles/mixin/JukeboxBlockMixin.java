package me.roundaround.jukeboxparticles.mixin;

import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin extends BlockWithEntity {
  private long lastParticleTick = 0;

  protected JukeboxBlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    if (!world.isClient || !state.get(JukeboxBlock.HAS_RECORD).booleanValue()) {
      return super.getTicker(world, state, type);
    }

    return JukeboxBlock.checkType(type, BlockEntityType.JUKEBOX, this::playingClientTick);
  }

  public void playingClientTick(World world, BlockPos pos, BlockState state, JukeboxBlockEntity jukebox) {
    long tick = world.getTime();

    // Based on 20 ticks/second
    if (tick - lastParticleTick >= 20) {
      Collection<Integer> possibleNotes = Properties.NOTE.getValues();
      int tone = List.copyOf(possibleNotes).get(world.random.nextInt(possibleNotes.size()));
      world.addParticle(
          ParticleTypes.NOTE,
          pos.getX() + 0.5,
          pos.getY() + 1.2,
          pos.getZ() + 0.5,
          tone / 24.0,
          0,
          0);

      lastParticleTick = tick;
    }
  }
}
