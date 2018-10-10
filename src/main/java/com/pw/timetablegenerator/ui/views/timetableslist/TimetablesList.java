package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

import javax.annotation.PostConstruct;

/**
 * Displays the list of available orders, with a search filter as well as
 * buttons to add a new order or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Timetables")
//@Tag("timetables-list")
//@HtmlImport("frontend://src/views/timetableslist/orders-list.html")
public class TimetablesList extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateList();
        }
    }

    public interface TimetablesModel extends TemplateModel {
//        @Encode(value = OrderStatusToStringEncoder.class, path = "status")
//        @Encode(value = LocalDateToStringEncoder.class, path = "orderDate")
//        @Encode(value = LongToStringEncoder.class, path = "counter")
//        @Encode(value = LongToStringEncoder.class, path = "orderId")
//        @Exclude({"owner", "orderedProduct"})
//        void setOrders(List<Order> orders);
    }

    @Id("search")
    private TextField search;
    @Id("newOrder")
    private Button addOrder;
    @Id("header")
    private H2 header;

    /**
     * Needs to be done here, because autowireds field are injected after constructor
     */
    //@PostConstruct
    public void TimetablesList(){
        search.setPlaceholder("Search timetables");
        search.addValueChangeListener(e -> updateList());
        search.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void updateList() {

    }

}
