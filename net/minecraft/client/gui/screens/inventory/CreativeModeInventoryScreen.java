java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
	at java.util.Vector.get(Vector.java:751)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
	at com.strobel.assembler.metadata.MetadataHelper.isSubTypeNoCapture(MetadataHelper.java:1268)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitGenericParameter(MetadataHelper.java:1874)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitGenericParameter(MetadataHelper.java:1815)
	at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
	at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
	at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:508)
	at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:561)
	at com.strobel.assembler.metadata.MetadataHelper.getConversionType(MetadataHelper.java:317)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:74)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.CastExpression.acceptVisitor(CastExpression.java:55)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitMemberReferenceExpression(DepthFirstAstVisitor.java:74)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitMemberReferenceExpression(InsertNecessaryConversionsTransform.java:89)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitMemberReferenceExpression(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.MemberReferenceExpression.acceptVisitor(MemberReferenceExpression.java:120)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitInvocationExpression(DepthFirstAstVisitor.java:59)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitExpressionStatement(DepthFirstAstVisitor.java:109)
	at com.strobel.decompiler.languages.java.ast.ExpressionStatement.acceptVisitor(ExpressionStatement.java:47)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitBlockStatement(DepthFirstAstVisitor.java:104)
	at com.strobel.decompiler.languages.java.ast.BlockStatement.acceptVisitor(BlockStatement.java:72)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitIfElseStatement(DepthFirstAstVisitor.java:134)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitIfElseStatement(InsertNecessaryConversionsTransform.java:487)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitIfElseStatement(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.IfElseStatement.acceptVisitor(IfElseStatement.java:83)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitBlockStatement(DepthFirstAstVisitor.java:104)
	at com.strobel.decompiler.languages.java.ast.BlockStatement.acceptVisitor(BlockStatement.java:72)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitMethodDeclaration(DepthFirstAstVisitor.java:214)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitMethodDeclaration(ContextTrackingVisitor.java:64)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitMethodDeclaration(ContextTrackingVisitor.java:28)
	at com.strobel.decompiler.languages.java.ast.MethodDeclaration.acceptVisitor(MethodDeclaration.java:85)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitTypeDeclaration(DepthFirstAstVisitor.java:244)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:52)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:28)
	at com.strobel.decompiler.languages.java.ast.TypeDeclaration.acceptVisitor(TypeDeclaration.java:90)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitCompilationUnit(DepthFirstAstVisitor.java:249)
	at com.strobel.decompiler.languages.java.ast.CompilationUnit.acceptVisitor(CompilationUnit.java:81)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.run(ContextTrackingVisitor.java:84)
	at com.strobel.decompiler.languages.java.ast.transforms.TransformationPipeline.runTransformationsUntil(TransformationPipeline.java:93)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.runTransformations(AstBuilder.java:119)
	at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:76)
	at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
	at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
	at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
	at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
	at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
	at java.util.concurrent.ForkJoinPool$WorkQueue.execLocalTasks(ForkJoinPool.java:1040)
	at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1058)
	at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
	at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
