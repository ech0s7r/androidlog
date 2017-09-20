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
 * Lint rule: Android log detector
 *
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class AndroidLogDetector extends Detector implements Detector.JavaPsiScanner {

	public static final Issue ISSUE = Issue.create(
			"AndroidLogDetector",
			"Log Detector: Please use the Logger library.",
			"We are using RiM Logger library in this project, please replace android log statements with that.",
			Category.CORRECTNESS,
			8,
			Severity.FATAL,
			new Implementation(AndroidLogDetector.class, Scope.JAVA_FILE_SCOPE)
	);

	@Override
	public List<String> getApplicableMethodNames() {
		return Arrays.asList("tag", "format", "v", "d", "i", "w", "e", "wtf");
	}

	@Override
	public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
		PsiReferenceExpression methodExpression = call.getMethodExpression();
		String fullyQualifiedMethodName = methodExpression.getQualifiedName();
		if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
			context.report(ISSUE, call, context.getLocation(methodExpression), ISSUE.getBriefDescription(TextFormat.TEXT));
		}
	}

}

