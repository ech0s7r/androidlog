package com.ech0s7r.android.lintrules;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import com.google.common.collect.ImmutableList;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiStatement;

import java.util.List;

/**
 * Lint rule: No Logged exception detector
 * <p>
 *
 * @author marco.rocco
 */

public class NoLoggedException extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "NoLoggedException",
            "Exception not logged",
            "You should log the Exception",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(NoLoggedException.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return ImmutableList.of(PsiCatchSection.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new NoLoggedExceptionChecker(context);
    }

    private static class NoLoggedExceptionChecker extends JavaElementVisitor {
        private final JavaContext mContext;

        public NoLoggedExceptionChecker(JavaContext context) {
            this.mContext = context;
        }

        @Override
        public void visitCatchSection(PsiCatchSection section) {
            boolean valid = false;
            String exceptionParamName = section.getParameter().getName();
//			LintLog.log("visiting catch section text=[" + section.getText() + "] parameter: " + exceptionParamName);
//			LintLog.log("statements in catchblock: ");
            for (PsiStatement statement : section.getCatchBlock().getStatements()) {
//				LintLog.log("\tstatement=[" + statement + "]");
                if (statement != null && statement.getText() != null &&
                        statement.getText().contains("Logger")) {
                    String text = statement.getText();
                    text = text.replaceAll("\\s+", "");
                    if (text.contains("(" + exceptionParamName + ")")) {
                        valid = true;
                    }
                    if (text.contains("," + exceptionParamName)) {
                        valid = true;
                    }
                    if (text.contains("+" + exceptionParamName + ",") || text.contains("+" + exceptionParamName + ")")) {
                        valid = true;
                    }
                    if (text.contains(exceptionParamName + ".")) {
                        valid = true;
                    }
                }
            }
//			LintLog.log("\n\n");

            if (!valid) {
                // show error
                mContext.report(ISSUE, section, mContext.getLocation(section), ISSUE.getBriefDescription(TextFormat.TEXT));
            }
        }

    }
}
