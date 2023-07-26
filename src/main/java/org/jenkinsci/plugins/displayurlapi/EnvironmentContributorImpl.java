package org.jenkinsci.plugins.displayurlapi;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TaskListener;

@Extension
public class EnvironmentContributorImpl extends EnvironmentContributor {

    @Override
    public void buildEnvironmentFor(@NonNull Run r, @NonNull EnvVars envs, @NonNull TaskListener listener) {
        try (DisplayURLContext ctx = DisplayURLContext.open(false)) { // environment contributor "comes from" core
            ctx.run(r);
            envs.put("RUN_DISPLAY_URL", DisplayURLProvider.get("run").getRunURL(r));
            envs.put("RUN_ARTIFACTS_DISPLAY_URL", DisplayURLProvider.get("artifacts").getArtifactsURL(r));
            envs.put("RUN_CHANGES_DISPLAY_URL", DisplayURLProvider.get("changes").getChangesURL(r));
            envs.put("RUN_TESTS_DISPLAY_URL", DisplayURLProvider.get("tests").getTestsURL(r));
        }
    }

    @Override
    public void buildEnvironmentFor(@NonNull Job j, @NonNull EnvVars envs, @NonNull TaskListener listener) {
        try (DisplayURLContext ctx = DisplayURLContext.open(false)) {
            ctx.job(j);
            envs.put("JOB_DISPLAY_URL", DisplayURLProvider.get("job").getJobURL(j));
        }
    }
}
