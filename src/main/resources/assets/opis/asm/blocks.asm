### TileEntity updates.
list n_TileEntityTick
ALOAD 2
CHECKCAST net/minecraft/util/ITickable
INVOKEINTERFACE net/minecraft/util/ITickable.func_73660_a ()V

list i_TileEntityTickPre
GETSTATIC mcp/mobius/opis/profiler/ProfilerSection.TILEENT_UPDATETIME : Lmcp/mobius/opis/profiler/ProfilerSection;
ALOAD 2
INVOKEVIRTUAL mcp/mobius/opis/profiler/ProfilerSection.start (Ljava/lang/Object;)V

list i_TileEntityTickPost
GETSTATIC mcp/mobius/opis/profiler/ProfilerSection.TILEENT_UPDATETIME : Lmcp/mobius/opis/profiler/ProfilerSection;
ALOAD 2
INVOKEVIRTUAL mcp/mobius/opis/profiler/ProfilerSection.stop (Ljava/lang/Object;)V

list n_UpdateEntity
ALOAD 0
ALOAD 2
INVOKEVIRTUAL net/minecraft/world/World.updateEntity (Lnet/minecraft/entity/Entity;)V

list i_UpdateEntityPre
GETSTATIC mcp/mobius/opis/profiler/ProfilerSection.ENTITY_UPDATETIME : Lmcp/mobius/opis/profiler/ProfilerSection;
ALOAD 2
INVOKEVIRTUAL mcp/mobius/opis/profiler/ProfilerSection.start (Ljava/lang/Object;)V

list i_UpdateEntityPost
GETSTATIC mcp/mobius/opis/profiler/ProfilerSection.ENTITY_UPDATETIME : Lmcp/mobius/opis/profiler/ProfilerSection;
ALOAD 2
INVOKEVIRTUAL mcp/mobius/opis/profiler/ProfilerSection.stop (Ljava/lang/Object;)V
