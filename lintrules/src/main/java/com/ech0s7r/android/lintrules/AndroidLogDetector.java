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

import java.util.Arrays;
import java.util.List;


/**
 * Lint rule: Android log detector
 *
 * @author ech0s7r
 */
@SuppressWarnings("unused")
public class AndroidLogDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create(
            "AndroidLogDetector",
            "Log Detector: Please use the Logger library.",
            "We are using a custom Logger library in this project, please replace android log statements with that.",
            Category.CORRECTNESS,
            8,
            Severity.FATAL,
            new Implementation(AndroidLogDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    private static final String ANDROID_LOG = "android.util.Log";


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("tag", "format", "v", "d", "i", "w", "e", "wtf");
    }


    @Override
    public void visitMethod(JavaContext context, UCallExpression call, PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isMemberInClass(method, ANDROID_LOG)) {
            context.report(ISSUE, call, context.getLocation(call), ISSUE.getBriefDescription(TextFormat.TEXT));
        }
    }

}

