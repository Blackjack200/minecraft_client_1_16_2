java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
	at java.util.Vector.get(Vector.java:751)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:79)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:96)
	at com.strobel.assembler.metadata.MetadataHelper.isEnclosedBy(MetadataHelper.java:97)
	at com.strobel.decompiler.languages.java.ast.transforms.EliminateSyntheticAccessorsTransform.visitInvocationExpression(EliminateSyntheticAccessorsTransform.java:90)
	at com.strobel.decompiler.languages.java.ast.transforms.EliminateSyntheticAccessorsTransform.visitInvocationExpression(EliminateSyntheticAccessorsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitExpressionStatement(DepthFirstAstVisitor.java:109)
	at com.strobel.decompiler.languages.java.ast.ExpressionStatement.acceptVisitor(ExpressionStatement.java:47)
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
	at com.strobel.decompiler.languages.java.ast.transforms.EliminateSyntheticAccessorsTransform.run(EliminateSyntheticAccessorsTransform.java:57)
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
	at java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:401)
	at java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:734)
	at java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:160)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
	at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
	at java.lang.Thread.run(Thread.java:748)
