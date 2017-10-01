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
public final class PrintStackTraceDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "PrintStackTraceDetector",
            "Do not use printStackTrace",
            "Instead of printStackTrace, use RiM Logger library",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(PrintStackTraceDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("printStackTrace");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor,
                            PsiMethodCallExpression call, PsiMethod method) {
        PsiReferenceExpression methodExpression = call.getMethodExpression();
        String fullyQualifiedMethodName = methodExpression.getQualifiedName();
        if (context.getEvaluator().isMemberInClass(method, "java.lang.Throwable")
                || context.getEvaluator().isMemberInSubClassOf(method, "java.lang.Throwable", false)
                || context.getEvaluator().isMemberInSubClassOf(method, "java.lang.Throwable", true)
                || fullyQualifiedMethodName.startsWith("java.lang.Throwable.printStackTrace")) {
            context.report(ISSUE, call, context.getLocation(methodExpression), ISSUE.getBriefDescription(TextFormat.TEXT));
        }
    }
}