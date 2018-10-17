package com.pw.timetablegenerator.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.Serializable;

@Tag("rating-stars-component")
public class RatingStarsComponent extends Component implements HasComponents, Serializable {

    Integer actualStarValue = 1;

    public Integer getStarValue(){
        return actualStarValue;
    }

    public RatingStarsComponent(){
        Icon firstStarIcon = VaadinIcon.STAR.create();
        firstStarIcon.setColor("yellow");
        Span firstStart = new Span(firstStarIcon);
        Span secondStart = new Span(VaadinIcon.STAR.create());
        Span thirdStart = new Span(VaadinIcon.STAR.create());
        Span fourthStart = new Span(VaadinIcon.STAR.create());
        Span fifthStart = new Span(VaadinIcon.STAR.create());

        firstStart.addClickListener(e ->{
            changeToBlackStar(secondStart);
            changeToBlackStar(thirdStart);
            changeToBlackStar(fourthStart);
            changeToBlackStar(fifthStart);
            actualStarValue = 1;
        });

        secondStart.addClickListener(e -> {
            changeToYellowStar(secondStart);
            changeToBlackStar(thirdStart);
            changeToBlackStar(fourthStart);
            changeToBlackStar(fifthStart);
            actualStarValue = 2;
        });

        thirdStart.addClickListener(e -> {
            changeToYellowStar(secondStart);
            changeToYellowStar(thirdStart);
            changeToBlackStar(fourthStart);
            changeToBlackStar(fifthStart);
            actualStarValue = 3;
        });

        fourthStart.addClickListener(e -> {
            changeToYellowStar(secondStart);
            changeToYellowStar(thirdStart);
            changeToYellowStar(fourthStart);
            changeToBlackStar(fifthStart);
            actualStarValue = 4;
        });

        fifthStart.addClickListener(e -> {
            changeToYellowStar(secondStart);
            changeToYellowStar(thirdStart);
            changeToYellowStar(fourthStart);
            changeToYellowStar(fifthStart);
            actualStarValue = 5;
        });

        HorizontalLayout ratingStartLayout
                = new HorizontalLayout(firstStart, secondStart, thirdStart, fourthStart, fifthStart);
        add(ratingStartLayout);
    }

    private void changeToYellowStar(Span secondStart) {
        secondStart.removeAll();
        final Icon starIcon = VaadinIcon.STAR.create();
        starIcon.setColor("yellow");
        secondStart.add(starIcon);
    }

    private void changeToBlackStar(Span secondStart) {
        secondStart.removeAll();
        secondStart.add(VaadinIcon.STAR.create());
    }

}
