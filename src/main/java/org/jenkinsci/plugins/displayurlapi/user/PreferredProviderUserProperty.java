package org.jenkinsci.plugins.displayurlapi.user;

import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.Extension;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;

import java.util.stream.Collectors;

import org.jenkinsci.plugins.displayurlapi.DisplayURLProvider;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class PreferredProviderUserProperty extends UserProperty {

    @Extension
    public static final UserPropertyDescriptor DESCRIPTOR = new PreferredProviderUserPropertyDescriptor();

    @Nullable
    private String providerId;

    @Nullable
    private String providerIdRun;

    @Nullable
    private String providerIdConsole;

    @Nullable
    private String providerIdArtifacts;

    @Nullable
    private String providerIdChanges;

    @Nullable
    private String providerIdTests;

    public PreferredProviderUserProperty(@Nullable String providerId) {
        this.providerId = providerId;
        this.providerIdRun = providerId;
        this.providerIdConsole = providerId;
        this.providerIdArtifacts = providerId;
        this.providerIdChanges = providerId;
        this.providerIdTests = providerId;
    }

    @DataBoundConstructor
    public PreferredProviderUserProperty(@Nullable String providerId, @Nullable String providerIdRun, @Nullable String providerIdConsole, @Nullable String providerIdArtifacts, @Nullable String providerIdChanges, @Nullable String providerIdTests) {
        this.providerId = providerId;
        this.providerIdRun = providerIdRun;
        this.providerIdConsole = providerIdConsole;
        this.providerIdArtifacts = providerIdArtifacts;
        this.providerIdChanges = providerIdChanges;
        this.providerIdTests = providerIdTests;
    }

    public ProviderOption getProvider(String type) {
        final DisplayURLProvider provider = getConfiguredProvider(type);
        return provider == null ? ProviderOption.DEFAULT_OPTION
            : new ProviderOption(provider.getClass().getName(), provider.getDisplayName());
    }

    public static PreferredProviderUserProperty forCurrentUser() {
        final User current = User.current();
        if (current == null) {
            return (PreferredProviderUserProperty) DESCRIPTOR.newInstance((User) null);
        }

        PreferredProviderUserProperty property = current.getProperty(PreferredProviderUserProperty.class);
        if (property == null) {
            return (PreferredProviderUserProperty) DESCRIPTOR.newInstance(current);
        }
        return property;
    }

    private String getProviderClass(String type) {
        if (type == null) {
            return providerId;
        }
        switch (type) {
        case "run":
            return providerIdRun;
        case "console":
            return providerIdConsole;
        case "artifacts":
            return providerIdArtifacts;
        case "changes":
            return providerIdChanges;
        case "tests":
            return providerIdTests;
        default:
            return providerId;
        }
    }

    public DisplayURLProvider getConfiguredProvider(String type) {
        return DisplayURLProvider.all().stream()
            .filter(input -> input.getClass().getName().equals(getProviderClass(type)))
            .findFirst()
            .orElse(null);
    }

    public List<ProviderOption> getAll() {
        List<ProviderOption> options = DisplayURLProvider.all().stream()
            .map(input -> new ProviderOption(input.getClass().getName(), input.getDisplayName()))
            .collect(Collectors.toList());
        return ImmutableList.<ProviderOption>builder()
            .add(ProviderOption.DEFAULT_OPTION).addAll(options).build();
    }

    public boolean isSelected(String type, String providerId) {
        return getProvider(type).getId().equals(providerId);
    }

    public static class ProviderOption {

        public static final ProviderOption DEFAULT_OPTION = new ProviderOption("default", "Default");

        private final String id;
        private final String name;

        public ProviderOption(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
