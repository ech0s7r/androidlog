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

import java.util.Collections;
import java.util.List;


/**
 * @author ech0s7r
 */
@SuppressWarnings("unused")
public final class PrintStackTraceDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create(
            "PrintStackTraceDetector",
            "Do not use printStackTrace",
            "Instead of printStackTrace, use right Logger library",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(PrintStackTraceDetector.class, Scope.JAVA_FILE_SCOPE));

    private static final String THROWABLE = "java.lang.Throwable";
    private static final String PRINT_STACK_TRACE = "java.lang.Throwable.printStackTrace";

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("printStackTrace");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression call, PsiMethod method) {
        String fullyQualifiedMethodName = call.getMethodName();
        if (context.getEvaluator().isMemberInClass(method, THROWABLE)
                || context.getEvaluator().isMemberInSubClassOf(method, THROWABLE, false)
                || context.getEvaluator().isMemberInSubClassOf(method, THROWABLE, true)
                || (fullyQualifiedMethodName != null && fullyQualifiedMethodName.startsWith(PRINT_STACK_TRACE))) {
            context.report(ISSUE, call, context.getLocation(call), ISSUE.getBriefDescription(TextFormat.TEXT));
        }
    }
}