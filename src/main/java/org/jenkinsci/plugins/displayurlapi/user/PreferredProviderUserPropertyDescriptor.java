package org.jenkinsci.plugins.displayurlapi.user;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.util.ListBoxModel;
import org.jenkinsci.plugins.displayurlapi.Messages;
import org.jenkinsci.plugins.displayurlapi.user.PreferredProviderUserProperty.ProviderOption;

public class PreferredProviderUserPropertyDescriptor extends UserPropertyDescriptor {

    public PreferredProviderUserPropertyDescriptor() {
        super(PreferredProviderUserProperty.class);
    }

    @Override
    public UserProperty newInstance(User user) {
        return new PreferredProviderUserProperty(ProviderOption.DEFAULT_OPTION.getId());
    }

    private ListBoxModel fillProvidedItems(String type) {
        ListBoxModel items = new ListBoxModel();
        PreferredProviderUserProperty property = PreferredProviderUserProperty.forCurrentUser();
        for (ProviderOption providerOption : property.getAll()) {
            ListBoxModel.Option option = new ListBoxModel.Option(
                    providerOption.getName(),
                    providerOption.getId(),
                    property.isSelected(type, providerOption.getId())
            );
            items.add(option);
        }
        return items;
    }

    public ListBoxModel doFillProviderIdItems() {
        return fillProvidedItems("job");
    }

    public ListBoxModel doFillProviderIdRunItems() {
        return fillProvidedItems("run");
    }

    public ListBoxModel doFillProviderIdConsoleItems() {
        return fillProvidedItems("console");
    }

    public ListBoxModel doFillProviderIdArtifactsItems() {
        return fillProvidedItems("artifacts");
    }

    public ListBoxModel doFillProviderIdChangesItems() {
        return fillProvidedItems("changes");
    }

    public ListBoxModel doFillProviderIdTestsItems() {
        return fillProvidedItems("tests");
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return Messages.display_url();
    }
}
