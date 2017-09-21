package com.ech0s7r.android.lintrules;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class LintRegistry extends IssueRegistry {
	@Override
	public List<Issue> getIssues() {
		return ImmutableList.of(
				AndroidLogDetector.ISSUE,
				PrintStackTraceDetector.ISSUE,
				SystemOutDetector.ISSUE,
				NoLoggedException.ISSUE//,
				//NoBaseActivity.ISSUE
		);
	}
}
