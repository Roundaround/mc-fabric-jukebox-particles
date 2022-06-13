package me.roundaround.jukeboxparticles.mixin;

import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(Block.class)
public abstract class BlockMixin {
  @Inject(method = "randomDisplayTick", at = @At(value = "HEAD"), cancellable = true)
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo info) {
    if (((Object) this) instanceof JukeboxBlock) {
      if (!state.get(JukeboxBlock.HAS_RECORD).booleanValue()) {
        return;
      }

      if (random.nextInt(8) == 0) {
        Collection<Integer> possibleNotes = Properties.NOTE.getValues();
        int tone = List.copyOf(possibleNotes).get(random.nextInt(possibleNotes.size()));
        world.addParticle(
          ParticleTypes.NOTE,
          pos.getX() + 0.5,
          pos.getY() + 1.2,
          pos.getZ() + 0.5,
          tone / 24.0,
          0,
          0);
      }
    }
  }
}
