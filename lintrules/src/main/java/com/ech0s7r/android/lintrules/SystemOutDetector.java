package com.ech0s7r.android.lintrules;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiMethod;

import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.java.JavaAbstractUExpression;

import java.util.Arrays;
import java.util.List;


/**
 * @author ech0s7r
 */
@SuppressWarnings("unused")
public final class SystemOutDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create(
            "SystemOutDetector",
            "Use of java.lang.System",
            "Instead of java.lang.System*, use the right Logger library",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(SystemOutDetector.class, Scope.JAVA_FILE_SCOPE));


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("print", "println");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression call, PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        if (call.getReceiver() instanceof JavaAbstractUExpression) {
            JavaAbstractUExpression expr = (JavaAbstractUExpression) call.getReceiver();
            if (expr != null) {
                String name = expr.asRenderString();
                if (evaluator.isMemberInClass(method, "java.io.PrintStream") || name.contains("System.out") || name.contains("System.err")) {
                    context.report(ISSUE, call, context.getLocation(call), ISSUE.getBriefDescription(TextFormat.TEXT));
                }
            }
        }
    }

}