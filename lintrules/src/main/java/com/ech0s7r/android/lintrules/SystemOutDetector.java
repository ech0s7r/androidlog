package com.ech0s7r.android.lintrules;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import java.util.Arrays;
import java.util.List;


/**
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public final class SystemOutDetector extends Detector implements Detector.JavaPsiScanner {

	public static final Issue ISSUE = Issue.create(
			"SystemOutDetector",
			"Use of java.lang.System",
			"Instead of java.lang.System*, use RiM Logger library",
			Category.CORRECTNESS,
			8,
			Severity.FATAL,
			new Implementation(SystemOutDetector.class, Scope.JAVA_FILE_SCOPE));


	@Override
	public List<String> getApplicableMethodNames() {
		return Arrays.asList("print", "println");
	}

	@Override
	public void visitMethod(JavaContext context, JavaElementVisitor visitor,
							PsiMethodCallExpression call, PsiMethod method) {
		PsiReferenceExpression methodExpression = call.getMethodExpression();
		String fullyQualifiedMethodName = methodExpression.getQualifiedName();
//		log("fullyQualifiedMethodName=[" + fullyQualifiedMethodName + "]" + " " + call + " " + method);
//		log(method.getClass().getSimpleName());
		if (fullyQualifiedMethodName.contains("System.out.print") || fullyQualifiedMethodName.contains("System.err.print")) {
			context.report(ISSUE, call, context.getLocation(methodExpression), ISSUE.getBriefDescription(TextFormat.TEXT));
		}
	}

}