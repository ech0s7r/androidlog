/*
 * Copyright 2011 ech0s7r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @author ech0s7r
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

