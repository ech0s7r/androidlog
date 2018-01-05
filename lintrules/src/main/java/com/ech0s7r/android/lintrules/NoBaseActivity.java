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
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;

import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Collections;
import java.util.List;

/**
 * Lint rule: No Logged exception detector
 * <p>
 *
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class NoBaseActivity extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create(
            "NoBaseActivity",
            "No BaseActivity extension",
            "You should extend BaseActivity",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(NoBaseActivity.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new NoBaseActivityChecker(context);
    }

    private static class NoBaseActivityChecker extends JavaElementVisitor {
        private final JavaContext mContext;

        NoBaseActivityChecker(JavaContext context) {
            this.mContext = context;
        }

        @Override
        public void visitClass(PsiClass aClass) {
            super.visitClass(aClass);
            if (aClass != null && aClass.getQualifiedName() != null) {
                boolean valid = true;
                for (PsiClassType c : aClass.getExtendsListTypes()) {
                    if (c != null) {
                        if (c.getCanonicalText().contains("android.app.Activity")
                                || c.getCanonicalText().contains("android.support.v4.app.FragmentActivity")
                                || c.getCanonicalText().contains("android.app.ListActivity"))
                            valid = false;
                    }
                }
                if (!valid) {
                    mContext.report(ISSUE, aClass, mContext.getLocation(aClass), ISSUE.getBriefDescription(TextFormat.TEXT));
                }
            }
        }
    }
}
