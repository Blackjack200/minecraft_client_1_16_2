java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 6
	at java.util.Vector.get(Vector.java:751)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
	at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
	at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
	at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
	at com.strobel.assembler.metadata.MetadataSystem.lookupTypeCore(MetadataSystem.java:86)
	at com.strobel.assembler.metadata.MetadataResolver.lookupType(MetadataResolver.java:46)
	at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:63)
	at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
	at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
	at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
	at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
	at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
	at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
	at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
	at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
