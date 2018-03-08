### Generic needles.
list n_ReturnV
RETURN

### TileEntity updates.
list n_TileEntityTick
ALOAD 2
CHECKCAST net/minecraft/util/ITickable
INVOKEINTERFACE net/minecraft/util/ITickable.func_73660_a ()V

list i_TileEntityTickPre
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getTileUpdateProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 2
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start (Ljava/lang/Object;)V

list i_TileEntityTickPost
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getTileUpdateProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 2
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop (Ljava/lang/Object;)V

### Entity Updates.
list n_UpdateEntity
ALOAD 0
ALOAD 2
INVOKEVIRTUAL net/minecraft/world/World.func_72870_g (Lnet/minecraft/entity/Entity;)V

list i_UpdateEntityPre
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getEntityUpdateProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 2
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start (Ljava/lang/Object;)V

list i_UpdateEntityPost
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getEntityUpdateProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 2
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop (Ljava/lang/Object;)V

### WorldServer ticks.
list i_WorldServerTickPre
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getWorldServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 0
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start (Ljava/lang/Object;)V

list i_WorldServerTickPost
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getWorldServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 0
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop (Ljava/lang/Object;)V

### Dimension Ticks.
list i_FMLCH_PreWorldTick
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getDimensionTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 1
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start (Ljava/lang/Object;)V

list i_FMLCH_PostWorldTick
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getDimensionTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 1
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop (Ljava/lang/Object;)V

### ServerTicks
list i_FMLCH_PreServerTick
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start ()V

list i_FMLCH_PostServerTick
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop ()V
