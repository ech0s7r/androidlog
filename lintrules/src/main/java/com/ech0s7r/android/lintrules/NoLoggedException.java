package com.ech0s7r.android.lintrules;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;

import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UCatchClause;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.List;

/**
 * Lint rule: No Logged exception detector
 * <p>
 *
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class NoLoggedException extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create(
            "NoLoggedException",
            "Exception not logged",
            "You should log the Exception",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(NoLoggedException.class, Scope.JAVA_FILE_SCOPE));

    private static final String LOGGER_LIB = "com.ech0s7r.android.log.Logger";

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UCatchClause.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new UastHandler(context);
    }

    private class UastHandler extends UElementHandler {

        private JavaContext mContext;
        private boolean mIsValid;

        UastHandler(JavaContext context) {
            mContext = context;
        }

        @Override
        public void visitCatchClause(final UCatchClause section) {
            section.accept(new AbstractUastVisitor() {
                @Override
                public boolean visitElement(UElement node) {
                    if (node instanceof UCallExpression) {
                        if (mContext.getEvaluator().isMemberInClass(((UCallExpression) node).resolve(), LOGGER_LIB)) {
                            mIsValid = true;
                        }
                    }
                    return super.visitElement(node);
                }

                @Override
                public void afterVisitCatchClause(UCatchClause node) {
                    if (!mIsValid) {
                        mContext.report(ISSUE, section, mContext.getLocation(section), ISSUE.getBriefDescription(TextFormat.TEXT));
                    }
                }
            });
        }
    }

}

