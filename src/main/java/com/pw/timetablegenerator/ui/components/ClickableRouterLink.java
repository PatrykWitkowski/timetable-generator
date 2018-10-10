package com.pw.timetablegenerator.ui.components;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouterLink;

import java.io.Serializable;

/**
 * Enclose a basic router link and add a possibility of use a click listener.
 */
public class ClickableRouterLink extends RouterLink implements ClickNotifier, Serializable {

    public ClickableRouterLink(String text, Class<? extends Component> navigationTarget) {
        super(text, navigationTarget);
    }

}
