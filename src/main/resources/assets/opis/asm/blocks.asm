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
list n_WorldServerTick
RETURN

list i_WorldServerTickPre
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getWorldServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 0
GETFIELD net/minecraft/world/World.field_73011_w : Lnet/minecraft/world/WorldProvider;
INVOKEVIRTUAL net/minecraft/world/WorldProvider.getDimension ()I
INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.start (Ljava/lang/Object;)V

list i_WorldServerTickPost
INVOKESTATIC mcp/mobius/opis/profiler/Profilers.getWorldServerTickProfiler ()Lmcp/mobius/opis/profiler/IProfiler;
ALOAD 0
GETFIELD net/minecraft/world/World.field_73011_w : Lnet/minecraft/world/WorldProvider;
INVOKEVIRTUAL net/minecraft/world/WorldProvider.getDimension ()I
INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
INVOKEINTERFACE mcp/mobius/opis/profiler/IProfiler.stop (Ljava/lang/Object;)V
