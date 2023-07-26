package org.jenkinsci.plugins.displayurlapi;

import hudson.Extension;
import hudson.link.ConsoleURLProvider;
import hudson.model.Run;

@Extension
public class DisplayURLConsoleProvider extends ConsoleURLProvider {
    @Override
    public String getConsoleURL(Run<?, ?> run) {
        return DisplayURLProvider.get("run").getConsoleURL(run);
    }
}
