package org.jenkinsci.plugins.displayurlapi;

import hudson.Extension;
import hudson.link.LinkProvider;
import hudson.model.Job;
import hudson.model.Run;

@Extension
public class DisplayURLRedirectProvider extends LinkProvider {
    @Override
    public String getRedirectURL(Run<?, ?> run) {
        return DisplayURLProvider.get().getRunURL(run);
    }

    @Override
    public String getRedirectURL(Job<?, ?> job) {
        return DisplayURLProvider.get().getJobURL(job);
    }
}
