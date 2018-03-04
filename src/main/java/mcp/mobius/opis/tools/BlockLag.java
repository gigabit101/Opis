package mcp.mobius.opis.tools;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockLag extends BlockContainer {

    public BlockLag(Material par2Material) {
        super(par2Material);
		setCreativeTab(CreativeTabs.MISC);
		this.setUnlocalizedName("lag_generator");
    }	

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileLag();
	}

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
